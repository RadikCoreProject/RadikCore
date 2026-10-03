package com.radik.property.client;

import com.radik.client.ClientInit;
import com.radik.property.Property;

public class ClientProperties {
    public static Property<SettingsProperty> SETTINGS_PROPERTY = new Property<>(SettingsProperty.class, "settings");

    @ClientInit
    public static void initialize() {}
}
