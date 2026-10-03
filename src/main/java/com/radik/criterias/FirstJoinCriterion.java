package com.radik.criterias;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FirstJoinCriterion extends AbstractCriterion<FirstJoinCriterion.Conditions> {
    public static Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
        Codec.INT.fieldOf("matches").forGetter(Conditions::count)
    ).apply(instance, Conditions::new));

    private static Criterias.FirstJoinChecker checker = (name) -> true;
    public static void setChecker(Criterias.FirstJoinChecker newChecker) {
        checker = newChecker;
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> checker.isNotTwin(player.getName().getString()));
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int count) implements AbstractCriterion.Conditions {
        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }
    }
}
