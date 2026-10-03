package com.radik.item.custom.chemistry;

import net.minecraft.text.Text;

import java.util.List;

public enum VialContainer {
    SALT_WATER ("salt_water", Types.LIQUID, List.of(Behaviours.PLACEABLE)),
    WATER ("salt_water", Types.LIQUID, List.of(Behaviours.PLACEABLE)),
    DISTILLED_WATER ("distilled_water", Types.LIQUID, List.of()),
    HYDROGEN ("hydrogen", Types.GAS, List.of(Behaviours.PLACEABLE)),
    HELIUM ("helium", Types.GAS, List.of(Behaviours.PLACEABLE));


    private final Text translate;
    private final Types type;
    private final String id;
    private final List<Behaviours> behaviours;

    VialContainer(String text, Types type, List<Behaviours> b) {
        this.behaviours = b;
        this.id = text;
        this.translate = Text.translatable("radik.chem.vial." + text);
        this.type = type;
    }

    public Text getTranslate() {
        return translate;
    }

    public Types getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public List<Behaviours> getBehaviours() {
        return behaviours;
    }
}
