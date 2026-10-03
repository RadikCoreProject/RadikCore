package com.radik.item;

import com.radik.MainInit;
import com.radik.Radik;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import static net.minecraft.item.equipment.EquipmentAssetKeys.REGISTRY_KEY;

public interface EquipmentAssetKeys {
    RegistryKey<EquipmentAsset> LEAD = register("lead");
    RegistryKey<EquipmentAsset> RADIATION_SUIT = register("lead");

    private static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(Radik.MOD_ID, name));
    }

    @MainInit
    static void initialize() {}
}
