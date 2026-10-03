package com.radik.property.base;

import com.radik.property.PropertyEnum;

public enum InGameProperty implements PropertyEnum<InGameProperty> {
    MINECART_MULTIPLIER ("minecart_multiplier", 20, Integer.class);

    private final String id;
    private final Object defaults;
    private final Class<?> clazz;

    InGameProperty(String id, Object def, Class<?> clazz) {
        this.id = id;
        this.defaults = def;
        this.clazz = clazz;
    }

    @Override public String getId() { return id; }
    @Override public Object getDef() { return defaults; }
    @Override public Class<?> getType() { return clazz; }
}