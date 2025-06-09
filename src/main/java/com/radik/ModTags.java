package com.radik;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> FROGLIGHTS_BLOCK = createTag("froglights_block");
        public static final TagKey<Block> BRICKS_BLOCK = createTag("bricks_block");


        private static TagKey<net.minecraft.block.Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Radik.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> FROGLIGHTS_ITEM = createTag("froglights_item");
        public static final TagKey<Item> BRICKS_ITEM = createTag("bricks_item");
        public static final TagKey<Item> WINTER_STONE = createTag("winter_stone");
        public static final TagKey<Item> SUGAR = createTag("sugar");
        public static final TagKey<Item> SCARF = createTag("scarf");
        public static final TagKey<Item> CONCRETE = createTag("concrete");
        public static final TagKey<Item> CONCRETE_POWDER = createTag("concrete_powder");
        public static final TagKey<Item> CHRISTMAS_FOOD = createTag("christmas_food");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, name));
        }
    }



    public static void registerTags() {}
}
