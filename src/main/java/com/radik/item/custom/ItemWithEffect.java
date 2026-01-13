package com.radik.item.custom;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemWithEffect extends Item {
    public ItemWithEffect(@NotNull Settings settings) {
        super(settings.food(createFoodComponent()).useCooldown(5));
    }

    private static FoodComponent createFoodComponent() {
        return new FoodComponent.Builder()
            .saturationModifier(0.0f)
            .alwaysEdible()
            .nutrition(0)
            .build();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 50;
    }

    @Override
    public ActionResult use(World world, @NotNull PlayerEntity user, Hand hand) {
        if (user.canConsume(true)) {
            user.setCurrentHand(hand);
            return super.use(world, user, hand);
        }

        return super.use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, @NotNull World world, LivingEntity user) {
        if (!world.isClient()) {
            if (user instanceof PlayerEntity player) {
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NAUSEA,
                    200,
                    0,
                    false,
                    true
                ));

                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                    SoundCategory.PLAYERS,
                    0.5f, 1.0f);
            }
        }

        if (user instanceof PlayerEntity player && !player.isCreative()) {
            stack.decrement(1);
        }

        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    public void usageTick(@NotNull World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world instanceof ClientWorld w && remainingUseTicks % 10 == 0) {
            Vec3d look = user.getRotationVec(1.0F).normalize();
            Vec3d eyePos = user.getEyePos();
            double f = 0.5;
            double s = -0.3;
            double v = -0.2;

            Vec3d pos = eyePos
                .add(look.multiply(f))
                .add(user.getRotationVector(1.0F, 0).multiply(s))
                .add(user.getRotationVector(0, 1.0F).multiply(v));

            w.addParticleClient(
                ParticleTypes.LARGE_SMOKE,
                pos.x,
                pos.y,
                pos.z,
                0.0, 0.0, 0.0
            );
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }
}
