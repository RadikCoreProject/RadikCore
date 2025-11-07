package com.radik.block.custom.blockentity;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.block.custom.blockentity.event.EventBlockEntity;
import com.radik.registration.IRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntities implements IRegistry {
//    public static final BlockEntityType<ElectrolyzerEntity> ELECTROLYZER_ENTITY =
//            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "electrolyzer"),
//                    FabricBlockEntityTypeBuilder.create(ElectrolyzerEntity::new, RegisterBlocks.ELECTROLYZER).build());

    public static final BlockEntityType<EventBlockEntity> EVENT_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "event"),
                    FabricBlockEntityTypeBuilder.create(EventBlockEntity::new, RegisterBlocks.EVENT_BLOCK).build());

    public static void initialize() {}
}
