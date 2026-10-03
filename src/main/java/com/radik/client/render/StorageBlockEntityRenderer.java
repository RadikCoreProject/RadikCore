package com.radik.client.render;

import com.radik.Data;
import com.radik.block.custom.blockentity.storage.StorageBlock;
import com.radik.block.custom.blockentity.storage.StorageBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class StorageBlockEntityRenderer implements BlockEntityRenderer<StorageBlockEntity, StorageBlockEntityRenderer.StorageBlockEntityRenderState> {
    private final ItemModelManager manager;
    private final TextRenderer textRenderer;

    public StorageBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.manager = ctx.itemModelManager();
        this.textRenderer = ctx.textRenderer();
    }

    @Override
    public StorageBlockEntityRenderState createRenderState() {
        return new StorageBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(StorageBlockEntity blockEntity, StorageBlockEntityRenderState state,
                                  float tickDelta, Vec3d cameraPos,
                                  ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickDelta, cameraPos, crumblingOverlay);

        if (blockEntity.type == null) return;
        state.itemStack = blockEntity.type.copy();
        state.count = blockEntity.filling;
        state.maxCount = blockEntity.storage;
        state.facing = blockEntity.getCachedState().get(StorageBlock.FACING);
        state.hasItem = !state.itemStack.isEmpty();
        if (blockEntity.getWorld() != null) state.lightmapCoordinates = blockEntity.getWorld().getLightLevel(blockEntity.getPos());

        if (state.hasItem) {
            this.manager.update(
                state.itemRenderState,
                state.itemStack,
                ItemDisplayContext.FIXED,
                blockEntity.getWorld(),
                null,
                0
            );
        }
    }

    @Override
    public void render(StorageBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (!state.hasItem) return;
        Direction facing = state.facing;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.translate(facing.getOffsetX() * 0.4375, facing.getOffsetY() * 0.4375, facing.getOffsetZ() * 0.4375);

        if (facing.getAxis().isHorizontal()) {
            float yaw = 180.0F - facing.getPositiveHorizontalDegrees();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        } else {
            float pitch = facing == Direction.UP ? -90.0F : 90.0F;
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        }

        matrices.push();
        matrices.translate(0, 0.09, -0.1);
        matrices.scale(0.5f, 0.5f, 0.5f);
        Box box = state.itemRenderState.getModelBoundingBox();
        double max = Math.max(Math.max(box.maxX, box.maxY), box.maxZ);
        double min = Math.min(Math.min(box.minX, box.minY), box.minZ);
        float f = (float) (0.6 / (max - min));
        matrices.scale(f, f, f);
        state.itemRenderState.render(matrices, queue, 15728880, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();

        matrices.push();
        matrices.translate(0, 0.09, -0.02);

        float magicNumber = 0.010416667F * 1.5F;
        matrices.scale(-magicNumber, -magicNumber, magicNumber);

        int c = state.count;
        int color = 0xFF000000 + Data.getColorInvert((float) c / state.maxCount);
        String countStr = String.valueOf(c);
        float countWidth = -textRenderer.getWidth(countStr) / 2f;
        queue.submitText(matrices, countWidth, 20, Text.literal(countStr).asOrderedText(),
            false,
            TextRenderer.TextLayerType.POLYGON_OFFSET,
            15728880,
            color,
            0,
            shouldRenderTextOutline(state.pos) ? ColorHelper.scaleRgb(color, 0.4F) : 0);

        matrices.pop();
        matrices.pop();
    }

    public static class StorageBlockEntityRenderState extends BlockEntityRenderState {
        public ItemStack itemStack = ItemStack.EMPTY;
        public int count = 0;
        public int maxCount = 0;
        public Direction facing = Direction.NORTH;
        public boolean hasItem = false;
        public final ItemRenderState itemRenderState = new ItemRenderState();
    }

    private static boolean shouldRenderTextOutline(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null && client.options.getPerspective().isFirstPerson() && player.isUsingSpyglass()) {
            return true;
        } else {
            Entity entity = client.getCameraEntity();
            return entity != null && entity.squaredDistanceTo(Vec3d.ofCenter(pos)) < MathHelper.square(16);
        }
    }
}

