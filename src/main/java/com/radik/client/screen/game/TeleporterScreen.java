package com.radik.client.screen.game;

import com.radik.packets.TeleportPayload;
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
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

import static com.radik.Data.*;
import static com.radik.client.RadikClient.CLIENT;

@Environment(EnvType.CLIENT)
public class TeleporterScreen extends Screen {
    private TextFieldWidget xField, yField, zField;
    protected ItemStack parent;
    private final Vec3d lastPosition;
    private Boolean pos = null;
    private Text text = Text.of("-");
    private Vec3d position;

    public TeleporterScreen(ItemStack teleporter) {
        super(Text.literal("Окно Телепортера"));
        this.parent = teleporter;
        this.lastPosition = parent.get(POSITION);
    }

    public TeleporterScreen(ItemStack teleporter, Boolean pos, Text text, Vec3d position) {
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
                    Triplet<Text, Boolean, Vec3d> d = calculate();
                    CLIENT.setScreen(new TeleporterScreen(this.parent, d.getParametrize(), d.getType(), d.getCount()));
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
                widget6.active = false;
                widget4.active = false;
                widget4.setTooltip(Tooltip.of(Text.of("Это не твой телепортер.")));
                widget6.setTooltip(Tooltip.of(Text.of("Это не твой телепортер.")));
                break label1;
            }
            if (CLIENT.player.getPos().distanceTo(lastPosition) > getDistance(Objects.requireNonNull(parent.get(TELEPORTER)))) {
                widget6.active = false;
                widget6.setTooltip(Tooltip.of(Text.of("Слишком далеко!")));
            }
            if (pos == null) {
                widget4.active = false;
                widget4.setTooltip(Tooltip.of(Text.of("Вычисли расстояние перед телепортацией")));
            }
            else if (!pos) {
                widget4.active = false;
                widget4.setTooltip(Tooltip.of(Text.of("Слишком далеко!")));
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

    private Triplet<Text, Boolean, Vec3d> calculate() {
        if (CLIENT.player == null) return new Triplet<>(Text.literal(""), false, null);
        try {
            double x = Double.parseDouble(xField.getText().replace(",", "."));
            double y = Double.parseDouble(yField.getText().replace(",", "."));
            double z = Double.parseDouble(zField.getText().replace(",", "."));
            Vec3d pos = CLIENT.player.getPos();
            Vec3d pos2 = new Vec3d(x, y, z);
            double distance = pos.distanceTo(pos2);
            int max = getDistance(Objects.requireNonNull(parent.get(TELEPORTER)));
            return new Triplet<>(Text.literal(String.format("%s%.1f", distance <= max ? "§2" : "§4", distance)), distance <= max, pos2);
        } catch (NumberFormatException e) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("Некорректные координаты!"), true);
            }
        }
        return new Triplet<>(Text.literal(""), false, Vec3d.ZERO);
    }

    private Text formatPosition(Vec3d pos) {
        return Text.literal(String.format("%.1f, %.1f, %.1f", pos.x, pos.y, pos.z));
    }

    private void teleport() {
        try {
            double x = Double.parseDouble(xField.getText().replace(",", "."));
            double y = Double.parseDouble(yField.getText().replace(",", "."));
            double z = Double.parseDouble(zField.getText().replace(",", "."));

            ClientPlayerEntity player = CLIENT.player;
            if (player != null) {
                ClientPlayNetworking.send(new TeleportPayload(new Vec3d(x, y, z)));
                close();
            }
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            if (pos != null) {
                if (pos) teleport();
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
