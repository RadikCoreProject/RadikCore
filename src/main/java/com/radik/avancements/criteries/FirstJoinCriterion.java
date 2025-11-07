package com.radik.avancements.criteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FirstJoinCriterion extends AbstractCriterion<FirstJoinCriterion.Conditions> {
    public static Codec<FirstJoinCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(FirstJoinCriterion.Conditions::player),
            Codec.INT.fieldOf("matches").forGetter(FirstJoinCriterion.Conditions::count)
    ).apply(instance, FirstJoinCriterion.Conditions::new));


    @Override
    public Codec<FirstJoinCriterion.Conditions> getConditionsCodec() {
        return CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> false);
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int count) implements AbstractCriterion.Conditions {
        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }
    }
}
