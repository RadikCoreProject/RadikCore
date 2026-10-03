package com.radik.entity.projictile.water_drop;

import com.radik.Radik;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class WaterDropEntityRenderer extends ProjectileEntityRenderer<WaterDropEntity, WaterDropEntityRenderer.WaterDropRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Radik.MOD_ID, "textures/entity/water_drop.png");
    private final WaterDropEntityModel model;

    public WaterDropEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new WaterDropEntityModel(ctx.getPart(WaterDropEntityModel.WATER_DROP_LAYER));
    }

    @Override
    public void render(@NotNull WaterDropRenderState state, @NotNull MatrixStack matrixStack, @NotNull OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw + 270.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch + 180));
        matrixStack.scale(1F, 1F, 1F);

        queue.submitModel(
            this.model,
            state,
            matrixStack,
            RenderLayers.entityTranslucent(this.getTexture(state)),
            state.light,
            OverlayTexture.DEFAULT_UV,
            state.outlineColor,
            null
        );
        matrixStack.pop();
    }

    @Override
    protected Identifier getTexture(WaterDropRenderState state) {
        return TEXTURE;
    }

    @Override
    public WaterDropRenderState createRenderState() {
        return new WaterDropRenderState();
    }

    @Override
    public void updateRenderState(WaterDropEntity entity, WaterDropRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.age = entity.age;
        state.tickDelta = tickDelta;
        state.light = LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    public static class WaterDropRenderState extends ProjectileEntityRenderState {
        public float tickDelta;
    }
}