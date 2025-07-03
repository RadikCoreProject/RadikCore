package com.radik.logic;

public class LogicInitialize {
    public static void registerLogic() {

        OnUse.register();
        OnWorldTick.register();
        ServerStruct.register();
    }
}
