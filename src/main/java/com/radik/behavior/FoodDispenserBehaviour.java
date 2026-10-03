package com.radik.behavior;

import com.radik.ModTags;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoodDispenserBehaviour extends ItemDispenserBehavior {
    private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();
    private final List<EntityType<? extends AnimalEntity>> entities;

    public FoodDispenserBehaviour(List<EntityType<? extends AnimalEntity>> entities) {
        this.entities = entities;
    }

    public FoodDispenserBehaviour(EntityType<? extends AnimalEntity> entities) {
        this.entities = List.of(entities);
    }

    public ItemStack dispenseSilently(BlockPointer pointer, @NotNull ItemStack stack) {
        Vec3d pos = pointer.centerPos();
        ServerWorld world = pointer.world();
        AnimalEntity animal = (AnimalEntity) world.getClosestEntity(
            ModTags.Entities.BREEDABLE,
            TargetPredicate.DEFAULT,
            null,
            pos.x, pos.y, pos.z,
            new Box(pos, pos).expand(2.0)
        );

        if (animal == null || animal.squaredDistanceTo(pos) > 1.5) return fallbackBehavior.dispense(pointer, stack);
        if (!animal.isBreedingItem(stack)) return fallbackBehavior.dispense(pointer, stack);

        if (animal.getBreedingAge() == 0 && animal.canEat()) {
            animal.setLoveTicks(600);
            stack.decrement(1);
            world.sendEntityStatus(animal, (byte) 18);
            world.playSound(null, animal.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return stack;
        }

        if (animal.isBaby()) {
            animal.growUp(animal.getBreedingAge() + 24000, true);
            stack.decrement(1);
            world.sendEntityStatus(animal, (byte) 18);
            world.playSound(null, animal.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return stack;
        }

        return fallbackBehavior.dispense(pointer, stack);
    }
}