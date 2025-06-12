package com.radik.logic;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.radik.Data.getDimension;
import static com.radik.block.RegisterBlocks.ELKA;
import static com.radik.logic.RadikCoreDEFEND.Logger;

public class OnPlace {
    private static ArrayList<Block> LOGGING = new ArrayList<>();

    static {
        LOGGING.add(Blocks.OBSIDIAN);
        LOGGING.add(Blocks.HOPPER);
        LOGGING.add(Blocks.WITHER_SKELETON_SKULL);
        LOGGING.add(Blocks.CHEST);
        LOGGING.add(Blocks.PISTON);
        LOGGING.add(Blocks.STICKY_PISTON);
        LOGGING.add(Blocks.DROPPER);
        LOGGING.add(Blocks.BEACON);
        LOGGING.add(Blocks.BEDROCK);
        LOGGING.add(Blocks.DISPENSER);
        LOGGING.add(Blocks.OBSERVER);
        LOGGING.add(Blocks.SLIME_BLOCK);
        LOGGING.add(Blocks.LIGHTNING_ROD);
        LOGGING.add(Blocks.TRIPWIRE_HOOK);
        LOGGING.add(ELKA);
        LOGGING.add(Blocks.BARREL);
        LOGGING.add(Blocks.END_PORTAL_FRAME);
    }

    protected static void initialize() {
        PlayerBlockBreakEvents.AFTER.register(OnPlace::placing);
    }

    private static void placing(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity block) {
        if (LOGGING.contains(blockState.getBlock())) {
            Logger(player.getName().getString(), blockState.getBlock().getName().getString(), getDimension(world), blockPos.getX(), blockPos.getY(), blockPos.getZ(), "place");
        }
    }
}
