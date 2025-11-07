package com.radik.client.packet;

import com.radik.Radik;
import com.radik.property.client.SettingsProperties;
import com.radik.property.client.Property;
import com.radik.connecting.PlayerSettings;
import com.radik.packets.payload.LoginPayload;
import com.radik.packets.payload.PlayerSettingsPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;

import static com.radik.client.RadikClient.PLAYER;

public class SendPacket {
    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
//            if (true) {
            if (!handler.getConnection().isLocal()) {
                try {
                    ClientPlayNetworking.send(new LoginPayload(PLAYER));
                    ClientPlayNetworking.send(new PlayerSettingsPayload(new PlayerSettings(
                        SettingsProperties.getBoolean(Property.NETHER_PLACES),
                        SettingsProperties.getBoolean(Property.OVERWORLD_PLACES),
                        SettingsProperties.getBoolean(Property.PRESENT_NOTIFY),
                        SettingsProperties.getBoolean(Property.ME_NOTIFY)
                    )));
                } catch (Exception e) {
                    client.disconnect(new DisconnectedScreen(new MultiplayerScreen(new TitleScreen()), Text.of("DISCONNECTED"), Text.of("INVALID PACKET")));
                }

            }
        });
    }
}
