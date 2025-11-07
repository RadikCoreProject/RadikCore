package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.connecting.event.Trade;
import com.radik.packets.PacketType;
import com.radik.packets.payload.IntegerPayload;
import com.radik.util.Triplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ShopAccessScreen extends HandledScreen<ShopScreenHandler.ShopAccessScreenHandler> {
    private static final Identifier PLUS_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/icon/plus.png");
    private static final Identifier ARROW_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/icon/arrow.png");
    private static final Identifier ARROW_STRICT_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/icon/arrow_strict.png");
    static final Identifier CHECK_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/check.png");
    static final Identifier CROSS_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/cross.png");

    private static final int BACKGROUND_WIDTH = 256;
    private static final int BACKGROUND_HEIGHT = 166;

    private final Trade trade;

    public static Integer COUNT;
    public static Integer TRADE;

    public ShopAccessScreen(ShopScreenHandler.ShopAccessScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        Trade trades1;
        this.backgroundWidth = BACKGROUND_WIDTH;
        this.backgroundHeight = BACKGROUND_HEIGHT;

        trades1 = handler.getTrade();
        if (trades1 == null) trades1 = createSampleTrade();
        this.trade = trades1;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        if (client == null || client.player == null) return;

        int buttonY = (int) (this.y + this.backgroundHeight / 2.8);
        context.drawTexture(RenderLayer::getGuiTextured, EventScreen.BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        if (!Objects.equals(COUNT, -1)) drawTrade(context, mouseX, mouseY);

        if (COUNT != null && !Objects.equals(COUNT, -1)) {
            PressableWidget widget = new TradeButton(
                this.x + this.backgroundWidth / 2 - 125 , buttonY, 50, 50,
                () -> {
                    if (EventBlockData.canPurchase(trade, client.player.getInventory())) {
                        EventBlockData.purchase(trade, client.player.getInventory());
                        ShopAccessScreen.COUNT--;
                        ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(0, TRADE, PacketType.BUY)));
                    }
                }
            );
            this.addDrawableChild(widget);
        }
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        boolean t = COUNT == null || COUNT <= 0;

        context.drawText(this.textRenderer, Text.literal("§lМагазин"),
            (this.backgroundWidth - this.textRenderer.getWidth("Магазин")) / 2 - 10,
            6, 0x00FF00, true);

        if (Objects.equals(COUNT, -1)) {
            context.drawText(this.textRenderer, Text.literal("Произошла ошибка ☹"),
                65, 75, 0x000000, false);
            return;
        }

        context.drawText(this.textRenderer, Text.literal(String.format("Количество предметов: §%s§l%d", t ? "4" : "2", t ? 0 : COUNT)),
            10, 45, 0x000000, false);
    }

    private void drawTrade(DrawContext context, int mouseX, int mouseY) {
        if (this.client == null) return;
        if (this.client.player == null) return;
        int y = this.y + 75;
        int x = this.x + 50;

        for (int i = 0; i < trade.buyer().length; i++) {
            ItemStack stack = trade.buyer()[i];

            context.drawItem(stack, x, y);
            context.drawStackOverlay(this.client.textRenderer, stack, x, y);
            if (isPointWithinBounds(x, y, 16, 16, mouseX, mouseY)) context.drawItemTooltip(this.client.textRenderer, stack, mouseX, mouseY);
            x += 20;

            if (i < trade.buyer().length - 1) {
                context.drawTexture(RenderLayer::getGuiTextured, PLUS_TEXTURE, x - 2, y + 2, 0, 0, 12, 12, 12, 12);
                x += 12;
            }
        }

        x += 5;
        boolean canPurchase = EventBlockData.canPurchase(trade, client.player.getInventory());
        Identifier arrowTexture = canPurchase ? ARROW_TEXTURE : ARROW_STRICT_TEXTURE;
        context.drawTexture(RenderLayer::getGuiTextured, arrowTexture, x - 7, y - 4, 0, 0, 24, 24, 24, 24);
        x += 20;

        context.drawItem(trade.seller(), x, y);
        context.drawStackOverlay(this.client.textRenderer, trade.seller(), x, y);
        if (isPointWithinBounds(x, y, 16, 16, mouseX, mouseY)) {
            context.drawItemTooltip(this.client.textRenderer, trade.seller(), mouseX, mouseY);
        }

        if (isPointWithinBounds(x - 25, y + 3, 16, 8, mouseX, mouseY)) {
            Text tooltip = canPurchase ?
                    Text.literal("Доступно для покупки") :
                    Text.literal("Недостаточно предметов");
            context.drawTooltip(this.textRenderer, tooltip, mouseX, mouseY);
        }
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
        ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(0, 0, PacketType.OPEN_SCREEN)));
    }

    private class TradeButton extends PressableWidget {
        private final Runnable onPress;

        public TradeButton(int x, int y, int width, int height, Runnable onPress) {
            super(x, y, width, height, Text.empty());
            this.onPress = onPress;
        }

        @Override
        public void onPress() {
            if (client != null && client.player != null && EventBlockData.canPurchase(trade, client.player.getInventory()) && COUNT > 0) {
                onPress.run();
            }
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (client == null || client.player == null) return;
            boolean canPurchase = EventBlockData.canPurchase(trade, client.player.getInventory()) && COUNT > 0;
            Identifier texture = canPurchase ? CHECK_TEXTURE : CROSS_TEXTURE;

            context.drawTexture(RenderLayer::getGuiTextured, texture, this.getX(), this.getY(), 0f, 0f, this.width, this.height, this.width, this.height);
            if (this.isMouseOver(mouseX + 10, mouseY + 10)) {
                Text tooltip = canPurchase ? Text.literal("Купить") : COUNT == null || COUNT.equals(0) ? Text.literal("Предметов не осталось в магазине") : Text.literal("Недостаточно предметов для покупки");
                context.drawTooltip(ShopAccessScreen.this.textRenderer, tooltip, mouseX, mouseY);
            }
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }
    }

    private Trade createSampleTrade() {

        return new Trade(new ItemStack(Items.DIAMOND), new ItemStack[] {
            new ItemStack(Items.SALMON, 16),
            new ItemStack(Items.STONE, 16),
            new ItemStack(Items.ACTIVATOR_RAIL, 64)
        }, false);
    }
}
