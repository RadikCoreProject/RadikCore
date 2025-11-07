package com.radik.logic;

import com.radik.registration.IRegistry;

public class LogicInitialize implements IRegistry {
    public static void initialize() {
        OnWorldTick.register();
    }
}
