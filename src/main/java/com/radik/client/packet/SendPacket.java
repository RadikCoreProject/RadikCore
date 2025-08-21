package com.radik.client.packet;

import com.radik.packets.LoginPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static com.radik.client.RadikClient.PLAYER;

public class SendPacket {
    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPlayNetworking.send(new LoginPayload(PLAYER));
        });
    }
}
