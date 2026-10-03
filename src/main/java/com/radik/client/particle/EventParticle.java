package com.radik.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EventParticle extends BillboardParticle {
    private final SpriteProvider spriteProvider;

    protected EventParticle(ClientWorld world, double x, double y, double z,
                            double vx, double vy, double vz,
                            SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider.getFirst());
        this.spriteProvider = spriteProvider;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.scale = 0.05F + random.nextFloat() * 0.05F;
        this.maxAge = 40 + random.nextInt(20);
        this.gravityStrength = 0.0F;
        this.setSprite(spriteProvider.getSprite(world.random));
    }

    @Override
    public void tick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        if (this.age++ >= this.maxAge) this.markDead();
        else this.move(this.velocityX, this.velocityY, this.velocityZ);
    }

    @Override
    public BillboardParticle.RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double vx, double vy, double vz, Random random) {
            return new EventParticle(world, x, y, z, vx, vy, vz, spriteProvider);
        }
    }
}
