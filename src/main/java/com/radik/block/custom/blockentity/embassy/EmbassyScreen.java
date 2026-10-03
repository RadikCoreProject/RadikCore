package com.radik.block.custom.blockentity.embassy;

import com.radik.Radik;
import com.radik.connecting.game.EmbassyData;
import com.radik.packets.EmbassyAction;
import com.radik.packets.payload.EmbassyPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class EmbassyScreen extends HandledScreen<EmbassyScreenHandler> {
    private EmbassyData data;
    public TextFieldWidget legate;
    public TextFieldWidget destination_X;
    public TextFieldWidget destination_Y;
    public TextFieldWidget destination_Z;

    protected static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("textures/gui/demo_background.png");
    private static final Identifier ON_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/bundle/bundle_progressbar_fill.png");
    private static final Identifier OFF_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/bundle/bundle_progressbar_full.png");
    private static final Identifier CHECK_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/check.png");
    private static final Identifier CROSS_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/butts/cross.png");
    private static final Identifier TP_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/embassy/tp.png");
    private static final Identifier TP_CROSS_TEXTURE = Identifier.of(Radik.MOD_ID, "textures/gui/embassy/tp_cross.png");

    private static final int BUTTON_WIDTH = 32;
    private static final int BUTTON_HEIGHT = 32;

    private static final int TITLE_Y = 15;
    private static final int OWNER_Y = 40;
    private static final int LEGATE_Y = 60;
    private static final int DESTINATION_Y = 80;
    private static final int COOLDOWN_Y = 100;
    private static final int BUTTONS_Y = 125;

    public EmbassyScreen(EmbassyScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.data = handler.getData();
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

        if (data.access == 2) {
            int bX = this.x + (this.backgroundWidth - BUTTON_WIDTH * 2) / 2 - 25;
            int bY = this.y + LEGATE_Y - 5;
            legate = new TextFieldWidget(textRenderer, bX, bY, 120, 20, Text.translatable("text.radik.embassy.set_legate"));
            legate.setMaxLength(20);
            legate.setText(data.legate);
            this.addDrawableChild(legate);

            ConditionalButtonWidget b1 = new ConditionalButtonWidget(
                bX + 140, bY - 5,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                () -> !legate.getText().equals(data.legate),
                () -> legate.getText().equals(data.legate) ? CROSS_TEXTURE : CHECK_TEXTURE,
                () -> legate.getText().equals(data.legate)
                    ? Text.translatable("text.radik.settings.no_change")
                    : Text.translatable("text.radik.embassy.set_legate"),
                () -> {
                    String newLegate = legate.getText();
                    data = new EmbassyData(data.owner, newLegate, data.from, data.to, data.cooldown, data.powered, data.access);
                    ClientPlayNetworking.send(new EmbassyPayload(EmbassyAction.EXCHANGE, data));
                }
            );
            this.addDrawableChild(b1);

            destinationFields();
            createButtons();
        }
    }

    private void destinationFields() {
        int space = 45;
        int bX = this.x + (this.backgroundWidth - BUTTON_WIDTH * 2) / 2 - 25;
        int bY = this.y + DESTINATION_Y - 5;

        destination_X = new TextFieldWidget(textRenderer, bX, bY, 40, 20, Text.literal("X: "));
        destination_Y = new TextFieldWidget(textRenderer, bX + space, bY, 40, 20, Text.literal("Y: "));
        destination_Z = new TextFieldWidget(textRenderer, bX + space * 2, bY, 40, 20, Text.literal("Z: "));
        destination_X.setMaxLength(7);
        destination_X.setText(String.valueOf(data.to.getX()));
        destination_Y.setMaxLength(7);
        destination_Y.setText(String.valueOf(data.to.getY()));
        destination_Z.setMaxLength(7);
        destination_Z.setText(String.valueOf(data.to.getZ()));
        this.addDrawableChild(destination_X);
        this.addDrawableChild(destination_Y);
        this.addDrawableChild(destination_Z);

        ConditionalButtonWidget b1 = new ConditionalButtonWidget(
            bX + 140, bY - 5,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            () -> !toPos().equals(data.to),
            () -> toPos().equals(data.to) ? CROSS_TEXTURE : CHECK_TEXTURE,
            () -> toPos().equals(data.to)
                ? Text.translatable("text.radik.settings.no_change")
                : Text.translatable("text.radik.embassy.set_legate"),
            () -> {
                BlockPos newPos = toPos();
                data = new EmbassyData(data.owner, data.legate, data.from, newPos, data.cooldown, data.access, (byte) 1);
                ClientPlayNetworking.send(new EmbassyPayload(EmbassyAction.EXCHANGE, data));
            }
        );
        this.addDrawableChild(b1);
    }

    @Contract(" -> new")
    private @NotNull BlockPos toPos() {
        String tx = destination_X.getText();
        String ty = destination_Y.getText();
        String tz = destination_Z.getText();
        int x = 0, y = 0, z = 0;
        try {
            x = Integer.parseInt(tx);
            y = Integer.parseInt(ty);
            z = Integer.parseInt(tz);
        } catch (NumberFormatException ignored) {}
        return new BlockPos(x, y, z);
    }

    private void createButtons() {
        if (client == null || client.player == null) return;

        int bX = this.x + (this.backgroundWidth) / 2 - BUTTON_WIDTH - 8;
        int bY = this.y + BUTTONS_Y;

        ConditionalButtonWidget tpButton = new ConditionalButtonWidget(
            bX, bY,
            BUTTON_WIDTH * 2, BUTTON_HEIGHT,
            () -> getTpCondition() == 0,
            () -> getTpCondition() == 0 ? TP_TEXTURE : TP_CROSS_TEXTURE,
            () -> {
                byte cond = getTpCondition();
                return cond == 0
                    ? Text.translatable("text.radik.embassy.tp")
                    : Text.translatable("text.radik.embassy.error_" + cond);
            },
            () -> ClientPlayNetworking.send(new EmbassyPayload(EmbassyAction.TP, data))
        );

        ConditionalButtonWidget changeButton = new ConditionalButtonWidget(
            bX + 100, bY,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            () -> data.powered != 0 && LocalDateTime.now().isAfter(data.cooldown),
            () -> data.powered == 2 ? ON_TEXTURE : OFF_TEXTURE,
            () -> Text.translatable("text.radik.embassy." + switch (data.powered) {
                case 1 -> "on";
                case 2 -> "off";
                default -> "cant";
            }),
            () -> {
                this.data = new EmbassyData(data.owner, data.legate, data.from, data.to, data.cooldown, data.access, (byte) (data.powered == 2 ? 1 : 2));
                ClientPlayNetworking.send(new EmbassyPayload(EmbassyAction.EXCHANGE, data));
            }
        );

        this.addDrawableChild(tpButton);
        this.addDrawableChild(changeButton);
    }

    private byte getTpCondition() {
        if (client == null || client.player == null) return 2;
        if (LocalDateTime.now().isBefore(data.cooldown)) return 1;
        String name = client.player.getName().getString();
        if (!name.equals(data.legate) && !name.equals(data.owner)) return 2;
        if (data.powered < 2) return 3;
        return 0;
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    protected void drawForeground(@NotNull DrawContext context, int mouseX, int mouseY) {
        LocalDateTime time = LocalDateTime.now();
        int secs = Math.max(0, (int) Duration.between(time, data.cooldown).toSeconds());

        context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.block"),
            (this.backgroundWidth - this.textRenderer.getWidth(Text.translatable("block.radik.embassy_block"))) / 2 - 10,
            TITLE_Y, 0xFF00FF00, true);

        context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.owner").append(data.owner),
            10, OWNER_Y, 0xFF000000, false);

        context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.cooldown")
                .append(Text.literal(String.valueOf(secs))
                    .append(Text.translatable("radik.util.second"))
                    .withColor(secs != 0 ? Colors.RED : Colors.GREEN)),
            10, COOLDOWN_Y, 0xFF000000, false);


        if (data.access == 2) {
            context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.legate"),
                10, LEGATE_Y, 0xFF000000, false);

            context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.dest"),
                10, DESTINATION_Y, 0xFF000000, false);
        } else {
            context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.dest").append(getPos()),
                10, DESTINATION_Y, 0xFF000000, false);

            context.drawText(this.textRenderer, Text.translatable("text.radik.embassy.legate").append(Text.of(data.legate)),
                10, LEGATE_Y, 0xFF000000, false);
        }
    }

    private @NotNull String getPos() {
        BlockPos pos = data.to;
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private static class ConditionalButtonWidget extends PressableWidget {
        private final Supplier<Boolean> activeCondition;
        private final Supplier<Identifier> textureSupplier;
        private final Supplier<Text> tooltipSupplier;
        private final Runnable onClick;

        public ConditionalButtonWidget(int x, int y, int width, int height,
                                       Supplier<Boolean> activeCondition,
                                       Supplier<Identifier> textureSupplier,
                                       Supplier<Text> tooltipSupplier,
                                       Runnable onClick) {
            super(x, y, width, height, Text.empty());
            this.activeCondition = activeCondition;
            this.textureSupplier = textureSupplier;
            this.tooltipSupplier = tooltipSupplier;
            this.onClick = onClick;
        }

        @Override
        public void onPress(AbstractInput input) {
            if (activeCondition.get()) {
                onClick.run();
            }
        }

        @Override
        protected void drawIcon(@NotNull DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            Identifier tex = textureSupplier.get();
            context.drawTexture(RenderPipelines.GUI_TEXTURED, tex, this.getX(), this.getY(),
                0f, 0f, this.width, this.height, this.width, this.height);

            if (this.isHovered()) {
                this.setTooltip(Tooltip.of(tooltipSupplier.get()));
            }
        }

        @Override
        public void appendClickableNarrations(NarrationMessageBuilder b) {
            this.appendDefaultNarrations(b);
        }
    }

    @Override
    public boolean keyPressed(@NotNull KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_E) {
            if (legate != null && legate.isFocused()) {
                return legate.keyPressed(input);
            }
            return true;
        }
        return super.keyPressed(input);
    }
}
