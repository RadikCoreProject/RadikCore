package com.radik.connecting;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Decoration {
    public byte id;
    public byte type;
    public short cost;
    public String owner;
    public String description;
    public String name;

    @Contract(pure = true)
    public Decoration(byte @NotNull [] bytes, short cost, String @NotNull [] strings) {
        this.id = bytes[0];
        this.type = bytes[1];
        this.cost = cost;
        this.owner = strings[0];
        this.description = strings[1];
        this.name = strings[2];
    }

    @Override
    public String toString() {
        return String.format("""
                Decoration: %s
                id - %d
                cost - %d
                type - %d
                description - %s
                owner - %s
                """, name, id, cost, type, description, owner);
    }
}
