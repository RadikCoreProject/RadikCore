package com.radik.mixin;

import net.minecraft.data.DataGenerator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.nio.file.Path;

@Mixin(DataGenerator.class)
public abstract class DataGeneratorMixin {

    @Accessor("outputPath")
    abstract Path getOutputPath();

    @Accessor("LOGGER")
    abstract Logger getLogger();

    @Inject(
            method = "run",
            at = @At("HEAD")
    )
    private void onRunStart(CallbackInfo ci) {
        Path outputPath = this.getOutputPath();

        Logger LOGGER = this.getLogger();
        LOGGER.info("Starting DataGenerator with output path: {}", outputPath);
    }
}