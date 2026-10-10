package com.radik.mixin.book;

import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EditBoxWidget.class)
public interface EditBoxWidgetAccessor {
    @Accessor("editBox")
    EditBox getEditBox();
}