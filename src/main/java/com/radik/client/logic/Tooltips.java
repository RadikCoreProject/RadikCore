package com.radik.client.logic;

import com.radik.client.ClientInit;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Map;

import static com.radik.Data.EVENT_TYPE;

public class Tooltips {
    private static final Map<Item, String> AUTHORS = Map.of(
            RegisterItems.SUMMER_AXE, "ABOBUSNIEV",
            RegisterItems.SUMMER_PICKAXE, "ABOBUSNIEV",
            RegisterItems.SUMMER_SHOVEL, "ABOBUSNIEV",
            RegisterItems.SUMMER_SWORD, "ABOBUSNIEV",
            RegisterItems.SUMMER_HOE, "ABOBUSNIEV"
            );

    public static void register() {
        ItemTooltipCallback.EVENT.register(Tooltips::tooltip);
    }

    private static void tooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> texts) {
        Item item = itemStack.getItem();
        String author = AUTHORS.get(item);
        ChallengeEvent type = itemStack.get(EVENT_TYPE);
        if (type != null) texts.add(Text.translatable("tooltip.radik." + type.name().toLowerCase()));
        if (author != null) texts.add(Text.translatable("text.radik.author").append(author).formatted(Formatting.GRAY, Formatting.ITALIC));
    }
}
