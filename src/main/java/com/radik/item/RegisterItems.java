package com.radik.item;

import com.radik.Music;
import com.radik.Radik;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.radik.Data.FOOD_COMPONENTS;
import static com.radik.Music.*;

public class RegisterItems {
    public static final Item DYE_AMBER = registerItem("dye_amber", Item::new);
    public static final Item DYE_AQUA = registerItem("dye_aqua", Item::new);
    public static final Item DYE_BEIGE = registerItem("dye_beige", Item::new);
    public static final Item DYE_CORAL = registerItem("dye_coral", Item::new);
    public static final Item DYE_FOREST = registerItem("dye_forest", Item::new);
    public static final Item DYE_GINGER = registerItem("dye_ginger", Item::new);
    public static final Item DYE_INDIGO = registerItem("dye_indigo", Item::new);
    public static final Item DYE_MAROON = registerItem("dye_maroon", Item::new);
    public static final Item DYE_MINT = registerItem("dye_mint", Item::new);
    public static final Item DYE_NAVY = registerItem("dye_navy", Item::new);
    public static final Item DYE_OLIVE = registerItem("dye_olive", Item::new);
    public static final Item DYE_ROSE = registerItem("dye_rose", Item::new);
    public static final Item DYE_SLATE = registerItem("dye_slate", Item::new);
    public static final Item DYE_TAN = registerItem("dye_tan", Item::new);
    public static final Item DYE_TEAL = registerItem("dye_teal", Item::new);
    public static final Item DYE_VERDANT = registerItem("dye_verdant", Item::new);

    public static final Item DISC_CRYSTAL_PEAK = registerItem("disc_crystal_peak", settings -> new Item(settings.jukeboxPlayable(CRYSTAL_PEAK_KEY).maxCount(1)));
    public static final Item DISC_PURE_VESSEL = registerItem("disc_pure_vessel", settings -> new Item(settings.jukeboxPlayable(PURE_VESSEL_KEY).maxCount(1)));
    public static final Item DISC_SAVIOR_OF_THE_WAKING_WORLD = registerItem("disc_savior_of_the_waking_world", settings -> new Item(settings.jukeboxPlayable(SAVIOR_OF_THE_WAKED_WORLD_KEY).maxCount(1)));
    public static final Item DISC_DIRTMOUTH = registerItem("disc_dirtmouth", settings -> new Item(settings.jukeboxPlayable(DIRTMOUTH_KEY).maxCount(1)));
    public static final Item DISC_HOMESTUCK = registerItem("disc_homestuck", settings -> new Item(settings.jukeboxPlayable(HOMESTUCK_KEY).maxCount(1)));
    public static final Item DISC_BONEBOTTOM = registerItem("disc_bonebottom", settings -> new Item(settings.jukeboxPlayable(BONEBOTTOM_KEY).maxCount(1)));
    public static final Item DISC_NOSK = registerItem("disc_nosk", settings -> new Item(settings.jukeboxPlayable(NOSK_KEY).maxCount(1)));
    public static final Item DISC_THE_LAST_HUMAN = registerItem("disc_the_last_human", settings -> new Item(settings.jukeboxPlayable(THE_LAST_HUMAN_KEY).maxCount(1)));
    public static final Item DISC_NEUTRON_STARS = registerItem("disc_neutron_stars", settings -> new Item(settings.jukeboxPlayable(NEUTRON_STARS_KEY).maxCount(1)));

    public static final Item LEDENETS = registerItem("ledenets", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item LEDENETS1 = registerItem("ledenets1", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets1"))));
    public static final Item LEDENETS2 = registerItem("ledenets2", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item STAR = registerItem("star", Item::new);
    public static final Item DISC_JINGLE_BELLS = registerItem("disc_jingle_bells", settings -> new Item(settings.jukeboxPlayable(JINGLE_BELLS_KEY).maxCount(1)));
    public static final Item DISC_MERRY_CHRISTMAS = registerItem("disc_merry_christmas", settings -> new Item(settings.jukeboxPlayable(MERRY_CHRISTMAS_KEY).maxCount(1)));
    public static final Item APPLE_PIE = registerItem("winter_apple_pie", settings -> new Item(settings.food(FOOD_COMPONENTS.get("1"))));
    public static final Item BISCUITS_SNOWMAN = registerItem("winter_biscuits_snowman", settings -> new Item(settings.food(FOOD_COMPONENTS.get("2"))));
    public static final Item CANDY_APPLE = registerItem("winter_candy_apple", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item CHOCKOLATE_BAR = registerItem("winter_chocolate_bar", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item CHRISTMAS_PUDDING = registerItem("winter_christmas_pudding", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item CHRISTMAS_REINDEER_BISCUITS = registerItem("winter_christmas_reindeer_biscuits", settings -> new Item(settings.food(FOOD_COMPONENTS.get("2"))));
    public static final Item EGGNOG = registerItem("winter_eggnog", settings -> new Item(settings.food(FOOD_COMPONENTS.get("3"))));
    public static final Item GINGERBREAD = registerItem("winter_gingerbread", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item GINGERBREAD_MAN = registerItem("winter_gingerbread_man", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item GUMDROPS = registerItem("winter_gumdrops", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item HOT_CHOCOLATE = registerItem("winter_hot_chocolate", settings -> new Item(settings.food(FOOD_COMPONENTS.get("3"))));
    public static final Item PANETTONE = registerItem("winter_panettone", settings -> new Item(settings.food(FOOD_COMPONENTS.get("1"))));
    public static final Item SCARF_1 = registerItem("winter_scarf_1", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_2 = registerItem("winter_scarf_2", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_3 = registerItem("winter_scarf_3", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_4 = registerItem("winter_scarf_4", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_5 = registerItem("winter_scarf_5", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_6 = registerItem("winter_scarf_6", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_7 = registerItem("winter_scarf_7", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_8 = registerItem("winter_scarf_8", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_9 = registerItem("winter_scarf_9", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_10 = registerItem("winter_scarf_10", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_11 = registerItem("winter_scarf_11", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_12 = registerItem("winter_scarf_12", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_13 = registerItem("winter_scarf_13", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_14 = registerItem("winter_scarf_14", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_15 = registerItem("winter_scarf_15", setting -> new Item(setting.maxCount(1)));
    public static final Item SCARF_16 = registerItem("winter_scarf_16", setting -> new Item(setting.maxCount(1)));
    public static final Item SUGAR_BROWN = registerItem("sugar_brown", Item::new);
    public static final Item SUGAR_RED = registerItem("sugar_red", Item::new);
    public static final Item SUGAR_YELLOW = registerItem("sugar_yellow", Item::new);

    public static final Item SODIUM_LAMP = registerItem("lamp_sodium", Item::new);
    public static final Item MERCURY_LAMP = registerItem("lamp_mercury", Item::new);

    public static final Item ADVENTURE_HAT = registerItem("adventure_hat", setting -> new Item(setting.maxCount(1)));
    public static final Item TASHERS_CRONE = registerItem("tasher_crone", setting -> new Item(setting.maxCount(1)));
    public static final Item DISK_PENIS_BOLSHOY = registerItem("disc_penis_bolshoy", settings -> new Item(settings.jukeboxPlayable(PENIS_BOLSHOY_KEY).maxCount(1)));
    public static final Block DISK_DEBRIS = registerBlockItem("disc_debris", new Item.Settings().jukeboxPlayable(Music.DEBRIS_KEY).maxCount(1));
    public static final Item DISK_BOLSHOY_KUSH = registerItem("disc_bolshoy_kush", settings -> new Item(settings.jukeboxPlayable(BOLSHOY_KUSH_KEY).maxCount(1)));

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, name)))));
    }

    private static Block registerBlockItem(@NotNull String name, Item.Settings settings) {
        Function<AbstractBlock.Settings, Block> function1 = sets -> new Block(sets.strength(1, 2).sounds(BlockSoundGroup.METAL).luminance(state -> 5).noCollision().nonOpaque());
        Block block = function1.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Radik.MOD_ID, name))));
        Registry.register(Registries.BLOCK, Identifier.of(Radik.MOD_ID, name), block);
        Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, name),
                new BlockItem(block, settings.useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, name)))));
        return block;
    }

    public static void registerItem() {
        Radik.LOGGER.info("Items initialized");
    }
}
