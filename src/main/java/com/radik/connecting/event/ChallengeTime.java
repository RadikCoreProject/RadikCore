package com.radik.connecting.event;

import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum ChallengeTime {
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private final Text timer;

    ChallengeTime(String timer) {
        this.timer = Text.translatable("text.radik.event." + timer);
    }

    public Text getTimer() {
        return timer;
    }

    @Contract(pure = true)
    public @NotNull String toNbt() {
        return this.name();
    }

    public static ChallengeTime fromNbt(String nbtString) {
        return ChallengeTime.valueOf(nbtString);
    }
}
