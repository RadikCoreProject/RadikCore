package com.radik.mixin;

import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// ебал в рот шалав
@Mixin(UpdateStructureBlockC2SPacket.class)
public abstract class UpdateStructureBlockC2SPacketMixin {
    @Unique private static final int MAX_SIZE = 512;
    @Unique private static final int MIN_COORD = -MAX_SIZE;

    // редирект клампа пакетов СУКА
    @Redirect(
            method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"
            )
    )
    private int overrideClamp(int value, int min, int max) {
        if (min == -48 && max == 48) {
            return MathHelper.clamp(value, MIN_COORD, MAX_SIZE);
        } else if (min == 0 && max == 48) {
            return MathHelper.clamp(value, 0, MAX_SIZE);
        }
        return MathHelper.clamp(value, min, max);
    }

    // редирект позиции пакетов
    @Redirect(
            method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
            at = @At(
                    value = "NEW",
                    target = "(III)Lnet/minecraft/util/math/BlockPos;"
            )
    )
    private BlockPos createModifiedBlockPos(int x, int y, int z) {
        return new BlockPos(
                MathHelper.clamp(x, MIN_COORD, MAX_SIZE),
                MathHelper.clamp(y, MIN_COORD, MAX_SIZE),
                MathHelper.clamp(z, MIN_COORD, MAX_SIZE)
        );
    }

    // редирект инициализатора Vec3d
    // уебища моджанги идите нахуй
    @Redirect(
            method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
            at = @At(
                    value = "NEW",
                    target = "(III)Lnet/minecraft/util/math/Vec3i;"
            )
    )
    private Vec3i createModifiedVec3i(int x, int y, int z) {
        return new Vec3i(
                MathHelper.clamp(x, 0, MAX_SIZE),
                MathHelper.clamp(y, 0, MAX_SIZE),
                MathHelper.clamp(z, 0, MAX_SIZE)
        );
    }
}