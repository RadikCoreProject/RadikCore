package com.radik.connecting.event;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ChallengeType {
    BROKE,
    PLACE,
    KILL,
    EAT;

    @Contract(pure = true)
    public @NotNull String toNbt() {
        return this.name();
    }

    public static ChallengeType fromNbt(String nbtString) {
        return ChallengeType.valueOf(nbtString);
    }
}
