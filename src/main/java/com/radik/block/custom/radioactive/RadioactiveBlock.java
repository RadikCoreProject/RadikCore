package com.radik.block.custom.radioactive;

import com.radik.ModTags;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.world.biome.RegisterBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public class RadioactiveBlock extends Block implements Radioactive {
    private final int doza;

    public RadioactiveBlock(Settings settings, int doza) {
        super(settings);
        this.doza = doza;
    }

    @Override
    public void randomTick(@NotNull BlockState state, ServerWorld world, BlockPos pos, @NotNull Random random) {
        if (doza >= 5) Radioactive.spread(random, pos, world);
    }

    @Override
    public void onSteppedOn(@NotNull World world, BlockPos pos, @NotNull BlockState state, Entity entity) {
        Radioactive.onSteppedOn(world, pos, entity, 2000 - 200 * doza);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, @NotNull PlayerEntity player) {
        if (!player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.ANTI_RADIOACTIVE))
            Radioactive.onBroken(world, pos, Math.max(1, (30 - doza) * (player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.LEAD_INSTRUMENT) ? 2 : 1)));
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world instanceof ServerWorld serverWorld)
            Radioactive.replaceBiomeColumn(serverWorld, pos, RegisterBiomes.GINGER_FOREST, t -> true);
    }
}
