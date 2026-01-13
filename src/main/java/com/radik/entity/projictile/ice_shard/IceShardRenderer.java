package com.radik.entity.projictile.ice_shard;

import com.radik.Radik;
import com.radik.entity.projictile.bullet.BulletEntity;
import com.radik.entity.projictile.bullet.BulletEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class IceShardRenderer extends ProjectileEntityRenderer<IceShardEntity, ProjectileEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.of(Radik.MOD_ID, "textures/entity/ice_shard.png");
    private final IceShardModel model;

    public IceShardRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new IceShardModel(ctx.getPart(IceShardModel.ICE_SHARD_LAYER));
    }

    @Override
    public void render(@NotNull ProjectileEntityRenderState state, @NotNull MatrixStack matrixStack, @NotNull OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw));
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(state.pitch));
        matrixStack.translate(0, -1.2, 0);
        matrixStack.scale(1F, 1F, 1F);

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
    protected Identifier getTexture(ProjectileEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public ProjectileEntityRenderState createRenderState() {
        return new ProjectileEntityRenderState();
    }
}