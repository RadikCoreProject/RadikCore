package com.radik.block.custom.radioactive;

import com.radik.ModTags;
import com.radik.block.RegisterBlocks;
import com.radik.effect.custom.RadiationEffect;
import com.radik.world.biome.RegisterBiomes;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public interface Radioactive {
    static void onBroken(@NotNull WorldAccess world, BlockPos pos, int chance) {
        if (world.isClient()) return;
        PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, true);
        if (player != null) {
            RadiationEffect.applyRadiation(player, (World) world, pos, chance);
            RegistryKey<Biome> key1 = world.getBiome(pos.north()).getKey().orElse(null);
            RegistryKey<Biome> key2 = world.getBiome(pos.south()).getKey().orElse(null);
            RegistryKey<Biome> key3 = world.getBiome(pos.west()).getKey().orElse(null);
            RegistryKey<Biome> key4 = world.getBiome(pos.east()).getKey().orElse(null);
            boolean b = setBiome((ServerWorld) world, key1, pos.east());
            if (b) setBiome((ServerWorld) world, key2, pos.south());
            if (b) setBiome((ServerWorld) world, key3, pos.west());
            if (b) setBiome((ServerWorld) world, key4, pos.east());
        }
    }

    static void onBroken(WorldAccess world, BlockPos pos, PlayerEntity player) {
        onBroken(world, pos, player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.LEAD_INSTRUMENT) ? 70 : 7);
    }

    private static boolean setBiome(ServerWorld world, RegistryKey<Biome> biome, BlockPos pos) {
        if (biome == null) return false;
        Identifier val = biome.getValue();
        if (!val.equals(RegisterBiomes.GINGER_FOREST.getValue())) {
            replaceBiomeColumn(world, pos, biome, b -> b.getKey().orElseThrow().equals(RegisterBiomes.GINGER_FOREST));
            return true;
        }
        return false;
    }

    static void spread(Random random, BlockPos pos, ServerWorld world) {
        if (random.nextInt(5) != 0) return;

        BlockPos pos2 = pos.add(random.nextBetween(-1, 2), random.nextBetween(-1, 2), random.nextBetween(-1, 2));
        BlockState state2 = world.getBlockState(pos2);
        Block block = state2.getBlock();

        if (block instanceof Radioactive) return;

        boolean s = true;
        BlockState states = Blocks.AIR.getDefaultState();
        if (block instanceof GrassBlock || block instanceof MyceliumBlock || block.getDefaultState().isOf(Blocks.PODZOL))
            states = RegisterBlocks.RADIOACTIVE_GRASS.getDefaultState();
        else if (block instanceof LeavesBlock)
            states = RegisterBlocks.RADIOACTIVE_LEAVES.getDefaultState();
        else if (block.getDefaultState().isIn(BlockTags.LOGS))
            states = RegisterBlocks.RADIOACTIVE_LOG.getDefaultState().with(PillarBlock.AXIS, state2.get(PillarBlock.AXIS));
        else if (block instanceof PlantBlock || block instanceof TallPlantBlock || block == Blocks.SHORT_GRASS || block == Blocks.FERN)
            states = Blocks.DEAD_BUSH.getDefaultState();
        else if (block == Blocks.IRON_BLOCK)
            states = RegisterBlocks.RADIOACTIVE_IRON_BLOCK.getDefaultState();
        else if (block instanceof OxidizableBlock)
            states = RegisterBlocks.RADIOACTIVE_COPPER_BLOCK.getDefaultState();
        else if (block == RegisterBlocks.LEAD_BLOCK)
            states = RegisterBlocks.LEAD_RADIOACTIVE_BLOCK.getDefaultState();
        else if (block instanceof OxidizableStairsBlock) states = RegisterBlocks.RADIOACTIVE_COPPER_STAIRS.getDefaultState()
            .with(StairsBlock.WATERLOGGED, state2.get(StairsBlock.WATERLOGGED))
            .with(StairsBlock.FACING, state2.get(StairsBlock.FACING))
            .with(StairsBlock.HALF, state2.get(StairsBlock.HALF))
            .with(StairsBlock.SHAPE, state2.get(StairsBlock.SHAPE));
        else if (block instanceof OxidizableSlabBlock) states = RegisterBlocks.RADIOACTIVE_COPPER_SLAB.getDefaultState()
            .with(SlabBlock.WATERLOGGED, state2.get(SlabBlock.WATERLOGGED))
            .with(SlabBlock.TYPE, state2.get(SlabBlock.TYPE));
        else s = false;

        if (s) world.setBlockState(pos2, states);

        RegistryKey<Biome> biome = world.getBiome(pos2).getKey().orElse(null);
        if (s && biome != null && !biome.getValue().equals(RegisterBiomes.GINGER_FOREST.getValue())) {
            replaceBiomeColumn(world, pos2, RegisterBiomes.GINGER_FOREST, (b) -> true);
        }
    }

    static void onSteppedOn(@NotNull World world, BlockPos pos, Entity entity, int chance) {
        if (!world.isClient() && entity instanceof LivingEntity living) {
            RadiationEffect.applyRadiation(living, world, pos, chance);
        }
    }

    static void onSteppedOn(@NotNull World world, BlockPos pos, Entity entity) {
        onSteppedOn(world, pos, entity, 500);
    }

    static void replaceBiomeColumn(ServerWorld world, BlockPos pos, RegistryKey<Biome> targetBiomeKey, Predicate<RegistryEntry<Biome>> filter) {
        RegistryEntryLookup<Biome> biomeLookup = world.getRegistryManager().getOrThrow(RegistryKeys.BIOME);
        RegistryEntry.Reference<Biome> targetEntry = biomeLookup.getOrThrow(targetBiomeKey);
        Chunk chunk = world.getChunk(pos);

        int minBlockY = Math.max(world.getBottomY(), pos.getY() - 10);
        int maxBlockY = Math.min(319, pos.getY() + 10);

        int minBiomeX = pos.getX() >> 2;
        int minBiomeZ = pos.getZ() >> 2;

        BlockBox box = BlockBox.create(
            new BlockPos(minBiomeX << 2, minBlockY, minBiomeZ << 2),
            new BlockPos((minBiomeX << 2) + 3, maxBlockY, (minBiomeZ << 2) + 3)
        );

        BiomeSupplier supplier = (x, y, z, noise) -> {
            int blockX = x << 2;
            int blockY = y << 2;
            int blockZ = z << 2;
            if (box.contains(blockX, blockY, blockZ)) {
                RegistryEntry<Biome> current = chunk.getBiomeForNoiseGen(x, y, z);
                if (filter.test(current)) {
                    return targetEntry;
                }
            }
            return chunk.getBiomeForNoiseGen(x, y, z);
        };

        chunk.populateBiomes(supplier, world.getChunkManager().getNoiseConfig().getMultiNoiseSampler());
        chunk.markNeedsSaving();
        world.getChunkManager().chunkLoadingManager.sendChunkBiomePackets(List.of(chunk));
    }
}
