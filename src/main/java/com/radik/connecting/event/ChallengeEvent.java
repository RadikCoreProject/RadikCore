package com.radik.connecting.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ChallengeEvent {
    HALLOWEEN,
    WINTER,
    FLOWERY;

    @Contract(pure = true)
    public @NotNull String toNbt() {
        return this.name();
    }

    public static ChallengeEvent fromNbt(String nbtString) {
        return ChallengeEvent.valueOf(nbtString);
    }
}
