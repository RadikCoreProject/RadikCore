package com.radik.connecting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlayerSettings {
    public final boolean netherHotSpot;
    public final boolean overworldHotSpot;
    public final boolean presentOpening;
    public final boolean antiMuteCommands;

    @Contract(pure = true)
    public PlayerSettings(boolean @NotNull [] booleans) {
        this.netherHotSpot = booleans[0];
        this.overworldHotSpot = booleans[1];
        this.presentOpening = booleans[2];
        this.antiMuteCommands = booleans[3];
    }

    public PlayerSettings(boolean netherHotSpot, boolean overworldHotSpot, boolean presentOpening, boolean antiMuteCommands) {
        this.netherHotSpot = netherHotSpot;
        this.overworldHotSpot = overworldHotSpot;
        this.presentOpening = presentOpening;
        this.antiMuteCommands = antiMuteCommands;
    }

    public byte toByte() {
        byte rs = 0;
        if (netherHotSpot) rs |= 1;
        if (overworldHotSpot) rs |= 2;
        if (presentOpening) rs |= 4;
        if (antiMuteCommands) rs |= 8;
        return rs;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlayerSettings fromByte(byte data) {
        boolean[] booleans = new boolean[4];
        booleans[0] = (data & 1) != 0;
        booleans[1] = (data & 2) != 0;
        booleans[2] = (data & 4) != 0;
        booleans[3] = (data & 8) != 0;
        return new PlayerSettings(booleans);
    }

    @Override
    public String toString() {
        return String.format("PlayerSettings[netherHotSpot=%s, overworldHotSpot=%s, presentOpening=%s, antiMuteCommands=%s]", netherHotSpot, overworldHotSpot, presentOpening, antiMuteCommands);
    }
}