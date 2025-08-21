package com.radik.mixin;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.gui.screen.ingame.StructureBlockScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// мрази откройте апи
@Mixin(StructureBlockScreen.class)
public abstract class StructureBlockScreenMixin {
    @Unique
    private static final int MAX_SIZE = 512;
    @Unique
    private static final int MIN_COORD = -MAX_SIZE;
    @Unique
    private static final int MAX_COORD = MAX_SIZE;

    @Shadow
    private TextFieldWidget inputPosX;
    @Shadow
    private TextFieldWidget inputPosY;
    @Shadow
    private TextFieldWidget inputPosZ;
    @Shadow
    private TextFieldWidget inputSizeX;
    @Shadow
    private TextFieldWidget inputSizeY;
    @Shadow
    private TextFieldWidget inputSizeZ;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        clampTextField(inputPosX, MIN_COORD, MAX_COORD);
        clampTextField(inputPosY, MIN_COORD, MAX_COORD);
        clampTextField(inputPosZ, MIN_COORD, MAX_COORD);
        clampTextField(inputSizeX, 0, MAX_SIZE);
        clampTextField(inputSizeY, 0, MAX_SIZE);
        clampTextField(inputSizeZ, 0, MAX_SIZE);
    }

    @Unique
    private void clampTextField(TextFieldWidget field, int min, int max) {
        try {
            int value = Integer.parseInt(field.getText());
            int clamped = MathHelper.clamp(value, min, max);
            if (value != clamped) {
                field.setText(String.valueOf(clamped));
            }
        } catch (NumberFormatException e) {
        }
    }

    @Redirect(
            method = "updateStructureBlock",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/network/packet/c2s/play/UpdateStructureBlockC2SPacket"
            )
    )
    private UpdateStructureBlockC2SPacket modifyPacket(
            BlockPos pos, StructureBlockBlockEntity.Action action, StructureBlockMode mode, String templateName, BlockPos offset, Vec3i size, BlockMirror mirror, BlockRotation rotation, String metadata, boolean ignoreEntities, boolean strict, boolean showAir, boolean showBoundingBox, float integrity, long seed
    ) {
        BlockPos clampedOffset = new BlockPos(
                MathHelper.clamp(offset.getX(), MIN_COORD, MAX_COORD),
                MathHelper.clamp(offset.getY(), MIN_COORD, MAX_COORD),
                MathHelper.clamp(offset.getZ(), MIN_COORD, MAX_COORD)
        );

        Vec3i clampedSize = new Vec3i(
                MathHelper.clamp(size.getX(), 0, MAX_SIZE),
                MathHelper.clamp(size.getY(), 0, MAX_SIZE),
                MathHelper.clamp(size.getZ(), 0, MAX_SIZE)
        );

        return new UpdateStructureBlockC2SPacket(
                pos, action, mode, templateName,
                clampedOffset, clampedSize,
                mirror, rotation, metadata,
                ignoreEntities, strict, showAir, showBoundingBox,
                integrity, seed
        );
    }
}