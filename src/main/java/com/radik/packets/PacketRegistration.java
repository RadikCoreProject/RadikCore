package com.radik.packets;

import com.radik.Radik;
import com.radik.block.custom.blockentity.event.*;
import com.radik.item.custom.Teleporter;
import com.radik.item.custom.tool.Tools;
import com.radik.packets.payload.*;
import com.radik.registration.IRegistry;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.radik.Data.*;

public class PacketRegistration implements IRegistry {
    private static final ConcurrentHashMap<String, LocalDateTime> TELEPORTER_MAP = new ConcurrentHashMap<>();

    public static void initialize() {
        LOGGER.info("Registering Packets");
        PayloadTypeRegistry.playS2C().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DecorationsPayload.ID, DecorationsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(IntegerPayload.ID, IntegerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EventPayload.ID, EventPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TradeListPayload.ID, TradeListPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ChallengesDataPayload.ID, ChallengesDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LeaderboardPayload.ID, LeaderboardPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(VecPayload.ID, VecPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ActionPayload.ID, ActionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PlayerSettingsPayload.ID, PlayerSettingsPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(IntegerPayload.ID, IntegerPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ActionPayload.ID, PacketRegistration::registerAction);
        ServerPlayNetworking.registerGlobalReceiver(VecPayload.ID, PacketRegistration::registerTeleporter);
    }

    private static void registerAction(@NotNull ActionPayload actionPayload, ServerPlayNetworking.@NotNull Context context) {
        Action action = actionPayload.action();
        ServerPlayerEntity player = context.player();
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        switch (action) {
            case CHANGE_ABILITY -> {
                if (stack.getItem() instanceof Tools) {
                    Boolean bool = stack.get(BOOL);
                    if (bool == null) stack.set(BOOL, false);
                    else stack.set(BOOL, !bool);
                }
            }
        }
    }

    private static void registerTeleporter(@NotNull VecPayload vecPayload, ServerPlayNetworking.@NotNull Context context) {
        Vec3d pos = vecPayload.pos();
        ServerPlayerEntity player = context.player();
        String name = player.getName().getString();
        LocalDateTime time = TELEPORTER_MAP.get(name);
        LocalDateTime now = LocalDateTime.now();
        if (time != null) {
            if (time.isAfter(now)) {
                Radik.kickPlayer(player);
                return;
            }
        }
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        Integer teleporter = stack.get(TELEPORTER);
        String owner = stack.get(OWNER);
        String dimension = getDimension(context.player().getWorld());
        Duplet<Integer, Boolean> duplet = Teleporter.calculateCooldown(stack, pos, player.getPos(), player.getWorld());

        if (duplet.parametrize() == null || duplet.type() == null) {
            Radik.kickPlayer(player);
            return;
        }
        int cooldown = duplet.type();
        if (teleporter != null && name.equals(owner) && duplet.parametrize() && !dimension.equals("end")) {
            stack.set(POSITION, player.getPos());
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
            TELEPORTER_MAP.put(name, now.plusSeconds(cooldown));
            ServerPlayNetworking.send(player, new IntegerPayload(new Triplet<>(cooldown, null, PacketType.TELEPORTER_COOLDOWN)));
            TELEPORTER_MAP.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
            return;
        }
        Radik.kickPlayer(player);
    }
}
