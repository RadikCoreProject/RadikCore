package com.radik.item.custom.chemistry;

import net.minecraft.text.Text;

public enum Behaviours {
    PLACEABLE ("placeable");

    private final Text translate;
    private final String id;

    Behaviours(String text) {
        id = text;
        translate = Text.translatable("radik.chem.beh." + text);
    }

    public Text getTranslate() {
        return translate;
    }

    public String getId() {
        return id;
    }
}
