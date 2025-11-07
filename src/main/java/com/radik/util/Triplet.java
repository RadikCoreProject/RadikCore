package com.radik.util;

import org.jetbrains.annotations.Nullable;

public class Triplet<T, P, C> implements Nplet<T, P> {

    private final T t;
    private final P p;
    private final C c;

    public Triplet(@Nullable T type, @Nullable P parametrize, C count) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
    }

    public @Nullable T getType() {
        return this.t;
    }

    public @Nullable P getParametrize() {
        return this.p;
    }

    public @Nullable C getCount() {
        return this.c;
    }

    @Override
    public boolean isEmpty() {
        return this.t == null && this.c == null && this.p == null;
    }
}
