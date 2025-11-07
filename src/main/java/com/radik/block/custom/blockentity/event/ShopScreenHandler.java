package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.connecting.event.Trade;
import com.radik.packets.PacketType;
import com.radik.packets.payload.IntegerPayload;
import com.radik.ui.ClientHandlers;
import com.radik.util.Triplet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class ShopScreenHandler extends ScreenHandler {
    private static List<Trade> trades = new ArrayList<>();

    public ShopScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ClientHandlers.SHOP_SCREEN_HANDLER, syncId);
        trades = getSafeClientTrades();
    }

    private List<Trade> getSafeClientTrades() {
        try {
            if (isClientEnvironment()) return getClientTradesInternal();
        } catch (Exception e) {
            Radik.LOGGER.warn("Could not load client trades, using defaults: {}", e.getMessage());
        }
        return createDefaultTrades();
    }

    private static boolean isClientEnvironment() {
        try {
            Class<?> envTypeClass = Class.forName("net.fabricmc.api.EnvType");
            Class<?> fabricLoaderClass = Class.forName("net.fabricmc.loader.api.FabricLoader");
            Object fabricLoader = fabricLoaderClass.getMethod("getInstance").invoke(null);
            Object environment = fabricLoaderClass.getMethod("getEnvironmentType").invoke(fabricLoader);
            Object clientEnv = envTypeClass.getField("CLIENT").get(null);
            return environment.equals(clientEnv);
        } catch (Exception e) {
            return false;
        }
    }

    private List<Trade> getClientTradesInternal() {
        try {
            Class<?> radikClientClass = Class.forName("com.radik.client.RadikClient");
            java.lang.reflect.Field tradesField = radikClientClass.getField("TRADES");
            @SuppressWarnings("unchecked")
            List<Trade> clientTrades = (List<Trade>) tradesField.get(null);
            return clientTrades.isEmpty() ? createDefaultTrades() : clientTrades;
        } catch (Exception e) {
            return createDefaultTrades();
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    private static List<Trade> createDefaultTrades() {
        return List.of(
            new Trade(
                new ItemStack(Items.DIAMOND, 10),
                new ItemStack[] {
                    new ItemStack(Items.DRAGON_BREATH, 20),
                    new ItemStack(Items.ACTIVATOR_RAIL, 99),
                    new ItemStack(Items.BEDROCK, 1),
                    new ItemStack(Items.SALMON, 16),
                    new ItemStack(Items.NETHERRACK, 64)
                }, false),
            new Trade(
                new ItemStack(Items.EMERALD, 5),
                new ItemStack[]{
                    new ItemStack(Items.OBSIDIAN, 19)
                }, false
            ),
            new Trade(
                new ItemStack(Items.EMERALD, 4),
                new ItemStack[]{
                    new ItemStack(Items.OBSIDIAN, 19)
                }, false
            ),
            new Trade(
                new ItemStack(Items.EMERALD, 3),
                new ItemStack[]{
                    new ItemStack(Items.OBSIDIAN, 19)
                }, false
            ),
            new Trade(
                new ItemStack(Items.EMERALD, 2),
                new ItemStack[]{
                    new ItemStack(Items.OBSIDIAN, 19)
                }, false
            )
        );
    }

    public static class ShopAccessScreenHandler extends ScreenHandler {
        private final Trade trade;

        public ShopAccessScreenHandler(int syncId, PlayerInventory playerInventory) {
            super(ClientHandlers.SHOP_ACCESS_SCREEN_HANDLER, syncId);
            this.trade = getSafeClientTrade();
        }

        private Trade getSafeClientTrade() {
            try {
                if (isClientEnvironment()) {
                    return getClientTradeInternal();
                }
            } catch (Exception e) {
                Radik.LOGGER.warn("Could not load client trade, using default: {}", e.getMessage());
            }
            return createDefaultTrades().getFirst();
        }

        private Trade getClientTradeInternal() {
            try {
                Class<?> shopAccessScreenClass = Class.forName("com.radik.block.custom.blockentity.event.ShopAccessScreen");
                java.lang.reflect.Field tradeField = shopAccessScreenClass.getField("TRADE");
                Object tradeIndexObj = tradeField.get(null);

                if (tradeIndexObj instanceof Integer) {
                    int tradeIndex = (Integer) tradeIndexObj;
                    Class<?> radikClientClass = Class.forName("com.radik.client.RadikClient");
                    java.lang.reflect.Field tradesField = radikClientClass.getField("TRADES");
                    @SuppressWarnings("unchecked")
                    List<Trade> clientTrades = (List<Trade>) tradesField.get(null);

                    if (tradeIndex >= 0 && tradeIndex < clientTrades.size()) return clientTrades.get(tradeIndex);
                }
                return createDefaultTrades().getFirst();
            } catch (Exception e) {
                return createDefaultTrades().getFirst();
            }
        }

        @Override
        public ItemStack quickMove(PlayerEntity player, int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }

        protected Trade getTrade() {
            return this.trade;
        }
    }
}
