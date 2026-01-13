package com.radik.item.custom.reward;

import com.radik.item.RegisterItems;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class Magazine extends Item {
    public Magazine(@NotNull Settings settings) {
        super(settings.maxCount(16).component(STORAGE, 0));
    }

    public boolean onClicked(@NotNull ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        Integer c = stack.get(STORAGE);
        if (c == null || c == 100 || !otherStack.isOf(RegisterItems.CARTRIDGE)) return false;
        int sc = stack.getCount();

        int stackCount = otherStack.getCount();
        int p = Math.min((100 - c) * sc, stackCount);
        stack.set(STORAGE, c + (p / sc));
        cursorStackReference.set(RegisterItems.CARTRIDGE.getDefaultStack().copyWithCount(stackCount - p + p % sc));
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer count = stack.get(STORAGE);
        if (count == null) return;

        textConsumer.accept(Text.of((count == 100 ? "§2" : "§4") + count + " / 100"));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
