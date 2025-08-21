package com.radik.fluid.elements;

import com.radik.fluid.BasedGas;
import com.radik.fluid.RegisterFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;

public abstract class HydrogenFluid extends BasedGas {
    public static class Still extends HydrogenFluid {
        @Override
        public boolean isStill(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends HydrogenFluid {
        @Override
        public boolean isStill(FluidState state) {
            return false;
        }
    }
}
