package com.radik.block.custom;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.DyeColor;

public abstract class BlockData {
    public static final IntProperty COUNT = IntProperty.of("count", 1, 5);
    public static final IntProperty TYPE = IntProperty.of("type", 1, 9);
    public static final IntProperty TYPE2 = IntProperty.of("type", 1, 72);
    public static final BooleanProperty FACING = BooleanProperty.of("facing");
    public static final IntProperty FACING2 = IntProperty.of("facing", 0, 3);
    public static final IntProperty WATER_LEVEL = IntProperty.of("lvl", 0, 24);
}
