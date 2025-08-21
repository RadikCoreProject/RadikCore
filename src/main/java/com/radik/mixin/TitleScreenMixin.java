package com.radik.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @ModifyArg(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/PressableTextWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Lnet/minecraft/client/font/TextRenderer;)V"
            ),
            index = 4
    )
    private Text replaceCopyrightText(Text original) {
        return Text.of("§cRadik§6Core §0Titres");
    }

    @ModifyArg(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/PressableTextWidget;<init>(IIIILnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;Lnet/minecraft/client/font/TextRenderer;)V"
            ),
            index = 0
    )
    private int setWidth(int x) {
        return x + 140;
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I",
                    ordinal = 0
            ),
            index = 1
    )
    private String replaceVersionString(String original) {
        return "Minecraft 1.21.5";
    }

    @ModifyArg(
            method = "addNormalWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;tooltip(Lnet/minecraft/client/gui/tooltip/Tooltip;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;",
                    ordinal = 1
            )
    )
    private @NotNull Tooltip replaceRealmsTooltip(@Nullable Tooltip tooltip) {
        return Tooltip.of(Text.of("TEST CLIENT BUTTON"));
    }

    @ModifyArg(
            method = "addNormalWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;",
                    ordinal = 2
            ),
            index = 0
    )
    private Text replaceRealmsButtonText(Text original) {
        return Text.of("§0§l§k...§r§c§l Radik§6§lCore §0§l§k...");
    }
}