package com.radik;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

import static net.minecraft.stat.Stats.CUSTOM;

public final class Stats {
    public static final Identifier INTERACT_WITH_ELECTROLYZER = register("interact_with_electrolyzer", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_EVENT_BLOCK = register("interact_with_event_block", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = Identifier.of(Radik.MOD_ID, id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
