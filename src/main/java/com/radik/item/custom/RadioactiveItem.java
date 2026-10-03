package com.radik.item.custom;

import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class RadioactiveItem extends BlockItem {
    public RadioactiveItem(Block block, Settings settings, int doza) {
        super(block, settings.component(DOZA, doza));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer i = stack.get(DOZA);
        if (i == null) return;
        textConsumer.accept(Text.translatable("radik.util.radiation")
            .append(Text.of(i * stack.getCount() + ""))
            .append(Text.translatable("radik.util.doza")));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
