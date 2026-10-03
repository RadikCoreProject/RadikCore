package com.radik.property.base;

import com.radik.MainInit;
import com.radik.property.Property;

public class BaseProperties {
    public static Property<EventProperty> EVENT_PROPERTY = new Property<>(EventProperty.class, "event");
    public static Property<InGameProperty> INGAME_PROPERTY = new Property<>(InGameProperty.class, "inGame");
    public static Property<RadiationProperty> RADIATION_PROPERTY = new Property<>(RadiationProperty.class, "radiation");

    @MainInit
    public static void initialize() {}
}
