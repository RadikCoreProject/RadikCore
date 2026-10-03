package com.radik.client.particle;

import com.radik.Radik;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterParticles {
    public static final SimpleParticleType FLOWERY_PARTICLE = registerParticle("flowery_particle", FabricParticleTypes.simple(true));
    public static final SimpleParticleType LUCKY_PARTICLE = registerParticle("lucky_particle", FabricParticleTypes.simple(true));

    public static void register() {}

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particle) {
        SimpleParticleType p = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Radik.MOD_ID, name), particle);
        ParticleFactoryRegistry.getInstance().register(p, EventParticle.Factory::new);
        return p;
    }
}
