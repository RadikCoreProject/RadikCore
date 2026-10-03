package com.radik.client.screen;

import com.radik.client.screen.widget.CustomOptionListWidget;
import com.radik.connecting.client.Decoration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
    private static final Text[] colors = new Text[]{
        Text.literal("§4").append(Text.translatable("color.radik.dark_red")),
        Text.literal("§c").append(Text.translatable("color.minecraft.red")),
        Text.literal("§6").append(Text.translatable("color.minecraft.orange")),
        Text.literal("§e").append(Text.translatable("color.minecraft.yellow")),
        Text.literal("§2").append(Text.translatable("color.minecraft.green")),
        Text.literal("§a").append(Text.translatable("color.minecraft.lime")),
        Text.literal("§b").append(Text.translatable("color.minecraft.light_blue")),
        Text.literal("§3").append(Text.translatable("color.minecraft.cyan")),
        Text.literal("§1").append(Text.translatable("color.minecraft.blue")),
        Text.literal("§9").append(Text.translatable("color.radik.sea")),
        Text.literal("§d").append(Text.translatable("color.minecraft.pink")),
        Text.literal("§5").append(Text.translatable("color.minecraft.purple")),
        Text.literal("§7").append(Text.translatable("color.minecraft.light_gray")),
        Text.literal("§8").append(Text.translatable("color.minecraft.gray")),
        Text.literal("§0").append(Text.translatable("color.minecraft.black")),
        Text.literal("§f").append(Text.translatable("color.minecraft.white"))
    };

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

        TextWidget text = new TextWidget(
            Text.literal("Choose screen").withColor(0xBB0000).formatted(Formatting.BOLD),
            textRenderer
        );
        text.setPosition(width / 2 - 50, height / 8);
        this.addDrawableChild(text);
    }

    private void initLayout() {
        layout.addHeader(title, textRenderer);

        int bodyHeight = this.height - layout.getHeaderHeight() - layout.getFooterHeight();
        this.body = new CustomOptionListWidget(
            this.client,
            this.width,
            bodyHeight,
            0,
            this
        );

        addOptions();

        layout.addBody(this.body);
        layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> close()).width(200).build());
        layout.refreshPositions();
    }

    private void addOptions() {
        if (this.body == null) return;
        List<ClickableWidget> widgets = new ArrayList<>();

        DECORATIONS.forEach((deco, own) -> {
            if (deco.type == this.type) {
                int finalI = deco.id;
                ButtonWidget colorButton = ButtonWidget.builder(getColorText(finalI), button -> onColorSelected(finalI)).size(150, 20).build();
                if (!own) {
                    colorButton.active = false;
                    colorButton.setTooltip(Tooltip.of(Text.translatable("text.radik.settings.color_price").append(deco.cost + "₽")));
                }
                widgets.add(colorButton);
            }
        });

        ButtonWidget colorButton = ButtonWidget.builder(getColorText(17), button -> onColorSelected(17)).size(150, 20).build();
        widgets.add(colorButton);
        this.body.addAll(widgets);
    }

    private Text getColorText(int index) {
        if (type == 1) {
            return Text.of(colors[index - 2]);
        } else {
            return Text.translatable("text.radik.settings.option").append(String.valueOf(index + 1));
        }
    }

    private void onColorSelected(int colorIndex) {
        if (type == 1) {
            String[] a = PLAYER.colorCode.split("");
            a[data] = String.valueOf(colors[colorIndex - 2].getString().charAt(1));
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