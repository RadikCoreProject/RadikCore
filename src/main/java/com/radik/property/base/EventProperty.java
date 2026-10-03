package com.radik.property.base;

import com.radik.connecting.event.ChallengeEvent;
import com.radik.property.PropertyEnum;
import org.jetbrains.annotations.NotNull;

public enum EventProperty implements PropertyEnum<EventProperty> {
    // чем выше число, тем меньше шанс
    GREEN_CANDY_DROP_CHANCE ("green_candy_drop_chance", 100, ChallengeEvent.HALLOWEEN, Integer.class),
    YELLOW_CANDY_DROP_CHANCE ("yellow_candy_drop_chance", 100, ChallengeEvent.HALLOWEEN, Integer.class),
    AXE_STUN_CHANCE ("axe_stun_chance", 75, ChallengeEvent.HALLOWEEN, Integer.class),
    SHOVEL_DROP_CHANCE ("shovel_drop_chance", 75, ChallengeEvent.HALLOWEEN, Integer.class),
    LEAVE_DROP_CHANCE ("leave_drop_chance", 30, ChallengeEvent.SUMMER, Integer.class),
    CLEVER_DROP_CHANCE ("clever_drop_chance", 100, ChallengeEvent.SUMMER, Integer.class),
    PLACING_ROOT ("placing_root", 2, ChallengeEvent.HALLOWEEN, Integer.class),
    WEEKLY_CHALLENGE_MULTIPLIER ("weekly_challenge_multiplier", 10, ChallengeEvent.HALLOWEEN, Integer.class),
    WEEKLY_REWARD_MULTIPLIER ("weekly_reward_multiplier", 7, ChallengeEvent.HALLOWEEN, Integer.class),
    GLOBAL_CHALLENGE_MULTIPLIER ("global_challenge_multiplier", 100, ChallengeEvent.HALLOWEEN, Integer.class),
    GLOBAL_REWARD_MULTIPLIER ("global_reward_multiplier", 6, ChallengeEvent.HALLOWEEN, Integer.class),

    FLOWERY_AXE_APPLE_CHANCE ("flowery_axe_apple_chance", 10, ChallengeEvent.FLOWERY, Double.class),
    FLOWERY_AXE_GOLDEN_APPLE_CHANCE ("flowery_axe_golden_apple_chance", 3, ChallengeEvent.FLOWERY, Double.class),
    FLOWERY_AXE_NOTCH_APPLE_CHANCE ("flowery_axe_notch_apple_chance", 0.01F, ChallengeEvent.FLOWERY, Double.class),
    FLOWERY_PICKAXE_ULTA_CHANCE ("flowery_pickaxe_ulta_chance", 5, ChallengeEvent.FLOWERY, Integer.class),
    FLOWERY_SWORD_ULTA_CHANCE ("flowery_pickaxe_ulta_chance", 5, ChallengeEvent.FLOWERY, Integer.class),
    FLOWERY_AXE_ULTA_CHANCE ("flowery_axe_ulta_chance", 5, ChallengeEvent.FLOWERY, Integer.class);

    private final String id;
    private final Object defaults;
    private final ChallengeEvent propertyType;
    private final Class<?> clazz;

    EventProperty(String id, Object def, ChallengeEvent values, Class<?> clazz) {
        this.id = id;
        this.defaults = def;
        this.propertyType = values;
        this.clazz = clazz;
    }

    public String getId() {
            return id;
        }
    public @NotNull Object getDef() { return defaults; }
    public Class<?> getType() {return clazz;}

    public ChallengeEvent getProperyType() {
            return propertyType;
        }
}