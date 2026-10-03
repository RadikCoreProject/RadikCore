package com.radik.behavior;

import com.radik.block.RegisterBlocks;
import com.radik.item.RegisterItems;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class FuelBehavior {
    private final static int itemSmeltTime = 200;

    public static void initialize() {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(RegisterItems.URANUS_INGOT, itemSmeltTime * 500);
            builder.add(RegisterItems.URANUS_NUGGET, itemSmeltTime * 50);
            builder.add(RegisterBlocks.RADIOACTIVE_LOG, itemSmeltTime * 3 / 2);
        });
    }
}
