package com.radik.entity.projictile.bullet;

import com.radik.Radik;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class BulletEntityModel extends EntityModel<BulletEntityRenderer.BulletRenderState> {
	public static final EntityModelLayer BULLET_LAYER = new EntityModelLayer(Identifier.of(Radik.MOD_ID, "bullet"), "main");

	public BulletEntityModel(ModelPart root) {
		super(root, RenderLayer::getEntityCutout);
	}

	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild(
			"bullet",
			ModelPartBuilder.create()
				.uv(0, 0)
				.cuboid(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F),
			ModelTransform.NONE
		);

		return TexturedModelData.of(modelData, 32, 32);
	}
}
