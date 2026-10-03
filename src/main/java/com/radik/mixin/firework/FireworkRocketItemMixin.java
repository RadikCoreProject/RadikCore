package com.radik.mixin.firework;

import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketItem.class)
public abstract class FireworkRocketItemMixin {

    @ModifyArg(
            method = "useOnBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/ProjectileEntity;spawn(Lnet/minecraft/entity/projectile/ProjectileEntity;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/ProjectileEntity;"
            )
    )
    private ProjectileEntity modifySpawn(ProjectileEntity projectile) {
        if (projectile instanceof FireworkRocketEntity firework) {
            Vec3d velocity = firework.getVelocity();
            firework.setVelocity(
                    velocity.x * 0.5,
                    velocity.y * 0.5,
                    velocity.z * 0.5
            );
        }
        return projectile;
    }
}