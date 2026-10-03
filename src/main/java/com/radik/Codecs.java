package com.radik;

import com.mojang.serialization.Codec;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.custom.chemistry.VialContainer;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public final class Codecs {
    public static final Codec<Vec3d> VEC_3D = Codec.DOUBLE
            .listOf()
            .comapFlatMap(
                    list -> Util.decodeFixedLengthList(list, 3)
                            .map(listx -> new Vec3d(listx.getFirst(), listx.get(1), listx.get(2))),
                    vec3d -> List.of(vec3d.x, vec3d.y, vec3d.z)
            );

    public static final Codec<VialContainer> VIAL_CONTAINER =
            Codec.INT.xmap(
                    i -> VialContainer.values()[i],
                    Enum::ordinal
            );

    public static final Codec<ChallengeEvent> EVENT_TYPE =
            Codec.INT.xmap(
                    i -> ChallengeEvent.values()[i],
                    Enum::ordinal
            );
}
