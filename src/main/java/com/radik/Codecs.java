package com.radik;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Codecs {
    public static final Codec<Vec3d> VEC_3D = Codec.DOUBLE
            .listOf()
            .comapFlatMap(
                    list -> Util.decodeFixedLengthList(list, 3)
                            .map(listx -> new Vec3d(listx.getFirst(), listx.get(1), listx.get(2))),
                    vec3d -> List.of(vec3d.x, vec3d.y, vec3d.z)
            );
}
