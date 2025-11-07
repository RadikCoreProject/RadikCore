package com.radik.item.custom;

import com.radik.ModTags;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class Materials {
    public static ToolMaterial STAFF = new ToolMaterial(ModTags.Blocks.STAFFABLE, 1000, 1.0F, 4.0F, 22, ModTags.Items.STAFFABLE);
    public static ToolMaterial HALLOWEEN = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2500, 9.0F, 5.0F, 8, ItemTags.NETHERITE_TOOL_MATERIALS);
}