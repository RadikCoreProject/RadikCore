package com.radik.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityLifeMixin {

    @Shadow
    private int lifeTime;

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL")
    )
    private void onConstruct(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        FireworksComponent component = stack.get(DataComponentTypes.FIREWORKS);
        int duration = (component != null) ? component.flightDuration() : 0;

        FireworkRocketEntity self = (FireworkRocketEntity) (Object) this;
        this.lifeTime = 6 * (1 + duration) +
                self.getRandom().nextInt(2) +
                self.getRandom().nextInt(2);
    }
}