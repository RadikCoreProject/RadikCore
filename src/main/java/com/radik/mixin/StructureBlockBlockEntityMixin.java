package com.radik.mixin;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// моджанги уебища
@Mixin(StructureBlockBlockEntity.class)
public abstract class StructureBlockBlockEntityMixin {
    @Unique
    private boolean bypassProtection = true;

    // геттер для поля
    @Unique
    public boolean getBypassProtection() {
        return this.bypassProtection;
    }

    // сеттер для поля
    @Unique
    public void setBypassProtection(boolean bypass) {
        this.bypassProtection = bypass;
    }

    @ModifyConstant(
            method = "readNbt",
            constant = {
                    @Constant(intValue = 48, ordinal = 3),
                    @Constant(intValue = 48, ordinal = 4),
                    @Constant(intValue = 48, ordinal = 5)
            }
    )
    private int overrideSizeLimits(int original) {
        return 512;
    }

    @Redirect(
            method = "detectStructureSize",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/BlockPos;getX()I"
            )
    )
    private int redirectSearchRadius(BlockPos instance) {
        return 512;
    }

    @Contract("_ -> new")
    @ModifyArg(
            method = "saveStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/Identifier;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3i;ZLjava/lang/String;Z)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/structure/StructureTemplate;saveFromWorld(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3i;ZLnet/minecraft/block/Block;)V"
            ),
            index = 2
    )
    private static @NotNull Vec3i overrideSizeLimit(@NotNull Vec3i original) {
        return new Vec3i(
                Math.min(original.getX(), 512),
                Math.min(original.getY(), 512),
                Math.min(original.getZ(), 512)
        );
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeBypassNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
        nbt.putBoolean("bypassProtection", this.bypassProtection);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readBypassNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
        Optional<Boolean> b = nbt.getBoolean("bypassProtection");
        b.ifPresent(aBoolean -> this.bypassProtection = aBoolean);
    }

    @Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
    private void bypassProtectionCheck(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        StructureBlockBlockEntity self = (StructureBlockBlockEntity) (Object) this;
        if (this.bypassProtection) {
            if (player.getWorld().isClient()) {
                player.openStructureBlockScreen(self);
            }
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}