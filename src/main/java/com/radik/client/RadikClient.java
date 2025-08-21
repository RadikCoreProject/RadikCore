package com.radik.client;

import com.radik.Radik;
import com.radik.client.packet.SendPacket;
import com.radik.connecting.Decoration;
import com.radik.connecting.Player;
import com.radik.packets.DecorationsPayload;
import com.radik.packets.LoginPayload;
import com.radik.registration.ClientRegistrationController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
public class RadikClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static ArrayList<Decoration> DECORATIONS;
    public static Player PLAYER;
    @Override
    public void onInitializeClient() {
        if (Radik.enablePackets) registerPackets();
        ClientRegistrationController.init(); // регистрация
    }

    // пакеты думаю можно оставить пока что, как никак они связаны с константами
    private void registerPackets() {
        CompletableFuture.runAsync(() -> {
            SendPacket.register();
            ClientPlayNetworking.registerGlobalReceiver(LoginPayload.ID, (payload, context) -> {
                PLAYER = payload.player();
                System.out.println("received Login Payload");
            });
            ClientPlayNetworking.registerGlobalReceiver(DecorationsPayload.ID, (payload, context) -> {
                DECORATIONS = payload.decorations();
                System.out.println("received Decoration Payload");
            });
        });
    }
}