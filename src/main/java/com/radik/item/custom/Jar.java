package com.radik.item.custom;

import com.radik.item.RegisterItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.radik.Data.JAR;

public class Jar extends Item {
    private static Item getItem(int type) {
        return switch (type) {
            case 2 -> RegisterItems.CIGARETTE;
            case 1 -> RegisterItems.CUCUMBER;
            default -> Items.AIR;
        };
    }

    private static Item getJar(int type) {
        return switch (type) {
            case 0, 1 -> RegisterItems.JAR;
            default -> Items.AIR;
        };
    }

    public Jar(@NotNull Settings settings, int type) {
        super(settings.component(JAR, type));
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        super.use(world, user, hand);
        ItemStack stack = user.getStackInHand(hand);
        Integer type = stack.get(JAR);
        if (type == null || type == 0) return ActionResult.FAIL;

        double x = user.getX(), y = user.getY(), z = user.getZ();
        ItemEntity output = new ItemEntity(world, x, y, z, new ItemStack(getItem(type), 8));
        ItemEntity jar = new ItemEntity(world, x, y, z, new ItemStack(getJar(type)));
        stack.decrementUnlessCreative(1, user);
        world.spawnEntity(output);
        world.spawnEntity(jar);
        return ActionResult.SUCCESS;
    }
}
