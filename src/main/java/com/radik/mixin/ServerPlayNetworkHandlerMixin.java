package com.radik.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

import static com.radik.mixin.Data.*;

@Mixin({ServerPlayNetworkHandler.class})
public class ServerPlayNetworkHandlerMixin {
    public ServerPlayNetworkHandlerMixin() {
    }

//    @ModifyConstant(
//            method = {"onBookUpdate"},
//            constant = {@Constant(
//                    longValue = 100L
//            )}
//    )
//    private long longbooks$returnMaxPages(long original) {
//        return MAX_BOOK_PAGES;
//    }

    @ModifyExpressionValue(
            method = {"onBookUpdate"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/c2s/play/BookUpdateC2SPacket;title()Ljava/util/Optional;"
            )}
    )
    private Optional<String> longbooks$trimTitleIfNeeded(Optional<String> original) {
        return original.map((str) -> str.length() >= MAX_TITLE_WORDS ? str.substring(0, MAX_TITLE_WORDS) : str);
    }
}
