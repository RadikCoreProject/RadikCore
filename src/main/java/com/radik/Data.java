package com.radik;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.custom.reward.Medal;
import com.radik.item.custom.chemistry.VialContainer;
import com.radik.packets.Packets;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.radik.Codecs.VEC_3D;
import static com.radik.block.RegisterBlocks.*;

public final class Data {
    public static MinecraftServer SERVER;
    public static final String MOD_ID = "radik";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static List<Character> colors = List.of('l', '4', 'c', '6', 'e', '2', 'a', 'b', '3', '1', '9', 'd', '5', '7', '8', '0');

    public static final HashMap<String, FoodComponent> FOOD_COMPONENTS = new HashMap<>();
    public static final ComponentType<Boolean> BOOL = register("bool", builder -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));
    public static final ComponentType<String> OWNER = register("owner", builder -> builder.codec(Codecs.PLAYER_NAME).packetCodec(PacketCodecs.STRING));
    public static final ComponentType<Vec3d> POSITION = register("pos", builder -> builder.codec(VEC_3D).packetCodec(Packets.VEC3D));
    public static final ComponentType<Integer> CAPSULE_LEVEL = register("level", (builder) -> builder.codec(Codecs.rangedInt(0, 8)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> CAPSULE_FLUID = register("fluid", (builder) -> builder.codec(Codecs.rangedInt(0, 4)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> TELEPORTER = register("type", (builder) -> builder.codec(Codecs.rangedInt(0, 2)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<ChallengeEvent> EVENT_TYPE = register("event_type", (builder) -> builder.codec(com.radik.Codecs.EVENT_TYPE).packetCodec(Packets.EVENT_TYPE_CODEC));
    public static final ComponentType<Integer> MEDAL = register("medal", (builder) -> builder.codec(Codecs.rangedInt(0, Medal.MAX_TYPE)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> MEDAL_MATERIAL = register("material", (builder) -> builder.codec(Codecs.rangedInt(0, Medal.MAX_MATERIAL)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<String> TEXT = register("text", builder -> builder.codec(Codecs.PLAYER_NAME).packetCodec(PacketCodecs.STRING));
    public static final ComponentType<Integer> JAR = register("jar_type", (builder) -> builder.codec(Codecs.rangedInt(0, 1)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> STORAGE = register("storage", builder -> builder.codec(Codecs.rangedInt(0, 400)).packetCodec(PacketCodecs.VAR_INT));

    // TESTTTT
    public static final ComponentType<Integer> STAFF_TYPE = register("staff_type", (builder) -> builder.codec(Codecs.rangedInt(0, 3)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> STAFF_ATTACKS = register("staff_attacks", (builder) -> builder.codec(Codecs.rangedInt(0, 500)).packetCodec(PacketCodecs.VAR_INT));

    // CHEMISTRY
    public static final ComponentType<VialContainer> VIAL_CONTAINER = register("container", (builder) -> builder.codec(com.radik.Codecs.VIAL_CONTAINER).packetCodec(Packets.VIAL_CONTAINER_CODEC));

    public static final HashMap<String, HashMap<String, LocalDateTime>> MANA_USES_TIMER = new HashMap<>();
    public static final HashMap<String, Integer> MANA_TYPES = new HashMap<>();

    static {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        fireBlock.registerFlammableBlock(BRAID_SLAB_1, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_SLAB_2, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_SLAB_3, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_SLAB_5, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_SLAB_6, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_SLAB_7, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_1, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_2, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_3, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_5, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_6, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_STAIR_7, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_1, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_2, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_3, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_5, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_6, 5, 20);
        fireBlock.registerFlammableBlock(BRAID_7, 5, 20);
        MANA_TYPES.put("wind_staff_base", 3);
        MANA_TYPES.put("wind_staff_super", 120);
        MANA_TYPES.put("wind_staff_ultra", 5);
    }

    static {
        FOOD_COMPONENTS.put("wine", new FoodComponent(10, 1F, true));
        FOOD_COMPONENTS.put("ledenets", new FoodComponent(3, 0.6F, false));
        FOOD_COMPONENTS.put("ledenets1", new FoodComponent(5, 0.6F, false));
        FOOD_COMPONENTS.put("ledenets2", new FoodComponent(5, 0.3F, false));
        FOOD_COMPONENTS.put("1", new FoodComponent(8, 0.75F, false));
        FOOD_COMPONENTS.put("2", new FoodComponent(5, 1F, false));
        FOOD_COMPONENTS.put("3", new FoodComponent(5, 0.4F, false));
        FOOD_COMPONENTS.put("4", new FoodComponent(4, 0.15F, false));
        FOOD_COMPONENTS.put("empty", new FoodComponent(0, 0F, true));
        FOOD_COMPONENTS.put("candy", new FoodComponent(2, 2F, false));
    }

    @Contract(pure = true)
    public static byte getDecorationType(@NotNull String v) {
        return switch (v) {
            case "bold" -> 0;
            case "color" -> 1;
            case "crone" -> 2;
            case "particle" -> 3;
            default -> -1;
        };
    }

    public static String getDimension(@NotNull World world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    public static String getDimension(@NotNull WorldAccess world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    private static <T>ComponentType<T> register(String name, @NotNull UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void command(String command) {
        ServerCommandSource source = createCommandSource();
        if (source != null) command(command, source);
        else LOGGER.error("Cannot execute command: server not initialized");
    }

    public static @Nullable ServerCommandSource createCommandSource() {
        if (SERVER == null) return null;
        return SERVER.getCommandSource().withWorld(SERVER.getWorld(World.OVERWORLD));
    }

    public static void command(String command, boolean flag) throws CommandSyntaxException {
        ServerCommandSource source = createCommandSource();
        if (source == null) return;
        if (flag) command(command, source);
        else commandWithException(command, source);
    }

    public static void commandWithException(String command, @NotNull ServerCommandSource source) throws CommandSyntaxException {
        CommandDispatcher<ServerCommandSource> dispatcher = source.getServer().getCommandManager().getDispatcher();
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
        LOGGER.info(command);
        dispatcher.execute(parseResults);
    }

    public static void command(String command, @NotNull ServerCommandSource source) {
        CommandDispatcher<ServerCommandSource> dispatcher = source.getServer().getCommandManager().getDispatcher();
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
        LOGGER.info(command);
        try { dispatcher.execute(parseResults); } catch (CommandSyntaxException e) { LOGGER.error(e.getMessage()); }
    }

    public static boolean manaUsesTimer(String name, String type) {
        HashMap<String, LocalDateTime> need = MANA_USES_TIMER.get(name);
        if (need == null) { return false; }
        if (need.containsKey(type)) {
            return need.get(type).plusSeconds(MANA_TYPES.get(type)).isAfter(LocalDateTime.now());
        }
        return false;
    }
}
