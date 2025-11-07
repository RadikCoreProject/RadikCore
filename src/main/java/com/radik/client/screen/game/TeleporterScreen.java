package com.radik.client.screen.game;

import com.radik.item.custom.Teleporter;
import com.radik.packets.payload.VecPayload;
import com.radik.util.Duplet;
import com.radik.util.Triplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.radik.Data.*;
import static com.radik.client.RadikClient.CLIENT;

@Environment(EnvType.CLIENT)
public class TeleporterScreen extends Screen {
    public static LocalDateTime cooldown = LocalDateTime.now();

    private TextFieldWidget xField, yField, zField;
    protected ItemStack parent;
    private final Vec3d lastPosition;
    private int cooldownAfter = 0;
    private int cooldownAfterBack = 0;
    private int pos = -1;
    private int posBack = -1;
    private Text text = Text.of("-");
    private Vec3d position;

    public TeleporterScreen(ItemStack teleporter) {
        super(Text.literal("Окно Телепортера"));
        this.parent = teleporter;
        this.lastPosition = parent.get(POSITION);
    }

    public TeleporterScreen(ItemStack teleporter, int pos, Text text, Vec3d position) {
        super(Text.literal("Окно Телепортера"));
        this.parent = teleporter;
        this.lastPosition = parent.get(POSITION);
        this.pos = pos;
        this.text = text;
        this.position = position;
    }

    @Override
    protected void init() {
        super.init();
        calculateBack();
        int centerX = width / 2;
        int centerY = height / 2;
        int buttonWidth = 150;
        int buttonHeight = 20;

        TextWidget widget3 = new TextWidget(Text.literal("Расстояние до конечной точки: ").append(this.text), textRenderer);
        TextWidget widget1 = new TextWidget(Text.literal("Ваше местоположение: ").append(getPlayerPositionText()), textRenderer);
        TextWidget widget2 = new TextWidget(Text.literal("Прошлое местоположение: ").append(formatPosition(lastPosition)), textRenderer);
        widget1.setPosition(centerX - 150, centerY - 90);
        widget2.setPosition(centerX - 150, centerY - 70);
        widget3.setPosition(centerX - 150, centerY - 50);
        addDrawableChild(widget1);
        addDrawableChild(widget2);
        addDrawableChild(widget3);

        xField = new TextFieldWidget(textRenderer, centerX - 150, centerY - 20, 60, 20, Text.literal("X"));
        yField = new TextFieldWidget(textRenderer, centerX - 40, centerY - 20, 60, 20, Text.literal("Y"));
        zField = new TextFieldWidget(textRenderer, centerX + 70, centerY - 20, 60, 20, Text.literal("Z"));

        xField.setMaxLength(10);
        yField.setMaxLength(10);
        zField.setMaxLength(10);

        if (position != null) {
            xField.setText(String.valueOf(position.x));
            yField.setText(String.valueOf(position.y));
            zField.setText(String.valueOf(position.z));
        }

        addSelectableChild(xField);
        addSelectableChild(yField);
        addSelectableChild(zField);

        ButtonWidget widget4 = ButtonWidget.builder(Text.literal("Телепортироваться"), button -> teleport())
                .position(centerX - 185, centerY + 70)
                .size(buttonWidth, buttonHeight)
                .build();
        ButtonWidget widget5 = ButtonWidget.builder(Text.literal("Вычислить"), button -> {
                    Triplet<Text, Integer, Vec3d> d = calculate();
                    if (d.getParametrize() == null) return;
                    TeleporterScreen screen = new TeleporterScreen(this.parent, d.getParametrize(), d.getType(), d.getCount());
                    screen.cooldownAfter = this.cooldownAfter;
                    CLIENT.setScreen(screen);
                })
                .position(centerX - 70, centerY + 30)
                .size(buttonWidth - 40, buttonHeight)
                .build();
        ButtonWidget widget6 = ButtonWidget.builder(Text.literal("ТП обратно"), button -> teleportToLastPosition())
                .position(centerX + 25, centerY + 70)
                .size(buttonWidth, buttonHeight)
                .build();

        if (CLIENT.player == null) return;

        label1: {
            if (!Objects.equals(CLIENT.player.getStackInHand(Hand.MAIN_HAND).get(OWNER), CLIENT.player.getName().getString())) {
                widget5.active = false;
                widget6.active = false;
                widget4.active = false;
                Tooltip t = Tooltip.of(Text.of("Это не твой телепортер."));
                widget5.setTooltip(t);
                widget4.setTooltip(t);
                widget6.setTooltip(t);
                break label1;
            }

            if (cooldown.isAfter(LocalDateTime.now())) {
                widget4.active = false;
                widget5.active = false;
                widget6.active = false;
                Tooltip t = Tooltip.of(Text.of("§4Cooldown: " + Duration.between(cooldown, LocalDateTime.now()).abs().getSeconds() + "s"));
                widget4.setTooltip(t);
                widget5.setTooltip(t);
                widget6.setTooltip(t);
                break label1;
            }

            widget6.active = false;
            switch (posBack) {
                case 0 -> {
                    widget6.active = true;
                    widget6.setTooltip(Tooltip.of(Text.of("Кулдаун после телепортации: " + cooldownAfterBack + "s")));
                }
                case -1 -> widget6.setTooltip(Tooltip.of(Text.of("Вычисли расстояние перед телепортацией")));
                case -2 -> widget6.setTooltip(Tooltip.of(Text.of("Слишком далеко!")));
                case -3 -> widget6.setTooltip(Tooltip.of(Text.of("Телепортация в энде запрещена")));
            }

            widget4.active = false;
            switch (pos) {
                case 0 -> {
                    widget4.active = true;
                    widget4.setTooltip(Tooltip.of(Text.of("Кулдаун после телепортации: " + this.cooldownAfter + "s")));
                }
                case -1 -> widget4.setTooltip(Tooltip.of(Text.of("Вычисли расстояние перед телепортацией")));
                case -2 -> widget4.setTooltip(Tooltip.of(Text.of("Слишком далеко!")));
                case -3 -> widget4.setTooltip(Tooltip.of(Text.of("Телепортация в энде запрещена")));
            }
        }

        addDrawableChild(widget4);
        addDrawableChild(widget5);
        addDrawableChild(widget6);
    }

    private Text getPlayerPositionText() {
        PlayerEntity player = CLIENT.player;
        if (player != null) {
            return formatPosition(player.getPos());
        }
        return Text.literal("Недоступно");
    }

    private Triplet<Text, Integer, Vec3d> calculate() {
        if (CLIENT.player == null) return new Triplet<>(Text.literal(""), -1, null);
        String worldType = getDimension(CLIENT.player.getWorld());

        try {
            double x = Double.parseDouble(xField.getText().replace(",", "."));
            double y = Double.parseDouble(yField.getText().replace(",", "."));
            double z = Double.parseDouble(zField.getText().replace(",", "."));
            Vec3d pos = CLIENT.player.getPos();
            Vec3d pos2 = new Vec3d(x, y, z);
            if (worldType.equals("end")) return new Triplet<>(Text.literal("-"), -3, pos2);
            Duplet<Integer, Boolean> duplet = Teleporter.calculateCooldown(parent, pos, pos2, CLIENT.player.getWorld());
            if (duplet.type() == null || duplet.parametrize() == null) throw new NumberFormatException();
            boolean hasCooldown = duplet.parametrize();
            this.cooldownAfter = duplet.type();
            return new Triplet<>(Text.literal(String.format("%s%.1f", hasCooldown ? "§2" : "§4", pos.distanceTo(pos2))), hasCooldown ? 0 : -2, pos2);
        } catch (NumberFormatException e) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Некорректные координаты!"), true);
            }
        }
        return new Triplet<>(Text.literal(""), 0, Vec3d.ZERO);
    }

    private void calculateBack() {
        if (CLIENT.player == null) return;
        String worldType = getDimension(CLIENT.player.getWorld());
        if (worldType.equals("end")) {
            this.posBack = -3;
            return;
        }
        Duplet<Integer, Boolean> duplet = Teleporter.calculateCooldown(parent, CLIENT.player.getPos(), lastPosition, CLIENT.player.getWorld());
        if (duplet.type() == null || duplet.parametrize() == null) throw new NumberFormatException();
        boolean hasCooldown = duplet.parametrize();
        this.cooldownAfterBack = duplet.type();
        this.posBack = hasCooldown ? 0 : -2;
    }

    private Text formatPosition(Vec3d pos) {
        return Text.literal(String.format("%.1f, %.1f, %.1f", pos.x, pos.y, pos.z));
    }

    private void teleport() {
        try {
            double x = Double.parseDouble(xField.getText().replace(",", "."));
            double y = Double.parseDouble(yField.getText().replace(",", "."));
            double z = Double.parseDouble(zField.getText().replace(",", "."));

            ClientPlayNetworking.send(new VecPayload(new Vec3d(x, y, z)));
            cooldown = LocalDateTime.now().plusSeconds(cooldownAfter);
            close();

        } catch (NumberFormatException e) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Некорректные координаты!"), true);
            }
        }
    }

    private void teleportToLastPosition() {
        if (lastPosition != null) {
            xField.setText(String.format("%.1f", lastPosition.x));
            yField.setText(String.format("%.1f", lastPosition.y));
            zField.setText(String.format("%.1f", lastPosition.z));
            teleport();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        // texts
        context.drawText(textRenderer, "X:", width / 2 - 160, height / 2 - 15, 0xFFFFFF, false);
        context.drawText(textRenderer, "Y:", width / 2 - 50, height / 2 - 15, 0xFFFFFF, false);
        context.drawText(textRenderer, "Z:", width / 2 + 60, height / 2 - 15, 0xFFFFFF, false);
        xField.render(context, mouseX, mouseY, delta);
        yField.render(context, mouseX, mouseY, delta);
        zField.render(context, mouseX, mouseY, delta);
    }
}
