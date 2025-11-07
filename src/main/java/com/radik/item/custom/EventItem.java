package com.radik.item.custom;

import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.radik.Data.*;

public class EventItem extends Item {
    public EventItem(@NotNull Item.Settings settings) {
        super(settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        ChallengeEvent event = stack.get(EVENT_TYPE);
        if (event == null) return;
        textConsumer.accept(Text.translatable("tooltip.radik." + event.name().toLowerCase()));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
