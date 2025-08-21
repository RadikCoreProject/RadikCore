package com.radik.mixin;

import net.minecraft.component.type.WritableBookContentComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.text.RawFilteredPair;

import java.util.List;

@Mixin({WritableBookContentComponent.class})
public class WritableBookContentComponentMixin {
    public WritableBookContentComponentMixin() {
    }

    @ModifyConstant(
            method = {"<clinit>"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private static int longbooks$returnUpperBoundMaxPages(int original) {
        return 32767;
    }

    @ModifyConstant(
            method = {"<init>"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private int longbooks$returnMaxPages(int original) {
        return 32767;
    }
}
