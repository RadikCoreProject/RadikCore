package com.radik.mixin;

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
public abstract class RealmsMainScreenMixin extends Screen{

    @Shadow public abstract void close();

    @Unique
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this, 100, 50);

    protected RealmsMainScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(@NotNull CallbackInfo ci) {
        super.init();
        if (this.client == null) {ci.cancel(); return;}
        // header
        DirectionalLayoutWidget dir = this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(8));
        DirectionalLayoutWidget dir2 = dir.add(DirectionalLayoutWidget.horizontal()).spacing(8);
        ButtonWidget client_settings = ButtonWidget.builder(Text.translatable("text.radik.client_settings"), button -> this.client.setScreen(new ClientSettingsScreen(this))).width(200).build();
        ButtonWidget account_settings = ButtonWidget.builder(Text.translatable("text.radik.account_settings"), button -> this.client.setScreen(new AccountSettingsScreen(this))).width(200).build();
        //body
        GridWidget grid = new GridWidget();
        grid.getMainPositioner().marginX(4).marginBottom(5).alignHorizontalCenter();
        GridWidget.Adder adder = grid.createAdder(2);
        ButtonWidget name_customization = ButtonWidget.builder(Text.translatable("text.radik.name_customization"), button -> this.client.setScreen(new NameCustomizationScreen(this))).build();
        ButtonWidget decorations = ButtonWidget.builder(Text.translatable("text.radik.decorations"), button -> this.client.setScreen(new DecorationsScreen(this))).build();
        // off butts

        // TODO: butts
        Tooltip tooltip = Tooltip.of(Text.translatable("tooltip.radik.screen.future"));
        client_settings.setTooltip(tooltip);
        account_settings.setTooltip(tooltip);
        decorations.setTooltip(tooltip);
        client_settings.active = false;
        account_settings.active = false;
        decorations.active = false;

        if (PLAYER == null) {
            name_customization.active = false;
//            client_settings.active = false;
//            account_settings.active = false;
//            decorations.active = false;

            Tooltip tooltip1 = Tooltip.of(Text.translatable("tooltip.radik.screen.no_data"));
            name_customization.setTooltip(tooltip1);
//            client_settings.setTooltip(tooltip);
//            account_settings.setTooltip(tooltip);
//            decorations.setTooltip(tooltip);
        }

        // add header
        dir2.add(client_settings);
        dir2.add(account_settings);
        // add body
        adder.add(name_customization);
        adder.add(decorations);
        this.layout.addBody(grid);
        // add footer
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
        this.layout.forEachChild(this::addDrawableChild);
        this.layout.refreshPositions();
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
