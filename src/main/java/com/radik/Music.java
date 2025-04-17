package com.radik;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Music {
    public static final SoundEvent CRYSTAL_PEAK = registerSoundEvent("crystal_peak");
    public static final RegistryKey<JukeboxSong> CRYSTAL_PEAK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "crystal_peak"));
    public static final SoundEvent PURE_VESSEL = registerSoundEvent("pure_vessel");
    public static final RegistryKey<JukeboxSong> PURE_VESSEL_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "pure_vessel"));
    public static final SoundEvent SAVIOR_OF_THE_WAKED_WORLD = registerSoundEvent("savior_of_the_waking_world");
    public static final RegistryKey<JukeboxSong> SAVIOR_OF_THE_WAKED_WORLD_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "savior_of_the_waking_world"));
    public static final SoundEvent DIRTMOUTH = registerSoundEvent("dirtmouth");
    public static final RegistryKey<JukeboxSong> DIRTMOUTH_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "dirtmouth"));
    public static final SoundEvent HOMESTUCK = registerSoundEvent("homestuck");
    public static final RegistryKey<JukeboxSong> HOMESTUCK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "homestuck"));
    public static final SoundEvent BONEBOTTOM = registerSoundEvent("bonebottom");
    public static final RegistryKey<JukeboxSong> BONEBOTTOM_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "bonebottom"));
    public static final SoundEvent NOSK = registerSoundEvent("nosk");
    public static final RegistryKey<JukeboxSong> NOSK_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "nosk"));
    public static final SoundEvent THE_LAST_HUMAN = registerSoundEvent("the_last_human");
    public static final RegistryKey<JukeboxSong> THE_LAST_HUMAN_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "the_last_human"));
    public static final SoundEvent NEUTRON_STARS = registerSoundEvent("neutron_stars");
    public static final RegistryKey<JukeboxSong> NEUTRON_STARS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "neutron_stars"));
    public static final SoundEvent JINGLE_BELLS = registerSoundEvent("jingle_bells");
    public static final RegistryKey<JukeboxSong> JINGLE_BELLS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "jingle_bells"));
    public static final SoundEvent MERRY_CHRISTMAS = registerSoundEvent("merry_christmas");
    public static final RegistryKey<JukeboxSong> MERRY_CHRISTMAS_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "merry_christmas"));
    public static final SoundEvent PENIS_BOLSHOY = registerSoundEvent("penis_bolshoy");
    public static final RegistryKey<JukeboxSong> PENIS_BOLSHOY_KEY = RegistryKey.of(RegistryKeys.JUKEBOX_SONG, Identifier.of(Radik.MOD_ID, "penis_bolshoy"));

    public static final SoundEvent WARDEN = registerSoundEvent("warden");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Radik.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    @RadikCore
    public static void registerSounds() {
        Radik.LOGGER.info("sounds registered");
    }
}
