package com.radik.mixin.minecart;

import com.radik.property.InGameProperties;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ExperimentalMinecartController.class})
public abstract class ExperimentalMinecartControllerMixin implements MinecartControllerAccessor {
    /**
     * @author Radik
     * @reason bc gameRule not follow
     */
    @Overwrite
    public double getMaxSpeed(ServerWorld world) {
        return InGameProperties.get("minecart_multiplier") * (getMinecart().isTouchingWater() ? 0.5 : 1.0) / 20.0;
    }
}
