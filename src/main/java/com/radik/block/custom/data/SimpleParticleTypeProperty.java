package com.radik.block.custom.data;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import java.util.*;
import java.util.stream.Collectors;

public final class SimpleParticleTypeProperty extends Property<ComparableParticleType> {
    private final List<ComparableParticleType> values;
    private final Map<String, ComparableParticleType> byId;

    private SimpleParticleTypeProperty(String name, List<SimpleParticleType> particles) {
        super(name, ComparableParticleType.class);
        if (particles.isEmpty()) throw new IllegalArgumentException("Values must not be empty");
        this.values = particles.stream().map(ComparableParticleType::new).toList();
        this.byId = this.values.stream().collect(Collectors.toMap(
            v -> Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(v.particle())).toString(),
            v -> v
        ));
    }

    public static SimpleParticleTypeProperty of(String name, SimpleParticleType... particles) {
        return of(name, Arrays.asList(particles));
    }

    public static SimpleParticleTypeProperty of(String name, List<SimpleParticleType> particles) {
        return new SimpleParticleTypeProperty(name, particles);
    }

    @Override
    public List<ComparableParticleType> getValues() {
        return values;
    }

    @Override
    public Optional<ComparableParticleType> parse(String name) {
        return Optional.ofNullable(byId.get(name));
    }

    @Override
    public String name(ComparableParticleType value) {
        return Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(value.particle())).toString();
    }

    @Override
    public int ordinal(ComparableParticleType value) {
        return values.indexOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleParticleTypeProperty that)) return false;
        return super.equals(o) && this.values.equals(that.values);
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + values.hashCode();
    }
}
