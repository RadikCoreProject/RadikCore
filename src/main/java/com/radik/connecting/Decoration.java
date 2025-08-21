package com.radik.connecting;

public class Decoration {
    public byte id;
    public byte type;
    public short cost;
    public boolean own;
    public String owner;
    public String description;
    public String name;

    public Decoration(byte[] bytes, short cost, boolean own, String[] strings) {
        this.id = bytes[0];
        this.type = bytes[1];
        this.cost = cost;
        this.own = own;
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
                own - %b
                """, name, id, cost, type, description, owner, own);
    }
}
