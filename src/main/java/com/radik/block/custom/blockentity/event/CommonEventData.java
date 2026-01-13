package com.radik.block.custom.blockentity.event;

import com.radik.connecting.event.Trade;
import com.radik.item.RegisterItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommonEventData {
    public static boolean canPurchaseClient(@NotNull Trade trade, PlayerInventory inventory) {
        return Arrays.stream(trade.buyer()).allMatch(cost -> hasEnoughItems(inventory, cost)) && !(inventory.player instanceof ServerPlayerEntity);
    }

    public static void purchaseClient(@NotNull Trade trade, PlayerInventory inventory) {
        if (!canPurchaseClient(trade, inventory)) return;
        for (ItemStack cost : trade.buyer()) {
            if (CommonEventData.cantDeductItems(inventory, cost)) {
                return;
            }
        }
    }

    public static boolean hasEnoughItems(@NotNull PlayerInventory inventory, @NotNull ItemStack cost) {
        int required = cost.getCount();
        int found = 0;
        Item item = cost.getItem();
        boolean bool = item.equals(RegisterItems.CANDY_BASKET_GREEN) || item.equals(RegisterItems.CANDY_BASKET_YELLOW) || item.equals(RegisterItems.CANDY_BASKET_RED) || item.equals(RegisterItems.CANDY_BASKET_BLUE);

        for (int i = 0; i < inventory.size() && found < required; i++) {
            ItemStack stack = inventory.getStack(i);
            if (ItemStack.areItemsAndComponentsEqual(cost, stack) || (bool && stack.getItem().equals(RegisterItems.CANDY_BASKET_SUPER))) {
                found += stack.getCount();
            }
        }

        return found >= required;
    }

    private static boolean isSpecialBasket(@NotNull Item item) {
        return item.equals(RegisterItems.CANDY_BASKET_GREEN) ||
            item.equals(RegisterItems.CANDY_BASKET_YELLOW) ||
            item.equals(RegisterItems.CANDY_BASKET_RED) ||
            item.equals(RegisterItems.CANDY_BASKET_BLUE);
    }

    public static boolean cantDeductItems(@NotNull PlayerInventory inventory, @NotNull ItemStack cost) {
        int required = cost.getCount();
        Item item = cost.getItem();

        boolean isSpecialBasket = isSpecialBasket(item);

        for (int i = 0; i < inventory.size() && required > 0; i++) {
            ItemStack stack = inventory.getStack(i);
            boolean isSuperBasket = stack.getItem().equals(RegisterItems.CANDY_BASKET_SUPER);
            boolean itemsMatch = ItemStack.areItemsAndComponentsEqual(cost, stack);
            boolean basketsMatch = isSpecialBasket && isSuperBasket;

            if (itemsMatch || basketsMatch) {
                int deductAmount = Math.min(stack.getCount(), required);
                stack.decrement(deductAmount);
                required -= deductAmount;
            }
        }

        return required != 0;
    }

}
