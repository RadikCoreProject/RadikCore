package com.radik.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.component.type.WritableBookContentComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({AnvilScreen.class})
public class AnvilScreenMixin {

    @ModifyConstant(
            method = {"drawForeground(Lnet/minecraft/client/gui/DrawContext;II)V"},
            constant = {@Constant(
                    intValue = 40
            )}
    )
    private int maxAnvilCost(int original) {
        return 4096;
    }
}
