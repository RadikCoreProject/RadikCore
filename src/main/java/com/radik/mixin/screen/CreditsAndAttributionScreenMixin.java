package com.radik.mixin.screen;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreditsAndAttributionScreen.class)
public abstract class CreditsAndAttributionScreenMixin extends Screen {
    @Shadow @Final
    private ThreePartsLayoutWidget layout;
    @Unique
    private DirectionalLayoutWidget verticalLayout;

    protected CreditsAndAttributionScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;getMainPositioner()Lnet/minecraft/client/gui/widget/Positioner;",
                    ordinal = 0
            )
    )
    private void onInit(CallbackInfo ci, @Local DirectionalLayoutWidget directionalLayoutWidget) {
        this.verticalLayout = directionalLayoutWidget;
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addFooter(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;"
            )
    )
    private void addButtons(CallbackInfo ci) {
        if (this.verticalLayout != null) {
            GridWidget widget = new GridWidget();
            widget.setColumnSpacing(6);
            widget.setRowSpacing(8);

            this.verticalLayout.add(new TextWidget(50, 10, Text.literal("§cRadik§6Core"), this.textRenderer));
            ButtonWidget github = ButtonWidget.builder(
                    Text.translatable("text.radik.github"),
                    ConfirmLinkScreen.opening(this, "https://github.com/RadikCoreProject/RadikCore/tree/master")
            ).width(100).build();

            ButtonWidget owner = ButtonWidget.builder(
                    Text.translatable("text.radik.owner"),
                    ConfirmLinkScreen.opening(this, "https://t.me/Rad1k2")
            ).width(100).build();

            ButtonWidget bot = ButtonWidget.builder(
                    Text.translatable("text.radik.bot"),
                    ConfirmLinkScreen.opening(this, "https://t.me/testing_abc746_bot")
            ).width(100).build();

            ButtonWidget channel = ButtonWidget.builder(
                    Text.translatable("text.radik.channel"),
                    ConfirmLinkScreen.opening(this, "https://t.me/RadikDev")
            ).width(100).build();

            widget.add(github, 1, 1);
            widget.add(owner, 1, 2);
            widget.add(bot, 2, 1);
            widget.add(channel, 2, 2);
            this.verticalLayout.add(widget);
            this.layout.refreshPositions();
        }
    }
}
