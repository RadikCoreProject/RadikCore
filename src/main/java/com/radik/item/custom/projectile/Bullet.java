package com.radik.item.custom.projectile;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import static com.radik.Data.BULLET_TYPE;

public class Bullet extends Item {
    public Bullet(Settings settings, BulletType type) {
        super(settings.component(BULLET_TYPE, type));
    }

    public enum BulletType {
        TOMMY;

        public static final Codec<BulletType> CODEC =
                Codec.STRING.xmap(
                        name -> BulletType.valueOf(name.toUpperCase()),
                        action -> action.name().toLowerCase()
                );

        public static final PacketCodec<ByteBuf, BulletType> PACKET_CODEC =
                PacketCodecs.VAR_INT.xmap(
                        i -> BulletType.values()[i],
                        BulletType::ordinal
                );
    }
}
