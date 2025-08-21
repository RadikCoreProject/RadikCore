package com.radik.fluid.elements;

import com.radik.fluid.ExplosiveGasFluid;
import net.minecraft.fluid.FlowableFluid;

public class HydrogenBlock extends ExplosiveGasFluid {
    public HydrogenBlock(FlowableFluid fluid, Settings settings, float K) {
        super(fluid, settings, K);
    }
}
