package com.radik.logic;

import com.radik.RadikCore;

public class LogicInitialize {
    @RadikCore
    public static void register() {
        OnLogin.register();
        TransChatMessages.register();
        OnWorldTick.register();
        OnUse.register();
        OnBreak.register();
        OnEntityLoad.register();
        OnPlace.initialize();
    }
}
