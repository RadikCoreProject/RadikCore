package com.radik.ui.ducking;

import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public interface LivingEntityRenderStateDuck {
    default @Nullable Arm armmersive$getRenderingArm() {
        return null;
    }

    ItemRenderState wearThat$getChestItemRenderState();
    ItemRenderState wearThat$getLegsItemRenderState();
    ItemRenderState wearThat$getFeetItemRenderState();

    void wearThat$setEnchanted(boolean enchanted);
    boolean wearThat$isEnchanted();

    void wearThat$setColor(@Nullable Integer color);
    @Nullable Integer wearThat$getColor();

    void wearThat$mergeColor(int color);
    int wearThat$applyColor(int color);

    void wearThat$setEmissive(boolean emissive);
    boolean wearThat$isEmissive();
}
