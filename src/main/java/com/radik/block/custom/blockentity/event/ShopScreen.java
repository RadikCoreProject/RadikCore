package com.radik.block.custom.blockentity.event;

import com.radik.connecting.event.Trade;
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
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ShopScreen extends HandledScreen<ShopScreenHandler> {
    private static final int TRADE_HEIGHT = 30;

    private static final Identifier GOAL = Identifier.ofVanilla("textures/gui/sprites/advancements/goal_frame_unobtained.png");
    private static final Identifier CHALLENGE = Identifier.ofVanilla("textures/gui/sprites/advancements/challenge_frame_unobtained.png");

    private final List<Trade> trades;
    private final List<TradeButton> tradeButtons = new ArrayList<>();

    public ShopScreen(ShopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 166;
        this.trades = handler.getTrades();
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.updateTradeButtons();
    }

    private void updateTradeButtons() {
        for (TradeButton button : this.tradeButtons) this.remove(button);
        this.tradeButtons.clear();

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            TradeButton tradeButton = new TradeButton(
                x + 55 + TRADE_HEIGHT * i, y + backgroundHeight / 2 - 8, 16, 16, this.trades.get(i).seller(),
                () -> ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(1, finalI, PacketType.OPEN_SCREEN)))
            );

            this.tradeButtons.add(tradeButton);
            this.addDrawableChild(tradeButton);
        }
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, EventScreen.BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

        for (int i = 0; i < 5; i++) {
            context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                i == 4 ? CHALLENGE : GOAL,
                x + 49 + TRADE_HEIGHT * i,
                y + backgroundHeight / 2 - 14,
                0.0F, 0.0F, 28, 28, 28, 28);
        }
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, Text.literal("§lМагазин"),
                (this.backgroundWidth - this.textRenderer.getWidth("Магазин")) / 2 - 10,
                6, 0xFF00FF00, true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return pointX >= (double)(x - 1) && pointX < (double)(x + width + 1) &&
                pointY >= (double)(y - 1) && pointY < (double)(y + height + 1);
    }

    @Override
    public void close() {
        super.close();
        ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(-1, 0, PacketType.OPEN_SCREEN)));
    }

    private class TradeButton extends PressableWidget {
        private final ItemStack trade;
        private final Runnable onPress;

        public TradeButton(int x, int y, int width, int height, ItemStack trade, Runnable onPress) {
            super(x, y, width, height, Text.empty());
            this.trade = trade;
            this.onPress = onPress;
        }

        @Override
        public void onPress(AbstractInput input) {
            onPress.run();
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (client == null) return;

            context.drawItem(trade, this.getX(), this.getY());
            context.drawStackOverlay(client.textRenderer, trade, this.getX(), this.getY());
            if (isPointWithinBounds(this.getX(), this.getY(), 16, 16, mouseX, mouseY)) {
                context.drawItemTooltip(client.textRenderer, trade, mouseX, mouseY);
            }
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }
    }
}
