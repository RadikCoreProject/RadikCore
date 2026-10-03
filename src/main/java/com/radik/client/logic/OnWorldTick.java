package com.radik.client.logic;

import com.radik.packets.Action;
import com.radik.packets.payload.ActionPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import static com.radik.client.RadikClient.keyBinding;

public class OnWorldTick {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(OnWorldTick::onTick);
    }

    private static void onTick(MinecraftClient client) {
        while (keyBinding.wasPressed()) {
            ClientPlayNetworking.send(new ActionPayload(Action.CHANGE_ABILITY));
        }
    }
}
