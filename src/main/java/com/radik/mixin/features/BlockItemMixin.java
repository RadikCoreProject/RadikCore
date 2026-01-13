package com.radik.mixin.features;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.radik.Data.*;
import static com.radik.block.RegisterBlocks.*;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(
        method = "canPlace",
        at = @At("HEAD")
    )
    private static void canPlace(@NotNull ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            cir.setReturnValue(context.canPlace());
            return;
        }
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(FREEZER.asItem()) && !getDimension(player.getEntityWorld()).equals("overworld")) {
            cir.setReturnValue(false);
        }
    }
}
