package com.radik.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @Shadow
    private LivingEntity shooter;

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
            )
    )
    private void modifyElytraBoost(LivingEntity entity, Vec3d velocity) {
        if (entity == this.shooter && entity.isGliding()) {
            Vec3d rotation = entity.getRotationVector();
            Vec3d currentVel = entity.getVelocity();

            double boostMultiplier = 0.5;
            double baseBoost = 0.03;

            Vec3d newVelocity = new Vec3d(
                    currentVel.x + rotation.x * baseBoost + (rotation.x * boostMultiplier - currentVel.x) * 0.3,
                    currentVel.y + rotation.y * baseBoost + (rotation.y * boostMultiplier - currentVel.y) * 0.3,
                    currentVel.z + rotation.z * baseBoost + (rotation.z * boostMultiplier - currentVel.z) * 0.3
            );

            entity.setVelocity(newVelocity);
        } else {
            entity.setVelocity(velocity);
        }
    }
}