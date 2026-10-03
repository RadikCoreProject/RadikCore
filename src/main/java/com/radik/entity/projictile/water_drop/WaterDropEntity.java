package com.radik.entity.projictile.water_drop;

import com.radik.entity.RegisterEntities;
import com.radik.entity.projictile.ice_shard.IceShardEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.radik.item.custom.weapon.Tommy.BULLET_SPEED;
import static com.radik.item.custom.weapon.WaterPistol.DROP_SPEED;

public class WaterDropEntity extends PersistentProjectileEntity {
    public WaterDropEntity(EntityType<? extends WaterDropEntity> entityType, World world) {
        super(entityType, world);
    }

    public WaterDropEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.WATER_DROP, x, y, z, world, stack, shotFrom);
    }

    public WaterDropEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.WATER_DROP, owner, world, stack, shotFrom);
    }

    private ParticleEffect getParticleParameters() {
        ItemStack stack = this.getDefaultItemStack();
        return stack.isEmpty() ? ParticleTypes.BUBBLE : new ItemStackParticleEffect(ParticleTypes.ITEM, stack);
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
            if (entity instanceof BlazeEntity || entity instanceof MagmaCubeEntity) {
                DamageSource source = this.getDamageSources().freeze();
                entity.damage(world, source, 1f);
            }
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.SNOWBALL.getDefaultStack();
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

