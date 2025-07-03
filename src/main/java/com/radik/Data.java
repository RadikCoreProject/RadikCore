package com.radik;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public abstract class Data {
    public static final HashMap<String, FoodComponent> FOOD_COMPONENTS = new HashMap<>();
    public static final ComponentType<Integer> CAPSULE_LEVEL = register("level", (builder) -> builder.codec(Codecs.rangedInt(0, 8)).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> CAPSULE_FLUID = register("fluid", (builder) -> builder.codec(Codecs.rangedInt(0, 4)).packetCodec(PacketCodecs.VAR_INT));

    static {
        FOOD_COMPONENTS.put("apple", FoodComponents.APPLE);
        FOOD_COMPONENTS.put("ledenets", (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.5F).build());
        FOOD_COMPONENTS.put("ledenets1", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.5F).build());
        FOOD_COMPONENTS.put("ledenets2", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.3F).build());
        FOOD_COMPONENTS.put("1", (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.75F).build());
        FOOD_COMPONENTS.put("2", (new FoodComponent.Builder()).nutrition(5).saturationModifier(1F).build());
        FOOD_COMPONENTS.put("3", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.4F).build());
        FOOD_COMPONENTS.put("4", (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.15F).build());
        FOOD_COMPONENTS.put("empty", (new FoodComponent.Builder()).nutrition(0).saturationModifier(0).alwaysEdible().build());
    }

    public static String getDimension(@NotNull World world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    public static String getDimension(@NotNull WorldAccess world) {
        return world.getDimension().toString().split("/ ")[1].split("]")[0].split("_")[1];
    }

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Radik.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }
}
