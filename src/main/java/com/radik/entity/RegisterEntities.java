package com.radik.entity;

import com.radik.MainInit;
import com.radik.Radik;
import com.radik.entity.projictile.bullet.BulletEntity;
import com.radik.entity.projictile.ice_shard.IceShardEntity;
import com.radik.entity.projictile.water_drop.WaterDropEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RegisterEntities {
    private static final RegistryKey<EntityType<?>> BULLET_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "bullet"));

    public static final EntityType<BulletEntity> BULLET = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(Radik.MOD_ID, "bullet"),
        EntityType.Builder.<BulletEntity>create(BulletEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.1f, 0.1f).build(BULLET_KEY));

    private static final RegistryKey<EntityType<?>> WATER_DROP_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "water_drop"));

    public static final EntityType<WaterDropEntity> WATER_DROP = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Radik.MOD_ID, "water_drop"),
            EntityType.Builder.<WaterDropEntity>create(WaterDropEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.1f, 0.1f).build(WATER_DROP_KEY));

    private static final RegistryKey<EntityType<?>> ICE_SHARD_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Radik.MOD_ID, "ice_shard"));

    public static final EntityType<IceShardEntity> ICE_SHARD = Registry.register(Registries.ENTITY_TYPE,
        Identifier.of(Radik.MOD_ID, "ice_shard"),
        EntityType.Builder.<IceShardEntity>create(IceShardEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10).build(ICE_SHARD_KEY));

    @MainInit
    public static void initialize() {
    }
}
