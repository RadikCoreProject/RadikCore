package com.radik.logic;

import com.radik.MainInit;

public class LogicInitialize {
    @MainInit
    public static void initialize() {
        OnUse.register();
        WorldTick.register();
        OnBreak.register();
        ArmorListener.register();
        LootTableModifier.register();
    }
}
