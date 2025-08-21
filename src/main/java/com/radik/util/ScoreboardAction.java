package com.radik.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class ScoreboardAction {
    public static boolean enough(int count, @NotNull PlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        ScoreAccess access = getAccess(scoreboard, "mana", player.getNameForScoreboard());
        return access.getScore() >= count;
    }

    public static void setScore(String name, int count, @NotNull PlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        ScoreAccess access = getAccess(scoreboard, name, player.getNameForScoreboard());
        access.setScore(count);
    }

    public static void changeScore(String name, int count, @NotNull PlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        ScoreAccess access = getAccess(scoreboard, name, player.getNameForScoreboard());
        access.setScore(access.getScore() + count);
    }

    private static void addObjective(Scoreboard scoreboard, String name, String name2) {
        scoreboard.addObjective(name, ScoreboardCriterion.DUMMY, Text.literal(name2), ScoreboardCriterion.RenderType.INTEGER, false, null);
    }

    public static ScoreboardObjective getObjective(Scoreboard scoreboard, String name) {
        if (scoreboard.getNullableObjective(name) == null) addObjective(scoreboard, name, name);
        return scoreboard.getNullableObjective(name);
    }

    private static ScoreAccess getAccess(Scoreboard scoreboard, String name, String username) {
        if (getObjective(scoreboard, name) == null) { addObjective(scoreboard, name, name); }
        return scoreboard.getOrCreateScore(ScoreHolder.fromName(username), getObjective(scoreboard, name));
    }
}
