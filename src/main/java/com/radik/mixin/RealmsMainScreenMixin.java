package com.radik.mixin;

import com.radik.Radik;
import com.radik.client.screen.AccountSettingsScreen;
import com.radik.client.screen.ClientSettingsScreen;
import com.radik.client.screen.DecorationsScreen;
import com.radik.client.screen.NameCustomizationScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.radik.client.RadikClient.PLAYER;

@Environment(EnvType.CLIENT)
@Mixin({RealmsMainScreen.class})
public abstract class RealmsMainScreenMixin extends Screen {

    @Shadow public abstract void close();

    protected RealmsMainScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(@NotNull CallbackInfo ci) {
        if (this.client == null) {ci.cancel(); return;}

        this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("text.radik.client_settings"), button -> this.client.setScreen(new ClientSettingsScreen(this)))
                .dimensions(this.width / 2 - 210, this.height / 2 - 30, 200, 20)
            .build());
        ButtonWidget account_settings = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("text.radik.account_settings"), button -> this.client.setScreen(new AccountSettingsScreen(this)))
            .dimensions(this.width / 2 + 10, this.height / 2 - 30, 200, 20)
            .build());
        ButtonWidget name_customization = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("text.radik.name_customization"), button -> this.client.setScreen(new NameCustomizationScreen(this)))
            .dimensions(this.width / 2 - 210, this.height / 2 + 10, 200, 20)
            .build());
        ButtonWidget decorations = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("text.radik.decorations"), button -> this.client.setScreen(new DecorationsScreen(this)))
            .dimensions(this.width / 2 + 10, this.height / 2 + 10, 200, 20)
            .build());


        // TODO: butts
        Tooltip tooltip = Tooltip.of(Text.translatable("tooltip.radik.screen.future"));
        account_settings.setTooltip(tooltip);
        decorations.setTooltip(tooltip);
        account_settings.active = false;
        decorations.active = false;

        if (PLAYER == null) {
            name_customization.active = false;
//            account_settings.active = false;
//            decorations.active = false;

            Tooltip tooltip1 = Tooltip.of(Text.translatable("tooltip.radik.screen.no_data"));
            name_customization.setTooltip(tooltip1);
//            account_settings.setTooltip(tooltip);
//            decorations.setTooltip(tooltip);
        }

        ci.cancel();
    }

    /**
     * @author Radik
     * @reason for dis NPE
     */
    @Overwrite
    public Text getNarratedTitle() {
        return Text.of("RadikCore config");
    }

    /**
     * @author Radik
     * @reason render screen with cfg
     */
    @Overwrite
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "RadikCore config", width / 2, 20, 0xFF0000);
        super.render(context, mouseX, mouseY, delta);
    }
}
