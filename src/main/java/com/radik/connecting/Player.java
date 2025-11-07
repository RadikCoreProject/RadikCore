package com.radik.connecting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class Player {
    public String name;
    public String colorCode;
    public long id;
    public byte particle;
    public boolean bold;

    @Contract(pure = true)
    public Player(String @NotNull [] strings, long id, byte particle, boolean booleans) {
        this.name = strings[0];
        this.colorCode = strings[1];
        this.id = id;
        this.particle = particle;
        this.bold = booleans;
    }

    public Player() {
    }

    @Override
    public String toString() {
        String bolds = bold ? "l" : "";
        String colorCodes = colorCode == null ? "" : colorCode;
        AtomicReference<String> string = new AtomicReference<>(String.format("""
                Player %s:
                id: %d
                Color code: %s%s
                particle: %d
                """, name, id, bolds, colorCodes, particle));
        return string.toString();
    }
}
