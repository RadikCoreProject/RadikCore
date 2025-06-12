package com.radik.logic;

import com.radik.block.RegisterBlocks;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

import static com.radik.Data.getDimension;
import static com.radik.Radik.SERVER;
import static com.radik.block.RegisterBlocks.*;
import static com.radik.logic.RadikCoreDEFEND.Logger;

public class OnBreak {
    private static final HashMap<Block, String> PRESENT = new HashMap<>();
    private static final HashMap<Block, String> OWNERS = new HashMap<>();
    private static final ArrayList<Block> LOGGING = new ArrayList<>();


    static {
        PRESENT.put(PRESENT_SMALL, "§2маленький");
        PRESENT.put(PRESENT_MEDIUM, "§9средний");
        PRESENT.put(PRESENT_BIG, "§4большой");
        PRESENT.put(PRESENT_INSTRUMENT, "§bинструментальный");
        PRESENT.put(PRESENT_WINTER, "§1зимний");

        OWNERS.put(BICYCLE, "Boomboxcuff");
        OWNERS.put(PIX, "X_xPIXx_X");
        
        LOGGING.add(Blocks.CHEST);
        LOGGING.add(Blocks.WHITE_BED);
        LOGGING.add(Blocks.LIGHT_GRAY_BED);
        LOGGING.add(Blocks.GRAY_BED);
        LOGGING.add(Blocks.BLACK_BED);
        LOGGING.add(Blocks.RED_BED);
        LOGGING.add(Blocks.BROWN_BED);
        LOGGING.add(Blocks.ORANGE_BED);
        LOGGING.add(Blocks.YELLOW_BED);
        LOGGING.add(Blocks.LIME_BED);
        LOGGING.add(Blocks.GREEN_BED);
        LOGGING.add(Blocks.BLUE_BED);
        LOGGING.add(Blocks.LIGHT_BLUE_BED);
        LOGGING.add(Blocks.CYAN_BED);
        LOGGING.add(Blocks.PURPLE_BED);
        LOGGING.add(Blocks.MAGENTA_BED);
        LOGGING.add(Blocks.PINK_BED);
        LOGGING.add(Blocks.WHITE_SHULKER_BOX);
        LOGGING.add(Blocks.LIGHT_GRAY_SHULKER_BOX);
        LOGGING.add(Blocks.GRAY_SHULKER_BOX);
        LOGGING.add(Blocks.BLACK_SHULKER_BOX);
        LOGGING.add(Blocks.RED_SHULKER_BOX);
        LOGGING.add(Blocks.BROWN_SHULKER_BOX);
        LOGGING.add(Blocks.ORANGE_SHULKER_BOX);
        LOGGING.add(Blocks.YELLOW_SHULKER_BOX);
        LOGGING.add(Blocks.LIME_SHULKER_BOX);
        LOGGING.add(Blocks.GREEN_SHULKER_BOX);
        LOGGING.add(Blocks.BLUE_SHULKER_BOX);
        LOGGING.add(Blocks.LIGHT_BLUE_SHULKER_BOX);
        LOGGING.add(Blocks.CYAN_SHULKER_BOX);
        LOGGING.add(Blocks.PURPLE_SHULKER_BOX);
        LOGGING.add(Blocks.MAGENTA_SHULKER_BOX);
        LOGGING.add(Blocks.PINK_SHULKER_BOX);
        LOGGING.add(Blocks.DIAMOND_ORE);
        LOGGING.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        LOGGING.add(Blocks.BARREL);
        LOGGING.add(TROPHY_NOSTALGIC_BRONZE);
        LOGGING.add(TROPHY_NOSTALGIC_SILVER);
        LOGGING.add(TROPHY_NOSTALGIC_GOLD);
    }

    protected static synchronized void register() {
        PlayerBlockBreakEvents.BEFORE.register(OnBreak::onTryBroke);
    }

    private static boolean onTryBroke(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity block) {
        String name = player.getName().getString();
        Block blocks = world.getBlockState(blockPos).getBlock();

        if (LOGGING.contains(blocks)) {
            Logger(player.getName().getString(), blockState.getBlock().getName().getString(), getDimension(world), blockPos.getX(), blockPos.getY(), blockPos.getZ(), "break");
        }

        if(OWNERS.containsKey(blocks) && !OWNERS.containsValue(name) && !world.isClient) {
            player.sendMessage(Text.literal("Это не твоё!!!"), false);
            return false;
        }

        else if (PRESENT.containsKey(blocks)) {
            for (ServerPlayerEntity p : SERVER.getPlayerManager().getPlayerList()) {
                p.sendMessage(Text.literal("§a§l" + name + "§r открыл " + PRESENT.get(blocks) + " подарок§r!"), false);
            }
        }

        return true;
    }

}
