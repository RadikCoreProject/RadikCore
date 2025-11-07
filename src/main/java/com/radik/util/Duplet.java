package com.radik.util;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Duplet<T, P>(T type, P parametrize) {
    public Duplet(@Nullable T type, @Nullable P parametrize) {
        this.type = type;
        this.parametrize = parametrize;
    }

    @Override
    public @Nullable T type() {
        return this.type;
    }

    @Override
    public @Nullable P parametrize() {
        return this.parametrize;
    }

    public boolean isEmpty() {
        return this.type == null && this.parametrize == null;
    }

    @Override
    public @NotNull String toString() {
        return String.format("Type: %s, Parametrize: %s", this.type != null ? this.type.toString() : "null", this.parametrize != null ? this.parametrize.toString() : "null");
    }
}
