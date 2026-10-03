package com.radik.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class BoulderFeature extends Feature<BoulderFeatureConfig> {

    public BoulderFeature(Codec<BoulderFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<BoulderFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BoulderFeatureConfig config = context.getConfig();

        int actualVolume = calculateVolume(random, config.volume());
        int size = (int) Math.round(Math.cbrt(actualVolume));
        if (size < 1) size = 1;

        BlockPos surfacePos = findSurfacePos(world, origin);
        if (surfacePos == null) return false;

        int half = Math.max(1, size / 2);
        int undergroundDepth = 0;
        if (config.ground()) undergroundDepth = random.nextInt(half) + 1;

        int radius = Math.max(1, size / 2);
        double scaleX = 1.0 + (random.nextDouble() - 0.5) * 0.3;
        double scaleY = 1.0 + (random.nextDouble() - 0.5) * 0.2;
        double scaleZ = 1.0 + (random.nextDouble() - 0.5) * 0.3;

        List<BlockPos> chosen = reservoirSampleSphere(world, surfacePos, radius, actualVolume, undergroundDepth, random, scaleX, scaleY, scaleZ);

        int placed = 0;
        for (BlockPos pos : chosen) {
            BlockState current = world.getBlockState(pos);
            if (!canReplace(current)) continue;
            BlockState toPlace = config.base();
            if (!config.ores().isEmpty() && random.nextInt(100) < config.chance()) {
                int oreIndex = random.nextInt(config.ores().size());
                toPlace = config.ores().get(oreIndex);
            }
            world.setBlockState(pos, toPlace, 3);
            placed++;
        }

        return placed > 0;
    }

    private boolean canReplace(BlockState state) {
        if (state.isAir()) return true;
        return state.isReplaceable();
    }

    private List<BlockPos> reservoirSampleSphere(StructureWorldAccess world, BlockPos center, int radius,
                                                 int target, int undergroundDepth,
                                                 Random random,
                                                 double sx, double sy, double sz) {
        List<BlockPos> reservoir = new ArrayList<>(Math.min(target, 64));
        int count = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double nx = dx / (double) radius / sx;
                    double ny = dy / (double) radius / sy;
                    double nz = dz / (double) radius / sz;
                    if (nx * nx + ny * ny + nz * nz <= 1.0) {
                        mutable.set(center.getX() + dx, center.getY() + dy - undergroundDepth, center.getZ() + dz);
                        int y = mutable.getY();
                        int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ());
                        if (y < world.getBottomY() || y >= topY) continue;

                        count++;
                        if (reservoir.size() < target) reservoir.add(mutable.toImmutable());
                        else {
                            int r = random.nextInt(count);
                            if (r < target) reservoir.set(r, mutable.toImmutable());
                        }
                    }
                }
            }
        }
        return reservoir;
    }


    private int calculateVolume(Random random, int maxVolume) {
        if (maxVolume <= 1) return 1;
        double log2 = Math.log(maxVolume) / Math.log(2);
        int minVolume = Math.max(1, (int) (maxVolume / log2));
        return minVolume + random.nextInt(maxVolume - minVolume + 1);
    }

    private BlockPos findSurfacePos(StructureWorldAccess world, BlockPos origin) {
        int startY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, origin.getX(), origin.getZ()) - 1;
        for (int y = startY; y >= world.getBottomY(); y--) {
            BlockPos pos = new BlockPos(origin.getX(), y, origin.getZ());
            if (!world.isAir(pos)) return pos.up();
        }
        return null;
    }
}