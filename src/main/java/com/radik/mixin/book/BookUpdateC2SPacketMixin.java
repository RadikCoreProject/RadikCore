package com.radik.mixin.book;

import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BookUpdateC2SPacket.class)
public abstract class BookUpdateC2SPacketMixin {
    @ModifyConstant(
            method = {"<clinit>"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private static int maxPages(int original) {
        return 32767;
    }

    @ModifyConstant(
            method = {"<clinit>"},
            constant = {@Constant(
                    intValue = 32
            )}
    )
    private static int titleLength(int original) { return 32767; }
}
