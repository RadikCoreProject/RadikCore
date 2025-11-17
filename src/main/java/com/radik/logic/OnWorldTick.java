package com.radik.logic;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.radik.Data.*;
import static com.radik.util.ScoreboardAction.getObjective;

public class OnWorldTick {
    public static short HOUSE = -1;
    // TEST
    public static boolean MANA = false;

    protected static void register() {
        ServerTickEvents.START_WORLD_TICK.register(OnWorldTick::ticking);
    }

    /**
    * @author Radik
    * @test
    **/
    private static void ticking(@NotNull ServerWorld serverWorld) {
        if (!MANA) return;
        List<ServerPlayerEntity> players = SERVER.getPlayerManager().getPlayerList();
        Scoreboard scoreboard = serverWorld.getScoreboard();

        for (ServerPlayerEntity player : players) {
            Scoreboard scoreboard1 = player.getScoreboard();
            String name = player.getNameForScoreboard();
            ScoreAccess access = scoreboard1.getOrCreateScore(ScoreHolder.fromName(name), getObjective(scoreboard, "mana"));
            ScoreAccess access1 = scoreboard1.getOrCreateScore(ScoreHolder.fromName(name), getObjective(scoreboard, "max_mana"));
            int score = access.getScore();
            int score1 = access1.getScore();

            ItemStack stack = player.getMainHandStack();
            if (stack.getComponents().contains(STAFF_TYPE)) {
                Integer type = stack.getComponents().get(STAFF_TYPE);
                if (type == null) return;
                int mana = type == 1 ? 150 : 50;
                String text;
                if (score > mana) { text = "a"; } else { text = "4"; }
                player.sendMessage(Text.literal(String.format("§%s§l%s/%s", text, score, mana)), true);
            }

            if (access1.getScore() == 0) { access1.setScore(1000); }
            if (score < score1) { access.incrementScore(); };
        }
    }
}
