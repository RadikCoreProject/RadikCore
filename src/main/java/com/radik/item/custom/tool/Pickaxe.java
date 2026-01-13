package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

import java.lang.reflect.Method;

public class Pickaxe extends Item implements Tools {
    private static Method method = null;
    private static boolean init = false;

    public Pickaxe(ToolMaterial material, float attackDamage, float attackSpeed, Item.@NotNull Settings settings) {
        super(settings.pickaxe(material, attackDamage, attackSpeed));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean app = super.postMine(stack, world, state, pos, miner);
        if (!world.isClient() && Boolean.TRUE.equals(stack.get(BOOL))) {
            invokeServerHandler(state, world, pos, (ServerPlayerEntity) miner, stack);
        }
        return app;
    }

    private void invokeServerHandler(BlockState state, World world, BlockPos pos,
                                     ServerPlayerEntity miner, ItemStack stack) {
        try {
            if (!init) {
                initialize();
                init = true;
            }
            if (method != null) method.invoke(null, state, world, pos, miner, stack);
            else mine(state, world, pos, miner, stack);
        } catch (Exception e) {
            if (!world.isClient()) Radik.LOGGER.error(e.getMessage());
        }
    }

    private static synchronized void initialize() {
        try {
            Class<?> handlerClass = Class.forName("com.radik.server.reflection.PickaxeHandler");
            method = handlerClass.getMethod(
                "handlePickaxeMine",
                BlockState.class,
                World.class,
                BlockPos.class,
                ServerPlayerEntity.class,
                ItemStack.class
            );
            Radik.LOGGER.error("re");
        } catch (ClassNotFoundException e) {
            Radik.LOGGER.error("de");
            method = null;
        } catch (Exception e) {
            Radik.LOGGER.error("ne");
            method = null;
            Radik.LOGGER.error(e.getMessage());
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

    public static void mine(BlockState state, World world, BlockPos pos, ServerPlayerEntity miner, ItemStack stack) {
        switch (miner.getFacing()) {
            case NORTH, SOUTH, WEST, EAST -> breakBlocks(pos.up(), pos.down(), state, world, miner, stack);
            case null, default -> breakBlocks(pos.north(), pos.south(), state, world, miner, stack);
        }
    }

    private static void breakBlocks(BlockPos first, BlockPos second, @NotNull BlockState f, @NotNull World world, ServerPlayerEntity entity, ItemStack stack) {
        Block b1 = world.getBlockState(first).getBlock();
        Block b2 = world.getBlockState(second).getBlock();
        Block b3 = f.getBlock();

        if (b1.equals(b3)) {
            world.breakBlock(first, true, entity);
            stack.damage(1, entity);
        }

        if (b2.equals(b3)) {
            world.breakBlock(second, true, entity);
            stack.damage(1, entity);
        }
    }
}
