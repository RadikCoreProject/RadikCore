package com.radik.util;

import org.jetbrains.annotations.Nullable;

public interface Nplet<T, P> {
    boolean isEmpty();

    @Nullable T getType();

    @Nullable P getParametrize();
}
