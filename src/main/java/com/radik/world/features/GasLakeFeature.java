package com.radik.world.features;

import com.mojang.serialization.Codec;
import com.radik.fluid.elements.Hydrogen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.radik.Data.getDimension;
import static com.radik.fluid.RegisterFluids.FLOWING_HYDROGEN;
import static net.minecraft.block.Blocks.END_STONE;
import static net.minecraft.fluid.FlowableFluid.FALLING;
import static net.minecraft.fluid.FlowableFluid.LEVEL;

public class GasLakeFeature extends Feature<GasLakeFeatureConfig> {

    public GasLakeFeature(Codec<GasLakeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<GasLakeFeatureConfig> context) {
        WorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        GasLakeFeatureConfig config = context.getConfig();

        if (!getDimension(world).equals("end") ||
                isOnMainIsland(origin)) {
            return false;
        }

        // Проверяем, что озеро на поверхности
        BlockPos surfacePos = findSurfacePos(world, origin);
        if (surfacePos == null) {
            return false;
        }

        // Генерируем озеро
        return generateLake(world, surfacePos, random, config);
    }

    private boolean isOnMainIsland(@NotNull BlockPos pos) {
        int centerX = 0;
        int centerZ = 0;
        int radius = 1000;

        int dx = pos.getX() - centerX;
        int dz = pos.getZ() - centerZ;
        return dx * dx + dz * dz < radius * radius;
    }

    private @Nullable BlockPos findSurfacePos(@NotNull WorldAccess world, BlockPos pos) {
        for (int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos); y > world.getBottomY(); y--) {
            BlockPos testPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!world.isAir(testPos)) {
                return testPos;
            }
        }
        return null;
    }

    private boolean generateLake(WorldAccess world, BlockPos center, @NotNull Random random, @NotNull GasLakeFeatureConfig config) {
        int maxBlocks = config.maxBlocks();
        int blocksPlaced = 0;

        int radius = 2 + random.nextInt(3);
        List<BlockPos> lakePositions = new ArrayList<>();

        // Собираем позиции для озера
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos pos = center.add(x, 0, z);

                    // Проверяем, что можем разместить здесь водород
                    if (canPlaceHydrogen(world, pos)) {
                        lakePositions.add(pos);
                    }
                }
            }
        }

        // Проверяем размер озера
        if (lakePositions.size() > maxBlocks || lakePositions.isEmpty()) {
            return false;
        }

        for (BlockPos pos : lakePositions) {
            world.setBlockState(pos, ((Hydrogen) FLOWING_HYDROGEN).getMax().getBlockState(), 3);
            blocksPlaced++;
        }

        placeWalls(world, lakePositions, config.wallBlock());

        return blocksPlaced > 0;
    }

    private boolean canPlaceHydrogen(@NotNull WorldAccess world, @NotNull BlockPos pos) {
        BlockPos below = pos.down();
        return world.getBlockState(below).isSolidBlock(world, below) && world.getBlockState(pos.up()).isAir() && world.getBlockState(pos).getBlock().equals(END_STONE);
    }

    private void placeWalls(WorldAccess world, @NotNull List<BlockPos> lakePositions, BlockState wallBlock) {
        Set<BlockPos> wallPositions = new HashSet<>();

        for (BlockPos pos : lakePositions) {
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP) continue;

                BlockPos neighbor = pos.offset(direction);
                if (!lakePositions.contains(neighbor) && (world.isAir(neighbor) || world.getBlockState(neighbor).equals(END_STONE.getDefaultState()))) {
                    wallPositions.add(neighbor);
                }
            }
        }

        for (BlockPos pos : wallPositions) {
            world.setBlockState(pos, wallBlock, 3);
        }
    }
}