package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.connecting.event.Event;
import com.radik.connecting.event.Eventer;
import com.radik.connecting.event.factory.BlockEventData;
import com.radik.connecting.event.factory.EntityEventData;
import com.radik.connecting.event.factory.EventData;
import com.radik.connecting.event.factory.ItemEventData;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.radik.client.RadikClient.CHALLENGES;
import static com.radik.client.RadikClient.GLOBAL_CHALLENGE;

@Environment(EnvType.CLIENT)
public class ChallengesScreen extends HandledScreen<ChallengesScreenHandler> {
    protected static final Identifier BACKGROUND_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/event/halloween/background.png");
    private static final Identifier CHECK_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/check.png");
    private static final Identifier CROSS_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/cross.png");

    private static final int BACKGROUND_WIDTH = 256;
    private static final int BACKGROUND_HEIGHT = 166;
    private static final int TITLE_Y = 15;

    private final List<Eventer> challenges = new ArrayList<>();
    private final List<RewardButton> rewardButtons = new ArrayList<>();

    public ChallengesScreen(ChallengesScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = BACKGROUND_WIDTH;
        this.backgroundHeight = BACKGROUND_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        if (client == null || client.player == null) return;

        rewardButtons.clear();
        this.clearChildren();
        load();
        createButtons();
    }

    private void load() {
        if (client == null || client.player == null) return;
        challenges.clear();
        EventBlockEntity be = handler.getBlockEntity();
        if (be == null) return;
        if (CHALLENGES != null) challenges.addAll(Arrays.asList(CHALLENGES));
        challenges.add(GLOBAL_CHALLENGE);
    }

    private void createButtons() {
        int startY = this.y + 40;
        int space = 25;

        for (int i = 0; i < challenges.size(); i++) {
            Eventer challenge = challenges.get(i);
            if (challenge == null) continue;

            int buttonY = startY + i * space;
            RewardButton button = new RewardButton(i, this.x + 200, buttonY, 20, 20, challenge);
            rewardButtons.add(button);
            this.addDrawableChild(button);
        }
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
        drawProgressBars(context);
    }

    private void drawProgressBars(DrawContext context) {
        int startY = this.y + 40;
        int space = 25;
        int w = 120;
        int h = 7;

        for (int i = 0; i < challenges.size(); i++) {
            Eventer challenge = challenges.get(i);
            if (challenge == null) continue;

            int barX = this.x + 75;
            int barY = startY + i * space + 6;

            context.fill(barX, barY, barX + w, barY + h, 0xFF555555);

            float progress = (float) challenge.getValue() / challenge.getCount();
            int fw = (int) (w * progress);
            if (fw > 0) context.fill(barX, barY, barX + fw, barY + h, getColor(progress));

            String text = challenge.getValue() + "/" + challenge.getCount();
            int tw = textRenderer.getWidth(text);
            context.drawText(textRenderer, text, barX + (w - tw) / 2, barY, 0x111111, false);
        }
    }

    private int getColor(float progress) {
        progress = Math.max(0, Math.min(1, progress));

        int red, green;
        if (progress < 0.5f) {
            red = 255;
            green = (int) (510 * progress);
        } else {
            red = (int) (510 * (1 - progress));
            green = 255;
        }

        return 0xFF000000 | (red << 16) | (green << 8);
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, Text.literal("§lЗадания"),
            (this.backgroundWidth - this.textRenderer.getWidth("Задания")) / 2 - 10,
            TITLE_Y, 0x00FF00, true);

        drawDis(context);
    }

    private void drawDis(DrawContext context) {
        int startY = 55;
        int textSpacing = 29;

        for (int i = 0; i < challenges.size(); i++) {
            Event<?> challenge = (Event<?>) challenges.get(i);
            if (challenge == null) continue;

            int textY = startY + i * textSpacing;
            String challengeType = getText(challenge);

            context.getMatrices().push();
            context.getMatrices().scale(0.85f, 0.85f, 1.0f);
            context.drawText(textRenderer, Text.literal(challengeType), 10, textY, 0xFFFFFF, true);
            context.getMatrices().pop();

            EventData data = challenge.data();
            Text name = switch (data.getType()) {
                case "item" -> ((ItemEventData) data).item().getName();
                case "block" -> ((BlockEventData) data).block().getName();
                case "entity" -> ((EntityEventData) data).entityType().getName();
                default -> Text.of("ERROR");
            };

            context.getMatrices().push();
            context.getMatrices().scale(0.7f, 0.7f, 1.0f);
            Text description = Text.translatable(challenge.getText().getString()).append(" ").append(String.valueOf(challenge.count())).append(" ").append(name);
            context.drawText(textRenderer, description, 107, (int) (textY * 1.25 - 14), 0xCCCCCC, false);
            context.getMatrices().pop();
        }
    }

    private @NotNull String getText(Eventer challenge) {
        int index = challenges.indexOf(challenge);
        if (index < 3) return "§2Ежедневное " + (index + 1);
        if (index == 3) return "§5Еженедельное";
        if (index == 4) return "§9§lГлобальное";
        return "Задание";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (RewardButton button : rewardButtons) {
            if (button.isHovered()) button.renderTooltip(context, mouseX, mouseY);
        }

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    public void close() {
        super.close();
        ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(-1, 0, PacketType.OPEN_SCREEN)));
    }

    private class RewardButton extends PressableWidget {
        private final int challengeIndex;
        private final Eventer challenge;
        private boolean showingRewardTooltip = false;

        public RewardButton(int challengeIndex, int x, int y, int width, int height, Eventer challenge) {
            super(x, y, width, height, Text.empty());
            this.challengeIndex = challengeIndex;
            this.challenge = challenge;
            update();
        }

        @Override
        public void onPress() {
            if (client == null || client.player == null) return;
            EventBlockEntity be = handler.getBlockEntity();

            if (be != null) {
                String playerName = client.player.getName().getString();
                if (be.claimReward(playerName, challengeIndex)) {
                    ClientPlayNetworking.send(new IntegerPayload(new Triplet<>(challengeIndex, 0, PacketType.GET_REWARD)));
                }
                update();
            }
        }

        private void update() {
            this.active = challenge.isCompleted() && !challenge.isClaimed();
        }

        @Override
        public void renderWidget(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
            Identifier texture = challenge.isClaimed() ? CHECK_TEXTURE : challenge.isCompleted() ? CHECK_TEXTURE : CROSS_TEXTURE;
            context.drawTexture(RenderLayer::getGuiTextured, texture, getX(), getY(), 0, 0, width, height, width, height);
            if (!this.active) context.fill(getX(), getY(), getX() + width, getY() + height, 0x80000000);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }

        public void renderTooltip(DrawContext context, int mouseX, int mouseY) {
            Text tooltip;
            if (challenge.isClaimed()) {
                tooltip = Text.literal("Награда получена");
                context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
            } else if (challenge.isCompleted()) {
                tooltip = Text.literal("Получить награду");
                context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
            } else {
                showRewardTooltip(context, mouseX, mouseY);
            }
        }

        private void showRewardTooltip(DrawContext context, int mouseX, int mouseY) {
            ItemStack reward = challenge.getReward();

            if (reward.isEmpty()) {
                context.drawTooltip(textRenderer, Text.literal("Задание не завершено"), mouseX, mouseY);
                return;
            }

            int count = reward.getCount();
            ItemStack stack = reward.copy();

            if (count > reward.getMaxCount()) stack.setCount(count);

            int tooltipX = mouseX + 5;
            int tooltipY = mouseY - 20;

            context.fill(tooltipX - 3, tooltipY - 3, tooltipX + 19, tooltipY + 19, 0xFF000000);
            context.fill(tooltipX - 2, tooltipY - 2, tooltipX + 18, tooltipY + 18, 0xFF404040);

            context.drawItem(stack, tooltipX, tooltipY);
            context.drawStackOverlay(textRenderer, stack, tooltipX, tooltipY, String.valueOf(count));

            long last = 0;
            if (System.currentTimeMillis() - last > 1000 && !showingRewardTooltip) {
                showingRewardTooltip = true;
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            showingRewardTooltip = false;
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void setFocused(boolean focused) {
            if (!focused) {
                showingRewardTooltip = false;
            }
            super.setFocused(focused);
        }
    }
}
