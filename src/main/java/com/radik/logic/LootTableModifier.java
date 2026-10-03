package com.radik.logic;

import com.radik.block.RegisterBlocks;
import com.radik.item.RegisterItems;
import com.radik.item.custom.tool.Tools;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootTableModifier {
    private static final Map<Block, Item> AUTHOSMELT = new HashMap<>();

    static {
        AUTHOSMELT.put(Blocks.IRON_ORE, Items.IRON_INGOT);
        AUTHOSMELT.put(Blocks.DEEPSLATE_IRON_ORE, Items.IRON_INGOT);
        AUTHOSMELT.put(Blocks.GOLD_ORE, Items.GOLD_INGOT);
        AUTHOSMELT.put(Blocks.DEEPSLATE_GOLD_ORE, Items.GOLD_INGOT);
        AUTHOSMELT.put(Blocks.COPPER_ORE, Items.COPPER_INGOT);
        AUTHOSMELT.put(Blocks.DEEPSLATE_COPPER_ORE, Items.COPPER_INGOT);
        AUTHOSMELT.put(Blocks.COBBLESTONE, Items.STONE);
        AUTHOSMELT.put(Blocks.STONE, Items.SMOOTH_STONE);
        AUTHOSMELT.put(Blocks.COBBLED_DEEPSLATE, Items.DEEPSLATE);
        AUTHOSMELT.put(RegisterBlocks.LEAD_ORE, RegisterItems.LEAD_INGOT);
        AUTHOSMELT.put(RegisterBlocks.DEEPSLATE_LEAD_ORE, RegisterItems.LEAD_INGOT);
        AUTHOSMELT.put(RegisterBlocks.URANUS_ORE, RegisterItems.URANUS_INGOT);
        AUTHOSMELT.put(RegisterBlocks.DEEPSLATE_URANUS_ORE, RegisterItems.URANUS_INGOT);
        AUTHOSMELT.put(Blocks.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP);
        AUTHOSMELT.put(Blocks.NETHERRACK, Items.NETHER_BRICK);
    }

    protected static void register() {
        LootTableEvents.MODIFY_DROPS.register(LootTableModifier::modify);
    }

    private static void modify(RegistryEntry<LootTable> lootTableRegistryEntry, LootContext lootContext, List<ItemStack> itemStacks) {
        ItemStack tool = lootContext.get(LootContextParameters.TOOL);
        if (tool == null || !tool.isOf(RegisterItems.SUMMER_PICKAXE) || !Tools.activedPower(tool)) return;

        var manager = lootContext.getWorld().getRegistryManager();
        BlockState state = lootContext.get(LootContextParameters.BLOCK_STATE);
        var enchantmentRegistry = manager.getOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntry<Enchantment> silkEntry = enchantmentRegistry.getOrThrow(Enchantments.SILK_TOUCH);
        RegistryEntry<Enchantment> fortuneEntry = enchantmentRegistry.getOrThrow(Enchantments.FORTUNE);
        int silkLevel = EnchantmentHelper.getLevel(silkEntry, tool);
        int fortuneLevel = EnchantmentHelper.getLevel(fortuneEntry, tool);
        if (silkLevel > 0 || state == null) return;

        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack stack = itemStacks.get(i);
            Item smeltedItem = AUTHOSMELT.get(state.getBlock());
            if (smeltedItem == null) continue;
            int count = stack.getCount();
            if (fortuneLevel > 0) count = count * (1 + fortuneLevel);

            itemStacks.set(i, new ItemStack(smeltedItem, count));
        }
    }
}
