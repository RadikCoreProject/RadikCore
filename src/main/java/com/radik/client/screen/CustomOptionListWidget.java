package com.radik.client.screen;

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
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CustomOptionListWidget extends ElementListWidget<CustomOptionListWidget.WidgetEntry> {
    private static final int WIDGET_WIDTH = 310;
    private static final int ROW_HEIGHT = 0;
    private final ChooseScreen chooseScreen;

    public CustomOptionListWidget(
            MinecraftClient client,
            int width,
            int height,
            int top,
            int bottom,
            ChooseScreen chooseScreen
    ) {
        super(client, width, height, top, bottom, ROW_HEIGHT);
        this.chooseScreen = chooseScreen;
    }

    public void addWidget(ClickableWidget widget) {
        this.addEntry(WidgetEntry.create(Collections.singletonList(widget), chooseScreen));
    }

    public void addWidgets(List<ClickableWidget> widgets) {
        for (ClickableWidget widget : widgets) {
            this.addWidget(widget);
        }
    }

    @Override
    public int getRowWidth() {
        return WIDGET_WIDTH;
    }

    public void addAll(List<ClickableWidget> widgets) {
        for(int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, ClickableWidget secondWidget) {
        this.addEntry(WidgetEntry.create(List.of(firstWidget, secondWidget), chooseScreen));
    }

    @Environment(EnvType.CLIENT)
    protected static class WidgetEntry extends Entry<WidgetEntry> {
        final List<ClickableWidget> widgets;
        private final Screen screen;

        WidgetEntry(List<ClickableWidget> widgets, Screen screen) {
            this.widgets = ImmutableList.copyOf(widgets);
            this.screen = screen;
        }

        public static WidgetEntry create(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget, Screen screen) {
            return secondWidget == null ? new WidgetEntry(ImmutableList.of(firstWidget), screen) : new WidgetEntry(ImmutableList.of(firstWidget, secondWidget), screen);
        }

        public static WidgetEntry create(List<ClickableWidget> widgets, Screen screen) {
            return new WidgetEntry(widgets, screen);
        }

        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
            int i = 0;
            int j = this.screen.width / 2 - 155;

            for(ClickableWidget clickableWidget : this.widgets) {
                clickableWidget.setPosition(j + i, y);
                clickableWidget.render(context, mouseX, mouseY, tickProgress);
                i += 160;
            }

        }

        @Override
        public List<? extends Element> children() {
            return widgets;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return widgets;
        }
    }
}
