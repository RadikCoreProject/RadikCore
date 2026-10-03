package com.radik.item;

import com.radik.ModTags;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class ToolMaterials {
    public static ToolMaterial STAFF = new ToolMaterial(ModTags.Blocks.STAFFABLE, 1000, 1.0F, 4.0F, 22, ModTags.Items.STAFFABLE);
    public static ToolMaterial HALLOWEEN = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2500, 10.0F, 5.0F, 8, ItemTags.NETHERITE_TOOL_MATERIALS);
    public static ToolMaterial FLOWERY = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 4500, 8.0F, 4.0F, 13, ItemTags.NETHERITE_TOOL_MATERIALS);
    public static ToolMaterial SUMMER = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 3500, 10.0F, 6.0F, 10, ItemTags.NETHERITE_TOOL_MATERIALS);
    public static final ToolMaterial LEAD = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 3000, 8.0F, 3.0F, 10, ModTags.Items.REPAIRS_LEAD_ARMOR);
    public static final ToolMaterial GERMAN_SWORD = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 250, 5.0F, 3.0F, 30, null);

}