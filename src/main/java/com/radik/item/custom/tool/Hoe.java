package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.EventProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public class Hoe extends HoeItem implements Tools {
    private static final HashMap<Block, Item> CROPS = new HashMap<>();

    static {
        CROPS.put(Blocks.WHEAT, Items.WHEAT_SEEDS);
        CROPS.put(Blocks.BEETROOTS, Items.BEETROOT_SEEDS);
        CROPS.put(Blocks.POTATOES, Items.POTATO);
        CROPS.put(Blocks.CARROTS, Items.CARROT);
    }

    public Hoe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        if (event == null || bool == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik." + event.name().toLowerCase()));
        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + ".hoe"));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return super.canMine(stack, state, world, pos, user) && (state.getBlock() instanceof CropBlock && state.get(CropBlock.AGE) == 7 || !(state.getBlock() instanceof CropBlock));
    }

    public boolean postMine(@NotNull ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (Boolean.TRUE.equals(stack.get(BOOL))) {
            Block block = state.getBlock();
            Item need_item = CROPS.get(block);
            if (need_item != null) {
                ((PlayerEntity) miner).getInventory().forEach(stack1 -> {
                    if (stack1.getItem().equals(need_item)) {
                        stack1.decrement(1);
                        world.setBlockState(pos, block.getDefaultState());
                        Radik.sendEventToPlayers(0, pos, 0, (ServerWorld) world);
                        if (Radik.RANDOM.nextInt(1, EventProperties.get("green_candy_drop_chance") + 1) == 1)
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(RegisterItems.CANDY_GREEN, 1)));
                    }
                });
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        super.useOnBlock(context);
        ItemStack stack = context.getStack();
        if (Boolean.TRUE.equals(stack.get(BOOL))) {
            BlockPos blockPos = context.getBlockPos();
            World world = context.getWorld();
            Block block = world.getBlockState(blockPos).getBlock();
            if (block.equals(Blocks.DRAGON_HEAD)) {
                if (Radik.RANDOM.nextInt(64) == 5) world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                world.spawnEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(Items.DRAGON_BREATH)));
                stack.damage(1, context.getPlayer());
            }
        }
        return ActionResult.SUCCESS;
    }
}
