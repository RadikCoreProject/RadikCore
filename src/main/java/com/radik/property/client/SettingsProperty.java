package com.radik.property.client;

import com.radik.property.PropertyEnum;

import java.util.List;

public enum SettingsProperty implements PropertyEnum<SettingsProperty> {
    NETHER_PLACES ("nether_ways", "true", PropertyType.BOOLEAN, Boolean.class),
    OVERWORLD_PLACES ("overworld_ways", "true", PropertyType.BOOLEAN, Boolean.class),
    PRESENT_NOTIFY ("present_notify", "true", PropertyType.BOOLEAN, Boolean.class),
    ME_NOTIFY ("me_notify", "true", PropertyType.BOOLEAN, Boolean.class),
    EVENT_PARTICLES ("event_particles", "medium", PropertyType.QUADRO, String.class),
    TEST_FEATURES ("test_features", "false", PropertyType.BOOLEAN, Boolean.class);

    private final String id;
    private final String def;
    private final PropertyType propertyType;
    private final Class<?> clazz;

    SettingsProperty(String id, String def, PropertyType values, Class<?> clazz) {
        this.id = id;
        this.def = def;
        this.propertyType = values;
        this.clazz = clazz;
    }

    @Override public String getId() { return id; }
    @Override public String getDef() { return def; }
    @Override public Class<?> getType() { return clazz; }
    public PropertyType getProperyType() { return propertyType; }

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