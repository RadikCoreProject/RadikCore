package com.radik.avancements.criteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.radik.connecting.event.Trade;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.component.CustomDataPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.predicate.item.PotionContentsPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TradeCriterion extends AbstractCriterion<TradeCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Trade trade) {
        this.trigger(player, conditions -> conditions.matches(trade));
    }


    public record Conditions(Optional<LootContextPredicate> playerPredicate, Optional<String> item, Optional<Boolean> elite)
        implements AbstractCriterion.Conditions {

        public static final Codec<TradeCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::playerPredicate),
                Codec.STRING.optionalFieldOf("item").forGetter(Conditions::item),
                Codec.BOOL.optionalFieldOf("elite").forGetter(Conditions::elite)
            ).apply(instance, Conditions::new)
        );

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean matches(Trade trade) {
            if (item.isPresent()) {
                String expectedItem = item.get();
                String actualItem = trade.seller().getItem().toString();
                if (!expectedItem.equals(actualItem)) {
                    return false;
                }
            }

            if (elite.isPresent()) {
                boolean expectedElite = elite.get();
                return expectedElite == trade.elite();
            }
            return true;
        }
    }
}