package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;

public class Shovel extends ShovelItem implements Tools {
    private static final HashMap<Block, Item[]> proccess = new HashMap<>();
    static {
        proccess.put(Blocks.SAND, new Item[]{Items.DEAD_BUSH, Items.CACTUS, Items.OAK_SAPLING});
        proccess.put(Blocks.RED_SAND, new Item[]{Items.DEAD_BUSH, Items.CACTUS});
        proccess.put(Blocks.DIRT, new Item[]{Items.POTATO, Items.BEETROOT_SEEDS, Items.CARROT, Items.BUSH, Items.ACACIA_SAPLING});
        proccess.put(Blocks.GRASS_BLOCK, new Item[]{Items.POTATO, Items.BEETROOT_SEEDS, Items.CARROT, Items.WHEAT_SEEDS, Items.BIRCH_SAPLING, Items.CHERRY_SAPLING, Items.OAK_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING});
        proccess.put(Blocks.PODZOL, new Item[]{Items.SPRUCE_SAPLING, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BUSH, Items.WILDFLOWERS});
        proccess.put(Blocks.MYCELIUM, new Item[]{Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.CORNFLOWER, Items.TORCHFLOWER_SEEDS, Items.PALE_OAK_SAPLING});
    }

    public Shovel(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        if (event == null || bool == null) {return;}

        textConsumer.accept(Text.translatable("tooltip.radik." + event.name().toLowerCase()));
        textConsumer.accept(Text.translatable("tooltip.radik.instrument." + event.name().toLowerCase() + ".shovel"));
        textConsumer.accept(Text.translatable("tooltip.radik.super").append(Text.translatable("tooltip.radik." + bool)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean post = super.postMine(stack, world, state, pos, miner);
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        Block block = state.getBlock();
        if (event == null || bool == null) return post;

        if (bool && proccess.containsKey(block)) {
            if (Radik.RANDOM.nextInt(1, 50) == 5) {
                Item[] item = proccess.get(block);
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item[Radik.RANDOM.nextInt(item.length)])));
            }
        }
        return post;
    }
}
