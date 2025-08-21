package com.radik.fluid;

import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;

import static com.radik.Data.getDimension;
import static net.minecraft.fluid.FlowableFluid.FALLING;

public class BasedGasFluid extends FluidBlock {
    // TODO: та не релизнуто еще, не бей, я правда думаю как это релизнуть
    private final byte[] DENSITY = new byte[16];
    // K - коэффициент плотности вещества. при регистрации вещества умножаем реальный коэффициент на 1000.
    // разрешено небольшое округление. для водорода - K = 0.0000899 * 1000 = 0.0899 = 0.09
    protected final float K;

    public BasedGasFluid(FlowableFluid fluid, Settings settings, float K) {
        super(fluid, settings);
        this.K = K;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state)
                .with(FALLING, true);
    }
}
