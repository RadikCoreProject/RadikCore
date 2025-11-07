package com.radik.client.screen;

import com.radik.property.client.SettingsProperties;
import com.radik.property.client.Property;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientSettingsScreen extends Screen {
    Screen parent;
    ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public ClientSettingsScreen(Screen parent) {
        super(Text.of("Client settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        if (this.layout != null && client != null) {
            GridWidget gridWidget = new GridWidget();
            gridWidget.setColumnSpacing(6);
            gridWidget.setRowSpacing(8);
            for (Property property : Property.values()) {
                String id = property.getId();
                gridWidget.add(ButtonWidget.builder(Text.translatable("text.radik.settings." + id).append(Text.translatable("text.radik.settings." + SettingsProperties.get(id))), executor -> {
                    String value = SettingsProperties.PROPERTY.get(property);
                    List<String> values = property.getProperyType().getValues();
                    SettingsProperties.set(id, values.get((values.indexOf(value) + 1) % values.size()));
                    client.setScreen(new ClientSettingsScreen(parent));
                }).build(), (property.ordinal() + 1) / 2, property.ordinal() % 2);
            }

            DirectionalLayoutWidget horizontalLayout = new DirectionalLayoutWidget(1, 3, DirectionalLayoutWidget.DisplayAxis.HORIZONTAL);
            horizontalLayout.spacing(40);
            horizontalLayout.add(ButtonWidget.builder(Text.translatable("text.radik.reset"), executor -> {
                for (Property property : Property.values()) SettingsProperties.set(property.getId(), property.getDef());
                client.setScreen(new ClientSettingsScreen(parent));
            }).build());
            horizontalLayout.add(ButtonWidget.builder(Text.translatable("text.radik.done"), executor -> {
                client.setScreen(parent);
            }).build());

            this.layout.addHeader(new TextWidget(100, 10, Text.literal(""), this.textRenderer));
            this.layout.addBody(gridWidget);
            this.layout.addFooter(horizontalLayout);
            this.layout.forEachChild(this::addDrawableChild);
            this.layout.refreshPositions();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
