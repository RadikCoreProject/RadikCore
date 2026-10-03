package com.radik.logic;

import com.radik.block.custom.blockentity.storage.StorageBlockEntity;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OnBreak {
    protected static void register() {
        PlayerBlockBreakEvents.BEFORE.register(OnBreak::tryBreak);
    }

    private static boolean tryBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof StorageBlockEntity sbe) {
            String name = playerEntity.getName().getString();
            if (!(sbe.owner.equals(name) || playerEntity.isInCreativeMode())) {
                playerEntity.sendMessage(Text.of("Это не твое хранилище."), false);
                return false;
            }
        }
        return true;
    }
}
