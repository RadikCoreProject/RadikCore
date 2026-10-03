package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.radik.fluid.BasedGas;
import net.minecraft.block.Block;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.radik.Data.getDimension;

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

        if (getDimension(world).equals("end") && isOnMainIsland(origin)) return false;

        BlockPos targetPos = config.underground()
                ? findUndergroundPos(world, origin, random, config)
                : findSurfacePos(world, origin);

        if (targetPos == null) return false;

        return config.deep()
                ? generateDeepLake(world, targetPos, random, config)
                : generateLake(world, targetPos, random, config);
    }

    private boolean isOnMainIsland(@NotNull BlockPos pos) {
        int radius = 1000;
        long dx = pos.getX();
        long dz = pos.getZ();
        return dx * dx + dz * dz < radius * radius;
    }

    private @Nullable BlockPos findSurfacePos(@NotNull WorldAccess world, BlockPos pos) {
        for (int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos); y > world.getBottomY(); y--) {
            BlockPos testPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!world.isAir(testPos)) return testPos;
        }
        return null;
    }

    private @Nullable BlockPos findUndergroundPos(@NotNull WorldAccess world, BlockPos origin, Random random,
                                                  @NotNull GasLakeFeatureConfig config) {
        int attempts = 5;
        int radius = 7;

        int minY = world.getBottomY();
        int maxY = world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin) - 10;

        if (maxY <= minY) return null;

        for (int i = 0; i < attempts; i++) {
            int x = origin.getX() + random.nextInt(radius * 2 + 1) - radius;
            int z = origin.getZ() + random.nextInt(radius * 2 + 1) - radius;
            int y = random.nextInt(maxY - minY + 1) + minY;

            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).getBlock() != world.getBlockState(origin).getBlock()) continue;
            if (canPlaceHydrogen(world, pos, config.underground(), config.replaceState().getBlock())) {
                return pos;
            }
        }

        return null;
    }

    private boolean generateLake(WorldAccess world, BlockPos center, @NotNull Random random,
                                 @NotNull GasLakeFeatureConfig config) {
        int maxBlocks = config.maxBlocks();
        int blocksPlaced = 0;

        int radius = 2 + random.nextInt(5);
        List<BlockPos> lakePositions = new ArrayList<>();

        label: {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        BlockPos pos = center.add(x, 0, z);
                        if (canPlaceHydrogen(world, pos, config.underground(), config.replaceState().getBlock()))
                            lakePositions.add(pos);
                        if (lakePositions.size() == maxBlocks) break label;
                    }
                }
            }
        }

        if (lakePositions.isEmpty()) return false;

        for (BlockPos pos : lakePositions) {
            world.setBlockState(pos, ((BasedGas) config.hydrogenState().getFluidState().getFluid()).getStill().getDefaultState().getBlockState(), 3);
            blocksPlaced++;
        }

        placeWalls(world, lakePositions, config.wallBlocks(), config.replaceState().getBlock());
        return blocksPlaced > 0;
    }

    private boolean generateDeepLake(WorldAccess world, BlockPos center, @NotNull Random random,
                                     @NotNull GasLakeFeatureConfig config) {
        int max = config.maxBlocks();
        int placed = 0;

        int depth = 3 + random.nextInt(3);
        int baseRadius = 3 + random.nextInt(3);

        List<BlockPos> poses = new ArrayList<>();
        label: {
            for (int yOffset = 0; yOffset > -depth; yOffset--) {
                int r = baseRadius + yOffset;
                if (r < 1) break;

                for (int x = -r; x <= r; x++) {
                    for (int z = -r; z <= r; z++) {
                        if (x * x + z * z <= r * r) {
                            BlockPos pos = center.add(x, yOffset, z);
                            if (world.getBlockState(pos).getBlock().equals(config.replaceState().getBlock())) {
                                poses.add(pos);
                                if (poses.size() == max) break label;
                            }
                        }
                    }
                }
            }
        }

        if (poses.isEmpty()) return false;

        for (BlockPos pos : poses) {
            world.setBlockState(pos, ((BasedGas) config.hydrogenState().getFluidState().getFluid()).getStill().getDefaultState().getBlockState(), 3);
            placed++;
        }
        placeWalls(world, poses, config.wallBlocks(), config.replaceState().getBlock());

        return placed > 0;
    }

    private boolean canPlaceHydrogen(@NotNull WorldAccess world, @NotNull BlockPos pos, boolean underground, Block def) {
        BlockPos below = pos.down();
        BlockState up = world.getBlockState(pos.up());
        return world.getBlockState(below).isSolidBlock(world, below)
                && (underground != up.isAir())
                && world.getBlockState(pos).getBlock().equals(def);
    }

    private void placeWalls(@NotNull WorldAccess world, @NotNull List<BlockPos> lakePositions,
                            @NotNull List<BlockState> wallBlock, Block replace) {
        Random random = world.getRandom();
        HashMap<BlockPos, Byte> wallPositions = new HashMap<>();
        byte l = (byte) wallBlock.size();

        for (BlockPos pos : lakePositions) {
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = pos.offset(direction);
                if (!lakePositions.contains(neighbor)
                        && (world.isAir(neighbor) || world.getBlockState(neighbor).getBlock().equals(replace))) {
                    wallPositions.put(neighbor, (byte) random.nextInt(l));
                }
            }
        }

        for (BlockPos pos : wallPositions.keySet()) {
            world.setBlockState(pos, wallBlock.get(wallPositions.get(pos)), 3);
        }
    }
}