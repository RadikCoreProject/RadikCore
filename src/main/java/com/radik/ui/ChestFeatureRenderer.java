package com.radik.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ChestFeatureRenderer<S extends BipedEntityRenderState, M extends BipedEntityModel<S>>
    extends FeatureRenderer<S, M> {

    private static final float SCALE = 0.8F;
    private static final float Y_OFFSET = 0.2F;

    public ChestFeatureRenderer(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, @NotNull S state, float limbAngle, float limbDistance) {
        ItemStack chestStack = state.equippedChestStack;

        if (!chestStack.isEmpty() && shouldRenderAsBlock(chestStack)) {
            matrices.push();
            this.getContextModel().getRootPart().applyTransform(matrices);
            if (this.getContextModel().body != null) {
                this.getContextModel().body.applyTransform(matrices);
            }

            setupChestTransformation(matrices, state);

            BlockState blockState = Block.getBlockFromItem(chestStack.getItem()).getDefaultState();
            if (blockState == null || blockState.isAir()) {
                matrices.pop();
                return;
            }

            VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(
                blockState,
                matrices,
                vertexConsumers,
                light,
                OverlayTexture.DEFAULT_UV
            );

            matrices.pop();
        }
    }

    @Contract(pure = true)
    private boolean shouldRenderAsBlock(@NotNull ItemStack stack) {
        return stack.getItem() instanceof BlockItem;
    }

    private void setupChestTransformation(@NotNull MatrixStack matrices, @NotNull S state) {
        matrices.translate(0.0F, Y_OFFSET, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.scale(SCALE, -SCALE, -SCALE);

        if (state.isInPose(EntityPose.SLEEPING)) {
            matrices.translate(0.0F, 0.1F, 0.0F);
        }

        if (state.limbSwingAmplitude > 0.0F) {
            float swing = MathHelper.sin(state.limbSwingAnimationProgress * 0.5F) * 0.1F * state.limbSwingAmplitude;
            matrices.translate(0.0F, swing, 0.0F);
        }
    }

    private void renderBlockOnChest(@NotNull ItemStack itemStack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            BlockState blockState = blockItem.getBlock().getDefaultState();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockState, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        }
    }
}
