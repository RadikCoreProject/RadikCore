package com.radik.avancements.criteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.Radik;
import com.radik.block.custom.blockentity.event.EventBlockEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.radik.Data.MEDAL;
import static com.radik.Data.MEDAL_MATERIAL;

public class EventCriterion extends AbstractCriterion<EventCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, PlayerInventory inventory) {
        this.trigger(player, conditions -> conditions.matches(inventory));
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate, int event) implements AbstractCriterion.Conditions {
        public static Codec<EventCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                Codec.INT.fieldOf("event").forGetter(Conditions::event)
        ).apply(instance, Conditions::new));

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean matches(PlayerInventory inventory) {
            int type = EventBlockEntity.getEventType(LocalDateTime.now().getDayOfYear());
            return event == type || type > 0 && event == 0;
        }
    }
}