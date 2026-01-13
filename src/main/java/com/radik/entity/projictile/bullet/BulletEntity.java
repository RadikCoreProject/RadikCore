package com.radik.entity.projictile.bullet;

import com.radik.entity.RegisterEntities;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.radik.item.custom.reward.Tommy.BULLET_SPEED;

public class BulletEntity extends PersistentProjectileEntity {
    private static final TrackedData<Byte> PIERCE_LEVEL = DataTracker.registerData(BulletEntity.class, TrackedDataHandlerRegistry.BYTE);
        private int lifetime = 0;

    public BulletEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.BULLET, x, y, z, world, stack, shotFrom);
    }

    public BulletEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(RegisterEntities.BULLET, owner, world, stack, shotFrom);
    }

    public BulletEntity(EntityType<BulletEntity> bullet, World world) {
        super(bullet, world);
    }

    @Override
    public void tick() {
        Vec3d velocity = this.getVelocity();
        float speed = (float) velocity.length();

        super.tick();

        if (!this.isInGround() && speed > 0 && !velocity.equals(Vec3d.ZERO)) {
            Vec3d direction = velocity.normalize();
            this.setVelocity(direction.multiply(BULLET_SPEED));
        }

        if (this.getEntityWorld().isClient() && lifetime % 2 == 0 && !this.isInGround()) {
            this.getEntityWorld().addParticleClient(ParticleTypes.FLAME,
                this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }

        lifetime++;
        int maxLifetime = 200;
        if (lifetime > maxLifetime) this.discard();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PIERCE_LEVEL, (byte) 0);
    }

    public void setPierceLevel(byte level) {
        this.dataTracker.set(PIERCE_LEVEL, level);
    }

    public byte getPierceLevel() {
        return this.dataTracker.get(PIERCE_LEVEL);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putByte("PierceLevel", this.getPierceLevel());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.setPierceLevel(view.getByte("PierceLevel", (byte) 0));
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, BULLET_SPEED, 0);
    }
}

