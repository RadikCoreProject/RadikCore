package com.radik.mixin.book;

import com.radik.MixinData;
import net.minecraft.client.gui.EditBox;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.util.List;

@Mixin({BookEditScreen.class})
public class BookEditScreenMixin {
    @Shadow private EditBoxWidget editBox;
    @Final @Shadow private List<String> pages;
    @Shadow private int currentPage;
    @Shadow private void updatePage() {}
    @Shadow private PageTurnWidget nextPageButton;
    @Shadow private void updatePreviousPageButtonVisibility() {}

    @Inject(
            method = "keyPressed(Lnet/minecraft/client/input/KeyInput;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onKeyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (this.editBox.active && input.hasCtrl()) {
            EditBox innerEditBox = ((EditBoxWidgetAccessor) this.editBox).getEditBox();

            if (input.isPaste()) {
                String clipboardText = getClipboardText();
                if (clipboardText != null && !clipboardText.isEmpty()) {
                    innerEditBox.replaceSelection(clipboardText);
                    splitPagesIfNeeded();
                    this.updatePage();
                    this.updatePreviousPageButtonVisibility();
                    this.nextPageButton.visible = this.currentPage < this.pages.size() - 1;
                    cir.setReturnValue(true);
                    return;
                }
            }

            switch (input.key()) {
                case 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,
                     65, 66, 67, 68, 69, 70,
                     75, 76, 77, 78, 79, 82 -> {
                    if (innerEditBox.hasSelection()) {
                        EditBoxSubstringAccessor line = (EditBoxSubstringAccessor)(Object)innerEditBox.getSelection();
                        int selStart = line.getBeginIndex();
                        int selEnd = line.getEndIndex();
                        String text = innerEditBox.getText();
                        String s = "§" + KeyEvent.getKeyText(input.key()).toLowerCase();

                        StringBuilder sb = new StringBuilder();
                        int lastEnd = 0;

                        for (Object line1 : innerEditBox.getLines()) {
                            int lineStart = ((EditBoxSubstringAccessor) line1).getBeginIndex();
                            int lineEnd = ((EditBoxSubstringAccessor) line1).getEndIndex();

                            int from = Math.max(lineStart, selStart);
                            int to = Math.min(lineEnd, selEnd);

                            if (from >= to) continue;

                            sb.append(s);
                            sb.append(text, from, to);
                            if (to != selEnd) sb.append("\n");
                            lastEnd = to;
                        }

                        if (lastEnd < selEnd) {
                            sb.append(text, lastEnd, selEnd);
                        }
                        sb.append("§r");

                        innerEditBox.replaceSelection(sb.toString());
                        splitPagesIfNeeded();
                        this.updatePage();
                        this.updatePreviousPageButtonVisibility();
                        this.nextPageButton.visible = this.currentPage < this.pages.size() - 1;
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

    @Unique
    private String getClipboardText() {
        try {
            return (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    @Unique
    private void splitPagesIfNeeded() {
        final int MAX_PAGE_LEN = 1024;
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < this.pages.size(); i++) {
                String page = this.pages.get(i);
                if (page.length() > MAX_PAGE_LEN) {
                    String first = page.substring(0, MAX_PAGE_LEN);
                    String second = page.substring(MAX_PAGE_LEN);
                    this.pages.set(i, first);
                    this.pages.add(i + 1, second);
                    if (i < this.currentPage) this.currentPage++;
                    changed = true;
                    break;
                }
            }
        } while (changed);
    }

//    @ModifyConstant(
//        method = {"getNarratedTitle"},
//        constant = @Constant(intValue = 16)
//    )
//    private int titleLength(int original) {
//        return MixinData.MAX_TITLE_WORDS;
//    }

    @ModifyConstant(
            method = "appendNewPage()V",
            constant = @Constant(intValue = 100)
    )
    private int maxPages(int original) {
        return MixinData.MAX_BOOK_PAGES;
    }
}