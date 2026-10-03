package com.radik.block.custom.data;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public record ComparableParticleType(SimpleParticleType particle) implements Comparable<ComparableParticleType> {
    @Override
    public int compareTo(@NotNull ComparableParticleType o) {
        return Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(this.particle))
            .compareTo(Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(o.particle)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ComparableParticleType(SimpleParticleType particle1))) return false;
        return Objects.equals(this.particle, particle1);
    }

    @Override
    public @NotNull String toString() {
        return Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(particle)).toString();
    }
}
