package com.radik.mixin;

import net.minecraft.component.type.WrittenBookContentComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin({WrittenBookContentComponent.class})
public class WrittenBookContentComponentMixin {
    public WrittenBookContentComponentMixin() {
    }

    @ModifyConstant(
            method = {"method_57520"},
            constant = {@Constant(
                    intValue = 32
            )}
    )
    private static int longbooks$returnUpperBoundMaxTitleLengthForCodec(int original) {
        return 32767;
    }

    @ModifyConstant(
            method = {"<clinit>"},
            constant = {@Constant(
                    intValue = 32
            )}
    )
    private static int longbooks$returnUpperBoundMaxTitleLengthForPacketCodec(int original) {
        return 32767;
    }
}
