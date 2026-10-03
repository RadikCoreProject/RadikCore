package com.radik.client.screen.widget;

import com.radik.Radik;
import com.radik.client.screen.TutorialScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

import java.util.ArrayList;
import java.util.List;

public class TutorialListWidget extends AlwaysSelectedEntryListWidget<TutorialListWidget.Entry> {
    public TutorialListWidget(MinecraftClient client, int width, int height, int top, int h) {
        super(client, width, height, top, h);
    }

    @Override
    public int addEntry(Entry entry) {
        return super.addEntry(entry);
    }

    @Override
    public int getRowWidth() {
        return 300;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    public abstract static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        final TutorialListWidget widget;
        final TextWidget titleWidget;
        final TextRenderer textRenderer;

        public Entry(TextRenderer textRenderer, TutorialListWidget widget, Text title) {
            this.titleWidget = new TextWidget(title.copy().formatted(Formatting.GOLD), textRenderer);
            this.widget = widget;
            this.textRenderer = textRenderer;
        }

        public Entry(TextRenderer textRenderer, Text title) {
            this(textRenderer, null, title);
        }

        @Override
        public Text getNarration() {
            return titleWidget.getMessage();
        }
    }

    public static class IconEntry extends Entry {
        private static final Identifier CRAFT = Radik.id("textures/gui/tutorial/craft.png");

        private final MultilineTextWidget descriptionWidget;
        private final Item item;
        private final Identifier icon;
        private final byte scrn;
        private final int move;
        private final boolean bigTitle;
        private final int width;
        private final List<Item> craft;


        private IconEntry(Builder builder) {
            super(builder.textRenderer, builder.widget, builder.title);
            this.descriptionWidget = new MultilineTextWidget(builder.description, builder.textRenderer);
            this.descriptionWidget.setMaxWidth(builder.width - 60);
            this.item = builder.item;
            this.icon = builder.icon;
            this.scrn = builder.scrn;
            this.move = builder.move;
            this.bigTitle = builder.bigTitle;
            this.width = builder.width;
            this.craft = builder.craft;
        }

        public static Builder builder(TextRenderer textRenderer, TutorialListWidget widget, Text title, int width) {
            return new Builder(textRenderer, widget, title, width);
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            if (scrn == -1) return false;
            MinecraftClient client = this.widget.client;
            Screen scr = client.currentScreen;
            if (click.button() == 0 && isMouseOver(click.x(), click.y()) && scr != null) {
                client.setScreen(new TutorialScreen(scr, this.scrn));
                return true;
            }
            return super.mouseClicked(click, doubled);
        }

        @Override
        public void render(@NotNull DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            Matrix3x2fStack matrix = context.getMatrices();

            matrix.pushMatrix();
            matrix.scale(2.0f, 2.0f);
            float scaledX = (this.getX() + 10) / 2.0f;
            float scaledY = (this.getY() + (widget.itemHeight / 2.0f) - 16.0f) / 2.0f;
            if (item != null) context.drawItem(new ItemStack(item), (int)scaledX, (int)scaledY);
            else if (icon != null) context.drawTexture(RenderPipelines.GUI_TEXTURED, icon, (int) scaledX + this.move, (int) scaledY, 0, 0, 16, 16, 16, 16);
            matrix.popMatrix();

            if (bigTitle) {
                float screenCenterX = this.getX() + width / 2.0f;
                matrix.pushMatrix();
                matrix.scale(2, 2);

                Text title = titleWidget.getMessage();
                int titleWidth = textRenderer.getWidth(title);
                float tX = (screenCenterX / 2) - (titleWidth / 2.0f);
                float tY = (float) (this.getY() + 2) / 2;

                context.drawTextWithShadow(textRenderer, title, (int)tX, (int)tY, 0xFFFFAA00);
                matrix.popMatrix();
            } else {
                this.titleWidget.setPosition(this.getX() + 50, this.getY() + 2);
                this.titleWidget.render(context, mouseX, mouseY, deltaTicks);
            }

            this.descriptionWidget.setPosition(this.getX() + 50 + this.move, this.getY() + (bigTitle ? 24 : 12));
            this.descriptionWidget.render(context, mouseX, mouseY, deltaTicks);

            if (!craft.isEmpty()) {
                context.drawTexture(RenderPipelines.GUI_TEXTURED, CRAFT, (int) scaledX + this.move - 15, (int) scaledY + 35, 0, 0, 128, 128, 128, 128);
            }
        }

        public static class Builder {
            private final TextRenderer textRenderer;
            private final TutorialListWidget widget;
            private final Text title;
            private final int width;
            private Text description = Text.empty();
            private Item item = null;
            private Identifier icon = null;
            private byte scrn = -1;
            private int move = 0;
            private boolean bigTitle = false;
            private List<Item> craft = new ArrayList<>();

            public Builder(TextRenderer textRenderer, TutorialListWidget widget, Text title, int width) {
                this.textRenderer = textRenderer;
                this.widget = widget;
                this.title = title;
                this.width = width;
            }

            public Builder description(Text description) {
                this.description = description;
                return this;
            }

            public Builder item(Item item) {
                this.item = item;
                this.icon = null;
                return this;
            }

            public Builder icon(Identifier icon) {
                this.icon = icon;
                this.item = null;
                return this;
            }

            public Builder move(int move) {
                this.move = move;
                return this;
            }

            public Builder withCraft(List<Item> craft) {
                this.craft = craft;
                return this;
            }

            public Builder bigTitle() {
                this.bigTitle = true;
                return this;
            }

            public Builder screen(int scrn) {
                this.scrn = (byte) scrn;
                return this;
            }

            public IconEntry build() {
                if (item == null && icon == null) {
                    throw new IllegalStateException("Either item or icon must be provided");
                }
                return new IconEntry(this);
            }
        }
    }

    public static class TextEntry extends Entry {
        private static final float TITLE_SCALE = 2f;
        private static final float DESC_SCALE = 0.75f;

        private final TextWidget titleWidget;
        private final MultilineTextWidget descriptionWidget;
        private final int width;
        private final TextRenderer textRenderer;

        public TextEntry(TextRenderer textRenderer, Text title, Text description, int width) {
            super(textRenderer, title);
            this.titleWidget = new TextWidget(title.copy().formatted(Formatting.GREEN), textRenderer);
            this.descriptionWidget = new MultilineTextWidget(description, textRenderer);
            this.descriptionWidget.setMaxWidth(width);
            this.width = width;
            this.textRenderer = textRenderer;
        }

        @Override
        public void render(@NotNull DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            Matrix3x2fStack matrix = context.getMatrices();
            float screenCenterX = this.getX() + this.width / 2.0f;

            matrix.pushMatrix();
            matrix.scale(TITLE_SCALE, TITLE_SCALE);

            Text title = titleWidget.getMessage();
            int titleWidth = textRenderer.getWidth(title);
            float tX = (screenCenterX / TITLE_SCALE) - (titleWidth / 2.0f);
            float tY = (this.getY() + 2) / TITLE_SCALE;

            context.drawTextWithShadow(textRenderer, title, (int)tX, (int)tY, 0xFFFFAA00);
            matrix.popMatrix();

            matrix.pushMatrix();
            matrix.scale(DESC_SCALE, DESC_SCALE);
            int padding = 20;
            int availableWidth = (int) ((this.width - padding) / DESC_SCALE);
            List<OrderedText> lines = textRenderer.wrapLines(descriptionWidget.getMessage(), availableWidth);

            float offsetAfterHeader = (9 * TITLE_SCALE) + 5;
            float currentY = (this.getY() + offsetAfterHeader) / DESC_SCALE;

            for (OrderedText line : lines) {
                int lineWidth = textRenderer.getWidth(line);
                float lX = (screenCenterX / DESC_SCALE) - (lineWidth / 2.0f);
                context.drawText(textRenderer, line, (int)lX, (int)currentY, 0xFFFFFFFF, false);
                currentY += 10;
            }

            matrix.popMatrix();
        }

        @Override
        public Text getNarration() {
            return titleWidget.getMessage();
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            return false;
        }
    }

    public static class AttributedEntry extends Entry {
        private static final float TITLE_SCALE = 1.5f;
        private static final Text TITLE_TEXT = Text.literal("Аттрибуты").formatted(Formatting.GREEN);

        private final Text authors;
        private final Text date;

        private final int width;
        private final TextRenderer textRenderer;

        public AttributedEntry(TextRenderer textRenderer, List<String> authors, Text date1, Text date2, int width) {
            super(textRenderer, TITLE_TEXT);
            this.width = width;
            this.textRenderer = textRenderer;
            this.date = Text.literal("Дата написания: ").append(date1).append("\nПоследнее изменение: ").append(date2);
            this.authors = Text.literal("Авторы:\n").append(Text.of(String.join("\n", authors)));
        }

        @Override
        public void render(@NotNull DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            Matrix3x2fStack matrix = context.getMatrices();
            float screenCenterX = this.getX() + this.width / 2.0f;
            matrix.pushMatrix();
            matrix.scale(TITLE_SCALE, TITLE_SCALE);

            int titleWidth = textRenderer.getWidth(TITLE_TEXT);
            float tX = (screenCenterX / TITLE_SCALE) - (titleWidth / 2.0f);
            float tY = (this.getY() + 2) / TITLE_SCALE;

            context.drawTextWithShadow(textRenderer, TITLE_TEXT, (int)tX, (int)tY, 0xFFFFAA00);
            matrix.popMatrix();

            int maxWidth = this.width - 20;

            List<OrderedText> dateLines = textRenderer.wrapLines(date, maxWidth);
            List<OrderedText> authorsLines = textRenderer.wrapLines(authors, maxWidth);

            int currentY = (int) (this.getY() + (9 * TITLE_SCALE) + 6);

            for (OrderedText line : dateLines) {
                int lineWidth = textRenderer.getWidth(line);
                context.drawText(textRenderer, line,
                    (int) (screenCenterX - lineWidth / 2.0f), currentY, 0xFFFFFFFF, false);
                currentY += 10;
            }
            currentY += 2;

            for (OrderedText line : authorsLines) {
                int lineWidth = textRenderer.getWidth(line);
                context.drawText(textRenderer, line,
                    (int) (screenCenterX - lineWidth / 2.0f), currentY, 0xFFFFFFFF, false);
                currentY += 10;
            }
        }



        @Override
        public Text getNarration() {
            return TITLE_TEXT;
        }

        @Override
        public boolean mouseClicked(Click click, boolean doubled) {
            return false;
        }
    }
}
