package com.radik.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PlayerManager.class})
public class PlayerManagerMixin {
    @Redirect(
        method = "onPlayerConnect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V",
            ordinal = 0
        )
    )
    private void removeJoinMessage(PlayerManager instance, Text message, boolean overlay) {
    }
}
