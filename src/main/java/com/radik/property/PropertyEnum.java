package com.radik.property;

import java.util.Optional;

public interface PropertyEnum<T extends Enum<T> & PropertyEnum<T>> {
    String getId();
    Object getDef();
    Class<?> getType();

    static <E extends Enum<E> & PropertyEnum<E>> Optional<E> getProperty(Class<E> enumClass, String id) {
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getId().equals(id)) {
                return Optional.of(constant);
            }
        }
        return Optional.empty();
    }
}
