package com.radik.util;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.radik.connecting.event.Event;

/**
 * Добавлены статические метод для сериализации / десериализации кодеков
 *
 * @author radik
 * @since 1.4.1
 * @see Event
 */
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

    @Contract("_, _ -> new")
    public static <T, P> @NotNull Duplet<T, P> of(T type, P parametrize) {
        return new Duplet<>(type, parametrize);
    }

    public static <T, P> Codec<Duplet<T, P>> createCodec(Codec<T> typeCodec, Codec<P> paramCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
            typeCodec.fieldOf("type").forGetter(Duplet::type),
            paramCodec.fieldOf("parametrize").forGetter(Duplet::parametrize)
        ).apply(instance, Duplet::new));
    }
}
