package com.radik.avancements.criteries;

import net.minecraft.advancement.criterion.Criteria;

public class Criterias {
    public static final CapsuleFluidCriterion CAPSULE_FLUID_CRITERION = Criteria.register("radik:capsule_fluid_criterion", new CapsuleFluidCriterion());
    public static final MedalCriterion MEDAL_CRITERION = Criteria.register("radik:medal_criterion", new MedalCriterion());
    public static final FirstJoinCriterion FIRST_JOIN = Criteria.register("radik:first_join_criterion", new FirstJoinCriterion());
    public static final EventCriterion EVENT_CRITERION = Criteria.register("radik:event_criterion", new EventCriterion());
    public static final TradeCriterion TRADE_CRITERION = Criteria.register("radik:trade_criterion", new TradeCriterion());

    public static void initialize() {
    }
}