package com.radik.property.server;

import java.util.Optional;

public enum InGameProperty {
    MINECART_MULTIPLIER ("minecart_multiplier", "50");

    private final String id;
    private final String defaults;

    InGameProperty(String id, String def) {
        this.id = id;
        this.defaults = def;
    }

    public String getId() {
        return id;
    }

    public String getDef() { return defaults; }

    public static Optional<InGameProperty> getProperty(String id) {
        for (InGameProperty property : InGameProperty.values()) {
            if (property.id.equals(id)) return Optional.of(property);
        }
        return Optional.empty();
    }
}
