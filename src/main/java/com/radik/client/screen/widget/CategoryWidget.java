package com.radik.client.screen.widget;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import com.radik.client.RadikClient;
import com.radik.connecting.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//@Environment(EnvType.CLIENT)
//public class CategoryWidget extends ElementListWidget<CategoryWidget.Entry> {
//    final GameOptionsScreen parent;
//
//    public CategoryWidget(@NotNull GameOptionsScreen parent, MinecraftClient client) {
//        super(client, parent.width, parent.layout.getContentHeight(), parent.layout.getHeaderHeight(), 20);
//        this.parent = parent;
//        Text i = null;
//
//        for (Event event : RadikClient.EVENTS) {
//            Text j = event.time().getTimer();
//            if (j != i) {
//                i = j;
//                this.addEntry(new CategoryWidget.CategoryEntry(j));
//            }
//            Text text = event.getText();
//            this.addEntry(new CategoryWidget.KeyBindingEntry(text));
//        }
//    }
//
//    public void update() {
//        KeyBinding.updateKeysByCode();
//        this.updateChildren();
//    }
//
//    public void updateChildren() {
//        this.children().forEach(CategoryWidget.Entry::update);
//    }
//
//    @Override
//    public int getRowWidth() {
//        return 340;
//    }
//
//    @Environment(EnvType.CLIENT)
//    public class CategoryEntry extends CategoryWidget.Entry {
//        final Text text;
//        private final int textWidth;
//
//        public CategoryEntry(final Text text) {
//            this.text = text;
//            this.textWidth = CategoryWidget.this.client.textRenderer.getWidth(this.text);
//        }
//
//        @Override
//        public void render(@NotNull DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
//            context.drawTextWithShadow(
//                    CategoryWidget.this.client.textRenderer, this.text, CategoryWidget.this.width / 2 - this.textWidth / 2, y + entryHeight - 9 - 1, Colors.WHITE
//            );
//        }
//
//        @Nullable
//        @Override
//        public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
//            return null;
//        }
//
//        @Override
//        public List<? extends Element> children() {
//            return Collections.emptyList();
//        }
//
//        @Override
//        public List<? extends Selectable> selectableChildren() {
//            return ImmutableList.of(new Selectable() {
//                @Override
//                public Selectable.SelectionType getType() {
//                    return Selectable.SelectionType.HOVERED;
//                }
//
//                @Override
//                public void appendNarrations(NarrationMessageBuilder builder) {
//                    builder.put(NarrationPart.TITLE, CategoryEntry.this.text);
//                }
//            });
//        }
//
//        @Override
//        protected void update() {
//        }
//
//        @Override
//        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
//            context.drawTextWithShadow(
//                CategoryWidget.this.client.textRenderer, this.text, CategoryWidget.this.width / 2 - this.textWidth / 2, y + entryHeight - 9 - 1, Colors.WHITE
//            );
//        }
//    }
//
//    @Environment(EnvType.CLIENT)
//    public abstract static class Entry extends ElementListWidget.Entry<CategoryWidget.Entry> {
//        abstract void update();
//    }
//
//    @Environment(EnvType.CLIENT)
//    public class KeyBindingEntry extends CategoryWidget.Entry {
//        private final Text bindingName;
////        private final ButtonWidget backButton;
//
//        KeyBindingEntry(final Text bindingName) {
//            this.bindingName = bindingName;
////            this.backButton = ButtonWidget.builder(ScreenTexts.DONE, button -> parent.close()).dimensions(0, 0, 50, 20).narrationSupplier(textSupplier -> Text.translatable("narrator.controls.reset", bindingName)).build();
//            this.update();
//        }
//
//        @Override
//        public void render(@NotNull DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
////            int i = CategoryWidget.this.getScrollbarX() - this.backButton.getWidth() - 10;
////            int j = y - 2;
////            this.backButton.setPosition(i, j);
////            this.backButton.render(context, mouseX, mouseY, tickProgress);
//            context.drawTextWithShadow(CategoryWidget.this.client.textRenderer, this.bindingName, x, y + entryHeight / 2 - 9 / 2, Colors.WHITE);
//        }
//
//        @Override
//        public List<? extends Element> children() {
//            return ImmutableList.of();
////            return ImmutableList.of(this.backButton);
//        }
//
//        @Override
//        public List<? extends Selectable> selectableChildren() {
//            return ImmutableList.of();
////            return ImmutableList.of(this.backButton);
//        }
//
//        @Override
//        protected void update() {
//        }
//
//        @Override
//        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
//
//        }
//    }
//}
