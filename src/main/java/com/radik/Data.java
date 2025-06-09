package com.radik;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

import java.util.HashMap;

public abstract class Data {
    public static final HashMap<String, FoodComponent> FOOD_COMPONENTS = new HashMap<>();


    static {
        FOOD_COMPONENTS.put("apple", FoodComponents.APPLE);
        FOOD_COMPONENTS.put("ledenets", (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.25F).build());
        FOOD_COMPONENTS.put("ledenets1", (new FoodComponent.Builder()).nutrition(5).usingConvertsTo(Items.STICK).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200), 0.5f).saturationModifier(0.5F).build());
        FOOD_COMPONENTS.put("ledenets2", (new FoodComponent.Builder()).nutrition(5).usingConvertsTo(Items.STICK).saturationModifier(0.3F).build());
        FOOD_COMPONENTS.put("1", (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.35F).build());
        FOOD_COMPONENTS.put("2", (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.15F).build());
        FOOD_COMPONENTS.put("4", (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.15F).usingConvertsTo(Items.GLASS_BOTTLE).build());
        FOOD_COMPONENTS.put("empty", (new FoodComponent.Builder()).nutrition(0).saturationModifier(0).alwaysEdible().build());
    }
}
