package com.radik.mixin.book;

import com.radik.MixinData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
@Mixin({BookEditScreen.class})
public class BookEditScreenMixin {
    @Shadow private EditBoxWidget editBox;
    @Final @Shadow private List<String> pages;
    @Shadow private int currentPage;
    @Shadow private void updatePage() {}
    @Shadow private PageTurnWidget nextPageButton;
    @Shadow private void updatePreviousPageButtonVisibility() {}

    @Unique private static Field EDIT_BOX_FIELD;

    @Unique
    private int getSelectionStart(EditBox editBox) {
        try {
            Method getSelection = EditBox.class.getDeclaredMethod("getSelection");
            getSelection.setAccessible(true);
            Object selection = getSelection.invoke(editBox);
            Field beginField = selection.getClass().getDeclaredField("beginIndex");
            beginField.setAccessible(true);
            return (int) beginField.get(selection);
        } catch (Exception e) {
            return 0;
        }
    }

    @Unique
    private int getSelectionEnd(EditBox editBox) {
        try {
            Method getSelection = EditBox.class.getDeclaredMethod("getSelection");
            getSelection.setAccessible(true);
            Object selection = getSelection.invoke(editBox);
            Field endField = selection.getClass().getDeclaredField("endIndex");
            endField.setAccessible(true);
            return (int) endField.get(selection);
        } catch (Exception e) {
            return 0;
        }
    }

    static {
        try {
            EDIT_BOX_FIELD = EditBoxWidget.class.getDeclaredField("editBox");
            EDIT_BOX_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) throws IllegalAccessException {
        if (this.editBox.active && input.hasCtrl()) {
            Object innerEditBoxObj = EDIT_BOX_FIELD.get(this.editBox);
            if (!(innerEditBoxObj instanceof EditBox innerEditBox)) return;

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
                case 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 65, 66, 67, 68, 69, 70, 75, 76, 77, 78, 79, 82 -> {
                    int selStart = getSelectionStart(innerEditBox);
                    int selEnd = getSelectionEnd(innerEditBox);
                    if (selStart != selEnd) {
                        String selected = innerEditBox.getText().substring(selStart, selEnd);
                        String s = "§" + KeyEvent.getKeyText(input.key()).toLowerCase();
                        innerEditBox.replaceSelection(s + selected.replace("\n", "\n" + s) + "§r");
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
        method = {"appendNewPage"},
        constant = @Constant(intValue = 100)
    )
    private int maxPages(int original) {
        return MixinData.MAX_BOOK_PAGES;
    }
}