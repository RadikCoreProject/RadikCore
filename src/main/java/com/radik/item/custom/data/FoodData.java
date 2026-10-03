package com.radik.item.custom.data;

import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;

import java.util.List;
import java.util.function.Function;

import static com.radik.Data.EVENT_TYPE;

public final class FoodData {
    public static final FoodComponent RAW_FISH = new FoodComponent.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .build();

    public static final FoodComponent COOKED_FISH = new FoodComponent.Builder()
            .nutrition(5)
            .saturationModifier(0.6F)
            .build();

    public static final FoodComponent CHOCOCRAB = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(1.2F)
            .alwaysEdible()
            .build();

    public static final FoodComponent COOKED_CHOCOCRAB = new FoodComponent.Builder()
            .nutrition(8)
            .saturationModifier(0.8F)
            .build();

    public static final FoodComponent SODA = new FoodComponent.Builder()
            .nutrition(0)
            .saturationModifier(0F)
            .alwaysEdible()
            .build();

    public static final FoodComponent SUMMER_FOOD = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.3F)
            .alwaysEdible()
            .build();

    public static ConsumableComponent.Builder summerFood() {
        return ConsumableComponent.builder()
                .consumeSeconds(1.6F)
                .useAction(UseAction.EAT)
                .sound(SoundEvents.ENTITY_GENERIC_EAT)
                .consumeParticles(true);
    }

    public static ApplyEffectsConsumeEffect effect(RegistryEntry<StatusEffect> statusEffect) {
        return new ApplyEffectsConsumeEffect(List.of(
                new StatusEffectInstance(statusEffect, 600, 1)
        ));
    }

    public static Function<Item.Settings, Item> summerEffect(RegistryEntry<StatusEffect> effect) {
        return settings -> new Item(
                settings.maxCount(99).component(EVENT_TYPE, ChallengeEvent.SUMMER).food(SUMMER_FOOD,
                        summerFood().consumeEffect(effect(effect)).build())
        );
    }
}
