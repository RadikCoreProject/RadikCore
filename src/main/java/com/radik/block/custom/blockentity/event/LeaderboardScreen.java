package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.packets.PacketType;
import com.radik.packets.payload.IntegerPayload;
import com.radik.util.Triplet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static com.radik.client.RadikClient.LEADERBOARD;

// обосранный говнокодинг (не работает)
@Environment(EnvType.CLIENT)
public class LeaderboardScreen extends HandledScreen<LeaderboardScreenHandler> {
    protected static final Identifier BACKGROUND_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/event/halloween/background_leaderboard.png");
    private List<Map.Entry<String, Integer>> top10;
    private int points;
    private int top;
    private String name;

    public LeaderboardScreen(LeaderboardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        if (LEADERBOARD.getCount() == null || LEADERBOARD.getParametrize() == null || LEADERBOARD.getType() == null) return;

        this.name = inventory.player.getName().getString();
        this.points = LEADERBOARD.getType();
        this.top = LEADERBOARD.getParametrize();
        this.top10 = LEADERBOARD.getCount().entrySet().stream().toList();
        this.backgroundWidth = 180;
        this.backgroundHeight = 256;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        drawLeaderboard(context);
        if (top > 10) drawRank(context);
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, Text.literal("§lЛидерборд"),
            (this.backgroundWidth - this.textRenderer.getWidth("Лидерборд")) / 2 - 10,
            5, 0x00FF00, true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void drawLeaderboard(DrawContext context) {
        int sx = this.width / 2 - 90;
        int sy = this.height / 2 - 100;
        int entryH = 10;
        int sepH = 10;

        int entriesToShow = Math.min(10, top10.size());

        drawSep(context, sx, sy - 10, true);

        for (int i = 0; i < entriesToShow; i++) {
            Map.Entry<String, Integer> entry = top10.get(i);
            String name = entry.getKey();
            int score = entry.getValue();
            int rank = i + 1;
            int yPos = sy + (i * (entryH + sepH));
            boolean isCurrent = name.equals(this.name);

            Text rankText = Text.literal(rank + ". " + (i + 1 == 10 ? "" : " ") + "|").formatted(Formatting.WHITE);
            context.drawTextWithShadow(this.textRenderer, rankText, sx + 10, yPos, 0xFFFFFF);

            Text text = Text.literal(name);
            if (isCurrent) {
                text = text.copy().formatted(Formatting.BOLD);
                context.drawTextWithShadow(this.textRenderer, text, sx + 35, yPos, 0x880088);
            } else context.drawTextWithShadow(this.textRenderer, text, sx + 35, yPos, getRankColor(rank));

            Text scoreText = Text.literal(String.valueOf(score)).formatted(Formatting.BOLD, Formatting.AQUA);
            int scoreX = sx + 160 - this.textRenderer.getWidth(scoreText);
            context.drawTextWithShadow(this.textRenderer, scoreText, scoreX, yPos, 0xFFFFFF);

            drawSep(context, sx, yPos + entryH, false);
        }

        if (top10.size() < 10) {
            for (int i = top10.size(); i < 10; i++) {
                int yPos = sy + (i * (entryH + sepH));
                if (i < 9) {
                    drawSep(context, sx, yPos + entryH, false);
                }
                Text emptyText = Text.literal((i + 1) + ". " + ((i + 1) < 10 ? " " : "") + "|  ---").formatted(Formatting.GRAY);
                context.drawTextWithShadow(this.textRenderer, emptyText, sx + 10, yPos, 0x666666);
            }
        }
        drawSep(context, sx, sy + (9 * (entryH + sepH)) + entryH, true);
    }

    private void drawRank(@NotNull DrawContext context) {
        int startX = this.width / 2 - 90;
        int startY = this.height - 35;

        Text emptyText = Text.literal(top + ". |").formatted(Formatting.WHITE);
        context.drawTextWithShadow(this.textRenderer, emptyText, startX + 10, startY, 0x666666);

        Text nameText = Text.literal(name).formatted(Formatting.BOLD);
        context.drawTextWithShadow(this.textRenderer, nameText, startX + 35, startY, 0x880088);

        Text scoreText = Text.literal(String.valueOf(points)).formatted(Formatting.BOLD, Formatting.AQUA);
        int scoreX = startX + 200 - 40 - this.textRenderer.getWidth(scoreText);
        context.drawTextWithShadow(this.textRenderer, scoreText, scoreX, startY, 0xFFFFFF);
    }

    private void drawSep(@NotNull DrawContext context, int x, int y, boolean color) {
        String separator = "---------------------------";
        int sep = x + (170 - this.textRenderer.getWidth(separator)) / 2;
        MutableText text = color ? Text.literal(separator).formatted(Formatting.RED) : Text.literal(separator).formatted(Formatting.GRAY);
        context.drawTextWithShadow(this.textRenderer, text, sep, y, 0x666666);
    }

    private int getRankColor(int rank) {
        return switch (rank) {
            case 1 -> 0xFFD700;  // gold
            case 2 -> 0xC0C0C0;  // silver
            case 3 -> 0xCD7F32;  // bronze
            default -> 0xFFFFFF; // white
        };
    }

    public void updateLeaderboardData() {
        if (this.client == null || this.client.player == null) return;
        if (LEADERBOARD.getCount() == null || LEADERBOARD.getParametrize() == null || LEADERBOARD.getType() == null) return;

        this.points = LEADERBOARD.getType();
        this.top = LEADERBOARD.getParametrize();
        this.top10 = LEADERBOARD.getCount().entrySet().stream().toList();
    }

    @Override
    public void close() {
        super.close();
        ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(-1, 0, PacketType.OPEN_SCREEN)));
    }
}
