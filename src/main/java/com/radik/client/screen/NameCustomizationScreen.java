package com.radik.client.screen;

import com.radik.client.screen.ChooseScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.radik.client.RadikClient.DECORATIONS;
import static com.radik.client.RadikClient.PLAYER;

@Environment(EnvType.CLIENT)
public class NameCustomizationScreen extends Screen {
    private final Screen parent;
    private final String playerName;
    private String[] colorCodeOld;
    public String[] colorCode;
    private boolean bold;
    private boolean boldOld;

    public NameCustomizationScreen(Screen parent) {
        super(Text.of("Name customization"));
        this.parent = parent;
        this.playerName = PLAYER.name;
        this.colorCodeOld = PLAYER.colorCode.split("");
        this.colorCode = this.colorCodeOld;
        this.boldOld = PLAYER.bold;
        this.bold = this.boldOld;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 150;
        int buttonHeight = 20;
        int verticalSpacing = 25;
        int currentY = this.height / 4;
        if (this.client == null) {return;}

        // по умолчанию
        ButtonWidget resetButton = ButtonWidget.builder(Text.of("Сбросить по умолчанию"), button -> {
            bold = false;
            colorCode = "f".repeat(playerName.length()).split("");
            this.close();
            this.client.setScreen(this);
        }).size(buttonWidth, buttonHeight).build();

        // bold butt
        ButtonWidget boldToggleButton = ButtonWidget.builder(
                Text.of("Жирный текст: " + (bold ? "ВКЛ" : "ВЫКЛ")),
                button -> {
                    bold = !bold;
                    button.setMessage(Text.of("Жирный текст: " + (bold ? "ВКЛ" : "ВЫКЛ")));
                    this.close();
                    this.client.setScreen(this);
                }
        ).size(buttonWidth, buttonHeight).build();

        // pos
        resetButton.setPosition(centerX - buttonWidth - 5, currentY);
        boldToggleButton.setPosition(centerX + 5, currentY);

        if (!DECORATIONS.get(1).own) {
            boldToggleButton.active = false;
            boldToggleButton.setTooltip(Tooltip.of(Text.of("У тебя не куплен жирный никнейм")));
        }
        if (Arrays.equals(colorCode, "f".repeat(playerName.length()).split("")) && !bold) {
            resetButton.active = false;
            resetButton.setTooltip(Tooltip.of(Text.of("Твой код цвета уже установлен по умолчанию")));
        }

        this.addDrawableChild(resetButton);
        this.addDrawableChild(boldToggleButton);

        currentY += verticalSpacing * 2;

        TextWidget nicknameText = new TextWidget(Text.of("Твой никнейм: " + getColoredName()), this.textRenderer);
        nicknameText.setPosition(centerX - nicknameText.getWidth() / 2, currentY);
        this.addDrawableChild(nicknameText);

        currentY += 15;

        TextWidget colorCodeText = new TextWidget(Text.of("Твой код цвета: " + (bold ? "l" : "") + String.join("", colorCode)), this.textRenderer);
        colorCodeText.setPosition(centerX - colorCodeText.getWidth() / 2, currentY);
        this.addDrawableChild(colorCodeText);

        currentY += verticalSpacing;

        int buttSize = 20;
        int letterSpacing = 2;
        int width = this.playerName.length() * (buttSize + letterSpacing) - letterSpacing;
        int startX = centerX - width / 2;

        for (int i = 0; i < this.playerName.length(); i++) {
            char letter = this.playerName.charAt(i);
            int finalI = i;
            ButtonWidget letterButton = ButtonWidget.builder(Text.of( "§" + this.colorCode[i] + (bold ? "§l" : "") + letter), button -> {this.client.setScreen(new ChooseScreen(this, Text.of("Color picker"), (byte) 1, finalI));}).size(buttSize, buttSize).build();

            letterButton.setPosition(startX + i * (buttSize + letterSpacing), currentY);
            this.addDrawableChild(letterButton);
        }

        currentY += verticalSpacing * 2;
        int bottomButtonWidth = 100;
        int bottomSpacing = 10;

        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Сохранить"), button -> {
            this.colorCodeOld = this.colorCode;
            this.boldOld = this.bold;
            AtomicReference<String> colors = new AtomicReference<>("");
            Arrays.stream(this.colorCode).forEach(t -> colors.updateAndGet(v -> v + t));
            PLAYER.colorCode = colors.get();
            PLAYER.bold = bold;
            this.close();
            this.client.setScreen(this);
        }).size(bottomButtonWidth, buttonHeight).build();

        ButtonWidget resetChangesButton = ButtonWidget.builder(Text.of("Сбросить"), button -> {
            this.colorCode = this.colorCodeOld;
            this.bold = this.boldOld;
            this.close();
            this.client.setScreen(this);
        }).size(bottomButtonWidth, buttonHeight).build();

        ButtonWidget doneButton = ButtonWidget.builder(Text.of("Готово"), button -> {
            AtomicReference<String> colors = new AtomicReference<>("");
            Arrays.stream(this.colorCode).forEach(t -> colors.updateAndGet(v -> v + t));
            PLAYER.colorCode = colors.get();
            PLAYER.bold = bold;
            this.close();
        }).size(bottomButtonWidth, buttonHeight).build();

        int totalBottomWidth = 3 * bottomButtonWidth + 2 * bottomSpacing;
        int bottomStartX = centerX - totalBottomWidth / 2;

        saveButton.setPosition(bottomStartX, currentY);
        resetChangesButton.setPosition(bottomStartX + bottomButtonWidth + bottomSpacing, currentY);
        doneButton.setPosition(bottomStartX + 2 * (bottomButtonWidth + bottomSpacing), currentY);

        if (this.colorCodeOld == this.colorCode && bold == boldOld) {
            saveButton.active = false;
            resetChangesButton.active = false;
            saveButton.setTooltip(Tooltip.of(Text.of("Изменений нет")));
            resetChangesButton.setTooltip(Tooltip.of(Text.of("Изменений нет")));
        }

        this.addDrawableChild(saveButton);
        this.addDrawableChild(resetChangesButton);
        this.addDrawableChild(doneButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    private String getColoredName() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < playerName.length(); i++) {
            ans.append("§").append(colorCode[i]).append(bold ? "§l" : "").append(playerName.charAt(i));
        }
        return ans.toString();
    }
}
