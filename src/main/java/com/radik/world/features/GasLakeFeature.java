package com.radik.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Проверяем, что находимся в Энде и не на главном острове
        if (!world.getBiome(origin).getKey().orElseThrow().getValue().getNamespace().equals("minecraft:the_end") ||
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

    private boolean isOnMainIsland(BlockPos pos) {
        // Главный остров в Энде - примерно в радиусе 100 блоков от (100, 50, 0)
        int centerX = 0;
        int centerZ = 0;
        int radius = 1000;

        int dx = pos.getX() - centerX;
        int dz = pos.getZ() - centerZ;
        return dx * dx + dz * dz < radius * radius;
    }

    private BlockPos findSurfacePos(WorldAccess world, BlockPos pos) {
        // Ищем позицию поверхности (первый не-воздушный блок сверху)
        for (int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos); y > world.getBottomY(); y--) {
            BlockPos testPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!world.isAir(testPos)) {
                return testPos.up(); // Позиция над поверхностью
            }
        }
        return null;
    }

    private boolean generateLake(WorldAccess world, BlockPos center, Random random, GasLakeFeatureConfig config) {
        int maxBlocks = config.maxBlocks();
        int blocksPlaced = 0;

        // Определяем форму озера (круг с случайным радиусом 2-4 блока)
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

        // Размещаем водород
        for (BlockPos pos : lakePositions) {
            world.setBlockState(pos, config.hydrogenState(), 3);
            blocksPlaced++;
        }

        // Размещаем стены вокруг водорода
        placeWalls(world, lakePositions, config.wallBlock());

        return blocksPlaced > 0;
    }

    private boolean canPlaceHydrogen(WorldAccess world, BlockPos pos) {
        // Проверяем, что блок ниже - твердый
        BlockPos below = pos.down();
        if (!world.getBlockState(below).isSolidBlock(world, below)) {
            return false;
        }

        // Проверяем, что текущая позиция - воздух
        return world.isAir(pos);
    }

    private void placeWalls(WorldAccess world, List<BlockPos> lakePositions, BlockState wallBlock) {
        Set<BlockPos> wallPositions = new HashSet<>();

        // Находим все позиции вокруг водорода
        for (BlockPos pos : lakePositions) {
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP) continue; // Пропускаем верх

                BlockPos neighbor = pos.offset(direction);
                if (!lakePositions.contains(neighbor) && world.isAir(neighbor)) {
                    wallPositions.add(neighbor);
                }
            }
        }

        // Размещаем стены
        for (BlockPos pos : wallPositions) {
            world.setBlockState(pos, wallBlock, 3);
        }
    }
}