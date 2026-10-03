package com.radik.item.custom.staff;

import com.radik.item.ToolMaterials;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import static com.radik.Data.ULTA;

public abstract class TESTStaff extends Item {
    public TESTStaff(@NotNull Settings settings) {
        super(settings.sword(ToolMaterials.STAFF, 4, -2.4f).component(ULTA, 0).component(STAFF_ATTACKS, 4));
    }

    @Override
    public ActionResult use(World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient()) {
            if (!base(user, world, stack)) return ActionResult.FAIL;
            stack.damage(1, user, EquipmentSlot.MAINHAND);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        Integer type = stack.getComponents().get(ULTA);
        if (type == null) return ActionResult.FAIL;

        if (player != null && !world.isClient()) {
            world.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 4.0f, World.ExplosionSourceType.NONE);
            if (ulta(player, world, stack)) {
                stack.set(ULTA, 1);
                stack.damage(10, player, EquipmentSlot.MAINHAND);
            } else if (base(player, world, stack)) {
                stack.damage(1, player, EquipmentSlot.MAINHAND);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void postHit(@NotNull ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Integer type = stack.getComponents().get(ULTA);
        if (type == null) return;
        if (type == 0) target.damage((ServerWorld) target.getEntityWorld(), attacker.getDamageSources().magic(), 4.0f);
        else target.damage((ServerWorld) target.getEntityWorld(), attacker.getDamageSources().magic(), 8.0f);
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    protected abstract boolean ulta(PlayerEntity user, World world, ItemStack stack);
    protected abstract boolean base(@NotNull PlayerEntity user, World world, ItemStack stack);
}
