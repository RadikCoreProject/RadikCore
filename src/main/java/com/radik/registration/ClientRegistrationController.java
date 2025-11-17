package com.radik.registration;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.event.*;
import com.radik.client.GasFluidRenderer;
import com.radik.property.client.SettingsProperties;
import com.radik.fluid.RegisterFluids;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;

public class ClientRegistrationController {
    public static void init() {
        SettingsProperties.initialize();
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.BATUT, RenderLayer.getCutout());
        HandledScreens.register(Radik.EVENT_SCREEN_HANDLER, EventScreen::new);
        HandledScreens.register(Radik.SHOP_SCREEN_HANDLER, ShopScreen::new);
        HandledScreens.register(Radik.SHOP_ACCESS_SCREEN_HANDLER, ShopAccessScreen::new);
        HandledScreens.register(Radik.CHALLENGES_SCREEN_HANDLER, ChallengesScreen::new);
        HandledScreens.register(Radik.LEADERBOARD_SCREEN_HANDLER, LeaderboardScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderLayer.getTranslucent(),
                RegisterFluids.HYDROGEN_BLOCK,
                RegisterFluids.HELIUM_BLOCK
        );

        registerGasRenderer(
                RegisterFluids.STILL_HYDROGEN,
                RegisterFluids.FLOWING_HYDROGEN,
                Identifier.of(Radik.MOD_ID, "block/fluid/hydrogen_still"),
                Identifier.of(Radik.MOD_ID, "block/fluid/hydrogen_flow")
        );

        registerGasRenderer(
                RegisterFluids.STILL_HELIUM,
                RegisterFluids.FLOWING_HELIUM,
                Identifier.of(Radik.MOD_ID, "block/fluid/helium_still"),
                Identifier.of(Radik.MOD_ID, "block/fluid/helium_flow")
        );
    }

    private static void registerGasRenderer(FlowableFluid still, FlowableFluid flowing, Identifier stillTexture, Identifier flowingTexture) {
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing);
        FluidRenderHandlerRegistry.INSTANCE.register(still, flowing, new GasFluidRenderer(stillTexture, flowingTexture, 0x30FFFFFF));
    }
}
