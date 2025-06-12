package com.radik;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class HydrogenFluidRenderer implements FluidRenderHandler {
    private static final Identifier STILL_TEXTURE = Identifier.of("radik", "block/fluid/hydrogen_still");
    private static final Identifier FLOWING_TEXTURE = Identifier.of("radik", "block/fluid/hydrogen_flow");

    private Sprite stillSprite;
    private Sprite flowingSprite;

    @Override
    public void renderFluid(BlockPos pos, BlockRenderView world, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        if (stillSprite == null || flowingSprite == null) {
            return;
        }

        Sprite sprite = fluidState.isStill() ? stillSprite : flowingSprite;

        int color = getFluidColor(world, pos, fluidState);

        RenderSystem.setShaderColor(
                (color >> 16 & 255) / 255f,
                (color >> 8 & 255) / 255f,
                (color & 255) / 255f,
                (color >> 24 & 255) / 255f
        );

        Tessellator tessellator = Tessellator.getInstance();
    }

    @Override
    public Sprite[] getFluidSprites(@Nullable BlockRenderView blockRenderView, @Nullable BlockPos blockPos, FluidState fluidState) {
        if (stillSprite == null || flowingSprite == null) {
            stillSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(STILL_TEXTURE);
            flowingSprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(FLOWING_TEXTURE);
        }
        return new Sprite[]{stillSprite, flowingSprite};
    }


    @Override
    public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
        return 0x88B3FFFF;
    }
}
