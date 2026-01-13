package com.radik.mixin.book;

import net.minecraft.component.type.WritableBookContentComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({WritableBookContentComponent.class})
public class WritableBookContentComponentMixin {
    @ModifyConstant(
            method = {"<clinit>"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private static int maxPagesBound(int original) {
        return 32767;
    }

    @ModifyConstant(
            method = {"<init>"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private int maxPages(int original) {
        return 32767;
    }
}
