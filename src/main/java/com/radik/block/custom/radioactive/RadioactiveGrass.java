package com.radik.block.custom.radioactive;

import com.radik.ModTags;
import com.radik.world.biome.RegisterBiomes;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadioactiveGrass extends GrassBlock implements Radioactive {
    public RadioactiveGrass(@NotNull Settings settings) {
        super(settings.mapColor(MapColor.ORANGE).ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.ANTI_RADIOACTIVE))
            Radioactive.onBroken(world, pos, player);
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, @NotNull Random random) {
        Radioactive.spread(random, pos, world);
    }

    @Override
    public void onSteppedOn(@NotNull World world, BlockPos pos, BlockState state, Entity entity) {
        Radioactive.onSteppedOn(world, pos, entity);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world instanceof ServerWorld serverWorld)
            Radioactive.replaceBiomeColumn(serverWorld, pos, RegisterBiomes.GINGER_FOREST, t -> true);
    }
}
