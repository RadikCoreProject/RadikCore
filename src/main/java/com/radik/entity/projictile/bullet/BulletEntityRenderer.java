package com.radik.entity.projictile.bullet;

import com.radik.Radik;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BulletEntityRenderer extends ProjectileEntityRenderer<BulletEntity, BulletEntityRenderer.BulletRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Radik.MOD_ID, "textures/entity/bullet.png");
    private final BulletEntityModel model;

    public BulletEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BulletEntityModel(ctx.getPart(BulletEntityModel.BULLET_LAYER));
    }

    @Override
    public void render(@NotNull BulletRenderState state, @NotNull MatrixStack matrixStack, @NotNull OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
        matrixStack.scale(0.1F, 0.1F, 0.1F);

        queue.submitModel(
            this.model,
            state,
            matrixStack,
            RenderLayer.getEntityCutout(this.getTexture(state)),
            state.light,
            OverlayTexture.DEFAULT_UV,
            state.outlineColor,
            null
        );
        matrixStack.pop();
    }

    @Override
    protected Identifier getTexture(BulletRenderState state) {
        return TEXTURE;
    }

    @Override
    public BulletRenderState createRenderState() {
        return new BulletRenderState();
    }

    @Override
    public void updateRenderState(BulletEntity entity, BulletRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.age = entity.age;
        state.tickDelta = tickDelta;
        state.light = LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    public static class BulletRenderState extends ProjectileEntityRenderState {
        public float tickDelta;
    }
}