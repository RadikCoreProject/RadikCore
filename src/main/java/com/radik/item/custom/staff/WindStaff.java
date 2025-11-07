package com.radik.item.custom.staff;

import com.radik.item.custom.Materials;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.radik.Data.*;
import static com.radik.util.ScoreboardAction.changeScore;
import static com.radik.util.ScoreboardAction.enough;

// test
public class WindStaff extends Item{
    public WindStaff(@NotNull Settings settings) {
        super(settings.sword(Materials.STAFF, 4, -2.4f).component(STAFF_TYPE, 0).component(STAFF_ATTACKS, 4));
    }

    private int count = 0;

    @Override
    public ActionResult use(World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Integer component = stack.getComponents().get(STAFF_TYPE);
        if (component == null) return ActionResult.FAIL;
        int enough = component == 1 ? 150 : 50;
        String attack = component == 1 ? "wind_staff_ultra" : "wind_staff_base";

        if (!enough(enough, user)) { return ActionResult.FAIL; }
        if (manaUsesTimer(user.getName().getString(), attack)) { return ActionResult.FAIL; }

        if (!world.isClient) {
            shootWind(user, world, user.getYaw());
            changeScore("mana", -enough, user);
            stack.damage(1, user, EquipmentSlot.MAINHAND);

            if (component == 1 && ++count == 5) {
                stack.applyComponentsFrom(ComponentMap.builder().add(STAFF_TYPE, 0).build());
                count = 0;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        Integer type = stack.getComponents().get(STAFF_TYPE);
        if (type == null) return ActionResult.FAIL;

        if (player != null && !world.isClient) {
            if (!enough(800, player) || manaUsesTimer(player.getName().getString(), "wind_staff_super") || type == 1) {
                return ActionResult.FAIL;
            }

            changeScore("mana", -800, player);
            world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 4.0f, World.ExplosionSourceType.NONE);

            stack.applyComponentsFrom(ComponentMap.builder().add(STAFF_TYPE, 1).build());
            stack.damage(10, player, EquipmentSlot.MAINHAND);
            shootWindProjectiles(player, world, 8);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void postHit(@NotNull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Integer type = stack.getComponents().get(STAFF_TYPE);
        if (type == null) return;
        if (type == 0) {
            target.damage((ServerWorld) target.getWorld(), attacker.getDamageSources().magic(), 4.0f);
        } else {
            target.damage((ServerWorld) target.getWorld(), attacker.getDamageSources().magic(), 8.0f);
        }
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    private void shootWindProjectiles(PlayerEntity user, World world, int count) {
        for (int i = 0; i < count; i++) {
            if (!world.isClient) {shootWind(user, world, user.getYaw() + (i * 45)); }
        }
    }

    private void shootWind(@NotNull PlayerEntity user, World world, float yaw) {
        WindChargeEntity projectile = new WindChargeEntity(EntityType.WIND_CHARGE, world);
        projectile.setPosition(user.getX(), user.getEyeY(), user.getZ());
        projectile.setVelocity(user, user.getPitch(), yaw, 0f, 1f, 1f);
        world.spawnEntity(projectile);
    }
}