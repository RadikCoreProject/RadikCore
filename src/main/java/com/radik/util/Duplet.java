package com.radik.util;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Duplet<T, P> implements Nplet<T, P> {

    private T t = null;
    private P p = null;

    public Duplet(@Nullable T type, @Nullable P parametrize) {
        this.t = type;
        this.p = parametrize;
    }

    public @Nullable T type() {
        return this.t;
    }

    public @Nullable P parametrize() {
        return this.p;
    }

    public boolean isEmpty() {
        return this.t == null && this.p == null;
    }

    public void setDuplet(T type, P parametrize) {
        this.t = type;
        this.p = parametrize;
    }

    @Override
    public @NotNull String toString() {
        return String.format("Type: %s, Parametrize: %s", this.t.toString(), this.p.toString());
    }
}
