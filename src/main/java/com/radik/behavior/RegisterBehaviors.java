package com.radik.behavior;

import com.radik.Radik;
import com.radik.item.RegisterItems;
import com.radik.registration.IRegistry;
import net.minecraft.block.*;
import net.minecraft.item.Items;

public class RegisterBehaviors implements IRegistry {
    public static void initialize() {
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

        Radik.LOGGER.info("Behaviours Registered");
    }
}
