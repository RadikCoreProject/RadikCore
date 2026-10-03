package com.radik.entity.projictile.water_drop;

import com.radik.Radik;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;

public class WaterDropEntityModel extends EntityModel<ProjectileEntityRenderState> {
	public static final EntityModelLayer WATER_DROP_LAYER = new EntityModelLayer(Identifier.of(Radik.MOD_ID, "water_drop"), "main");
	private final ModelPart blob;
	private final ModelPart bubbles;
	public WaterDropEntityModel(ModelPart root) {
        super(root);
        this.blob = root.getChild("blob");
		this.bubbles = root.getChild("bubbles");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData blob = modelPartData.addChild("blob", ModelPartBuilder.create().uv(0, 0).cuboid(-7.1F, -1.6F, -2.6F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 8).cuboid(-3.1F, -0.6F, -1.6F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 8).cuboid(-1.1F, -0.1F, -1.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(5.1F, -1F, 0.6F));

		ModelPartData bubbles = modelPartData.addChild("bubbles", ModelPartBuilder.create().uv(12, 8).cuboid(0.9F, -0.1F, -1.85F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 12).cuboid(1.9F, -0.1F, -1.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 12).cuboid(2.9F, 0.4F, -2.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 10).cuboid(-0.1F, -0.1F, -0.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.origin(5.1F, -1F, 0.6F));
		return TexturedModelData.of(modelData, 16, 16);
	}
}