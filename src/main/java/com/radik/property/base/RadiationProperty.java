package com.radik.property.base;

import com.radik.property.PropertyEnum;

public enum RadiationProperty implements PropertyEnum<RadiationProperty> {
    LEAD_PART_BUFF ("lead_part_buff", 5, Integer.class),
    FULL_LEAD_SET_BUFF ("full_lead_set_buff", 10, Integer.class),
    LEAD_PART_TIMER ("lead_part_timer", 10, Integer.class);

    private final String id;
    private final Object defaults;
    private final Class<?> clazz;

    RadiationProperty(String id, Object def, Class<?> clazz) {
        this.id = id;
        this.defaults = def;
        this.clazz = clazz;
    }

    @Override public String getId() { return id; }
    @Override public Object getDef() { return defaults; }
    @Override public Class<?> getType() { return clazz; }
}