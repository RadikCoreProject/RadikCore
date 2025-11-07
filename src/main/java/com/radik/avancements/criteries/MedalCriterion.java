package com.radik.avancements.criteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.Radik;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

import static com.radik.Data.*;

public class MedalCriterion extends AbstractCriterion<MedalCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, PlayerInventory inventory) {
        this.trigger(player, conditions -> conditions.matches(inventory));
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int type, int material) implements AbstractCriterion.Conditions {
        public static Codec<MedalCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                Codec.INT.fieldOf("type").forGetter(Conditions::type),
                Codec.INT.fieldOf("material").forGetter(Conditions::material)
        ).apply(instance, Conditions::new));

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean matches(PlayerInventory inventory) {
            for (ItemStack stack : inventory) {
                Integer i = stack.get(MEDAL);
                Integer j = stack.get(MEDAL_MATERIAL);
                if (i == null || j == null) continue;
                Radik.LOGGER.error("{} {}", type, material);
                if (i == type && j == material || i == type && material == -1) return true;
            }
            return false;
        }
    }
}