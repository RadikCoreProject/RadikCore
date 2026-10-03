package com.radik.entity.projictile.ice_shard;

import com.radik.entity.RegisterEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IceShardEntity extends PersistentProjectileEntity {
    public IceShardEntity(EntityType<? extends IceShardEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceShardEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.ICE_SHARD, x, y, z, world, stack, shotFrom);
    }

    public IceShardEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.ICE_SHARD, owner, world, stack, shotFrom);
    }

    private ParticleEffect getParticleParameters() {
        ItemStack stack = this.getDefaultItemStack();
        return stack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, stack);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect effect = this.getParticleParameters();
            for (int i = 0; i < 8; i++) {
                this.getEntityWorld().addParticleClient(effect,
                    this.getX(), this.getY(), this.getZ(), 
                    0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity.getEntityWorld() instanceof ServerWorld world) {
            int damage = 1;
            if (entity instanceof BlazeEntity || entity instanceof MagmaCubeEntity) {
                damage = 3;
            }
            DamageSource source = this.getDamageSources().freeze();
            entity.damage(world, source, damage);

            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0));
            }
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.BLUE_ICE.getDefaultStack();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getEntityWorld().isClient()) {
            this.getEntityWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }
}