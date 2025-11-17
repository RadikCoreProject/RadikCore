package com.radik.logic;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.radik.block.RegisterBlocks.*;

public class OnBreak {
    private static final HashMap<Block, String> OWNERS = new HashMap<>();


    static {
        OWNERS.put(BICYCLE, "Boomboxcuff");
        OWNERS.put(PIX, "X_xPIXx_X");
        OWNERS.put(HOUSE, "Xreped");
        OWNERS.put(JAVA_PROGRAMMER, "Yar1kGG");
    }

    protected static void register() {
        PlayerBlockBreakEvents.BEFORE.register(OnBreak::onTryBroke);
    }

    private static boolean onTryBroke(World world, @NotNull PlayerEntity player, BlockPos blockPos, @NotNull BlockState blockState, BlockEntity block) {
        String name = player.getName().getString();
        Block blocks = blockState.getBlock();

        if(OWNERS.containsKey(blocks) && !OWNERS.containsValue(name) && !world.isClient) {
            player.sendMessage(Text.literal("Это не твоё!!!"), false);
            return false;
        }

        return true;
    }

    public static boolean logging(World world, ServerPlayerEntity player, @NotNull Block block, BlockPos pos) {
        return onTryBroke(world, player, pos, block.getDefaultState(), null);
    }
}
