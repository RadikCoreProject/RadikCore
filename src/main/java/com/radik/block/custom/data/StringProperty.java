package com.radik.block.custom.data;

import net.minecraft.state.property.Property;
import java.util.*;

public final class StringProperty extends Property<String> {
    private final List<String> values;

    private StringProperty(String name, List<String> values) {
        super(name, String.class);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Values of " + name + " must not be empty");
        }
        Set<String> set = new HashSet<>(values);
        if (set.size() != values.size()) {
            throw new IllegalArgumentException("Values of " + name + " must be unique");
        }
        this.values = List.copyOf(values);
    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof StringProperty that)) return false;
        return super.equals(object) && this.values.equals(that.values);
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + values.hashCode();
    }

    public static StringProperty of(String name, String... values) {
        return of(name, Arrays.asList(values));
    }

    public static StringProperty of(String name, List<String> values) {
        return new StringProperty(name, values);
    }

    @Override
    public Optional<String> parse(String name) {
        return values.contains(name) ? Optional.of(name) : Optional.empty();
    }

    @Override
    public String name(String value) {
        return value;
    }

    @Override
    public int ordinal(String value) {
        int index = values.indexOf(value);
        return index >= 0 ? index : -1;
    }
}
