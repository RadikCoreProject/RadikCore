package com.radik;

import com.radik.block.RegisterBlocks;
import com.radik.item.RegisterItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.radik.block.RegisterBlocks.*;

public final class MixinData {
    public static final int MAX_BOOK_PAGES = 256;
    public static final int MAX_TITLE_WORDS = 100;

    public static final String NAME = "Yar1kGG";
    public static final HashMap<Item, Block> DOWNFALLED_BLOCKS = new HashMap<>();
    public static final HashMap<Item, Block> BLOCKITEMS = new HashMap<>();

    static {
        DOWNFALLED_BLOCKS.put(ELKA.asItem(), ELKA);
        DOWNFALLED_BLOCKS.put(RegisterBlocks.PIX.asItem(), RegisterBlocks.PIX);
        DOWNFALLED_BLOCKS.put(PRESENT_BIG.asItem(), PRESENT_BIG);

        BLOCKITEMS.put(RegisterItems.LEDENETS, LEDENETS);
        BLOCKITEMS.put(RegisterItems.LEDENETS1, LEDENETS1);
        BLOCKITEMS.put(RegisterItems.LEDENETS2, LEDENETS2);
    }
}
