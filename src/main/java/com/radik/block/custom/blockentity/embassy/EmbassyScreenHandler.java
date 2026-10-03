package com.radik.block.custom.blockentity.embassy;

import com.radik.connecting.game.EmbassyData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import static com.radik.ui.Handlers.EMBASSY_SCREEN_HANDLER;

public class EmbassyScreenHandler extends ScreenHandler {
    private final EmbassyData data;

    public EmbassyScreenHandler(int syncId, PlayerInventory inventory, EmbassyData data) {
        super(EMBASSY_SCREEN_HANDLER, syncId);
        this.data = data;
    }

    public EmbassyData getData() {
        return data;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}