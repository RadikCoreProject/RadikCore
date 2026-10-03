package com.radik.connecting.event;

import com.radik.client.particle.RegisterParticles;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ChallengeEvent implements StringIdentifiable {
    NONE (null, null, 0x000000),
    HALLOWEEN ("minecraft:dark_forest", () -> ParticleTypes.DRIPPING_DRIPSTONE_LAVA, 0xCC5500),
    WINTER ("minecraft:snowy_taiga", () -> ParticleTypes.SNOWFLAKE, 0x5555FF),
    FLOWERY ("minecraft:flower_forest", () -> RegisterParticles.FLOWERY_PARTICLE, 0xFF7777),
    SUMMER("minecraft:warm_ocean", () -> RegisterParticles.LUCKY_PARTICLE, 0x8888FF);

    private final String id;
    private final Supplier<SimpleParticleType> particle;
    private final int color;

    @Contract(pure = true)
    public @NotNull String toNbt() {
        return this.name();
    }

    public static ChallengeEvent fromNbt(String nbtString) {
        return ChallengeEvent.valueOf(nbtString);
    }

    ChallengeEvent(String name, Supplier<SimpleParticleType> particle, int color) {
        this.id = name;
        this.particle = particle;
        this.color = color;
    }

    public String id() {return id;}
    public SimpleParticleType particle() {return particle.get();}
    public int color() {return color;}

    @Contract(pure = true)
    @Override
    public @NotNull String asString() {
        return name().toLowerCase();
    }
}
