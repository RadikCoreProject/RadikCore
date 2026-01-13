package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.NotNull;

public class LeaderboardScreenHandler extends ScreenHandler {
    private final EventBlockEntity be;

    public LeaderboardScreenHandler(int id, @NotNull PlayerInventory inv) {
        super(Radik.LEADERBOARD_SCREEN_HANDLER, id);
        be = (EventBlockEntity) inv.player.getEntityWorld().getBlockEntity(Radik.EVENT_BLOCK_POS);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (be != null) {
            return player.squaredDistanceTo(
                    be.getPos().getX() + 0.5,
                    be.getPos().getY() + 0.5,
                    be.getPos().getZ() + 0.5
            ) <= 64.0;
        }
        return true;
    }
}
