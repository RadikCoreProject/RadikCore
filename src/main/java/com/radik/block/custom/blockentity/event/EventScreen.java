package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.packets.PacketType;
import com.radik.packets.payload.IntegerPayload;
import com.radik.util.Triplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

// TODO: ланг
@Environment(EnvType.CLIENT)
public class EventScreen extends HandledScreen<EventScreenHandler> {
    protected static final Identifier BACKGROUND_TEXTURE;
    private static final Identifier BUTTON1_NORMAL;
    private static final Identifier BUTTON1_HOVERED;
    private static final Identifier BUTTON2_NORMAL;
    private static final Identifier BUTTON2_HOVERED;
    private static final Identifier BUTTON3_NORMAL;
    private static final Identifier BUTTON3_HOVERED;

    private static final int BUTTON_WIDTH = 32;
    private static final int BUTTON_HEIGHT = 32;

    private static final int TITLE_Y = 15;
    private static final int EVENT_LABEL_Y = 40;
    private static final int TIME_LABEL_Y = 60;
    private static final int BUTTONS_START_X = 80;
    private static final int BUTTON_SPACING = 60;

    static {
        String time = EventBlockEntity.getEventType().name().toLowerCase();
        String start = "textures/gui/event/" + time + "/";

        BACKGROUND_TEXTURE = Identifier.of(Radik.MOD_ID, start + "background.png");
        BUTTON1_NORMAL = Identifier.of(Radik.MOD_ID, start + "leaderboard.png");
        BUTTON1_HOVERED = Identifier.of(Radik.MOD_ID, start + "leaderboard_hover.png");
        BUTTON2_NORMAL = Identifier.of(Radik.MOD_ID, start + "shop.png");
        BUTTON2_HOVERED = Identifier.of(Radik.MOD_ID, start + "shop_hover.png");
        BUTTON3_NORMAL = Identifier.of(Radik.MOD_ID, start + "quests.png");
        BUTTON3_HOVERED = Identifier.of(Radik.MOD_ID, start + "quests_hover.png");
    }

    public EventScreen(EventScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) return;
        if (client.player == null) return;

        // centre div
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

        int bX = (this.width - BUTTON_WIDTH) / 2;
        int bY = this.y + BUTTONS_START_X;

        EventButtonWidget b1 = new EventButtonWidget(
            bX - BUTTON_SPACING - 8, bY,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            BUTTON1_NORMAL, BUTTON1_HOVERED,
            Text.literal("Таблица лидеров"),
            () -> ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(3, 0, PacketType.OPEN_SCREEN)))
        );
        this.addDrawableChild(b1);

        EventButtonWidget b2 = new EventButtonWidget(
            bX - 8, bY,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            BUTTON2_NORMAL, BUTTON2_HOVERED,
            Text.literal("Магазин"),
            () -> ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(0, 0, PacketType.OPEN_SCREEN)))
        );
        this.addDrawableChild(b2);

        EventButtonWidget b3 = new EventButtonWidget(
            bX + BUTTON_SPACING - 8, bY,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            BUTTON3_NORMAL, BUTTON3_HOVERED,
            Text.literal("Задания"),
            () -> ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(2, 0, PacketType.OPEN_SCREEN)))

        );
        this.addDrawableChild(b3);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, Text.literal("§lБлок событий"),
                (this.backgroundWidth - this.textRenderer.getWidth("Блок событий")) / 2 - 10,
                TITLE_Y, 0xFF00FF00, true);

        context.drawText(this.textRenderer, Text.literal("Событие: §1§lНовый год"),
                10, EVENT_LABEL_Y, 0xFF000000, false);

        context.drawText(this.textRenderer, Text.literal("Время события: 14.01.2026-1.03.2026"),
                10, TIME_LABEL_Y, 0xFF000000, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private static class EventButtonWidget extends PressableWidget {
        private final Identifier n;
        private final Identifier h;
        private final Runnable onClick;

        public EventButtonWidget(int x, int y, int width, int height, Identifier n, Identifier h, Text tooltip, Runnable onClick) {
            super(x, y, width, height, tooltip);
            this.n = n;
            this.h = h;
            this.onClick = onClick;
            this.setTooltip(Tooltip.of(tooltip));
        }

        @Override
        public void onPress(AbstractInput input) {
            this.onClick.run();
        }

        @Override
        public void renderWidget(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
            Identifier texture = this.isHovered() ? h : n;
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(),
                    0f, 0f, this.width, this.height, this.width, this.height);
        }

        @Override
        public void appendClickableNarrations(NarrationMessageBuilder b) {
            this.appendDefaultNarrations(b);
        }
    }
}
