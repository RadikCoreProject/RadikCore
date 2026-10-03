package com.radik.behavior;

import com.radik.MainInit;
import com.radik.ModTags;
import com.radik.Radik;
import com.radik.item.RegisterItems;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Items;

import java.util.List;

public class RegisterBehaviors {
    @MainInit
    public static void initialize() {
        FuelBehavior.initialize();
        DispenserBlock.registerBehavior(Items.SAND, new FallingBlockDispenserBehavior(Blocks.SAND));
        DispenserBlock.registerBehavior(Items.GRAVEL, new FallingBlockDispenserBehavior(Blocks.GRAVEL));
        DispenserBlock.registerBehavior(Items.RED_SAND, new FallingBlockDispenserBehavior(Blocks.RED_SAND));
        DispenserBlock.registerBehavior(Items.BLACK_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.BLACK_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.GRAY_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.GRAY_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.YELLOW_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.YELLOW_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.BLUE_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.BLUE_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.RED_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.RED_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.GREEN_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.GREEN_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.BROWN_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.BROWN_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.LIME_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.LIME_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.WHITE_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.WHITE_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.MAGENTA_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.MAGENTA_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.PURPLE_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.PURPLE_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.CYAN_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.CYAN_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.LIGHT_BLUE_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.LIGHT_BLUE_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.LIGHT_GRAY_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.LIGHT_GRAY_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.ORANGE_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.ORANGE_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(Items.PINK_CONCRETE_POWDER, new FallingBlockDispenserBehavior(Blocks.PINK_CONCRETE_POWDER));
        DispenserBlock.registerBehavior(RegisterItems.CAPSULE, new CapsuleDispenserBehavior());
        DispenserBlock.registerBehavior(Items.WHEAT, new FoodDispenserBehaviour(List.of(EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.GOAT)));
        DispenserBlock.registerBehavior(Items.CARROT, new FoodDispenserBehaviour(List.of(EntityType.PIG, EntityType.RABBIT)));
        DispenserBlock.registerBehavior(Items.POTATO, new FoodDispenserBehaviour(EntityType.PIG));
        DispenserBlock.registerBehavior(Items.BEETROOT, new FoodDispenserBehaviour(EntityType.PIG));
        DispenserBlock.registerBehavior(Items.GOLDEN_APPLE, new FoodDispenserBehaviour(List.of(EntityType.HORSE, EntityType.DONKEY)));
        DispenserBlock.registerBehavior(Items.GOLDEN_CARROT, new FoodDispenserBehaviour(List.of(EntityType.HORSE, EntityType.DONKEY, EntityType.RABBIT)));
        DispenserBlock.registerBehavior(Items.BEETROOT_SEEDS, new FoodDispenserBehaviour(EntityType.CHICKEN));
        DispenserBlock.registerBehavior(Items.MELON_SEEDS, new FoodDispenserBehaviour(EntityType.CHICKEN));
        DispenserBlock.registerBehavior(Items.PUMPKIN_SEEDS, new FoodDispenserBehaviour(EntityType.CHICKEN));
        DispenserBlock.registerBehavior(Items.TORCHFLOWER_SEEDS, new FoodDispenserBehaviour(EntityType.CHICKEN));
        DispenserBlock.registerBehavior(Items.WHEAT_SEEDS, new FoodDispenserBehaviour(EntityType.CHICKEN));
        DispenserBlock.registerBehavior(Items.BEEF, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.PORKCHOP, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.CHICKEN, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.MUTTON, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.RABBIT, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.ROTTEN_FLESH, new FoodDispenserBehaviour(EntityType.WOLF));
        DispenserBlock.registerBehavior(Items.COD, new FoodDispenserBehaviour(List.of(EntityType.CAT, EntityType.OCELOT)));
        DispenserBlock.registerBehavior(Items.SALMON, new FoodDispenserBehaviour(List.of(EntityType.CAT, EntityType.OCELOT)));
        DispenserBlock.registerBehavior(Items.DANDELION, new FoodDispenserBehaviour(List.of(EntityType.RABBIT, EntityType.BEE)));
        DispenserBlock.registerBehavior(Items.HAY_BLOCK, new FoodDispenserBehaviour(EntityType.LLAMA));
        DispenserBlock.registerBehavior(Items.SEAGRASS, new FoodDispenserBehaviour(EntityType.TURTLE));
        DispenserBlock.registerBehavior(Items.SWEET_BERRIES, new FoodDispenserBehaviour(EntityType.FOX));
        DispenserBlock.registerBehavior(Items.GLOW_BERRIES, new FoodDispenserBehaviour(EntityType.FOX));
        DispenserBlock.registerBehavior(Items.BAMBOO, new FoodDispenserBehaviour(EntityType.PANDA));
        DispenserBlock.registerBehavior(Items.POPPY, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.BLUE_ORCHID, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.ALLIUM, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.AZURE_BLUET, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.ORANGE_TULIP, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.PINK_TULIP, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.RED_TULIP, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.WHITE_TULIP, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.OXEYE_DAISY, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.CORNFLOWER, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.LILY_OF_THE_VALLEY, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.TORCHFLOWER, new FoodDispenserBehaviour(EntityType.BEE));
        DispenserBlock.registerBehavior(Items.WARPED_FUNGUS, new FoodDispenserBehaviour(EntityType.STRIDER));
        DispenserBlock.registerBehavior(Items.CRIMSON_FUNGUS, new FoodDispenserBehaviour(EntityType.HOGLIN));
        DispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, new FoodDispenserBehaviour(EntityType.AXOLOTL));
        DispenserBlock.registerBehavior(Items.SLIME_BALL, new FoodDispenserBehaviour(EntityType.FROG));
        DispenserBlock.registerBehavior(Items.CACTUS, new FoodDispenserBehaviour(EntityType.CAMEL));

    }
}
