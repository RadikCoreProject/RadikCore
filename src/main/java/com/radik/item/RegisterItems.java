package com.radik.item;

import com.radik.Music;
import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.custom.*;
import com.radik.item.custom.chemistry.Vial;
import com.radik.item.custom.data.FoodData;
import com.radik.item.custom.projectile.Bullet;
import com.radik.item.custom.projectile.IceShard;
import com.radik.item.custom.weapon.Magazine;
import com.radik.item.custom.reward.Medal;
import com.radik.item.custom.reward.Teleporter;
import com.radik.item.custom.weapon.Tommy;
import com.radik.item.custom.tool.*;
import com.radik.item.custom.staff.WindStaff;
import com.radik.item.custom.weapon.WaterPistol;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

import static com.radik.Data.*;
import static com.radik.Music.*;
import static com.radik.item.custom.data.FoodData.*;

public class RegisterItems {
    public static final Function<Item.Settings, Item> neww = Item::new;
    private static final Function<Item.Settings, Item> nn = settings -> new Item(settings.maxCount(99));
    private static final Function<Item.Settings, Item> hat = setting -> new Item(setting.maxCount(1).equippable(EquipmentSlot.HEAD));
    private static final Function<Item.Settings, Item> summer = settings -> new Item(settings.maxCount(99).component(EVENT_TYPE, ChallengeEvent.SUMMER));
    private static final Function<Item.Settings, Item> summer_hat = settings -> new Item(settings.maxCount(1).equippable(EquipmentSlot.HEAD).component(EVENT_TYPE, ChallengeEvent.SUMMER));
    private static final Function<Item.Settings, Item> winter = settings -> new Item(settings.maxCount(99).component(EVENT_TYPE, ChallengeEvent.WINTER));
    private static final Function<Item.Settings, Item> halloween = settings -> new Item(settings.maxCount(99).component(EVENT_TYPE, ChallengeEvent.HALLOWEEN));
    private static final Function<Item.Settings, Item> halloween_candy = settings -> new Item(settings.maxCount(99).food(FOOD_COMPONENTS.get("candy")).component(EVENT_TYPE, ChallengeEvent.HALLOWEEN));
    public static final Function<Item.Settings, Item> SUMMER_PLAIN = settings -> new Item(settings.maxCount(99).food(FoodData.SUMMER_FOOD, FoodData.summerFood().build()).component(EVENT_TYPE, ChallengeEvent.SUMMER));

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
    public static final Item SALAD = registerItem("salad", settings -> new Item(settings.food(FOOD_COMPONENTS.get("1")).maxCount(16).component(EVENT_TYPE, ChallengeEvent.WINTER)));
    public static final Item CHAMPAGNE = registerItem("champagne", settings -> new Item(settings.food(FOOD_COMPONENTS.get("wine")).component(EVENT_TYPE, ChallengeEvent.WINTER)));
    public static final Item RED_WINE = registerItem("red_wine", settings -> new Item(settings.food(FOOD_COMPONENTS.get("wine")).component(EVENT_TYPE, ChallengeEvent.WINTER)));
    public static final Item ORANGE = registerItem("orange", settings -> new Item(settings.food(FoodComponents.APPLE).component(EVENT_TYPE, ChallengeEvent.WINTER)));
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

    public static final Item HALLOWEEN_PICKAXE = registerItem("halloween_pickaxe", settings -> new Pickaxe(ToolMaterials.HALLOWEEN, 1, -2.8F, settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
    public static final Item HALLOWEEN_AXE = registerItem("halloween_axe", settings -> new Axe(ToolMaterials.HALLOWEEN, 5.0F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
    public static final Item HALLOWEEN_HOE = registerItem("halloween_hoe", settings -> new Hoe(ToolMaterials.HALLOWEEN, -4.0F, 0.0F, settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
    public static final Item HALLOWEEN_SWORD = registerItem("halloween_sword", settings -> new Sword(ToolMaterials.HALLOWEEN, 3.0F, -2.4F, settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
    public static final Item HALLOWEEN_SHOVEL = registerItem("halloween_shovel", settings -> new Shovel(ToolMaterials.HALLOWEEN, 1.5F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
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
    public static final Item DISC_NETHER = registerItem("disc_nether", settings -> new Item(settings.jukeboxPlayable(NETHER_KEY).maxCount(1).component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));
    public static final Item DISC_NEST = registerItem("disc_nest", settings -> new Item(settings.jukeboxPlayable(NEST_KEY).maxCount(1).component(EVENT_TYPE, ChallengeEvent.HALLOWEEN)));

    public static final Item TEST_SWORD = registerItem("netherite_sword_1", settings -> new Item(settings.sword(ToolMaterial.NETHERITE, 3.0F, -2.4F).fireproof()));
    public static final Item TOMMY = registerItem("tommy", Tommy::new);
    public static final Item CARTRIDGE = registerItem("cartridge", properties -> new Bullet(properties, Bullet.BulletType.TOMMY));
    public static final Item MAGAZINE = registerItem("magazine", Magazine::new);
//    public static final Item SOUR_CREAM = registerItem("sour_cream", settings -> new Item(settings.food(FoodComponents.APPLE)));

    public static final Item CUCUMBER_SEEDS = registerSeeds("crop_cucumber_seeds", RegisterBlocks.CUCUMBER);
    public static final Item CUCUMBER = registerItem("crop_cucumber", settings -> new Item(settings.food(FoodComponents.APPLE)));
    public static final Item TOMATO = registerItem("crop_tomato", settings -> new Item(settings.food(FoodComponents.APPLE)));
    public static final Item JAR = registerItem("jar", settings -> new Jar(settings, 0));
    public static final Item JAR_CUCMBERS = registerItem("jar_cucumbers", settings -> new Jar(settings, 1));
    public static final Item CIGARETTE_PACK = registerItem("cigarette_pack", settings -> new Jar(settings, 2));
    public static final Item CIGARETTE = registerItem("cigarette", ItemWithEffect::new);

    public static final Item FLOWERY_PICKAXE = registerItem("flowery_pickaxe", settings -> new Pickaxe(ToolMaterials.FLOWERY, 1, -2.8F, settings.component(EVENT_TYPE, ChallengeEvent.FLOWERY)));
    public static final Item FLOWERY_AXE = registerItem("flowery_axe", settings -> new Axe(ToolMaterials.FLOWERY, 5.0F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.FLOWERY)));
    public static final Item FLOWERY_HOE = registerItem("flowery_hoe", settings -> new Hoe(ToolMaterials.FLOWERY, -4.0F, 0.0F, settings.component(EVENT_TYPE, ChallengeEvent.FLOWERY)));
    public static final Item FLOWERY_SWORD = registerItem("flowery_sword", settings -> new Sword(ToolMaterials.FLOWERY, 3.0F, -2.4F, settings.component(EVENT_TYPE, ChallengeEvent.FLOWERY)));
    public static final Item FLOWERY_SHOVEL = registerItem("flowery_shovel", settings -> new Shovel(ToolMaterials.FLOWERY, 1.5F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.FLOWERY)));

    public static final Item STORAGE_UPGRADE_WOOD = registerItem("storage_upgrade_wood", nn);
    public static final Item STORAGE_UPGRADE_COPPER = registerItem("storage_upgrade_copper", nn);
    public static final Item STORAGE_UPGRADE_IRON = registerItem("storage_upgrade_iron", nn);
    public static final Item STORAGE_UPGRADE_GOLD = registerItem("storage_upgrade_gold", nn);
    public static final Item STORAGE_UPGRADE_DIAMOND = registerItem("storage_upgrade_diamond", nn);
    public static final Item STORAGE_UPGRADE_EMERALD = registerItem("storage_upgrade_emerald", nn);
    public static final Item STORAGE_UPGRADE_OBSIDIAN = registerItem("storage_upgrade_obsidian", nn);

    public static final Item LEAD_BOOTS = registerItem("lead_boots", settings -> new Item(settings.armor(ArmorMaterials.LEAD, EquipmentType.BOOTS)));
    public static final Item LEAD_LEGGINGS = registerItem("lead_leggings", settings -> new Item(settings.armor(ArmorMaterials.LEAD, EquipmentType.LEGGINGS)));
    public static final Item LEAD_CHESTPLATE = registerItem("lead_chestplate", settings -> new Item(settings.armor(ArmorMaterials.LEAD, EquipmentType.CHESTPLATE)));
    public static final Item LEAD_HELMET = registerItem("lead_helmet", settings -> new Item(settings.armor(ArmorMaterials.LEAD, EquipmentType.HELMET)));
    public static final Item LEAD_SWORD = registerItem("lead_sword", settings -> new Item(settings.sword(ToolMaterials.LEAD, 3.0F, -2.4F)));
    public static final Item LEAD_SHOVEL = registerItem("lead_shovel", settings -> new ShovelItem(ToolMaterials.LEAD, 1.5F, -3.0F, settings));
    public static final Item LEAD_PICKAXE = registerItem("lead_pickaxe", settings -> new Item(settings.pickaxe(ToolMaterials.LEAD, 1.0F, -2.8F)));
    public static final Item LEAD_AXE = registerItem("lead_axe", settings -> new AxeItem(ToolMaterials.LEAD, 5.0F, -3.0F, settings));
    public static final Item LEAD_HOE = registerItem("lead_hoe", settings -> new HoeItem(ToolMaterials.LEAD, -3.0F, 0.0F, settings));
    public static final Item LEAD_INGOT = registerItem("lead_ingot", neww);
    public static final Item LEAD_NUGGET = registerItem("lead_nugget", neww);
    public static final Item LEAD_RAW = registerItem("lead_raw", neww);
    public static final Item URANUS_INGOT = registerItem("uranus_ingot", neww);
    public static final Item URANUS_NUGGET = registerItem("uranus_nugget", neww);
    public static final Item URANUS_RAW = registerItem("uranus_raw", neww);
    public static final Item DISC_REACTOR = registerItem("disc_reactor", settings -> new Item(settings.jukeboxPlayable(REACTOR_KEY).maxCount(1)));
//    public static final Item RADIATION_SUIT_BOOTS = registerItem("radiation_suit_boots", settings -> new Item(settings.armor(ArmorMaterials.RADIATION_SUIT, EquipmentType.BOOTS)));
//    public static final Item RADIATION_SUIT_LEGGINGS = registerItem("radiation_suit_leggings", settings -> new Item(settings.armor(ArmorMaterials.RADIATION_SUIT, EquipmentType.LEGGINGS)));
//    public static final Item RADIATION_SUIT_CHESTPLATE = registerItem("radiation_suit_chestplate", settings -> new Item(settings.armor(ArmorMaterials.RADIATION_SUIT, EquipmentType.CHESTPLATE)));
//    public static final Item RADIATION_SUIT_HELMET = registerItem("radiation_suit_helmet", settings -> new Item(settings.armor(ArmorMaterials.RADIATION_SUIT, EquipmentType.HELMET)));


    public static final Item SUMMER_PICKAXE = registerItem("summer_pickaxe", settings -> new Pickaxe(ToolMaterials.SUMMER, 1, -2.8F, settings.component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item SUMMER_AXE = registerItem("summer_axe", settings -> new Axe(ToolMaterials.SUMMER, 5.0F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item SUMMER_HOE = registerItem("summer_hoe", settings -> new Hoe(ToolMaterials.SUMMER, -4.0F, 0.0F, settings.component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item SUMMER_SWORD = registerItem("summer_sword", settings -> new Sword(ToolMaterials.SUMMER, 3.0F, -2.4F, settings.component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item SUMMER_SHOVEL = registerItem("summer_shovel", settings -> new Shovel(ToolMaterials.SUMMER, 1.5F, -3.0F, settings.component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item LEAVE_OAK = registerItem("leave_oak", summer);
    public static final Item LEAVE_DARK_OAK = registerItem("leave_dark_oak", summer);
    public static final Item LEAVE_PALE_OAK = registerItem("leave_pale_oak", summer);
    public static final Item LEAVE_MANGROVE = registerItem("leave_mangrove", summer);
    public static final Item LEAVE_SPRUCE = registerItem("leave_spruce", summer);
    public static final Item LEAVE_ACACIA = registerItem("leave_acacia", summer);
    public static final Item LEAVE_BIRCH = registerItem("leave_birch", summer);
    public static final Item LEAVE_JUNGLE = registerItem("leave_jungle", summer);
    public static final Item LEAVE_AZALEA = registerItem("leave_azalea", summer);
    public static final Item LEAVE_CHERRY = registerItem("leave_cherry", summer);
    public static final Item LEAVE_DEAD = registerItem("leave_dead", summer);
    public static final Item CLEVER3 = registerItem("clever3", summer);
    public static final Item CLEVER4 = registerItem("clever4", summer);
    public static final Item BANANA_CLOSE = registerItem("banana_close", summer);
    public static final Item BANANA_PEEL = registerItem("banana_peel", summer);
    public static final Item BANANA              = registerItem("banana_open",         SUMMER_PLAIN);
    public static final Item ICE_CREAM_VANILLA   = registerItem("ice_cream_vanilla",   SUMMER_PLAIN);
    public static final Item ICE_CREAM_CHOCOLATE = registerItem("ice_cream_chocolate", summerEffect(StatusEffects.RESISTANCE));
    public static final Item ICE_CREAM_BERRY     = registerItem("ice_cream_berry",     summerEffect(StatusEffects.REGENERATION));
    public static final Item ICE_CREAM_BANANA    = registerItem("ice_cream_banana",    summerEffect(StatusEffects.HASTE));
    public static final Item PANAMA = registerItem("panama", summer_hat);
    public static final Item WATER_PISTOL = registerItem("water_pistol", WaterPistol::new);
    public static final Item DISC_FOREST = registerItem("disc_forest", settings -> new Item(settings.jukeboxPlayable(FOREST_KEY).maxCount(1).component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item DISC_CALM = registerItem("disc_calm", settings -> new Item(settings.jukeboxPlayable(CALM_KEY).maxCount(1).component(EVENT_TYPE, ChallengeEvent.SUMMER)));
    public static final Item DISC_HOLIDAY = registerItem("disc_holiday", settings -> new Item(settings.jukeboxPlayable(HOLIDAY_KEY).maxCount(1).component(EVENT_TYPE, ChallengeEvent.SUMMER)));

//    public static final Item GERMAN_SWORD = registerItem("german_sword", settings -> new Item(settings.sword(ToolMaterials.GERMAN_SWORD, 7.0F, -2.4F)));

    public static final Item ANEVRIZM_CHOCOLATE_CRAB = registerItem("anevrizm_chocolate_crab", s -> new Item(s.food(CHOCOCRAB)));
    public static final Item ANEVRIZM_COOKED_CRAB = registerItem("anevrizm_cooked_crab", s -> new Item(s.food(COOKED_CHOCOCRAB)));
    public static final Item ANEVRIZM_ENERGY_DRINK = registerItem("anevrizm_energy_drink", s -> new Item(s.food(SODA, ConsumableComponents.food().consumeEffect(new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.SPEED, 600, 1), new StatusEffectInstance(StatusEffects.HASTE, 600, 1)))).build())));
    public static final Item ANEVRIZM_EYEFISH = registerItem("anevrizm_eyefish", s -> new Item(s.food(RAW_FISH)));
    public static final Item ANEVRIZM_COOKED_EYEFISH = registerItem("anevrizm_cooked_eyefish", s -> new Item(s.food(COOKED_FISH)));
    public static final Item ANEVRIZM_PUFFERGUM_FISH = registerItem("anevrizm_puffergum_fish", s -> new Item(s.food(RAW_FISH)));
    public static final Item ANEVRIZM_PUFFERGUM_FISH_COOKED = registerItem("anevrizm_puffergum_fish_cooked", s -> new Item(s.food(COOKED_FISH)));
    public static final Item ANEVRIZM_SOUR_SARDINE = registerItem("anevrizm_sour_sardine", s -> new Item(s.food(RAW_FISH)));
    public static final Item ANEVRIZM_SOUR_SARDINE_COOKED = registerItem("anevrizm_sour_sardine_cooked", s -> new Item(s.food(COOKED_FISH)));
    public static final Item ANEVRIZM_EMERALD = registerItem("anevrizm_emerald", nn);

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

    public static void initialize() {}
}
