package com.radik.fluid;

import com.radik.Radik;
import com.radik.fluid.elements.ExplosiveGas;
import com.radik.fluid.elements.Hydrogen;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static com.radik.Radik.MOD_ID;

public class RegisterFluids {
    public static final FlowableFluid STILL_HYDROGEN = new Hydrogen.Still();
    public static final FlowableFluid FLOWING_HYDROGEN = new Hydrogen.Flowing();
    public static Block HYDROGEN_BLOCK;
    public static Item HYDROGEN_BUCKET;

    private static final Function<AbstractBlock.Settings, Block> hydrogen = properties -> new ExplosiveGas(RegisterFluids.STILL_HYDROGEN, properties.replaceable().mapColor(MapColor.WATER_BLUE).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY).strength(0.0f).suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false));
    private static final Function<Item.Settings, Item> hydrogen_bucket = settings -> new BucketItem(STILL_HYDROGEN, settings.maxCount(1));

    public static void register() {
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, "hydrogen"), STILL_HYDROGEN);
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, "flowing_hydrogen"), FLOWING_HYDROGEN);

        Block toRegister = hydrogen.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Radik.MOD_ID, "hydrogen"))));
        HYDROGEN_BLOCK = Registry.register(
                Registries.BLOCK,
                Identifier.of("radik", "hydrogen"),
                toRegister
        );

        FlammableBlockRegistry.getDefaultInstance().add(HYDROGEN_BLOCK, 5, 5);

        // Регистрация ведра
        HYDROGEN_BUCKET = Registry.register(Registries.ITEM, Identifier.of(Radik.MOD_ID, "hydrogen_bucket"),
                hydrogen_bucket.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Radik.MOD_ID, "hydrogen_bucket")))));
    }
}
