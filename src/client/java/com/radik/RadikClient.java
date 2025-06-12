package com.radik;

import com.radik.fluid.RegisterFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class RadikClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Регистрация рендера жидкости
        FluidRenderHandlerRegistry.INSTANCE.register(
                RegisterFluids.STILL_HYDROGEN,
                RegisterFluids.FLOWING_HYDROGEN,
                new SimpleFluidRenderHandler(
                        Identifier.of("radik", "block/fluid/hydrogen_still"),
                        Identifier.of("radik", "block/fluid/hydrogen_flow")
                )
        );

        // Установка прозрачного рендер-слоя
        BlockRenderLayerMap.INSTANCE.putBlock(
                RegisterFluids.HYDROGEN_BLOCK,
                RenderLayer.getTranslucent()
        );

        FluidRenderHandlerRegistry.INSTANCE.register(
                RegisterFluids.STILL_HYDROGEN,
                RegisterFluids.FLOWING_HYDROGEN,
                new HydrogenFluidRenderer()
        );
    }
}