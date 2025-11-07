package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.radik.fluid.BasedGas;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.radik.Data.getDimension;
import static net.minecraft.block.Blocks.END_STONE;

public class GasLakeFeature extends Feature<GasLakeFeatureConfig> {

    public GasLakeFeature(Codec<GasLakeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(@NotNull FeatureContext<GasLakeFeatureConfig> context) {
        WorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        GasLakeFeatureConfig config = context.getConfig();

        if (!getDimension(world).equals("end") || isOnMainIsland(origin)) return false;
        BlockPos surfacePos = findSurfacePos(world, origin);
        if (surfacePos == null) return false;
        return generateLake(world, surfacePos, random, config);
    }

    private boolean isOnMainIsland(@NotNull BlockPos pos) {
        int radius = 1000;

        int dx = pos.getX();
        int dz = pos.getZ();
        return (long) dx * dx + (long) dz * dz < radius * radius;
    }

    private @Nullable BlockPos findSurfacePos(@NotNull WorldAccess world, BlockPos pos) {
        for (int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos); y > world.getBottomY(); y--) {
            BlockPos testPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!world.isAir(testPos)) return testPos;
        }
        return null;
    }

    private boolean generateLake(WorldAccess world, BlockPos center, @NotNull Random random, @NotNull GasLakeFeatureConfig config) {
        int maxBlocks = config.maxBlocks();
        int blocksPlaced = 0;

        int radius = 2 + random.nextInt(3);
        List<BlockPos> lakePositions = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos pos = center.add(x, 0, z);
                    if (canPlaceHydrogen(world, pos)) lakePositions.add(pos);
                }
            }
        }

        if (lakePositions.size() > maxBlocks || lakePositions.isEmpty()) return false;

        for (BlockPos pos : lakePositions) {
            world.setBlockState(pos, ((BasedGas) config.hydrogenState().getFluidState().getFluid()).getFlowing().getDefaultState().getBlockState(), 3);
            blocksPlaced++;
        }

        placeWalls(world, lakePositions, config.wallBlocks());
        return blocksPlaced > 0;
    }

    private boolean canPlaceHydrogen(@NotNull WorldAccess world, @NotNull BlockPos pos) {
        BlockPos below = pos.down();
        return world.getBlockState(below).isSolidBlock(world, below) && world.getBlockState(pos.up()).isAir() && world.getBlockState(pos).getBlock().equals(END_STONE);
    }

    private void placeWalls(@NotNull WorldAccess world, @NotNull List<BlockPos> lakePositions, @NotNull List<BlockState> wallBlock) {
        Random random = world.getRandom();
        HashMap<BlockPos, Byte> wallPositions = new HashMap<>();
        byte l = (byte) wallBlock.size();

        for (BlockPos pos : lakePositions) {
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP) continue;

                BlockPos neighbor = pos.offset(direction);
                if (!lakePositions.contains(neighbor) && (world.isAir(neighbor) || world.getBlockState(neighbor).equals(END_STONE.getDefaultState()))) {
                    wallPositions.put(neighbor, (byte) random.nextInt(l));
                }
            }
        }

        for (BlockPos pos : wallPositions.keySet()) {
            world.setBlockState(pos, wallBlock.get(wallPositions.get(pos)), 3);
        }
    }
}