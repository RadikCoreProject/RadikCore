package com.radik.avancements.criteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.Radik;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.radik.Data.CAPSULE_FLUID;

public class CapsuleFluidCriterion extends AbstractCriterion<CapsuleFluidCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, PlayerInventory inventory) {
        this.trigger(player, conditions -> conditions.matches(inventory));
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int count, int value) implements AbstractCriterion.Conditions {
        public static Codec<CapsuleFluidCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                Codec.INT.fieldOf("count").forGetter(Conditions::count),
                Codec.INT.fieldOf("value").forGetter(Conditions::value)
        ).apply(instance, Conditions::new));

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean matches(@NotNull PlayerInventory inventory) {
            int c = 0;
            for (ItemStack stack : inventory) {
                Integer i = stack.get(CAPSULE_FLUID);
                if (i == null) continue;
                if (i == value) c += stack.getCount();
            }
            return c >= count;
        }
    }
}