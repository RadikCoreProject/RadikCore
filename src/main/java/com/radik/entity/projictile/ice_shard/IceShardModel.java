package com.radik.entity.projictile.ice_shard;

import com.radik.Radik;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class IceShardModel extends EntityModel<ProjectileEntityRenderState> {
	public static final EntityModelLayer ICE_SHARD_LAYER = new EntityModelLayer(Identifier.of(Radik.MOD_ID, "ice_shard"), "main");
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	private final ModelPart bb_main;

	public IceShardModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
		this.bone2 = root.getChild("bone2");
		this.bone3 = root.getChild("bone3");
		this.bone4 = root.getChild("bone4");
		this.bb_main = root.getChild("bb_main");
	}

	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = bone.addChild("cube_r1", ModelPartBuilder.create().uv(42, 14).cuboid(0.0F, -1.0F, 0.5F, 0.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.1682F, -1.236F, -1.1379F, 0.0928F, 0.0924F, -0.7811F));

		ModelPartData cube_r2 = bone.addChild("cube_r2", ModelPartBuilder.create().uv(8, 42).cuboid(0.0F, 0.0F, 0.5F, 0.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 24).cuboid(0.0F, -1.0F, -4.5F, 0.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-0.3318F, -1.736F, -1.1379F, 0.0928F, 0.0924F, -0.7811F));

		ModelPartData cube_r3 = bone.addChild("cube_r3", ModelPartBuilder.create().uv(0, 42).cuboid(0.0F, -2.0F, -4.5F, 1.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.1682F, -1.236F, -1.1379F, 0.0928F, 0.0924F, -0.7811F));

		ModelPartData bone2 = modelPartData.addChild("bone2", ModelPartBuilder.create(), ModelTransform.origin(-2.0F, 19.925F, 0.0F));

		ModelPartData cube_r4 = bone2.addChild("cube_r4", ModelPartBuilder.create().uv(28, 38).cuboid(0.0F, 0.0F, 0.5F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.236F, 0.1682F, -1.1379F, -0.0924F, 0.0928F, -0.7811F));

		ModelPartData cube_r5 = bone2.addChild("cube_r5", ModelPartBuilder.create().uv(38, 24).cuboid(-1.0F, 0.0F, 0.5F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 14).cuboid(-1.0F, 0.0F, -4.5F, 2.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.736F, -0.3318F, -1.1379F, -0.0924F, 0.0928F, -0.7811F));

		ModelPartData cube_r6 = bone2.addChild("cube_r6", ModelPartBuilder.create().uv(38, 36).cuboid(0.0F, 0.0F, -4.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.236F, 0.1682F, -1.1379F, -0.0924F, 0.0928F, -0.7811F));

		ModelPartData bone3 = modelPartData.addChild("bone3", ModelPartBuilder.create(), ModelTransform.origin(2.0F, 18.0F, 0.0F));

		ModelPartData cube_r7 = bone3.addChild("cube_r7", ModelPartBuilder.create().uv(24, 42).cuboid(0.0F, 0.0F, 0.5F, 0.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 42).cuboid(-1.0F, 0.0F, -4.5F, 1.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.1682F, 1.236F, -1.1379F, -0.0928F, -0.0924F, -0.7811F));

		ModelPartData cube_r8 = bone3.addChild("cube_r8", ModelPartBuilder.create().uv(42, 19).cuboid(0.0F, -1.0F, 0.5F, 0.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 31).cuboid(0.0F, -1.0F, -4.5F, 0.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.3318F, 1.736F, -1.1379F, -0.0928F, -0.0924F, -0.7811F));

		ModelPartData bone4 = modelPartData.addChild("bone4", ModelPartBuilder.create(), ModelTransform.origin(0.0F, 13.925F, 0.0F));

		ModelPartData cube_r9 = bone4.addChild("cube_r9", ModelPartBuilder.create().uv(38, 32).cuboid(-1.0F, 0.0F, 0.5F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(2.764F, 7.9818F, -1.1379F, 0.0924F, -0.0928F, -0.7811F));

		ModelPartData cube_r10 = bone4.addChild("cube_r10", ModelPartBuilder.create().uv(38, 28).cuboid(0.0F, 0.0F, 0.5F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F))
		.uv(28, 19).cuboid(-1.0F, 0.0F, -4.5F, 2.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.264F, 8.4818F, -1.1379F, 0.0924F, -0.0928F, -0.7811F));

		ModelPartData cube_r11 = bone4.addChild("cube_r11", ModelPartBuilder.create().uv(38, 40).cuboid(-2.0F, -1.0F, -4.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(2.764F, 7.9818F, -1.1379F, 0.0924F, -0.0928F, -0.7811F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(32, 44).cuboid(0.0F, -3.9862F, -6.045F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r12 = bb_main.addChild("cube_r12", ModelPartBuilder.create().uv(28, 0).cuboid(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -2.5F, 4.0F, 0.1309F, 0.0F, 0.0F));

		ModelPartData cube_r13 = bb_main.addChild("cube_r13", ModelPartBuilder.create().uv(0, 28).cuboid(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -3.5F, 4.0F, -0.1309F, 0.0F, 0.0F));

		ModelPartData cube_r14 = bb_main.addChild("cube_r14", ModelPartBuilder.create().uv(0, 14).cuboid(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -3.0F, 4.0F, 0.0F, 0.1309F, 0.0F));

		ModelPartData cube_r15 = bb_main.addChild("cube_r15", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -10.0F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -3.0F, 4.0F, 0.0F, -0.1309F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
}