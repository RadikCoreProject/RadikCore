package com.radik.item.custom.chemistry;

import net.minecraft.text.Text;

public enum Types {
    GAS ("gas"),
    LIQUID ("liquid"),
    SOLID ("solid");

    private final Text translate;
    private final String id;

    Types(String text) {
        id = text;
        translate = Text.translatable("radik.chem.type." + text);
    }

    public Text getTranslate() {
        return translate;
    }

    public String getId() {
        return id;
    }
}
