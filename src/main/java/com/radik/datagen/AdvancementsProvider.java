package com.radik.datagen;

import com.radik.Data;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.criterias.*;
import com.radik.effect.RegisterEffect;
import com.radik.item.RegisterItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.radik.Data.*;
import static com.radik.block.RegisterBlocks.*;
import static com.radik.criterias.Criterias.*;
import static com.radik.item.RegisterItems.*;
import static net.minecraft.item.Items.*;

public class AdvancementsProvider extends FabricAdvancementProvider {
    private static final ItemStack HYDROGEN1 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack HYDROGEN2 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack HYDROGEN3 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack HELIUM1 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack HELIUM2 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack HELIUM3 = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack WATER = RegisterItems.CAPSULE.getDefaultStack();
    private static final ItemStack LAVA = RegisterItems.CAPSULE.getDefaultStack();

    private static final ItemStack EVENT_PARTICIPANT = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack BRONZE_MEDAL = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack SILVER_MEDAL = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack GOLD_MEDAL = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack EVENT_CREATOR = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack SUPPORTER_1 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack SUPPORTER_2 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack SUPPORTER_3 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack SUPPORTER_4 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_1 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_2 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_3 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_4 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_5 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_6 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_7 = RegisterItems.MEDAL.getDefaultStack();
    private static final ItemStack OLD_8 = RegisterItems.MEDAL.getDefaultStack();

    static {
        HYDROGEN1.set(CAPSULE_FLUID, 3);
        HYDROGEN1.set(CAPSULE_LEVEL, 2);
        HYDROGEN2.set(CAPSULE_FLUID, 3);
        HYDROGEN2.set(CAPSULE_LEVEL, 5);
        HYDROGEN3.set(CAPSULE_FLUID, 3);
        HYDROGEN3.set(CAPSULE_LEVEL, 8);

        HELIUM1.set(CAPSULE_FLUID, 4);
        HELIUM1.set(CAPSULE_LEVEL, 2);
        HELIUM2.set(CAPSULE_FLUID, 4);
        HELIUM2.set(CAPSULE_LEVEL, 5);
        HELIUM3.set(CAPSULE_FLUID, 4);
        HELIUM3.set(CAPSULE_LEVEL, 8);

        WATER.set(CAPSULE_FLUID, 1);
        WATER.set(CAPSULE_LEVEL, 8);
        LAVA.set(CAPSULE_FLUID, 2);
        LAVA.set(CAPSULE_LEVEL, 8);

        EVENT_PARTICIPANT.set(Data.MEDAL, 1);
        EVENT_PARTICIPANT.set(MEDAL_MATERIAL, 0);
        BRONZE_MEDAL.set(Data.MEDAL, 1);
        BRONZE_MEDAL.set(MEDAL_MATERIAL, 1);
        SILVER_MEDAL.set(Data.MEDAL, 1);
        SILVER_MEDAL.set(MEDAL_MATERIAL, 2);
        GOLD_MEDAL.set(Data.MEDAL, 1);
        GOLD_MEDAL.set(MEDAL_MATERIAL, 3);
        EVENT_CREATOR.set(Data.MEDAL, 2);
        EVENT_CREATOR.set(MEDAL_MATERIAL, 5);
        SUPPORTER_1.set(Data.MEDAL, 3);
        SUPPORTER_1.set(MEDAL_MATERIAL, 4);
        SUPPORTER_2.set(Data.MEDAL, 3);
        SUPPORTER_2.set(MEDAL_MATERIAL, 6);
        SUPPORTER_3.set(Data.MEDAL, 3);
        SUPPORTER_3.set(MEDAL_MATERIAL, 7);
        SUPPORTER_4.set(Data.MEDAL, 3);
        SUPPORTER_4.set(MEDAL_MATERIAL, 8);
        OLD_1.set(Data.MEDAL, 5);
        OLD_1.set(MEDAL_MATERIAL, 1);
        OLD_2.set(Data.MEDAL, 5);
        OLD_2.set(MEDAL_MATERIAL, 2);
        OLD_3.set(Data.MEDAL, 5);
        OLD_3.set(MEDAL_MATERIAL, 3);
        OLD_4.set(Data.MEDAL, 5);
        OLD_4.set(MEDAL_MATERIAL, 4);
        OLD_5.set(Data.MEDAL, 5);
        OLD_5.set(MEDAL_MATERIAL, 5);
        OLD_6.set(Data.MEDAL, 5);
        OLD_6.set(MEDAL_MATERIAL, 6);
        OLD_7.set(Data.MEDAL, 5);
        OLD_7.set(MEDAL_MATERIAL, 7);
        OLD_8.set(Data.MEDAL, 5);
        OLD_8.set(MEDAL_MATERIAL, 8);
    }

    protected AdvancementsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.@NotNull WrapperLookup wrapperLookup, @NotNull Consumer<AdvancementEntry> consumer) {
        AdvancementEntry pull = Advancement.Builder.create()
                .display(
                        RegisterBlocks.FROG1, // The display icon
                        Text.translatable("advancements.radik.RadikCore.title"), // The title
                        Text.translatable("advancements.radik.RadikCore.description"), // The description
                        Identifier.of(Radik.MOD_ID, "block/chisel/array/1"), // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
                        AdvancementFrame.TASK, // TASK, CHALLENGE, or GOAL
                        false, // Show the toast when completing it
                        false, // Announce it to chat
                        false // Hide it in the advancement tab until it's achieved
                )
                    .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                    .rewards(AdvancementRewards.NONE)
                .build(consumer, "radik:pull");

        AdvancementEntry pull2 = Advancement.Builder.create()
                .display(
                        Items.AMETHYST_SHARD,
                        Text.translatable("advancements.radik.RadikCoreEvent.title"),
                        Text.translatable("advancements.radik.RadikCoreEvent.description"),
                        Identifier.of(Radik.MOD_ID, "block/frog/2"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                )
                .criterion("pull2", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .rewards(AdvancementRewards.NONE)
                .build(consumer, "radik:pull2");

        AdvancementEntry medal = Advancement.Builder.create()
            .display(
                RegisterItems.MEDAL.getDefaultStack(),
                Text.translatable("advancements.radik.medal.title"),
                Text.translatable("advancements.radik.medal.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(pull2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", InventoryChangedCriterion.Conditions.items(RegisterItems.MEDAL))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/medal")))
            .build(consumer, "radik:medal");

        AdvancementEntry event_participant = Advancement.Builder.create()
            .display(
                EVENT_PARTICIPANT,
                Text.translatable("advancements.radik.event_participant.title"),
                Text.translatable("advancements.radik.event_participant.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(medal)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 1, -1)))
            .rewards(AdvancementRewards.Builder.experience(20).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/event_participant")))
            .build(consumer, "radik:event_participant");

        AdvancementEntry event_winner_1 = Advancement.Builder.create()
            .display(
                BRONZE_MEDAL,
                Text.translatable("advancements.radik.event_winner_1.title"),
                Text.translatable("advancements.radik.event_winner_1.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(event_participant)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 1, 1)))
            .rewards(AdvancementRewards.Builder.experience(30).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/event_winner_1")))
            .build(consumer, "radik:event_winner_1");

        AdvancementEntry event_winner_2 = Advancement.Builder.create()
            .display(
                SILVER_MEDAL,
                Text.translatable("advancements.radik.event_winner_2.title"),
                Text.translatable("advancements.radik.event_winner_2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(event_participant)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 1, 2)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/event_winner_2")))
            .build(consumer, "radik:event_winner_2");

        AdvancementEntry event_winner_3 = Advancement.Builder.create()
            .display(
                GOLD_MEDAL,
                Text.translatable("advancements.radik.event_winner_3.title"),
                Text.translatable("advancements.radik.event_winner_3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(event_participant)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 1, 3)))
            .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/event_winner_3")))
            .build(consumer, "radik:event_winner_3");

        AdvancementEntry event_creator = Advancement.Builder.create()
            .display(
                EVENT_CREATOR,
                Text.translatable("advancements.radik.event_creator.title"),
                Text.translatable("advancements.radik.event_creator.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(medal)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 2, 5)))
            .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/event_creator")))
            .build(consumer, "radik:event_creator");

        AdvancementEntry server_support_1 = Advancement.Builder.create()
            .display(
                SUPPORTER_1,
                Text.translatable("advancements.radik.server_support_1.title"),
                Text.translatable("advancements.radik.server_support_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(medal)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 3, 4)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/server_support_1")))
            .build(consumer, "radik:server_support_1");

        AdvancementEntry server_support_2 = Advancement.Builder.create()
            .display(
                SUPPORTER_2,
                Text.translatable("advancements.radik.server_support_2.title"),
                Text.translatable("advancements.radik.server_support_2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(server_support_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 3, 6)))
            .rewards(AdvancementRewards.Builder.experience(100).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/server_support_2")))
            .build(consumer, "radik:server_support_2");

        AdvancementEntry server_support_3 = Advancement.Builder.create()
            .display(
                SUPPORTER_3,
                Text.translatable("advancements.radik.server_support_3.title"),
                Text.translatable("advancements.radik.server_support_3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(server_support_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 3, 7)))
            .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/server_support_3")))
            .build(consumer, "radik:server_support_3");

        AdvancementEntry server_support_4 = Advancement.Builder.create()
            .display(
                SUPPORTER_4,
                Text.translatable("advancements.radik.server_support_4.title"),
                Text.translatable("advancements.radik.server_support_4.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(server_support_3)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 3, 8)))
            .rewards(AdvancementRewards.Builder.experience(700).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/server_support_4")))
            .build(consumer, "radik:server_support_4");

        AdvancementEntry old_1 = Advancement.Builder.create()
            .display(
                OLD_1,
                Text.translatable("advancements.radik.old_1.title"),
                Text.translatable("advancements.radik.old_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(medal)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 1)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_1")))
            .build(consumer, "radik:old_1");

        AdvancementEntry old_2 = Advancement.Builder.create()
            .display(
                OLD_2,
                Text.translatable("advancements.radik.old_2.title"),
                Text.translatable("advancements.radik.old_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(old_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 2)))
            .rewards(AdvancementRewards.Builder.experience(20).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_2")))
            .build(consumer, "radik:old_2");

        AdvancementEntry old_3 = Advancement.Builder.create()
            .display(
                OLD_3,
                Text.translatable("advancements.radik.old_3.title"),
                Text.translatable("advancements.radik.old_3.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(old_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 3)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_3")))
            .build(consumer, "radik:old_3");

        AdvancementEntry old_4 = Advancement.Builder.create()
            .display(
                OLD_4,
                Text.translatable("advancements.radik.old_4.title"),
                Text.translatable("advancements.radik.old_4.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(old_3)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 4)))
            .rewards(AdvancementRewards.Builder.experience(100).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_4")))
            .build(consumer, "radik:old_4");

        AdvancementEntry old_5 = Advancement.Builder.create()
            .display(
                OLD_5,
                Text.translatable("advancements.radik.old_5.title"),
                Text.translatable("advancements.radik.old_5.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(old_4)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 5)))
            .rewards(AdvancementRewards.Builder.experience(250).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_5")))
            .build(consumer, "radik:old_5");

        AdvancementEntry old_6 = Advancement.Builder.create()
            .display(
                OLD_6,
                Text.translatable("advancements.radik.old_6.title"),
                Text.translatable("advancements.radik.old_6.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(old_5)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 6)))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_6")))
            .build(consumer, "radik:old_6");

        AdvancementEntry old_7 = Advancement.Builder.create()
            .display(
                OLD_7,
                Text.translatable("advancements.radik.old_7.title"),
                Text.translatable("advancements.radik.old_7.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(old_6)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 7)))
            .rewards(AdvancementRewards.Builder.experience(1000).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_7")))
            .build(consumer, "radik:old_7");

        AdvancementEntry old_8 = Advancement.Builder.create()
            .display(
                OLD_8,
                Text.translatable("advancements.radik.old_8.title"),
                Text.translatable("advancements.radik.old_8.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(old_7)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_medal", MEDAL_CRITERION.create(new MedalCriterion.Conditions(Optional.empty(), 5, 8)))
            .rewards(AdvancementRewards.Builder.experience(5000).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/medal/old_8")))
            .build(consumer, "radik:old_8");

        AdvancementEntry event = Advancement.Builder.create()
            .display(
                RegisterBlocks.EVENT_BLOCK,
                Text.translatable("advancements.radik.event.title"),
                Text.translatable("advancements.radik.event.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(pull2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EVENT_CRITERION.create(new EventCriterion.Conditions(Optional.empty(), 0)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/base")))
            .build(consumer, "radik:event");

        AdvancementEntry event_1 = Advancement.Builder.create()
            .display(
                Blocks.CARVED_PUMPKIN,
                Text.translatable("advancements.radik.event_1.title"),
                Text.translatable("advancements.radik.event_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EVENT_CRITERION.create(new EventCriterion.Conditions(Optional.empty(), 1)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/event_1")))
            .build(consumer, "radik:event_1");

        AdvancementEntry event_2 = Advancement.Builder.create()
            .display(
                RegisterBlocks.SNOWMAN,
                Text.translatable("advancements.radik.event_2.title"),
                Text.translatable("advancements.radik.event_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EVENT_CRITERION.create(new EventCriterion.Conditions(Optional.empty(), 2)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/event_2")))
            .build(consumer, "radik:event_2");

        AdvancementEntry event_2_1 = Advancement.Builder.create()
            .display(
                RegisterItems.DYE_RAINBOW,
                Text.translatable("advancements.radik.event_2_1.title"),
                Text.translatable("advancements.radik.event_2_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(RegisterItems.DYE_RAINBOW))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/2/1")))
            .build(consumer, "radik:event_2_1");

        AdvancementEntry event_2_2 = Advancement.Builder.create()
            .display(
                RegisterBlocks.GARLAND,
                Text.translatable("advancements.radik.event_2_2.title"),
                Text.translatable("advancements.radik.event_2_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(RegisterBlocks.GARLAND))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/2/2")))
            .build(consumer, "radik:event_2_2");

        AdvancementEntry event_2_3 = Advancement.Builder.create()
            .display(
                RegisterBlocks.RAINBOW_STAINED_GLASS,
                Text.translatable("advancements.radik.event_2_3.title"),
                Text.translatable("advancements.radik.event_2_3.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(event_2_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("glass", InventoryChangedCriterion.Conditions.items(RegisterBlocks.RAINBOW_STAINED_GLASS))
            .criterion("glass_pane", InventoryChangedCriterion.Conditions.items(RegisterBlocks.RAINBOW_STAINED_GLASS_PANE))
            .criterion("wool", InventoryChangedCriterion.Conditions.items(RegisterBlocks.RAINBOW_WOOL))
            .rewards(AdvancementRewards.Builder.experience(40).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/2/3")))
            .build(consumer, "radik:event_2_3");

        AdvancementEntry event_2_4 = Advancement.Builder.create()
            .display(
                RegisterItems.CHAMPAGNE,
                Text.translatable("advancements.radik.event_2_4.title"),
                Text.translatable("advancements.radik.event_2_4.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(event_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("wine", InventoryChangedCriterion.Conditions.items(RegisterItems.RED_WINE))
            .criterion("champagne", InventoryChangedCriterion.Conditions.items(RegisterItems.CHAMPAGNE))
            .criterion("salad", InventoryChangedCriterion.Conditions.items(RegisterItems.SALAD))
            .criterion("orange", InventoryChangedCriterion.Conditions.items(RegisterItems.ORANGE))
            .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/2/4")))
            .build(consumer, "radik:event_2_4");

        AdvancementEntry event_2_5 = Advancement.Builder.create()
            .display(
                PEDESTAL,
                Text.translatable("advancements.radik.event_2_5.title"),
                Text.translatable("advancements.radik.event_2_5.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(event_2_4)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(PEDESTAL))
            .rewards(AdvancementRewards.Builder.experience(1000).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/2/5")))
            .build(consumer, "radik:event_2_5");

        AdvancementEntry event_3 = Advancement.Builder.create()
            .display(
                Blocks.SUNFLOWER,
                Text.translatable("advancements.radik.event_3.title"),
                Text.translatable("advancements.radik.event_3.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EVENT_CRITERION.create(new EventCriterion.Conditions(Optional.empty(), 3)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/event_3")))
            .build(consumer, "radik:event_3");

        AdvancementEntry event_4 = Advancement.Builder.create()
            .display(
                Blocks.SHORT_GRASS,
                Text.translatable("advancements.radik.event_4.title"),
                Text.translatable("advancements.radik.event_4.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EVENT_CRITERION.create(new EventCriterion.Conditions(Optional.empty(), 4 )))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/event_4")))
            .build(consumer, "radik:event_4");

        AdvancementEntry summer_1 = Advancement.Builder.create()
                .display(
                        LEAVE_OAK,
                        Text.translatable("advancements.radik.event_4_1.title"),
                        Text.translatable("advancements.radik.event_4_1.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(event_4)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("leave", InventoryChangedCriterion.Conditions.items(LEAVE_OAK))
                .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/1")))
                .build(consumer, "radik:event_4_1");

        AdvancementEntry summer_2 = Advancement.Builder.create()
                .display(
                        LEAVE_DEAD,
                        Text.translatable("advancements.radik.event_4_2.title"),
                        Text.translatable("advancements.radik.event_4_2.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(summer_1)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("leave", InventoryChangedCriterion.Conditions.items(LEAVE_DEAD))
                .rewards(AdvancementRewards.Builder.experience(30).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/2")))
                .build(consumer, "radik:event_4_2");

        AdvancementEntry summer_3 = Advancement.Builder.create()
                .display(
                        LEAVE_CHERRY,
                        Text.translatable("advancements.radik.event_4_3.title"),
                        Text.translatable("advancements.radik.event_4_3.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(summer_2)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("leave_1", InventoryChangedCriterion.Conditions.items(LEAVE_OAK))
                .criterion("leave_2", InventoryChangedCriterion.Conditions.items(LEAVE_ACACIA))
                .criterion("leave_3", InventoryChangedCriterion.Conditions.items(LEAVE_DEAD))
                .criterion("leave_4", InventoryChangedCriterion.Conditions.items(LEAVE_AZALEA))
                .criterion("leave_5", InventoryChangedCriterion.Conditions.items(LEAVE_BIRCH))
                .criterion("leave_6", InventoryChangedCriterion.Conditions.items(LEAVE_CHERRY))
                .criterion("leave_7", InventoryChangedCriterion.Conditions.items(LEAVE_DARK_OAK))
                .criterion("leave_8", InventoryChangedCriterion.Conditions.items(LEAVE_JUNGLE))
                .criterion("leave_9", InventoryChangedCriterion.Conditions.items(LEAVE_MANGROVE))
                .criterion("leave_10", InventoryChangedCriterion.Conditions.items(LEAVE_PALE_OAK))
                .criterion("leave_11", InventoryChangedCriterion.Conditions.items(LEAVE_SPRUCE))
                .rewards(AdvancementRewards.Builder.experience(90).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/3")))
                .build(consumer, "radik:event_4_3");

        AdvancementEntry summer_4 = Advancement.Builder.create()
                .display(
                        ICE_CREAM_VANILLA,
                        Text.translatable("advancements.radik.event_4_4.title"),
                        Text.translatable("advancements.radik.event_4_4.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(event_4)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("ice_cream", InventoryChangedCriterion.Conditions.items(ICE_CREAM_VANILLA))
                .rewards(AdvancementRewards.Builder.experience(30).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/4")))
                .build(consumer, "radik:event_4_4");

        AdvancementEntry summer_5 = Advancement.Builder.create()
                .display(
                        ICE_CREAM_CHOCOLATE,
                        Text.translatable("advancements.radik.event_4_5.title"),
                        Text.translatable("advancements.radik.event_4_5.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(summer_4)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("ice_cream_1", InventoryChangedCriterion.Conditions.items(ICE_CREAM_VANILLA))
                .criterion("ice_cream_2", InventoryChangedCriterion.Conditions.items(ICE_CREAM_BANANA))
                .criterion("ice_cream_3", InventoryChangedCriterion.Conditions.items(ICE_CREAM_BERRY))
                .criterion("ice_cream_4", InventoryChangedCriterion.Conditions.items(ICE_CREAM_CHOCOLATE))
                .rewards(AdvancementRewards.Builder.experience(60).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/5")))
                .build(consumer, "radik:event_4_5");

        AdvancementEntry summer_6 = Advancement.Builder.create()
                .display(
                        BANANA_PEEL,
                        Text.translatable("advancements.radik.event_4_6.title"),
                        Text.translatable("advancements.radik.event_4_6.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(event_4)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("banana_1", InventoryChangedCriterion.Conditions.items(BANANA))
                .criterion("banana_2", InventoryChangedCriterion.Conditions.items(BANANA_CLOSE))
                .criterion("banana_3", InventoryChangedCriterion.Conditions.items(BANANA_PEEL))
                .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/6")))
                .build(consumer, "radik:event_4_6");

        AdvancementEntry summer_7 = Advancement.Builder.create()
                .display(
                        DISC_CALM,
                        Text.translatable("advancements.radik.event_4_7.title"),
                        Text.translatable("advancements.radik.event_4_7.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(summer_5)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("disc", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:disc_calm"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/7")))
                .build(consumer, "radik:event_4_7");

        AdvancementEntry summer_8 = Advancement.Builder.create()
                .display(
                        DISC_FOREST,
                        Text.translatable("advancements.radik.event_4_8.title"),
                        Text.translatable("advancements.radik.event_4_8.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(summer_5)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("disc", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:disc_forest"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/8")))
                .build(consumer, "radik:event_4_8");

        AdvancementEntry summer_9 = Advancement.Builder.create()
                .display(
                        DISC_HOLIDAY,
                        Text.translatable("advancements.radik.event_4_9.title"),
                        Text.translatable("advancements.radik.event_4_9.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(summer_5)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("disc", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:disc_holiday"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/9")))
                .build(consumer, "radik:event_4_9");

        AdvancementEntry summer_10 = Advancement.Builder.create()
                .display(
                        SUMMER_AXE,
                        Text.translatable("advancements.radik.event_4_10.title"),
                        Text.translatable("advancements.radik.event_4_10.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(summer_3)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:summer_axe"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/10")))
                .build(consumer, "radik:event_4_10");

        AdvancementEntry summer_11 = Advancement.Builder.create()
                .display(
                        SUMMER_PICKAXE,
                        Text.translatable("advancements.radik.event_4_11.title"),
                        Text.translatable("advancements.radik.event_4_11.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(summer_3)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:summer_pickaxe"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/11")))
                .build(consumer, "radik:event_4_11");


        AdvancementEntry summer_12 = Advancement.Builder.create()
                .display(
                        SUMMER_HOE,
                        Text.translatable("advancements.radik.event_4_12.title"),
                        Text.translatable("advancements.radik.event_4_12.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(summer_3)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:summer_hoe"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/12")))
                .build(consumer, "radik:event_4_12");

        AdvancementEntry summer_13 = Advancement.Builder.create()
                .display(
                        SUMMER_SWORD,
                        Text.translatable("advancements.radik.event_4_13.title"),
                        Text.translatable("advancements.radik.event_4_13.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(summer_3)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:summer_sword"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/13")))
                .build(consumer, "radik:event_4_13");

        AdvancementEntry summer_14 = Advancement.Builder.create()
                .display(
                        SUMMER_SHOVEL,
                        Text.translatable("advancements.radik.event_4_14.title"),
                        Text.translatable("advancements.radik.event_4_14.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .parent(summer_3)
                .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
                .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:summer_shovel"), Optional.of(true))))
                .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/4/14")))
                .build(consumer, "radik:event_4_14");


        AdvancementEntry halloween_1 = Advancement.Builder.create()
            .display(
                RegisterItems.CANDY_BLUE,
                Text.translatable("advancements.radik.event_1_1.title"),
                Text.translatable("advancements.radik.event_1_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(event_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("red_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_RED))
            .criterion("blue_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BLUE))
            .criterion("yellow_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_YELLOW))
            .criterion("green_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_GREEN))
            .rewards(AdvancementRewards.Builder.experience(30).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/1")))
            .build(consumer, "radik:event_1_1");


        AdvancementEntry halloween_2 = Advancement.Builder.create()
            .display(
                RegisterItems.CANDY_BASKET_BLUE,
                Text.translatable("advancements.radik.event_1_2.title"),
                Text.translatable("advancements.radik.event_1_2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(halloween_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("red_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_RED))
            .criterion("blue_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_BLUE))
            .criterion("yellow_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_YELLOW))
            .criterion("green_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_GREEN))
            .criterion("empty_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_EMPTY))
            .criterion("lucky_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_LUCKY))
            .criterion("super_candy", InventoryChangedCriterion.Conditions.items(RegisterItems.CANDY_BASKET_SUPER))
            .rewards(AdvancementRewards.Builder.experience(80).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/2")))
            .build(consumer, "radik:event_1_2");


        AdvancementEntry halloween_3 = Advancement.Builder.create()
            .display(
                RegisterItems.HALLOWEEN_AXE,
                Text.translatable("advancements.radik.event_1_3.title"),
                Text.translatable("advancements.radik.event_1_3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(halloween_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:halloween_axe"), Optional.of(true))))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/3")))
            .build(consumer, "radik:event_1_3");

        AdvancementEntry halloween_4 = Advancement.Builder.create()
            .display(
                RegisterItems.HALLOWEEN_HOE,
                Text.translatable("advancements.radik.event_1_4.title"),
                Text.translatable("advancements.radik.event_1_4.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(halloween_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:halloween_hoe"), Optional.of(true))))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/4")))
            .build(consumer, "radik:event_1_4");

        AdvancementEntry halloween_5 = Advancement.Builder.create()
            .display(
                RegisterItems.HALLOWEEN_SHOVEL,
                Text.translatable("advancements.radik.event_1_5.title"),
                Text.translatable("advancements.radik.event_1_5.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(halloween_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:halloween_shovel"), Optional.of(true))))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/5")))
            .build(consumer, "radik:event_1_5");

        AdvancementEntry halloween_6 = Advancement.Builder.create()
            .display(
                RegisterItems.HALLOWEEN_SWORD,
                Text.translatable("advancements.radik.event_1_6.title"),
                Text.translatable("advancements.radik.event_1_6.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(halloween_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:halloween_sword"), Optional.of(true))))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/6")))
            .build(consumer, "radik:event_1_6");

        AdvancementEntry halloween_7 = Advancement.Builder.create()
            .display(
                RegisterItems.HALLOWEEN_PICKAXE,
                Text.translatable("advancements.radik.event_1_7.title"),
                Text.translatable("advancements.radik.event_1_7.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(halloween_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("instrument", TRADE_CRITERION.create(new TradeCriterion.Conditions(Optional.empty(), Optional.of("radik:halloween_pickaxe"), Optional.of(true))))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/event/block/1/7")))
            .build(consumer, "radik:event_1_7");



        AdvancementEntry radiation_1 = Advancement.Builder.create()
            .display(
                RADIOACTIVE_GRASS,
                Text.translatable("advancements.radik.radiation_1.title"),
                Text.translatable("advancements.radik.radiation_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                true
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createBiome(wrapperLookup.getOrThrow(RegistryKeys.BIOME).getOrThrow(BiomeKeys.PLAINS))))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/1")))
            .build(consumer, "radik:radiation_1");

        AdvancementEntry radiation_2 = Advancement.Builder.create()
            .display(
                RADIOACTIVE_IRON_BLOCK,
                Text.translatable("advancements.radik.radiation_2.title"),
                Text.translatable("advancements.radik.radiation_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(radiation_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", EffectsChangedCriterion.Conditions.create(EntityEffectPredicate.Builder.create().addEffect(RegisterEffect.RADIATION_EFFECT)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/2")))
            .build(consumer, "radik:radiation_2");

        AdvancementEntry radiation_3 = Advancement.Builder.create()
            .display(
                URANUS_ORE,
                Text.translatable("advancements.radik.radiation_3.title"),
                Text.translatable("advancements.radik.radiation_3.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(radiation_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(URANUS_RAW))
            .rewards(AdvancementRewards.Builder.experience(80).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/3")))
            .build(consumer, "radik:radiation_3");

        AdvancementEntry radiation_4 = Advancement.Builder.create()
            .display(
                LEAD_ORE,
                Text.translatable("advancements.radik.radiation_4.title"),
                Text.translatable("advancements.radik.radiation_4.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(radiation_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(LEAD_RAW))
            .rewards(AdvancementRewards.Builder.experience(80).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/4")))
            .build(consumer, "radik:radiation_4");

        AdvancementEntry radiation_5 = Advancement.Builder.create()
            .display(
                LEAD_RADIOACTIVE_BLOCK,
                Text.translatable("advancements.radik.radiation_5.title"),
                Text.translatable("advancements.radik.radiation_5.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(radiation_4)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(LEAD_BLOCK))
            .criterion("block2", InventoryChangedCriterion.Conditions.items(LEAD_BRICK))
            .rewards(AdvancementRewards.Builder.experience(120).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/5")))
            .build(consumer, "radik:radiation_5");

        AdvancementEntry radiation_6 = Advancement.Builder.create()
            .display(
                LEAD_BRICK,
                Text.translatable("advancements.radik.radiation_6.title"),
                Text.translatable("advancements.radik.radiation_6.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(radiation_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event1", InventoryChangedCriterion.Conditions.items(RADIOACTIVE_GRASS))
            .criterion("event2", InventoryChangedCriterion.Conditions.items(RADIOACTIVE_LOG))
            .criterion("event3", InventoryChangedCriterion.Conditions.items(RADIOACTIVE_LEAVES))
            .criterion("event4", InventoryChangedCriterion.Conditions.items(RADIOACTIVE_COPPER_BLOCK))
            .criterion("event5", InventoryChangedCriterion.Conditions.items(RADIOACTIVE_IRON_BLOCK))
            .criterion("event6", InventoryChangedCriterion.Conditions.items(LEAD_RADIOACTIVE_BLOCK))
            .rewards(AdvancementRewards.Builder.experience(200).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/6")))
            .build(consumer, "radik:radiation_6");

        AdvancementEntry radiation_7 = Advancement.Builder.create()
            .display(
                LEAD_PICKAXE,
                Text.translatable("advancements.radik.radiation_7.title"),
                Text.translatable("advancements.radik.radiation_7.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(radiation_5)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event1", InventoryChangedCriterion.Conditions.items(LEAD_HELMET))
            .criterion("event2", InventoryChangedCriterion.Conditions.items(LEAD_CHESTPLATE))
            .criterion("event3", InventoryChangedCriterion.Conditions.items(LEAD_LEGGINGS))
            .criterion("event4", InventoryChangedCriterion.Conditions.items(LEAD_BOOTS))
            .criterion("event5", InventoryChangedCriterion.Conditions.items(LEAD_HOE))
            .criterion("event6", InventoryChangedCriterion.Conditions.items(LEAD_SWORD))
            .criterion("event7", InventoryChangedCriterion.Conditions.items(LEAD_PICKAXE))
            .criterion("event8", InventoryChangedCriterion.Conditions.items(LEAD_AXE))
            .criterion("event9", InventoryChangedCriterion.Conditions.items(LEAD_SHOVEL))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/radiation/7")))
            .build(consumer, "radik:radiation_7");


        AdvancementEntry capsule_1 = Advancement.Builder.create()
            .display(
                RegisterItems.CAPSULE.getDefaultStack(),
                Text.translatable("advancements.radik.capsule.title"),
                Text.translatable("advancements.radik.capsule.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_capsule", InventoryChangedCriterion.Conditions.items(RegisterItems.CAPSULE))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/capsule")))
            .build(consumer, "radik:capsule");

        AdvancementEntry hydrogen_1 = Advancement.Builder.create()
            .display(
                HYDROGEN1,
                Text.translatable("advancements.radik.hydrogen1.title"),
                Text.translatable("advancements.radik.hydrogen1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(capsule_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_hydrogen", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 3)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/hydrogen_reward1")))
            .build(consumer, "radik:hydrogen1");

        AdvancementEntry hydrogen_2 = Advancement.Builder.create()
            .display(
                HYDROGEN2,
                Text.translatable("advancements.radik.hydrogen2.title"),
                Text.translatable("advancements.radik.hydrogen2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(hydrogen_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_hydrogen", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 3)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/hydrogen_reward2")))
            .build(consumer, "radik:hydrogen2");
        AdvancementEntry hydrogen_3 = Advancement.Builder.create()
            .display(
                HYDROGEN3,
                Text.translatable("advancements.radik.hydrogen3.title"),
                Text.translatable("advancements.radik.hydrogen3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(hydrogen_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_hydrogen", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 256, 3)))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/hydrogen_reward3")))
            .build(consumer, "radik:hydrogen3");

        AdvancementEntry helium_1 = Advancement.Builder.create()
            .display(
                HELIUM1,
                Text.translatable("advancements.radik.helium1.title"),
                Text.translatable("advancements.radik.helium1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(capsule_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_helium", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 4)))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/helium_reward1")))
            .build(consumer, "radik:helium1");
        AdvancementEntry helium_2 = Advancement.Builder.create()
            .display(
                HELIUM2,
                Text.translatable("advancements.radik.helium2.title"),
                Text.translatable("advancements.radik.helium2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(helium_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_helium", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 4)))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/helium_reward2")))
            .build(consumer, "radik:helium2");
        AdvancementEntry helium_3 = Advancement.Builder.create()
            .display(
                HELIUM3,
                Text.translatable("advancements.radik.helium3.title"),
                Text.translatable("advancements.radik.helium3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(helium_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_helium", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 256, 4)))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/helium_reward3")))
            .build(consumer, "radik:helium3");

        AdvancementEntry capsule_2 = Advancement.Builder.create()
            .display(
                WATER,
                Text.translatable("advancements.radik.capsule_2.title"),
                Text.translatable("advancements.radik.capsule_2.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(capsule_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_empty", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 0)))
            .criterion("get_water", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 1)))
            .criterion("get_lava", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 2)))
            .criterion("get_hydrogen", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 3)))
            .criterion("get_helium", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 1, 4)))
            .rewards(AdvancementRewards.Builder.experience(100).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/capsule_2")))
            .build(consumer, "radik:capsule_2");

        AdvancementEntry capsule_3 = Advancement.Builder.create()
            .display(
                LAVA,
                Text.translatable("advancements.radik.capsule_3.title"),
                Text.translatable("advancements.radik.capsule_3.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(capsule_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("get_empty", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 0)))
            .criterion("get_water", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 1)))
            .criterion("get_lava", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 2)))
            .criterion("get_hydrogen", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 3)))
            .criterion("get_helium", CAPSULE_FLUID_CRITERION.create(new CapsuleFluidCriterion.Conditions(Optional.empty(), 16, 4)))
            .rewards(AdvancementRewards.Builder.experience(400).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/capsule_3")))
            .build(consumer, "radik:capsule_3");

        AdvancementEntry cigarette_1 = Advancement.Builder.create()
            .display(
                CIGARETTE,
                Text.translatable("advancements.radik.food_cigarette_1.title"),
                Text.translatable("advancements.radik.food_cigarette_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                true
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(CIGARETTE))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/food/cigarette_1")))
            .build(consumer, "radik:cigarette_1");

        AdvancementEntry cigarette_2 = Advancement.Builder.create()
            .display(
                CIGARETTE_PACK,
                Text.translatable("advancements.radik.food_cigarette_2.title"),
                Text.translatable("advancements.radik.food_cigarette_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                true
            )
            .parent(cigarette_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", ConsumeItemCriterion.Conditions.item(wrapperLookup.getOrThrow(RegistryKeys.ITEM), CIGARETTE))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/food/cigarette_2")))
            .build(consumer, "radik:cigarette_2");

        AdvancementEntry dye_1 = Advancement.Builder.create()
            .display(
                DYE_NAVY,
                Text.translatable("advancements.radik.dye_1.title"),
                Text.translatable("advancements.radik.dye_1.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                true
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("1", InventoryChangedCriterion.Conditions.items(RED_DYE))
            .criterion("2", InventoryChangedCriterion.Conditions.items(ORANGE_DYE))
            .criterion("3", InventoryChangedCriterion.Conditions.items(YELLOW_DYE))
            .criterion("4", InventoryChangedCriterion.Conditions.items(GREEN_DYE))
            .criterion("5", InventoryChangedCriterion.Conditions.items(LIME_DYE))
            .criterion("6", InventoryChangedCriterion.Conditions.items(MAGENTA_DYE))
            .criterion("7", InventoryChangedCriterion.Conditions.items(PURPLE_DYE))
            .criterion("8", InventoryChangedCriterion.Conditions.items(BLACK_DYE))
            .criterion("9", InventoryChangedCriterion.Conditions.items(LIGHT_BLUE_DYE))
            .criterion("10", InventoryChangedCriterion.Conditions.items(GRAY_DYE))
            .criterion("11", InventoryChangedCriterion.Conditions.items(LIGHT_GRAY_DYE))
            .criterion("12", InventoryChangedCriterion.Conditions.items(WHITE_DYE))
            .criterion("13", InventoryChangedCriterion.Conditions.items(CYAN_DYE))
            .criterion("14", InventoryChangedCriterion.Conditions.items(PINK_DYE))
            .criterion("15", InventoryChangedCriterion.Conditions.items(BROWN_DYE))
            .criterion("16", InventoryChangedCriterion.Conditions.items(BLUE_DYE))
            .criterion("17", InventoryChangedCriterion.Conditions.items(DYE_NAVY))
            .criterion("18", InventoryChangedCriterion.Conditions.items(DYE_AMBER))
            .criterion("19", InventoryChangedCriterion.Conditions.items(DYE_BEIGE))
            .criterion("20", InventoryChangedCriterion.Conditions.items(DYE_AQUA))
            .criterion("21", InventoryChangedCriterion.Conditions.items(DYE_CORAL))
            .criterion("22", InventoryChangedCriterion.Conditions.items(DYE_FOREST))
            .criterion("23", InventoryChangedCriterion.Conditions.items(DYE_GINGER))
            .criterion("24", InventoryChangedCriterion.Conditions.items(DYE_INDIGO))
            .criterion("25", InventoryChangedCriterion.Conditions.items(DYE_MAROON))
            .criterion("26", InventoryChangedCriterion.Conditions.items(DYE_MINT))
            .criterion("27", InventoryChangedCriterion.Conditions.items(DYE_OLIVE))
            .criterion("28", InventoryChangedCriterion.Conditions.items(DYE_ROSE))
            .criterion("29", InventoryChangedCriterion.Conditions.items(DYE_SLATE))
            .criterion("30", InventoryChangedCriterion.Conditions.items(DYE_VERDANT))
            .criterion("31", InventoryChangedCriterion.Conditions.items(DYE_TAN))
            .criterion("32", InventoryChangedCriterion.Conditions.items(DYE_TEAL))
            .rewards(AdvancementRewards.Builder.experience(350).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/dye/1")))
            .build(consumer, "radik:dye_1");

        AdvancementEntry dye_2 = Advancement.Builder.create()
            .display(
                BRICK23,
                Text.translatable("advancements.radik.dye_2.title"),
                Text.translatable("advancements.radik.dye_2.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                true
            )
            .parent(dye_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("1", InventoryChangedCriterion.Conditions.items(BRICK1))
            .criterion("2", InventoryChangedCriterion.Conditions.items(BRICK2))
            .criterion("3", InventoryChangedCriterion.Conditions.items(BRICK3))
            .criterion("4", InventoryChangedCriterion.Conditions.items(BRICK4))
            .criterion("5", InventoryChangedCriterion.Conditions.items(BRICK5))
            .criterion("6", InventoryChangedCriterion.Conditions.items(BRICK6))
            .criterion("7", InventoryChangedCriterion.Conditions.items(BRICK7))
            .criterion("8", InventoryChangedCriterion.Conditions.items(BRICK8))
            .criterion("9", InventoryChangedCriterion.Conditions.items(BRICK9))
            .criterion("10", InventoryChangedCriterion.Conditions.items(BRICK10))
            .criterion("11", InventoryChangedCriterion.Conditions.items(BRICK11))
            .criterion("12", InventoryChangedCriterion.Conditions.items(BRICK12))
            .criterion("13", InventoryChangedCriterion.Conditions.items(BRICK13))
            .criterion("14", InventoryChangedCriterion.Conditions.items(BRICK14))
            .criterion("15", InventoryChangedCriterion.Conditions.items(BRICK15))
            .criterion("16", InventoryChangedCriterion.Conditions.items(BRICK16))
            .criterion("17", InventoryChangedCriterion.Conditions.items(BRICK17))
            .criterion("18", InventoryChangedCriterion.Conditions.items(BRICK18))
            .criterion("19", InventoryChangedCriterion.Conditions.items(BRICK19))
            .criterion("20", InventoryChangedCriterion.Conditions.items(BRICK20))
            .criterion("21", InventoryChangedCriterion.Conditions.items(BRICK21))
            .criterion("22", InventoryChangedCriterion.Conditions.items(BRICK22))
            .criterion("23", InventoryChangedCriterion.Conditions.items(BRICK23))
            .criterion("24", InventoryChangedCriterion.Conditions.items(BRICK24))
            .criterion("25", InventoryChangedCriterion.Conditions.items(BRICK25))
            .criterion("26", InventoryChangedCriterion.Conditions.items(BRICK26))
            .criterion("27", InventoryChangedCriterion.Conditions.items(BRICK27))
            .criterion("28", InventoryChangedCriterion.Conditions.items(BRICK28))
            .criterion("29", InventoryChangedCriterion.Conditions.items(BRICK29))
            .criterion("30", InventoryChangedCriterion.Conditions.items(BRICK30))
            .criterion("31", InventoryChangedCriterion.Conditions.items(BRICK31))
            .criterion("32", InventoryChangedCriterion.Conditions.items(BRICK32))
            .rewards(AdvancementRewards.Builder.experience(500).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/dye/2")))
            .build(consumer, "radik:dye_2");

        AdvancementEntry industry_1 = Advancement.Builder.create()
            .display(
                COPPER_INGOT,
                Text.translatable("advancements.radik.industry_1.title"),
                Text.translatable("advancements.radik.industry_1.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(COPPER_INGOT))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/1")))
            .build(consumer, "radik:industry_1");

        AdvancementEntry industry_2 = Advancement.Builder.create()
            .display(
                IRON_BARS,
                Text.translatable("advancements.radik.industry_2.title"),
                Text.translatable("advancements.radik.industry_2.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(industry_1)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(IRON_BARS))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/2")))
            .build(consumer, "radik:industry_2");

        AdvancementEntry industry_3 = Advancement.Builder.create()
            .display(
                SODIUM_LAMP,
                Text.translatable("advancements.radik.industry_3.title"),
                Text.translatable("advancements.radik.industry_3.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(industry_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(SODIUM_LAMP))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/3")))
            .build(consumer, "radik:industry_3");

        AdvancementEntry industry_4 = Advancement.Builder.create()
            .display(
                MERCURY_LAMP,
                Text.translatable("advancements.radik.industry_4.title"),
                Text.translatable("advancements.radik.industry_4.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(industry_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(MERCURY_LAMP))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/4")))
            .build(consumer, "radik:industry_4");

        AdvancementEntry industry_5 = Advancement.Builder.create()
            .display(
                LAMP,
                Text.translatable("advancements.radik.industry_5.title"),
                Text.translatable("advancements.radik.industry_5.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(industry_2)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(LAMP))
            .rewards(AdvancementRewards.Builder.experience(10).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/5")))
            .build(consumer, "radik:industry_5");

        AdvancementEntry industry_6 = Advancement.Builder.create()
            .display(
                FONAR_LAMP20,
                Text.translatable("advancements.radik.industry_6.title"),
                Text.translatable("advancements.radik.industry_6.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(industry_3)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(FONAR_LAMP20))
            .rewards(AdvancementRewards.Builder.experience(40).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/6")))
            .build(consumer, "radik:industry_6");

        AdvancementEntry industry_7 = Advancement.Builder.create()
            .display(
                FONAR_LAMP4,
                Text.translatable("advancements.radik.industry_7.title"),
                Text.translatable("advancements.radik.industry_7.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(industry_3)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("1", InventoryChangedCriterion.Conditions.items(FONAR_LAMP1))
            .criterion("2", InventoryChangedCriterion.Conditions.items(FONAR_LAMP2))
            .criterion("3", InventoryChangedCriterion.Conditions.items(FONAR_LAMP3))
            .criterion("4", InventoryChangedCriterion.Conditions.items(FONAR_LAMP4))
            .rewards(AdvancementRewards.Builder.experience(60).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/7")))
            .build(consumer, "radik:industry_7");

        AdvancementEntry industry_8 = Advancement.Builder.create()
            .display(
                FONAR_LAMP4,
                Text.translatable("advancements.radik.industry_8.title"),
                Text.translatable("advancements.radik.industry_8.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(industry_4)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("1", InventoryChangedCriterion.Conditions.items(FONAR_LAMP11))
            .criterion("2", InventoryChangedCriterion.Conditions.items(FONAR_LAMP12))
            .criterion("3", InventoryChangedCriterion.Conditions.items(FONAR_LAMP13))
            .criterion("4", InventoryChangedCriterion.Conditions.items(FONAR_LAMP14))
            .rewards(AdvancementRewards.Builder.experience(60).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/8")))
            .build(consumer, "radik:industry_8");

        AdvancementEntry industry_9 = Advancement.Builder.create()
            .display(
                WOOD_STORAGE_BLOCK,
                Text.translatable("advancements.radik.industry_9.title"),
                Text.translatable("advancements.radik.industry_9.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                false
            )
            .parent(pull)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(WOOD_STORAGE_BLOCK))
            .rewards(AdvancementRewards.Builder.experience(50).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/9")))
            .build(consumer, "radik:industry_9");

        AdvancementEntry industry_10 = Advancement.Builder.create()
            .display(
                DIAMOND_STORAGE_BLOCK,
                Text.translatable("advancements.radik.industry_10.title"),
                Text.translatable("advancements.radik.industry_10.description"),
                null,
                AdvancementFrame.GOAL,
                true,
                true,
                false
            )
            .parent(industry_9)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(DIAMOND_STORAGE_BLOCK))
            .rewards(AdvancementRewards.Builder.experience(300).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/10")))
            .build(consumer, "radik:industry_10");

        AdvancementEntry industry_11 = Advancement.Builder.create()
            .display(
                OBSIDIAN_STORAGE_BLOCK,
                Text.translatable("advancements.radik.industry_11.title"),
                Text.translatable("advancements.radik.industry_11.description"),
                null,
                AdvancementFrame.CHALLENGE,
                true,
                true,
                false
            )
            .parent(industry_10)
            .criterion("pull", Criterias.FIRST_JOIN.create(new FirstJoinCriterion.Conditions(Optional.empty(), 1)))
            .criterion("event", InventoryChangedCriterion.Conditions.items(OBSIDIAN_STORAGE_BLOCK))
            .rewards(AdvancementRewards.Builder.experience(1000).setFunction(Identifier.of(Radik.MOD_ID, "advancements/base/industry/11")))
            .build(consumer, "radik:industry_11");
    }
}
