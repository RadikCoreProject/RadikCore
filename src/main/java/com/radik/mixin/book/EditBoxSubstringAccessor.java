package com.radik.mixin.book;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.client.gui.EditBox$Substring")
public interface EditBoxSubstringAccessor {
    @Accessor("beginIndex")
    int getBeginIndex();

    @Accessor("endIndex")
    int getEndIndex();
}