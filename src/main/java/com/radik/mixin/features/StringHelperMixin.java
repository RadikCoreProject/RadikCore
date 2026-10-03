package com.radik.mixin.features;

import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StringHelper.class)
public abstract class StringHelperMixin {
    @Inject(
        at = @At("HEAD"),
        method = "isValidChar(I)Z",
        cancellable = true
    )
    private static void isValidChar(int c, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(c >= 32 && c != 127);
        cir.cancel();
    }
}
