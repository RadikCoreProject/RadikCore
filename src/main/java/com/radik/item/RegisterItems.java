package com.radik.item;

import com.radik.Music;
import com.radik.Radik;
import com.radik.RadikCore;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.radik.Data.FOOD_COMPONENTS;

public class RegisterItems {
    public static final Item DYE_AMBER = registerItem("dye_amber", new Item(new Item.Settings()));
    public static final Item DYE_AQUA = registerItem("dye_aqua", new Item(new Item.Settings()));
    public static final Item DYE_BEIGE = registerItem("dye_beige", new Item(new Item.Settings()));
    public static final Item DYE_CORAL = registerItem("dye_coral", new Item(new Item.Settings()));
    public static final Item DYE_FOREST = registerItem("dye_forest", new Item(new Item.Settings()));
    public static final Item DYE_GINGER = registerItem("dye_ginger", new Item(new Item.Settings()));
    public static final Item DYE_INDIGO = registerItem("dye_indigo", new Item(new Item.Settings()));
    public static final Item DYE_MAROON = registerItem("dye_maroon", new Item(new Item.Settings()));
    public static final Item DYE_MINT = registerItem("dye_mint", new Item(new Item.Settings()));
    public static final Item DYE_NAVY = registerItem("dye_navy", new Item(new Item.Settings()));
    public static final Item DYE_OLIVE = registerItem("dye_olive", new Item(new Item.Settings()));
    public static final Item DYE_ROSE = registerItem("dye_rose", new Item(new Item.Settings()));
    public static final Item DYE_SLATE = registerItem("dye_slate", new Item(new Item.Settings()));
    public static final Item DYE_TAN = registerItem("dye_tan", new Item(new Item.Settings()));
    public static final Item DYE_TEAL = registerItem("dye_teal", new Item(new Item.Settings()));
    public static final Item DYE_VERDANT = registerItem("dye_verdant", new Item(new Item.Settings()));

    public static final Item DISC_CRYSTAL_PEAK = registerItem("disc_crystal_peak", new Item(new Item.Settings().jukeboxPlayable(Music.CRYSTAL_PEAK_KEY).maxCount(1)));
    public static final Item DISC_PURE_VESSEL = registerItem("disc_pure_vessel", new Item(new Item.Settings().jukeboxPlayable(Music.PURE_VESSEL_KEY).maxCount(1)));
    public static final Item DISC_SAVIOR_OF_THE_WAKING_WORLD = registerItem("disc_savior_of_the_waking_world", new Item(new Item.Settings().jukeboxPlayable(Music.SAVIOR_OF_THE_WAKED_WORLD_KEY).maxCount(1)));
    public static final Item DISC_DIRTMOUTH = registerItem("disc_dirtmouth", new Item(new Item.Settings().jukeboxPlayable(Music.DIRTMOUTH_KEY).maxCount(1)));
    public static final Item DISC_HOMESTUCK = registerItem("disc_homestuck", new Item(new Item.Settings().jukeboxPlayable(Music.HOMESTUCK_KEY).maxCount(1)));
    public static final Item DISC_BONEBOTTOM = registerItem("disc_bonebottom", new Item(new Item.Settings().jukeboxPlayable(Music.BONEBOTTOM_KEY).maxCount(1)));
    public static final Item DISC_NOSK = registerItem("disc_nosk", new Item(new Item.Settings().jukeboxPlayable(Music.NOSK_KEY).maxCount(1)));
    public static final Item DISC_THE_LAST_HUMAN = registerItem("disc_the_last_human", new Item(new Item.Settings().jukeboxPlayable(Music.THE_LAST_HUMAN_KEY).maxCount(1)));
    public static final Item DISC_NEUTRON_STARS = registerItem("disc_neutron_stars", new Item(new Item.Settings().jukeboxPlayable(Music.NEUTRON_STARS_KEY).maxCount(1)));

    public static final Item LEDENETS = registerItem("ledenets", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item LEDENETS1 = registerItem("ledenets1", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets1"))));
    public static final Item LEDENETS2 = registerItem("ledenets2", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item STAR = registerItem("star", new Item(new Item.Settings()));
    public static final Item DISC_JINGLE_BELLS = registerItem("disc_jingle_bells", new Item(new Item.Settings().jukeboxPlayable(Music.JINGLE_BELLS_KEY).maxCount(1)));
    public static final Item DISC_MERRY_CHRISTMAS = registerItem("disc_merry_christmas", new Item(new Item.Settings().jukeboxPlayable(Music.MERRY_CHRISTMAS_KEY).maxCount(1)));
    public static final Item APPLE_PIE = registerItem("winter_apple_pie", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("1"))));
    public static final Item BISCUITS_SNOWMAN = registerItem("winter_biscuits_snowman", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("2"))));
    public static final Item CANDY_APPLE = registerItem("winter_candy_apple", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item CHOCKOLATE_BAR = registerItem("winter_chocolate_bar", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item CHRISTMAS_PUDDING = registerItem("winter_christmas_pudding", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item CHRISTMAS_REINDEER_BISCUITS = registerItem("winter_christmas_reindeer_biscuits", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("2"))));
    public static final Item EGGNOG = registerItem("winter_eggnog", new Item(new Item.Settings().maxCount(16).food(FOOD_COMPONENTS.get("3"))));
    public static final Item GINGERBREAD = registerItem("winter_gingerbread", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item GINGERBREAD_MAN = registerItem("winter_gingerbread_man", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item GUMDROPS = registerItem("winter_gumdrops", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item HOT_CHOCOLATE = registerItem("winter_hot_chocolate", new Item(new Item.Settings().maxCount(16).food(FOOD_COMPONENTS.get("3"))));
    public static final Item PANETTONE = registerItem("winter_panettone", new Item(new Item.Settings().food(FOOD_COMPONENTS.get("1"))));
    public static final Item SCARF_1 = registerItem("winter_scarf_1", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_2 = registerItem("winter_scarf_2", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_3 = registerItem("winter_scarf_3", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_4 = registerItem("winter_scarf_4", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_5 = registerItem("winter_scarf_5", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_6 = registerItem("winter_scarf_6", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_7 = registerItem("winter_scarf_7", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_8 = registerItem("winter_scarf_8", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_9 = registerItem("winter_scarf_9", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_10 = registerItem("winter_scarf_10", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_11 = registerItem("winter_scarf_11", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_12 = registerItem("winter_scarf_12", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_13 = registerItem("winter_scarf_13", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_14 = registerItem("winter_scarf_14", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_15 = registerItem("winter_scarf_15", new Item(new Item.Settings().maxCount(1)));
    public static final Item SCARF_16 = registerItem("winter_scarf_16", new Item(new Item.Settings().maxCount(1)));
    public static final Item SUGAR_BROWN = registerItem("sugar_brown", new Item(new Item.Settings()));
    public static final Item SUGAR_RED = registerItem("sugar_red", new Item(new Item.Settings()));
    public static final Item SUGAR_YELLOW = registerItem("sugar_yellow", new Item(new Item.Settings()));

    public static final Item SODIUM_LAMP = registerItem("lamp_sodium", new Item(new Item.Settings()));
    public static final Item MERCURY_LAMP = registerItem("lamp_mercury", new Item(new Item.Settings()));

    public static final Item ADVENTURE_HAT = registerItem("adventure_hat", new Item(new Item.Settings().maxCount(1)));
    public static final Item TASHERS_CRONE = registerItem("tasher_crone", new Item(new Item.Settings().maxCount(1)));
    public static final Item DISK_PENIS_BOLSHOY = registerItem("disc_penis_bolshoy", new Item(new Item.Settings().jukeboxPlayable(Music.PENIS_BOLSHOY_KEY).maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, name), item);
    }

    @RadikCore
    public static void initialize() {
        Radik.LOGGER.info("Items initialized");
    }
}
