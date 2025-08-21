package com.radik.packets;

import com.radik.registration.IRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.radik.Data.*;

public class PacketRegistration implements IRegistry {
    private static final ConcurrentHashMap<String, LocalDateTime> TELEPORTER_MAP = new ConcurrentHashMap<>();

    public static void initialize() {
        LOGGER.info("Registering Packets");
        PayloadTypeRegistry.playS2C().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DecorationsPayload.ID, DecorationsPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TeleportPayload.ID, TeleportPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TeleportPayload.ID, PacketRegistration::registerTeleporter);

    }

    private static void registerTeleporter(@NotNull TeleportPayload teleportPayload, ServerPlayNetworking.@NotNull Context context) {
        Vec3d pos = teleportPayload.pos();
        ServerPlayerEntity player = context.player();
        String name = player.getName().getString();
        LocalDateTime time = TELEPORTER_MAP.get(name);
        LocalDateTime now = LocalDateTime.now();
        if (time != null) {
            if (time.isAfter(now)) {
                player.sendMessage(Text.literal("Cooldown: ").append(Text.literal(Duration.between(now, time).getSeconds() + "s").formatted(Formatting.RED)));
                return;
            }
        }
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        Integer teleporter = stack.get(TELEPORTER);
        String owner = stack.get(OWNER);
        double distance = pos.distanceTo(player.getPos());

        if (teleporter != null && name.equals(owner) && distance <= getDistance(teleporter)) {
            player.teleport(
                    (ServerWorld) player.getWorld(),
                    pos.x,
                    pos.y,
                    pos.z,
                    PositionFlag.DELTA,
                    player.getYaw(),
                    player.getPitch(),
                    true
            );
            stack.set(POSITION, player.getPos());
            int cooldown = (int) (distance / 5.612 * getTeleporterK(teleporter));
            TELEPORTER_MAP.put(name, now.plusSeconds(cooldown));
            player.sendMessage(Text.literal("Cooldown: ").append(Text.literal(cooldown + "s").formatted(Formatting.RED)));
            TELEPORTER_MAP.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
            return;
        }
        player.networkHandler.disconnect(Text.literal("§4INVALID PACKET ERROR\n§9Why do you want to cheat?\nCheat report sent to Administrator"));
    }
}
