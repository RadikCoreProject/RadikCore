package com.radik.property;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum EventProperty {
    // чем выше число, тем меньше шанс
    GREEN_CANDY_DROP_CHANCE ("green_candy_drop_chance", 100, EventType.HALLOWEEN),
    YELLOW_CANDY_DROP_CHANCE ("yellow_candy_drop_chance", 100, EventType.HALLOWEEN),
    AXE_STUN_CHANCE ("axe_stun_chance", 75, EventType.HALLOWEEN),
    PLACING_ROOT ("placing_root", 2, EventType.HALLOWEEN),
    WEEKLY_CHALLENGE_MULTIPLIER ("weekly_challenge_multiplier", 10, EventType.HALLOWEEN),
    WEEKLY_REWARD_MULTIPLIER ("weekly_reward_multiplier", 7, EventType.HALLOWEEN),
    GLOBAL_CHALLENGE_MULTIPLIER ("global_challenge_multiplier", 100, EventType.HALLOWEEN),
    GLOBAL_REWARD_MULTIPLIER ("global_reward_multiplier", 6, EventType.HALLOWEEN);

    private final String id;
    private final int defaults;
    private final EventType propertyType;

    EventProperty(String id, int def, EventType values) {
        this.id = id;
        this.defaults = def;
        this.propertyType = values;
    }

    public String getId() {
        return id;
    }

    @Contract(pure = true)
    public @NotNull String getDef() { return String.valueOf(defaults); }

    public EventType getProperyType() {
        return propertyType;
    }

    public static Optional<EventProperty> getProperty(String id) {
        for (EventProperty property : EventProperty.values()) {
            if (property.id.equals(id)) return Optional.of(property);
        }
        return Optional.empty();
    }

    public enum EventType {
        HALLOWEEN,
        WINTER
    }
}
