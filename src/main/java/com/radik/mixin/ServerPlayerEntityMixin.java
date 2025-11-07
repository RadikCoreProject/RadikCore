package com.radik.mixin;

import com.radik.avancements.criteries.Criterias;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.network.ServerPlayerEntity$2")
public class ServerPlayerEntityMixin {
    @Shadow
    @Final
    ServerPlayerEntity field_29183;

    @Inject(
        method = "onSlotUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/criterion/InventoryChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;)V",
            shift = At.Shift.AFTER
        )
    )
    private void onInventoryChanged(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
        Criterias.MEDAL_CRITERION.trigger(this.field_29183, this.field_29183.getInventory());
    }
}