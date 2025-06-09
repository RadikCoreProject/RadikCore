package com.radik.logic;

public class LogicInitialize {
    public static void registerLogic() {
        // serveronly
//        OnLogin.register();
//        TransChatMessages.register();
//        OnUse.register();

        OnWorldTick.register();
        OnBreak.register();
        OnPlace.initialize();
        OnEntityUse.initialize();
    }
}
