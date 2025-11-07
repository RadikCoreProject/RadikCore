package com.radik.client;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Function;

public class GasFluidRenderer implements FluidRenderHandler {
    private final Identifier stillTexture;
    private final Identifier flowingTexture;
    private final int color;
    private Sprite stillSprite;
    private Sprite flowingSprite;

    public GasFluidRenderer(Identifier still, Identifier flowing, int color) {
        this.stillTexture = still;
        this.flowingTexture = flowing;
        this.color = color;
    }

    @Override
    public void renderFluid(BlockPos pos, BlockRenderView world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        if (vertexConsumer == null) return;

        int level = fluidState.getLevel();
        float height = (level + 1) / 9f;

        Sprite[] sprites = getFluidSprites(world, pos, fluidState);
        if (sprites[0] == null) return;

        int light = world.getLightLevel(LightType.BLOCK, pos);
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        int packedLight = LightmapTextureManager.pack(light, skyLight);

        renderLayeredFluid(pos, world, vertexConsumer, sprites[0], height, packedLight);
    }

    private void renderLayeredFluid(@NotNull BlockPos pos, BlockRenderView world,
                                    @NotNull VertexConsumer vertexConsumer, @NotNull Sprite sprite,
                                    float height, int packedLight) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());

        float a = (color >> 24 & 0xFF) / 255f;
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        Vector3f normal = new Vector3f(0, 1, 0);

        vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), 0, height, 1)
                .color(r, g, b, a)
                .texture(sprite.getMinU(), sprite.getMaxV())
                .light(packedLight)
                .normal(normal.x, normal.y, normal.z);

        vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), 1, height, 1)
                .color(r, g, b, a)
                .texture(sprite.getMaxU(), sprite.getMaxV())
                .light(packedLight)
                .normal(normal.x, normal.y, normal.z);

        vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), 1, height, 0)
                .color(r, g, b, a)
                .texture(sprite.getMaxU(), sprite.getMinV())
                .light(packedLight)
                .normal(normal.x, normal.y, normal.z);

        vertexConsumer.vertex(matrixStack.peek().getPositionMatrix(), 0, height, 0)
                .color(r, g, b, a)
                .texture(sprite.getMinU(), sprite.getMinV())
                .light(packedLight)
                .normal(normal.x, normal.y, normal.z);
    }

    @Override
    public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
        return color;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Sprite[] getFluidSprites(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
        if (stillSprite == null || flowingSprite == null) {
            MinecraftClient client = MinecraftClient.getInstance();
            Function<Identifier, Sprite> atlas = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
            stillSprite = atlas.apply(stillTexture);
            flowingSprite = atlas.apply(flowingTexture);
        }
        return new Sprite[]{stillSprite, flowingSprite};
    }
}