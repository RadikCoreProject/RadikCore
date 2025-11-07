package com.radik.block.custom.blockentity.event;

import com.radik.Radik;
import com.radik.connecting.event.Event;
import com.radik.ui.ClientHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EventScreenHandler extends ScreenHandler {
    private final EventBlockEntity be;

    public EventScreenHandler(int id, PlayerInventory inv) {
        super(ClientHandlers.EVENT_SCREEN_HANDLER, id);
        be = (EventBlockEntity) inv.player.getWorld().getBlockEntity(Radik.EVENT_BLOCK_POS);
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
