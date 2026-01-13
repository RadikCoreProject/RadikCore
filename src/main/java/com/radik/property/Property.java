package com.radik.property;

import java.util.List;
import java.util.Optional;

public enum Property {
    NETHER_PLACES ("nether_ways", "true", PropertyType.BOOLEAN),
    OVERWORLD_PLACES ("overworld_ways", "true", PropertyType.BOOLEAN),
    PRESENT_NOTIFY ("present_notify", "true", PropertyType.BOOLEAN),
    ME_NOTIFY ("me_notify", "true", PropertyType.BOOLEAN),
    EVENT_PARTICLES ("event_particles", "medium", PropertyType.QUADRO);

    private final String id;
    private final String def;
    private final PropertyType propertyType;

    Property(String id, String def, PropertyType values) {
        this.id = id;
        this.def = def;
        this.propertyType = values;
    }

    public String getId() {
        return id;
    }

    public String getDef() {
        return def;
    }

    public PropertyType getProperyType() {
        return propertyType;
    }

    public static Optional<Property> getProperty(String id) {
        for (Property property : Property.values()) {
            if (property.id.equals(id)) return Optional.of(property);
        }
        return Optional.empty();
    }

    public static int getOrdinal(String property) {
        for (PropertyType properti : PropertyType.values()) {
            if (properti.values.contains(property)) return properti.values.indexOf(property);
        }
        return 0;
    }

    public enum PropertyType {
        BOOLEAN (List.of("false", "true")),
        QUADRO (List.of("none", "low", "medium", "max"));

        private final List<String> values;

        PropertyType(List<String> values) {
            this.values = values;
        }

        public List<String> getValues() {
            return values;
        }
    }
}
