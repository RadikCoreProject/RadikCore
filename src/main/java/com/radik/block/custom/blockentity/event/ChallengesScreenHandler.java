package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.ui.ClientHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.Nullable;

public class ChallengesScreenHandler extends ScreenHandler {
    private final EventBlockEntity be;

    public ChallengesScreenHandler(int id, PlayerInventory inv) {
        this(id, inv, (EventBlockEntity) inv.player.getWorld().getBlockEntity(Radik.EVENT_BLOCK_POS));
    }

    public ChallengesScreenHandler(int id, PlayerInventory inv, @Nullable EventBlockEntity be) {
        super(ClientHandlers.CHALLENGES_SCREEN_HANDLER, id);
        this.be = be;
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

    public EventBlockEntity getBlockEntity() {
        return be;
    }
}
