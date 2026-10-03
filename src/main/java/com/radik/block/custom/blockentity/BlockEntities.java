package com.radik.block.custom.blockentity;

import com.radik.MainInit;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.embassy.EmbassyBlockEntity;
import com.radik.block.custom.blockentity.event.EventBlockEntity;
import com.radik.block.custom.blockentity.storage.StorageBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntities {
    public static final BlockEntityType<EventBlockEntity> EVENT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "event"),
                    FabricBlockEntityTypeBuilder.create(EventBlockEntity::new, RegisterBlocks.EVENT_BLOCK).build());

    public static final BlockEntityType<EmbassyBlockEntity> EMBASSY_BLOCK_ENTITY =
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "embassy"),
            FabricBlockEntityTypeBuilder.create(EmbassyBlockEntity::new, RegisterBlocks.EMBASSY_BLOCK).build());

    public static final BlockEntityType<StorageBlockEntity> STORAGE_BLOCK_ENTITY =
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "storage"),
            FabricBlockEntityTypeBuilder.create(StorageBlockEntity::new,
                RegisterBlocks.WOOD_STORAGE_BLOCK,
                RegisterBlocks.COPPER_STORAGE_BLOCK,
                RegisterBlocks.IRON_STORAGE_BLOCK,
                RegisterBlocks.GOLD_STORAGE_BLOCK,
                RegisterBlocks.DIAMOND_STORAGE_BLOCK,
                RegisterBlocks.EMERALD_STORAGE_BLOCK,
                RegisterBlocks.OBSIDIAN_STORAGE_BLOCK).build());

    @MainInit
    public static void initialize() {}
}
