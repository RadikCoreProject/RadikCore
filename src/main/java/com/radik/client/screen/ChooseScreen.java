package com.radik.client.screen;

import com.radik.connecting.Decoration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.radik.client.RadikClient.DECORATIONS;
import static com.radik.client.RadikClient.PLAYER;

@Environment(EnvType.CLIENT)
public class ChooseScreen extends Screen {
    private CustomOptionListWidget body;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final byte type;
    private final int data;
    private final Screen parent;
    private static final String[] colors = new String[]{"§4Бордовый", "§cКрасный", "§6Оранжевый", "§eЖелтый", "§2Зелёный", "§aЛаймовый", "§bГолубой", "§3Бирюзовый", "§1Синий", "§9Морской", "§dРозовый", "§5Фиолетовый", "§7Светло-серый", "§8Серый", "§0Чёрный", "Белый"};

    public ChooseScreen(Screen parent, Text title, byte type, int data) {
        super(title);
        this.type = type;
        this.data = data;
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        initLayout();
        layout.forEachChild(this::addDrawableChild);
    }

    private void initLayout() {
        layout.addHeader(title, textRenderer);

        int bodyHeight = height - layout.getHeaderHeight() - layout.getFooterHeight();
        this.body = new CustomOptionListWidget(client, width, bodyHeight, layout.getHeaderHeight(), layout.getFooterHeight(), this);
        addOptions();
        layout.addBody(body);

        layout.addFooter(
                ButtonWidget.builder(ScreenTexts.DONE, button -> close())
                        .width(200)
                        .build()
        );

        // Обновление позиций
        layout.refreshPositions();
    }

    private void addOptions() {
        if (this.body == null) return;
        List<ClickableWidget> widgets = new ArrayList<>();

        for (Decoration i : DECORATIONS) {
            if (i.type != this.type) continue;
            int finalI = i.id;
            ButtonWidget colorButton = ButtonWidget.builder(getColorText(finalI), button -> onColorSelected(finalI)).size(150, 20).build();
            if (!i.own) {
                colorButton.active = false;
                colorButton.setTooltip(Tooltip.of(Text.of("У вас не куплен этот цвет\nЦена: " + i.cost + "₽")));
            }
            widgets.add(colorButton);
        }

        ButtonWidget colorButton = ButtonWidget.builder(getColorText(17), button -> onColorSelected(17)).size(150, 20).build();
        widgets.add(colorButton);
        this.body.addAll(widgets);
    }

    private Text getColorText(int index) {
        if (type == 1) {
            return Text.of(colors[index - 2]);
        } else {
            return Text.of("Вариант " + (index + 1));
        }
    }

    private void onColorSelected(int colorIndex) {
        if (type == 1) {
            String[] a = PLAYER.colorCode.split("");
            a[data] = String.valueOf(colors[colorIndex - 2].charAt(1));
            PLAYER.colorCode = String.join("", a);
        }
        close();
    }

    @Override
    public void close() {
        super.close();
        if (this.client != null) {
            if (type == 1) {
                ((NameCustomizationScreen) parent).colorCode = PLAYER.colorCode.split("");
                this.client.setScreen(parent);
            }
        }
    }
}