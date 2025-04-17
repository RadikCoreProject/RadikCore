package com.radik.logic;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

import static com.radik.Music.WARDEN;
import static com.radik.Radik.command;

public class OnEntityLoad {
    private static final Map<UUID, Boolean> musicPlayingMap = new HashMap<>();
    protected static void register() {
        ServerEntityEvents.ENTITY_LOAD.register(OnEntityLoad::onLoad);
    }


    private static void onLoad(Entity entity, ServerWorld serverWorld) {
        if(entity instanceof WardenEntity) {
            UUID wardenId = entity.getUuid();
            if (!musicPlayingMap.getOrDefault(wardenId, false)) {
                serverWorld.playSound(entity, entity.getBlockPos(), WARDEN, SoundCategory.MUSIC, 7f, 1f);

                int x = entity.getBlockX();
                int y = entity.getBlockY();
                int z = entity.getBlockZ();
                Random random = new Random();

                for(int i = 0; i < 10; i++) {
                    int x1 = random.nextInt(11) - 5 + x;
                    int z1 = random.nextInt(11) - 5 + z;
                    command(String.format("summon pig %d %d %d", x1, y, z1));
                }

                command(String.format("execute positioned %d %d %d run effect give @e[type=pig, distance=..20] minecraft:levitation 10 1", x, y, z));
                command(String.format("execute positioned %d %d %d run effect give @e[type=pig, distance=..20] minecraft:slow_falling 25 1", x, y, z));
            }
        }
    }
}
