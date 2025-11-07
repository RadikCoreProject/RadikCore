package com.radik.fluid;

import com.radik.fluid.elements.HeliumBlock;
import com.radik.fluid.elements.HeliumFluid;
import com.radik.fluid.elements.HydrogenBlock;
import com.radik.fluid.elements.HydrogenFluid;
import com.radik.registration.IRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

import static com.radik.Data.MOD_ID;

public class RegisterFluids implements IRegistry {
    public static final HydrogenFluid.Still STILL_HYDROGEN = new HydrogenFluid.Still();
    public static final HydrogenFluid.Flowing FLOWING_HYDROGEN = new HydrogenFluid.Flowing();
    public static Block HYDROGEN_BLOCK;
    public static final HeliumFluid.Still STILL_HELIUM = new HeliumFluid.Still();
    public static final HeliumFluid.Flowing FLOWING_HELIUM = new HeliumFluid.Flowing();
    public static Block HELIUM_BLOCK;

    private static final Function<AbstractBlock.Settings, Block> hydrogen = properties -> new HydrogenBlock(RegisterFluids.STILL_HYDROGEN, properties.replaceable().mapColor(MapColor.WATER_BLUE).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY).strength(0.0f).suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false), 0.09f);
    private static final Function<AbstractBlock.Settings, Block> helium = properties -> new HeliumBlock(RegisterFluids.STILL_HELIUM, properties.replaceable().mapColor(MapColor.WATER_BLUE).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid().sounds(BlockSoundGroup.INTENTIONALLY_EMPTY).strength(0.0f).suffocates((state, world, pos) -> false).blockVision((state, world, pos) -> false), 0.178f);

    public static void initialize() {
        HYDROGEN_BLOCK = gen(hydrogen, "hydrogen", STILL_HYDROGEN, FLOWING_HYDROGEN);
        HELIUM_BLOCK = gen(helium, "helium", STILL_HELIUM, FLOWING_HELIUM);

        FlammableBlockRegistry.getDefaultInstance().add(HYDROGEN_BLOCK, 5, 5);
    }

    public static void swapFluids(World world, BlockPos pos1, BlockPos pos2) {
        BlockState block1 = world.getBlockState(pos1);
        BlockState block2 = world.getBlockState(pos2);
        world.setBlockState(pos2, block1, Block.NOTIFY_ALL);
        world.setBlockState(pos1, block2, Block.NOTIFY_ALL);
    }

    private static Block gen(Function<AbstractBlock.Settings, Block> function, String name, FlowableFluid still, FlowableFluid flowing) {
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, name), still);
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, "flowing_" + name), flowing);

        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, name))));
        return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, name), toRegister);
    }
}
