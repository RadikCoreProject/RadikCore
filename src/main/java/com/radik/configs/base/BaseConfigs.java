package com.radik.configs.base;

import com.radik.Radik;

public final class BaseConfigs {
    public static RadiationConfig RADIATION_CONFIG = RadiationConfig.getInstance();

    public static void reloadAll() {
        RADIATION_CONFIG.reload();
        Radik.LOGGER.info("All server configs reloaded");
    }
}
