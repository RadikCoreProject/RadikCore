package com.radik.util;

import org.jetbrains.annotations.Nullable;

public class Quadriplet<T, P, C, Q> implements Nplet<T, P> {

    private T t;
    private P p;
    private C c;
    private Q q;

    public Quadriplet(T type, P parametrize, C count, Q queue) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
        this.q = queue;
    }

    public T getType() { return this.t; }

    public P getParametrize() { return this.p; }

    public C getCount() { return this.c; }

    public Q getQueue() {
        return this.q;
    }

    @Override
    public boolean isEmpty() {
        return this.t == null && this.c == null && this.p == null && this.q == null;
    }

    public void setTriplet(T type, P parametrize, C count, Q queue) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
        this.q = queue;
    }
}
