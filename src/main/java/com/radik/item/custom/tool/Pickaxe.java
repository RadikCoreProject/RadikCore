package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.block.custom.blockentity.event.EventBlockData;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.connecting.event.ChallengeType;
import com.radik.connecting.event.factory.BlockEventData;
import com.radik.logic.OnBreak;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public class Pickaxe extends Item implements Tools {
    public Pickaxe(ToolMaterial material, float attackDamage, float attackSpeed, Item.@NotNull Settings settings) {
        super(settings.pickaxe(material, attackDamage, attackSpeed));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean app = super.postMine(stack, world, state, pos, miner);
        if (miner instanceof ServerPlayerEntity && Boolean.TRUE.equals(stack.get(BOOL))) {
            switch (miner.getFacing()) {
                case NORTH, SOUTH, WEST, EAST -> breakBlocks(pos.up(), pos.down(), state, world, (ServerPlayerEntity) miner, stack);
                case null, default -> breakBlocks(pos.north(), pos.south(), state, world, (ServerPlayerEntity) miner, stack);
            }
        }
        return app;
    }

    private void breakBlocks(BlockPos first, BlockPos second, @NotNull BlockState f, @NotNull World world, ServerPlayerEntity entity, ItemStack stack) {
        Block b1 = world.getBlockState(first).getBlock();
        Block b2 = world.getBlockState(second).getBlock();
        Block b3 = f.getBlock();

        if (b1.equals(b3)) {
            world.breakBlock(first, true, entity);
            Radik.sendEventToPlayers(0, first, 0, (ServerWorld) world);
            stack.damage(1, entity);
        }

        if (b2.equals(b3)) {
            world.breakBlock(second, true, entity);
            Radik.sendEventToPlayers(0, second, 0, (ServerWorld) world);
            stack.damage(1, entity);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        if (event == null || bool == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik." + event.name().toLowerCase()));
        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + ".pickaxe"));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
