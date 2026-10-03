package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.gui.PlayerListGui;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public interface Tools {
    enum Tool {
        PICKAXE,
        AXE,
        HOE,
        SWORD,
        SHOVEL
    }

    static void mine3x1(ItemStack stack, ServerWorld world, BlockState state, BlockPos pos, ServerPlayerEntity miner) {
        switch (miner.getFacing()) {
            case NORTH, SOUTH, WEST, EAST -> breakBlocks(pos.up(), pos.down(), state, world, miner, stack);
            case null, default -> {
                float yaw = miner.getYaw();
                BlockPos pos1 = pos.east(), pos2 = pos.west();
                // i love magic numbers
                if (yaw < -135 || yaw > 135 || (yaw < 45 && yaw > -45)) {
                    pos1 = pos.north();
                    pos2 = pos.south();
                }
                breakBlocks(pos1, pos2, state, world, miner, stack);
            }
        }
    }

    private static void breakBlocks(BlockPos first, BlockPos second, @NotNull BlockState f, @NotNull World world, ServerPlayerEntity entity, ItemStack stack) {
        Block b1 = world.getBlockState(first).getBlock();
        Block b2 = world.getBlockState(second).getBlock();
        Block b3 = f.getBlock();

        if (b1.equals(b3)) breakBlock(first, world, entity, stack);
        if (b2.equals(b3)) breakBlock(second, world, entity, stack);
    }

    static void breakBlock(BlockPos pos, @NotNull World world, ServerPlayerEntity entity, @NotNull ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        ChallengeEvent s = stack.get(EVENT_TYPE);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        world.breakBlock(pos, false, entity);
        block.afterBreak(world, entity, pos, state, blockEntity, stack);

        Radik.sendEventToPlayers(s == null ? 0 : s.ordinal() - 1, pos, 0, (ServerWorld) world);
        stack.damage(1, entity);
    }

    static boolean hasSilkTouch(@NotNull ItemStack stack) {
        return stack.getEnchantments().getEnchantments().contains(RegistryEntry.of(Enchantments.SILK_TOUCH));
    }

    static void appendTooltip(@NotNull ItemStack stack, Consumer<Text> textConsumer, Tool toolType) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        if (event == null || bool == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + "." + toolType.toString().toLowerCase()));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
    }

    static boolean activedPower(ItemStack stack) {
        return stack != null && Boolean.TRUE.equals(stack.get(BOOL));
    }

    static ChallengeEvent toolType(ItemStack stack) {
        return stack.get(EVENT_TYPE);
    }
}
