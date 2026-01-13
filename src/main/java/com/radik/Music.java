package com.radik;

import com.radik.registration.IRegistry;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static com.radik.Data.LOGGER;
import static com.radik.Data.MOD_ID;

public final class Music implements IRegistry {
    public static final SoundEvent CRYSTAL_PEAK = registerSoundEvent("crystal_peak");
    public static final RegistryKey<JukeboxSong> CRYSTAL_PEAK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "crystal_peak"));
    public static final SoundEvent PURE_VESSEL = registerSoundEvent("pure_vessel");
    public static final RegistryKey<JukeboxSong> PURE_VESSEL_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "pure_vessel"));
    public static final SoundEvent SAVIOR_OF_THE_WAKED_WORLD = registerSoundEvent("savior_of_the_waking_world");
    public static final RegistryKey<JukeboxSong> SAVIOR_OF_THE_WAKED_WORLD_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "savior_of_the_waking_world"));
    public static final SoundEvent DIRTMOUTH = registerSoundEvent("dirtmouth");
    public static final RegistryKey<JukeboxSong> DIRTMOUTH_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "dirtmouth"));
    public static final SoundEvent HOMESTUCK = registerSoundEvent("homestuck");
    public static final RegistryKey<JukeboxSong> HOMESTUCK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "homestuck"));
    public static final SoundEvent BONEBOTTOM = registerSoundEvent("bonebottom");
    public static final RegistryKey<JukeboxSong> BONEBOTTOM_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "bonebottom"));
    public static final SoundEvent NOSK = registerSoundEvent("nosk");
    public static final RegistryKey<JukeboxSong> NOSK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "nosk"));
    public static final SoundEvent THE_LAST_HUMAN = registerSoundEvent("the_last_human");
    public static final RegistryKey<JukeboxSong> THE_LAST_HUMAN_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "the_last_human"));
    public static final SoundEvent NEUTRON_STARS = registerSoundEvent("neutron_stars");
    public static final RegistryKey<JukeboxSong> NEUTRON_STARS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "neutron_stars"));
    public static final SoundEvent JINGLE_BELLS = registerSoundEvent("jingle_bells");
    public static final RegistryKey<JukeboxSong> JINGLE_BELLS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "jingle_bells"));
    public static final SoundEvent MERRY_CHRISTMAS = registerSoundEvent("merry_christmas");
    public static final RegistryKey<JukeboxSong> MERRY_CHRISTMAS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "merry_christmas"));
    public static final SoundEvent PENIS_BOLSHOY = registerSoundEvent("penis_bolshoy");
    public static final RegistryKey<JukeboxSong> PENIS_BOLSHOY_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "penis_bolshoy"));
    public static final SoundEvent DEBRIS = registerSoundEvent("debris");
    public static final RegistryKey<JukeboxSong> DEBRIS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "debris"));
    public static final SoundEvent BOLSHOY_KUSH = registerSoundEvent("bolshoy_kush");
    public static final RegistryKey<JukeboxSong> BOLSHOY_KUSH_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "bolshoy_kush"));
    public static final SoundEvent JORDANAIRES = registerSoundEvent("jordanaires");
    public static final RegistryKey<JukeboxSong> JORDANAIRES_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "jordanaires"));
    public static final SoundEvent NEST = registerSoundEvent("nest");
    public static final RegistryKey<JukeboxSong> NEST_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "nest"));
    public static final SoundEvent NETHER = registerSoundEvent("nether");
    public static final RegistryKey<JukeboxSong> NETHER_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "nether"));
    public static final SoundEvent PRESIDENT_IS_DEAD = registerSoundEvent("president_is_dead");
    public static final RegistryKey<JukeboxSong> PRESIDENT_IS_DEAD_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(MOD_ID, "president_is_dead"));

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {
        LOGGER.info("sounds registered");
    }
}
