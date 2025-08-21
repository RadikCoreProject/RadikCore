package com.radik;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.radik.packets.Packets;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.radik.Codecs.VEC_3D;

public abstract class Data {
    public static MinecraftServer SERVER;
    static ServerCommandSource commandSource;
    public static CommandDispatcher<ServerCommandSource> dispatcher;
    public static final String MOD_ID = "radik";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static List<Character> colors = List.of('l', '4', 'c', '6', 'e', '2', 'a', 'b', '3', '1', '9', 'd', '5', '7', '8', '0');

    public static final HashMap<String, FoodComponent> FOOD_COMPONENTS = new HashMap<>();
    public static final ComponentType<String> OWNER = register("owner", builder -> builder.codec(Codecs.PLAYER_NAME).packetCodec(PacketCodecs.STRING));
    public static final ComponentType<Vec3d> POSITION = register("pos", builder -> builder.codec(VEC_3D).packetCodec(Packets.VEC3D));
    public static final ComponentType<Integer> CAPSULE_LEVEL = register("level", (builder) -> builder.codec(Codecs.rangedInt(0, 8)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> CAPSULE_FLUID = register("fluid", (builder) -> builder.codec(Codecs.rangedInt(0, 4)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> TELEPORTER = register("type", (builder) -> builder.codec(Codecs.rangedInt(0, 2)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> STAFF_TYPE = register("staff_type", (builder) -> builder.codec(Codecs.rangedInt(0, 3)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> STAFF_ATTACKS = register("staff_attacks", (builder) -> builder.codec(Codecs.rangedInt(0, 500)).packetCodec(PacketCodecs.VAR_INT));

    public static final HashMap<String, HashMap<String, LocalDateTime>> MANA_USES_TIMER = new HashMap<>();
    public static final HashMap<String, Integer> MANA_TYPES = new HashMap<>();

    static {
        MANA_TYPES.put("wind_staff_base", 3);
        MANA_TYPES.put("wind_staff_super", 120);
        MANA_TYPES.put("wind_staff_ultra", 5);
    }

    static {
        FOOD_COMPONENTS.put("apple", FoodComponents.APPLE);
        FOOD_COMPONENTS.put("ledenets", (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.6F).build());
        FOOD_COMPONENTS.put("ledenets1", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.6F).build());
        FOOD_COMPONENTS.put("ledenets2", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.3F).build());
        FOOD_COMPONENTS.put("1", (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.75F).build());
        FOOD_COMPONENTS.put("2", (new FoodComponent.Builder()).nutrition(5).saturationModifier(1F).build());
        FOOD_COMPONENTS.put("3", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.4F).build());
        FOOD_COMPONENTS.put("4", (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.15F).build());
        FOOD_COMPONENTS.put("empty", (new FoodComponent.Builder()).nutrition(0).saturationModifier(0).alwaysEdible().build());
    }

    public static byte getDecorationType(String v) {
        return switch (v) {
            case "bold" -> 0;
            case "color" -> 1;
            case "crone" -> 2;
            case "particle" -> 3;
            default -> -1;
        };
    }

    public static String getDistance(int lvl) {
        return switch (lvl) {
            case 0 -> "2.000";
            case 1 -> "6.000";
            case 2 -> "12.000";
            default -> "0";
        };
    }

    public static int getDistance(Integer lvl) {
        switch (lvl) {
            case 0 -> {return 2000;}
            case 1 -> {return 6000;}
            case 2 -> {return 12000;}
            default -> {return 0;}
        }
    }

    public static float getTeleporterK(Integer type) {
        switch (type) {
            case 1 -> {return 0.9F;}
            case 2 -> {return 0.75F;}
            default -> {return 1;}
        }
    }

    public static String getDimension(@NotNull World world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    public static String getDimension(@NotNull WorldAccess world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void command(String command) {
        command(command, commandSource);
    }

    public static void command(String command, boolean flag) throws CommandSyntaxException {
        if (flag) command(command, commandSource);
        else commandWithException(command, commandSource);
    }

    public static void commandWithException(String command, ServerCommandSource source) throws CommandSyntaxException {
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
        LOGGER.info(command);
        dispatcher.execute(parseResults);
    }

    public static void command(String command, ServerCommandSource source) {
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
        LOGGER.info(command);
        try { dispatcher.execute(parseResults); } catch (CommandSyntaxException e) { LOGGER.error(e.getMessage()); }
    }

    public static boolean manaUsesTimer(String name, String type) {
        HashMap<String, LocalDateTime> need = MANA_USES_TIMER.get(name);
        if (need == null) { return true; }
        if (need.containsKey(type)) {
            return !need.get(type).plusSeconds(MANA_TYPES.get(type)).isAfter(LocalDateTime.now());
        }
        return true;
    }
}
