package com.radik.packets;

import com.radik.MainInit;
import com.radik.Radik;
import com.radik.block.custom.blockentity.embassy.EmbassyBlock;
import com.radik.block.custom.blockentity.embassy.EmbassyBlockEntity;
import com.radik.connecting.game.EmbassyData;
import com.radik.item.custom.reward.Teleporter;
import com.radik.item.custom.tool.Tools;
import com.radik.packets.payload.*;
import com.radik.packets.payload.IntegerPayload;
import com.radik.util.Duplet;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static com.radik.Data.*;

public class PacketRegistration {
    public static final ConcurrentHashMap<String, LocalDateTime> TELEPORTER_MAP = new ConcurrentHashMap<>();

    @MainInit
    public static void initialize() {
        if (!Radik.enablePackets) return;
        PayloadTypeRegistry.playS2C().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DecorationsPayload.ID, DecorationsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(IntegerPayload.ID, IntegerPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EventPayload.ID, EventPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TradeListPayload.ID, TradeListPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ChallengesDataPayload.ID, ChallengesDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(LeaderboardPayload.ID, LeaderboardPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ActionPayload.ID, ActionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StringPayload.ID, StringPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(LoginPayload.ID, LoginPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(VecPayload.ID, VecPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ActionPayload.ID, ActionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(PlayerSettingsPayload.ID, PlayerSettingsPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(IntegerPayload.ID, IntegerPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StringPayload.ID, StringPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(EmbassyPayload.ID, EmbassyPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ActionPayload.ID, PacketRegistration::registerAction);
        ServerPlayNetworking.registerGlobalReceiver(VecPayload.ID, PacketRegistration::registerTeleporter);
        ServerPlayNetworking.registerGlobalReceiver(EmbassyPayload.ID, PacketRegistration::registerPos);
    }

    private static void registerPos(EmbassyPayload payload, ServerPlayNetworking.Context context) {
        EmbassyData data = payload.data();
        ServerPlayerEntity player = context.player();
        String name = player.getName().getString();
        ServerWorld world = context.server().getOverworld();
        BlockPos fromPos = data.from;
        BlockState state = world.getBlockState(fromPos);

        if (!(state.getBlock() instanceof EmbassyBlock)) return;

        EmbassyBlockEntity be = (EmbassyBlockEntity) world.getBlockEntity(fromPos);
        if (be == null) return;
        EmbassyData current = be.getData(state, name);

        switch (payload.action()) {
            case EXCHANGE -> {
                if (current.equals(data)) return;
                if (name.equals(current.legate)) {
                    if (!data.legate.equals(current.legate) || !data.to.equals(current.to))
                        Radik.LOGGER.error("Посол не может изменить это");
                } else if (!name.equals("SkyGlue555"))
                    Radik.LOGGER.error("Нет прав на изменение посольства");
                be.setData(state, data);
            }
            case TP -> {
                if (!name.equals(current.owner) && !name.equals(current.legate)) {
                    Radik.LOGGER.error("Вы не владелец и не посол этого посольства");
                }

                if (LocalDateTime.now().isBefore(current.cooldown)) {
                    Radik.LOGGER.error("Телепортация ещё на кулдауне");
                }

                BlockPos other = current.to;
                if (other == null) {
                    player.sendMessage(Text.of("Целевое посольство не указано"));
                    return;
                }

                BlockState otherState = world.getBlockState(other);
                if (!(otherState.getBlock() instanceof EmbassyBlock)) {
                    player.sendMessage(Text.of("Целевой блок не является посольством"));
                    return;
                }

                if (state.get(EmbassyBlock.ACTIVE) < 2) {
                    player.sendMessage(Text.of("Это посольство отключено"));
                    return;
                }

                if (otherState.get(EmbassyBlock.ACTIVE) < 2) {
                    player.sendMessage(Text.of("Целевое посольство отключено"));
                    return;
                }

                be.cooldown = LocalDateTime.now().plusMinutes(15);
                player.teleport(other.getX() + 0.5, other.getY() + 1, other.getZ() + 0.5, true);
                be.markDirty();
            }
            default -> throw new IllegalStateException("Unexpected value: " + payload.data());
        }
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
        ServerWorld world = player.getEntityWorld();
        String name = player.getName().getString();
        LocalDateTime time = TELEPORTER_MAP.get(name);
        LocalDateTime now = LocalDateTime.now();
        if (time != null) {
            return;
        }
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        Integer teleporter = stack.get(TELEPORTER);
        String owner = stack.get(OWNER);
        String dimension = getDimension(world);
        Duplet<Integer, Boolean> duplet = Teleporter.calculateCooldown(stack, pos, player.getEntityPos(), world);
        Integer cooldown = duplet.type();
        Boolean b = duplet.parametrize();

        if (cooldown == null || b == null) return;
        if (teleporter != null && name.equals(owner) && b && !dimension.equals("end")) {
            stack.set(POSITION, player.getEntityPos());
            player.teleport(
                world,
                pos.x,
                pos.y,
                pos.z,
                PositionFlag.DELTA,
                player.getYaw(),
                player.getPitch(),
                true
            );
            player.sendMessage(Text.of("Cooldown: " + cooldown + "s"));
            TELEPORTER_MAP.put(name, now.plusSeconds(cooldown));
            ServerPlayNetworking.send(player, new IntegerPayload(cooldown, null, PacketType.TELEPORTER_COOLDOWN));
            TELEPORTER_MAP.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
        }
    }
}
