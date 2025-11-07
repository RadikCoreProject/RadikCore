package com.radik.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({AnvilScreenHandler.class})
public class AnvilScreenHandlerMixin {
    @ModifyConstant(
            method = {"updateResult()V"},
            constant = {@Constant(
                    intValue = 40
            )}
    )
    private int maxAnvilCost(int original) {
        return 4096;
    }
}
