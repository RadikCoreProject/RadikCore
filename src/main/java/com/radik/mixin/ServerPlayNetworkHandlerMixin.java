package com.radik.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

import static com.radik.MixinData.*;

@Mixin({ServerPlayNetworkHandler.class})
public class ServerPlayNetworkHandlerMixin {
    @ModifyExpressionValue(
            method = "onBookUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/c2s/play/BookUpdateC2SPacket;title()Ljava/util/Optional;"
            )
    )
    private Optional<String> trim(@NotNull Optional<String> original) {
        return original.map((str) -> str.length() >= MAX_TITLE_WORDS ? str.substring(0, MAX_TITLE_WORDS) : str);
    }
}
