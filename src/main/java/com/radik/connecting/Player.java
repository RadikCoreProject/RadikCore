package com.radik.connecting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    public String ip;
    public String name;
    public String password;
    public String muted;
    public String colorCode;
    public long id;
    public byte particle;
    public boolean bold;
    public boolean reg;
    public boolean law;

    public Player(String[] strings, long id, byte particle, boolean[] booleans) {
        this.name = strings[0];
        this.ip = strings[1];
        this.password = strings[2];
        this.muted = strings[3];
        this.colorCode = strings[4];
        this.bold = booleans[0];
        this.reg = booleans[1];
        this.law = booleans[2];
        this.id = id;
        this.particle = particle;
    }

    public Player() {
    }

    @Override
    public String toString() {
        String bolds = bold ? "l" : "";
        String colorCodes = colorCode == null ? "" : colorCode;
        AtomicReference<String> string = new AtomicReference<>(String.format("""
                Player %s:
                IPv4: %s
                id: %d
                password hash: %s
                muted: %s
                Color code: %s%s
                particle: %d
                reg: %s
                law: %s
                """, name, ip, id, password, muted.equals("0") ? false : muted, bolds, colorCodes, particle, reg, law));
        return string.toString();
    }
}
