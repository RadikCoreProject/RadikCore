package com.radik.item;

import com.radik.ModTags;
import com.radik.Radik;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface ArmorMaterials {
    RegistryKey<EquipmentAsset> LEAD_ARMOR_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Radik.MOD_ID, "lead"));
    RegistryKey<EquipmentAsset> RADIATION_SUIT_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Radik.MOD_ID, "radiation_suit"));

    ArmorMaterial LEAD = new ArmorMaterial(
        33, // Базовая прочность
        Map.of(
            EquipmentType.BOOTS, 3,
            EquipmentType.LEGGINGS, 6,
            EquipmentType.CHESTPLATE, 8,
            EquipmentType.HELMET, 3
        ),
        10, // Зачаровываемость
        SoundEvents.ITEM_ARMOR_EQUIP_IRON,
        2.0F, // toughness
        0.0F, // knockback resistance
        ModTags.Items.REPAIRS_LEAD_ARMOR, // Тег для починки
        LEAD_ARMOR_KEY // Ключ для текстур
    );


    ArmorMaterial RADIATION_SUIT = new ArmorMaterial(
        70,
        Map.of(
            EquipmentType.BOOTS, 1,
            EquipmentType.LEGGINGS, 2,
            EquipmentType.CHESTPLATE, 2,
            EquipmentType.HELMET, 1
        ),
        5,
        SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
        2.0F,
        0.0F,
        null,
        RADIATION_SUIT_KEY
    );
}
