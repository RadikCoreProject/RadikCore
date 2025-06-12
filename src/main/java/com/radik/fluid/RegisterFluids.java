package com.radik.fluid;

import com.radik.fluid.elements.ExplosiveGas;
import com.radik.fluid.elements.Hydrogen;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.radik.Radik.MOD_ID;

public class RegisterFluids {
    public static final FlowableFluid STILL_HYDROGEN = new Hydrogen.Still();
    public static final FlowableFluid FLOWING_HYDROGEN = new Hydrogen.Flowing();
    public static Block HYDROGEN_BLOCK;
    public static Item HYDROGEN_BUCKET;

    public static void register() {
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, "hydrogen"), STILL_HYDROGEN);
        Registry.register(Registries.FLUID, Identifier.of(MOD_ID, "flowing_hydrogen"), FLOWING_HYDROGEN);

        HYDROGEN_BLOCK = Registry.register(
                Registries.BLOCK,
                Identifier.of("radik", "hydrogen"),
                new ExplosiveGas(
                        RegisterFluids.STILL_HYDROGEN,
                        AbstractBlock.Settings.copy(Blocks.WATER)
                                .noCollision()
                                .replaceable()
                                .strength(0.0f)
                                .dropsNothing()
                                .suffocates((state, world, pos) -> false)
                                .blockVision((state, world, pos) -> false)
                )
        );

        FlammableBlockRegistry.getDefaultInstance().add(HYDROGEN_BLOCK, 5, 5);

        // Регистрация ведра
        HYDROGEN_BUCKET = Registry.register(
                Registries.ITEM,
                Identifier.of(MOD_ID, "hydrogen_bucket"),
                new BucketItem(STILL_HYDROGEN, new Item.Settings().maxCount(1))
        );
    }
}
