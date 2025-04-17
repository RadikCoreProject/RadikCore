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

import java.util.HashMap;

import static com.radik.Radik.SERVER;
import static com.radik.block.RegisterBlocks.*;
//import static com.radik.logic.RadikCoreDEFEND.Logger;

public class OnBreak {
    private static final HashMap<Block, String> PRESENT = new HashMap<>();
    private static final HashMap<Block, String> OWNERS = new HashMap<>();


    static {
        PRESENT.put(PRESENT_SMALL, "§2маленький");
        PRESENT.put(PRESENT_MEDIUM, "§9средний");
        PRESENT.put(PRESENT_BIG, "§4большой");
        PRESENT.put(PRESENT_INSTRUMENT, "§bинструментальный");
        PRESENT.put(PRESENT_WINTER, "§1зимний");

        OWNERS.put(BICYCLE, "Boomboxcuff");
        OWNERS.put(PIX, "catgirl_cute777");
    }

    protected static synchronized void register() {
        PlayerBlockBreakEvents.BEFORE.register(OnBreak::onTryBroke);
    }

    private static boolean onTryBroke(World world, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity block) {
        String name = player.getName().getString();
        Block blocks = world.getBlockState(blockPos).getBlock();

        if(OWNERS.containsKey(blocks) && !OWNERS.containsValue(name) && !world.isClient) {
            player.sendMessage(Text.literal("Это не твоё!!!"));
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
