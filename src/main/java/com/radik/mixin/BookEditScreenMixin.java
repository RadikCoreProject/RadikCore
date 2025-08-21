package com.radik.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin({BookEditScreen.class})
public class BookEditScreenMixin {

    @ModifyConstant(
            method = {"method_27593"},
            constant = {@Constant(
                    intValue = 16
            )}
    )
    private static int longbooks$returnMaxTitleLength(int original) {
        return Data.MAX_TITLE_WORDS;
    }

    @ModifyConstant(
            method = {"appendNewPage"},
            constant = {@Constant(
                    intValue = 100
            )}
    )
    private int longbooks$returnMaxPages(int original) {
        return Data.MAX_BOOK_PAGES;
    }
}