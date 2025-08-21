package com.radik.mixin;

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
    private void addCustomButtons(CallbackInfo ci) {
        if (this.verticalLayout != null) {
            GridWidget gridWidget = new GridWidget();
            gridWidget.setColumnSpacing(6);
            gridWidget.setRowSpacing(8);

            this.verticalLayout.add(new TextWidget(100, 10, Text.literal("§cRadik§6Core"), this.textRenderer));
            ButtonWidget o = ButtonWidget.builder(
                    Text.translatable("text.radik.github"),
                    ConfirmLinkScreen.opening(this, "https://github.com/RadikCoreProject/RadikCore/tree/master")
            ).width(100).build();

            ButtonWidget oo = ButtonWidget.builder(
                    Text.translatable("text.radik.owner"),
                    ConfirmLinkScreen.opening(this, "https://t.me/Rad1k2")
            ).width(100).build();

            ButtonWidget ooo = ButtonWidget.builder(
                    Text.translatable("text.radik.bot"),
                    ConfirmLinkScreen.opening(this, "https://t.me/testing_abc746_bot")
            ).width(100).build();

            ButtonWidget oooo = ButtonWidget.builder(
                    Text.translatable("text.radik.channel"),
                    ConfirmLinkScreen.opening(this, "https://t.me/RadikDev")
            ).width(100).build();

            gridWidget.add(o, 1, 1);
            gridWidget.add(oo, 1, 2);
            gridWidget.add(ooo, 2, 1);
            gridWidget.add(oooo, 2, 2);
            this.verticalLayout.add(gridWidget);
            this.layout.refreshPositions();
        }
    }
}
