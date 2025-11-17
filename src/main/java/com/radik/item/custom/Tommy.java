package com.radik.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Tommy extends RangedWeaponItem {
    public static final float TICKS_FOR_CHARGE = 5f;
    public static final int RANGE = 15;

    public Tommy(Item.Settings settings) {
        super(settings);
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = playerEntity.getProjectileType(stack);

            if (!itemStack.isEmpty()) {
                int useTicks = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float pullProgress = getPullProgress(useTicks);

                if (pullProgress > 0.1F) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);

                    if (world instanceof ServerWorld serverWorld) {
                        if (!list.isEmpty()) {
                            shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, pullProgress * 3.0F, 1.0F, pullProgress == 1.0F, null);
                        }
                    }

                    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

                    return true;
                }
            }
        }

        return false;
    }

    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
    }

    public static float getPullProgress(int useTicks) {
        float pullProgress = (float)useTicks / TICKS_FOR_CHARGE;
        pullProgress = (pullProgress * pullProgress + pullProgress * 2.0F) / 3.0F;
        if (pullProgress > 1.0F) {
            pullProgress = 1.0F;
        }

        return pullProgress;
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean doesntHaveProjectile = user.getProjectileType(itemStack).isEmpty();

        if (!user.isInCreativeMode() && doesntHaveProjectile) {
            return ActionResult.FAIL;
        } else {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }
    }

    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public int getRange() {
        return RANGE;
    }
}
