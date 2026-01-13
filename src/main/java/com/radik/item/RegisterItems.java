package com.radik.item;

import com.radik.Music;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.custom.*;
import com.radik.item.custom.chemistry.Vial;
import com.radik.item.custom.projectile.IceShard;
import com.radik.item.custom.reward.Magazine;
import com.radik.item.custom.reward.Medal;
import com.radik.item.custom.reward.Teleporter;
import com.radik.item.custom.reward.Tommy;
import com.radik.item.custom.tool.*;
import com.radik.item.custom.staff.WindStaff;
import com.radik.registration.IRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.radik.Data.*;
import static com.radik.Music.*;

public class RegisterItems implements IRegistry {
    private static final Function<Item.Settings, Item> neww = Item::new;
    private static final Function<Item.Settings, Item> hat = setting -> new Item(setting.maxCount(1).equippable(EquipmentSlot.HEAD));
    private static final Function<Item.Settings, Item> winter = settings -> new EventItem(settings.maxCount(99), ChallengeEvent.WINTER);
    private static final Function<Item.Settings, Item> halloween = settings -> new EventItem(settings.maxCount(99), ChallengeEvent.HALLOWEEN);
    private static final Function<Item.Settings, Item> halloween_candy = settings -> new EventItem(settings.maxCount(99).food(FOOD_COMPONENTS.get("candy")), ChallengeEvent.HALLOWEEN);

    public static final Item DYE_AMBER = registerItem("dye_amber", neww);
    public static final Item DYE_AQUA = registerItem("dye_aqua", neww);
    public static final Item DYE_BEIGE = registerItem("dye_beige", neww);
    public static final Item DYE_CORAL = registerItem("dye_coral", neww);
    public static final Item DYE_FOREST = registerItem("dye_forest", neww);
    public static final Item DYE_GINGER = registerItem("dye_ginger", neww);
    public static final Item DYE_INDIGO = registerItem("dye_indigo", neww);
    public static final Item DYE_MAROON = registerItem("dye_maroon", neww);
    public static final Item DYE_MINT = registerItem("dye_mint", neww);
    public static final Item DYE_NAVY = registerItem("dye_navy", neww);
    public static final Item DYE_OLIVE = registerItem("dye_olive", neww);
    public static final Item DYE_ROSE = registerItem("dye_rose", neww);
    public static final Item DYE_SLATE = registerItem("dye_slate", neww);
    public static final Item DYE_TAN = registerItem("dye_tan", neww);
    public static final Item DYE_TEAL = registerItem("dye_teal", neww);
    public static final Item DYE_VERDANT = registerItem("dye_verdant", neww);
    public static final Item DYE_RAINBOW = registerItem("dye_rainbow", neww);

    public static final Item DISC_CRYSTAL_PEAK = registerItem("disc_crystal_peak", settings -> new Item(settings.jukeboxPlayable(CRYSTAL_PEAK_KEY).maxCount(1)));
    public static final Item DISC_PURE_VESSEL = registerItem("disc_pure_vessel", settings -> new Item(settings.jukeboxPlayable(PURE_VESSEL_KEY).maxCount(1)));
    public static final Item DISC_SAVIOR_OF_THE_WAKING_WORLD = registerItem("disc_savior_of_the_waking_world", settings -> new Item(settings.jukeboxPlayable(SAVIOR_OF_THE_WAKED_WORLD_KEY).maxCount(1)));
    public static final Item DISC_DIRTMOUTH = registerItem("disc_dirtmouth", settings -> new Item(settings.jukeboxPlayable(DIRTMOUTH_KEY).maxCount(1)));
    public static final Item DISC_HOMESTUCK = registerItem("disc_homestuck", settings -> new Item(settings.jukeboxPlayable(HOMESTUCK_KEY).maxCount(1)));
    public static final Item DISC_BONEBOTTOM = registerItem("disc_bonebottom", settings -> new Item(settings.jukeboxPlayable(BONEBOTTOM_KEY).maxCount(1)));
    public static final Item DISC_NOSK = registerItem("disc_nosk", settings -> new Item(settings.jukeboxPlayable(NOSK_KEY).maxCount(1)));
    public static final Item DISC_THE_LAST_HUMAN = registerItem("disc_the_last_human", settings -> new Item(settings.jukeboxPlayable(THE_LAST_HUMAN_KEY).maxCount(1)));
    public static final Item DISC_NEUTRON_STARS = registerItem("disc_neutron_stars", settings -> new Item(settings.jukeboxPlayable(NEUTRON_STARS_KEY).maxCount(1)));
    public static final Item DISC_PRESIDENT_IS_DEAD = registerItem("disc_president_is_dead", settings -> new Item(settings.jukeboxPlayable(PRESIDENT_IS_DEAD_KEY).maxCount(1)));

    public static final Item LEDENETS = registerItem("ledenets", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets"))));
    public static final Item LEDENETS1 = registerItem("ledenets1", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets1"))));
    public static final Item LEDENETS2 = registerItem("ledenets2", settings -> new Item(settings.food(FOOD_COMPONENTS.get("ledenets2"))));
    public static final Item STAR = registerItem("star", neww);
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
    public static final Item SCARF_1 = registerItem("winter_scarf_1", hat);
    public static final Item SCARF_2 = registerItem("winter_scarf_2", hat);
    public static final Item SCARF_3 = registerItem("winter_scarf_3", hat);
    public static final Item SCARF_4 = registerItem("winter_scarf_4", hat);
    public static final Item SCARF_5 = registerItem("winter_scarf_5", hat);
    public static final Item SCARF_6 = registerItem("winter_scarf_6", hat);
    public static final Item SCARF_7 = registerItem("winter_scarf_7", hat);
    public static final Item SCARF_8 = registerItem("winter_scarf_8", hat);
    public static final Item SCARF_9 = registerItem("winter_scarf_9", hat);
    public static final Item SCARF_10 = registerItem("winter_scarf_10", hat);
    public static final Item SCARF_11 = registerItem("winter_scarf_11", hat);
    public static final Item SCARF_12 = registerItem("winter_scarf_12", hat);
    public static final Item SCARF_13 = registerItem("winter_scarf_13", hat);
    public static final Item SCARF_14 = registerItem("winter_scarf_14", hat);
    public static final Item SCARF_15 = registerItem("winter_scarf_15", hat);
    public static final Item SCARF_16 = registerItem("winter_scarf_16", hat);
    public static final Item SUGAR_BROWN = registerItem("sugar_brown", neww);
    public static final Item SUGAR_RED = registerItem("sugar_red", neww);
    public static final Item SUGAR_YELLOW = registerItem("sugar_yellow", neww);
    public static final Item SALAD = registerItem("salad", settings -> new EventItem(settings.food(FOOD_COMPONENTS.get("1")).maxCount(16), ChallengeEvent.WINTER));
    public static final Item CHAMPAGNE = registerItem("champagne", settings -> new EventItem(settings.food(FOOD_COMPONENTS.get("wine")), ChallengeEvent.WINTER));
    public static final Item RED_WINE = registerItem("red_wine", settings -> new EventItem(settings.food(FOOD_COMPONENTS.get("wine")), ChallengeEvent.WINTER));
    public static final Item ORANGE = registerItem("orange", settings -> new EventItem(settings.food(FoodComponents.APPLE), ChallengeEvent.WINTER));
    public static final Item SNOWFLAKE = registerItem("snowflake", winter);
    public static final Item CHRISTMAS_BALLS = registerItem("christmas_balls", winter);
    public static final Item WINTER_HAT = registerItem("winter_hat", setting -> new EventItem(setting.maxCount(1).equippable(EquipmentSlot.HEAD), ChallengeEvent.WINTER));
    public static final Item ICE_SHARD = registerItem("ice_shard", IceShard::new);

    public static final Item SODIUM_LAMP = registerItem("lamp_sodium", neww);
    public static final Item MERCURY_LAMP = registerItem("lamp_mercury", neww);

    public static final Item MEDAL = registerItem("medal", setting -> new Medal(setting.maxCount(99).equippable(EquipmentSlot.HEAD)));
    public static final Item CAPSULE = registerItem("capsule", settings -> new Capsule(settings.maxCount(16)));
    public static final Item TELEPORTER = registerItem("teleporter", settings -> new Teleporter(settings.maxCount(1), 0, Vec3d.ZERO, ""));

    public static final Item DISC_JORDANAIRES = registerItem("disc_jordanaires", settings -> new Item(settings.jukeboxPlayable(JORDANAIRES_KEY).maxCount(1)));
    public static final Item ADVENTURE_HAT = registerItem("adventure_hat", hat);
    public static final Item ADVENTURE_1M_HAT = registerItem("adventure_1m_hat", hat);
    public static final Item TASHERS_CRONE = registerItem("tasher_crone", hat);
    public static final Item DISK_PENIS_BOLSHOY = registerItem("disc_penis_bolshoy", settings -> new Item(settings.jukeboxPlayable(PENIS_BOLSHOY_KEY).maxCount(1)));
    public static final Block DISC_DEBRIS = registerBlockItem("disc_debris", new Item.Settings().jukeboxPlayable(Music.DEBRIS_KEY).maxCount(1));
    public static final Item DISK_BOLSHOY_KUSH = registerItem("disc_bolshoy_kush", settings -> new Item(settings.jukeboxPlayable(BOLSHOY_KUSH_KEY).maxCount(1)));

    // TEST FEATURES
    public static final Item WIND_STAFF = registerItem("wind_staff", settings -> new WindStaff(settings.maxCount(1)));
    public static final Item VIAL = registerItem("vial", Vial::new);

    public static final Item HALLOWEEN_PICKAXE = registerItem("halloween_pickaxe", settings -> new Pickaxe(Materials.HALLOWEEN, 1, -2.8F, settings.fireproof().component(EVENT_TYPE, ChallengeEvent.HALLOWEEN).component(BOOL, false)));
    public static final Item HALLOWEEN_AXE = registerItem("halloween_axe", settings -> new Axe(Materials.HALLOWEEN, 5.0F, -3.0F, settings.fireproof().component(EVENT_TYPE, ChallengeEvent.HALLOWEEN).component(BOOL, false)));
    public static final Item HALLOWEEN_HOE = registerItem("halloween_hoe", settings -> new Hoe(Materials.HALLOWEEN, -4.0F, 0.0F, settings.fireproof().component(EVENT_TYPE, ChallengeEvent.HALLOWEEN).component(BOOL, false)));
    public static final Item HALLOWEEN_SWORD = registerItem("halloween_sword", settings -> new Sword(Materials.HALLOWEEN, 3.0F, -2.4F, settings.fireproof().component(EVENT_TYPE, ChallengeEvent.HALLOWEEN).component(BOOL, false)));
    public static final Item HALLOWEEN_SHOVEL = registerItem("halloween_shovel", settings -> new Shovel(Materials.HALLOWEEN, 1.5F, -3.0F, settings.fireproof().component(EVENT_TYPE, ChallengeEvent.HALLOWEEN).component(BOOL, false)));
    public static final Item CANDY_YELLOW = registerItem("halloween_candy_yellow", halloween_candy);
    public static final Item CANDY_BLUE = registerItem("halloween_candy_blue", halloween_candy);
    public static final Item CANDY_RED = registerItem("halloween_candy_red", halloween_candy);
    public static final Item CANDY_GREEN = registerItem("halloween_candy_green", halloween_candy);
    public static final Item CANDY_BASKET_YELLOW = registerItem("halloween_candy_basket_yellow", halloween);
    public static final Item CANDY_BASKET_BLUE = registerItem("halloween_candy_basket_blue", halloween);
    public static final Item CANDY_BASKET_RED = registerItem("halloween_candy_basket_red", halloween);
    public static final Item CANDY_BASKET_GREEN = registerItem("halloween_candy_basket_green", halloween);
    public static final Item CANDY_BASKET_EMPTY = registerItem("halloween_candy_basket_empty", halloween);
    public static final Item CANDY_BASKET_LUCKY = registerItem("halloween_candy_basket_lucky", LuckyBasket::new);
    public static final Item CANDY_BASKET_SUPER = registerItem("halloween_candy_basket_super", halloween);
    public static final Item DISC_NETHER = registerItem("disc_nether", settings -> new EventItem(settings.jukeboxPlayable(NETHER_KEY).maxCount(1), ChallengeEvent.HALLOWEEN));
    public static final Item DISC_NEST = registerItem("disc_nest", settings -> new EventItem(settings.jukeboxPlayable(NEST_KEY).maxCount(1), ChallengeEvent.HALLOWEEN));

    public static final Item TEST_SWORD = registerItem("netherite_sword_1", settings -> new Item(settings.sword(ToolMaterial.NETHERITE, 3.0F, -2.4F).fireproof()));
    public static final Item TOMMY = registerItem("tommy", Tommy::new);
    public static final Item CARTRIDGE = registerItem("cartridge", neww);
    public static final Item MAGAZINE = registerItem("magazine", Magazine::new);

    public static final Item CUCUMBER_SEEDS = registerSeeds("crop_cucumber_seeds", RegisterBlocks.CUCUMBER);
    public static final Item CUCUMBER = registerItem("crop_cucumber", settings -> new Item(settings.food(FoodComponents.APPLE)));
    public static final Item TOMATO = registerItem("crop_tomato", settings -> new Item(settings.food(FoodComponents.APPLE)));
    public static final Item JAR = registerItem("jar", settings -> new Jar(settings, 0));
    public static final Item JAR_CUCMBERS = registerItem("jar_cucumbers", settings -> new Jar(settings, 1));
    public static final Item CIGARETTE_PACK = registerItem("cigarette_pack", settings -> new Jar(settings, 2));
    public static final Item CIGARETTE = registerItem("cigarette", ItemWithEffect::new);

    private static Item registerItem(String name, @NotNull Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, name)))));
    }

    @Contract(pure = true)
    private static @NotNull Function<Item.Settings, Item> createBlockItemWithUniqueName(Block block) {
        return settings -> new BlockItem(block, settings.useItemPrefixedTranslationKey());
    }

    private static Item registerSeeds(String name, Block cropBlock) {
        return registerItem(name, createBlockItemWithUniqueName(cropBlock));
    }

    private static Block registerBlockItem(@NotNull String name, Item.@NotNull Settings settings) {
        Function<AbstractBlock.Settings, Block> function1 = sets -> new Block(sets.strength(1, 2).sounds(BlockSoundGroup.METAL).luminance(state -> 5).noCollision().nonOpaque());
        Block block = function1.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Radik.MOD_ID, name))));
        Registry.register(Registries.BLOCK, Identifier.of(Radik.MOD_ID, name), block);
        Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, name),
                new BlockItem(block, settings.useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, name)))));
        return block;
    }

    public static void initialize() {
        Radik.LOGGER.info("Items initialized");
    }
}
