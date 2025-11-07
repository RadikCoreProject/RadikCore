package com.radik.item.custom;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class Medal extends Item {
    public static final int MAX_TYPE = 5;
    public static final int MAX_MATERIAL  = 8;
    public Medal(@NotNull Item.Settings settings) {
        super(settings.component(MEDAL, 0).component(MEDAL_MATERIAL, 0).component(OWNER, "").component(TEXT, ""));
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        String owner = stack.get(OWNER);
        String text = stack.get(TEXT);
        Integer medal = stack.get(MEDAL);
        Integer material = stack.get(MEDAL_MATERIAL);
        if (medal == null || material == null || owner == null || text == null) return;
        textConsumer.accept(Text.translatable("tooltip.radik.medal").append(Text.translatable("tooltip.radik.medal." + medal)));
        textConsumer.accept(Text.translatable("tooltip.radik.material").append(Text.translatable("tooltip.radik.medal.material." + material)));
        textConsumer.accept(Text.translatable("tooltip.radik.owner").append(Text.literal(owner)));
        if (!text.isEmpty()) {
            textConsumer.accept(Text.of(""));
            textConsumer.accept(Text.literal(text).formatted(Formatting.AQUA));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
