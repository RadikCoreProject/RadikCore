package com.radik.client.screen.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CustomOptionListWidget extends ElementListWidget<CustomOptionListWidget.WidgetEntry> {
    private static final int WIDGET_WIDTH = 310;
    private static final int ITEM_HEIGHT = 24; // Высота одной строки
    private final Screen parentScreen;

    public CustomOptionListWidget(MinecraftClient client, int width, int height, int top, Screen parentScreen) {
        super(client, width, height, top, ITEM_HEIGHT);
        this.parentScreen = parentScreen;
        this.centerListVertically = false;
    }

    @Override
    public int getRowWidth() {
        return WIDGET_WIDTH;
    }

    public void addWidget(ClickableWidget widget) {
        this.addEntry(WidgetEntry.create(widget, parentScreen));
    }

    public void addWidgets(List<ClickableWidget> widgets) {
        widgets.forEach(this::addWidget);
    }

    public void addAll(List<ClickableWidget> widgets) {
        for(int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(
                widgets.get(i),
                i < widgets.size() - 1 ? widgets.get(i + 1) : null
            );
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(firstWidget, secondWidget, parentScreen));
    }

    @Environment(EnvType.CLIENT)
    protected static class WidgetEntry extends Entry<WidgetEntry> {
        final List<ClickableWidget> widgets;
        private final Screen parentScreen;

        WidgetEntry(List<ClickableWidget> widgets, Screen parentScreen) {
            this.widgets = ImmutableList.copyOf(widgets);
            this.parentScreen = parentScreen;
        }

        public static WidgetEntry create(ClickableWidget widget, Screen parentScreen) {
            return new WidgetEntry(ImmutableList.of(widget), parentScreen);
        }

        public static WidgetEntry create(ClickableWidget firstWidget, ClickableWidget secondWidget, Screen parentScreen) {
            List<ClickableWidget> list = new ArrayList<>();
            list.add(firstWidget);
            if (secondWidget != null) {
                list.add(secondWidget);
            }
            return new WidgetEntry(ImmutableList.copyOf(list), parentScreen);
        }

        @Override
        public List<? extends Element> children() {
            return widgets;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return widgets;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int widgetCount = widgets.size();
            int totalWidth = widgetCount * 160 - 10;
            int startX = (parentScreen.width - totalWidth) / 2;

            for (int i = 0; i < widgetCount; i++) {
                ClickableWidget widget = widgets.get(i);
                widget.setPosition(startX + i * 160, getY());
                widget.render(context, mouseX, mouseY, deltaTicks);
            }
        }
    }
}
