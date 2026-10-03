package com.radik.ui.ducking;

import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public interface ModelPartDuck {
    void wearThat$setTransform(Consumer<MatrixStack> consumer);
}