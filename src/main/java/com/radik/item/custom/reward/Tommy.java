package com.radik.item.custom.reward;

import com.radik.entity.RegisterEntities;
import com.radik.entity.projictile.bullet.BulletEntity;
import com.radik.item.RegisterItems;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.item.*;
import net.minecraft.util.math.Vec3d;

import static com.radik.Data.STORAGE;

// томпи
public class Tommy extends RangedWeaponItem {
    public static final float BULLET_SPEED = 2f;
    public static final int FIRE_RATE = 3;
    public static final int MAGAZINE_SIZE = 64;

    private int cooldown = 0;
    private int fired = 0;
    private boolean isFiring = false;

    public Tommy(Item.Settings settings) {
        super(settings.maxDamage(3).maxCount(1).component(STORAGE, 0));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, EquipmentSlot slot) {
        if (cooldown > 0) cooldown--;

        if (fired >= MAGAZINE_SIZE) {
            fired = 0;
            cooldown = 100;
        }

        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public ActionResult use(World world, @NotNull PlayerEntity user, Hand hand) {
        if (cooldown > 0) return ActionResult.FAIL;
        user.setCurrentHand(hand);
        isFiring = true;
        return ActionResult.CONSUME;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        isFiring = false;
        fired = 0;
        return false;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player) || cooldown > 0) return;
        if (isFiring && fired < MAGAZINE_SIZE) {
            if (world.getTime() % FIRE_RATE == 0) {
                fireBullet(world, player, stack);
                fired++;

                if (fired >= MAGAZINE_SIZE) {
                    cooldown = 20;
                    isFiring = false;
                }
            }
        }
    }

    private void fireBullet(World world, PlayerEntity player, ItemStack stack) {
        if (!hasAmmo(player, stack) && !player.isCreative()) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 0.5f, 1.0f);
            return;
        }

        if (!world.isClient()) {
            BulletEntity bullet = createBullet(world, player);
            Vec3d rot = player.getRotationVec(1.0f);
            bullet.setVelocity(rot.x, rot.y, rot.z, BULLET_SPEED, 0);
            player.addVelocity(-rot.x * 0.05, -rot.y * 0.05, -rot.z * 0.05);
            world.spawnEntity(bullet);
            if (!player.isCreative()) consumeAmmo(player, stack);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,
            0.8f, 1.5f + world.random.nextFloat() * 0.2f);

        player.getItemCooldownManager().set(new ItemStack(this), 1);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    private @NotNull BulletEntity createBullet(World world, @NotNull PlayerEntity player) {
        BulletEntity bullet = new BulletEntity(RegisterEntities.BULLET, world);
        bullet.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
        bullet.setDamage(3.0f);
        bullet.setCritical(false);

        bullet.setSound(SoundEvents.ITEM_CROSSBOW_SHOOT);

        return bullet;
    }

    private boolean hasAmmo(@NotNull PlayerEntity player, ItemStack weapon) {
        Integer c = weapon.get(STORAGE);
        return !(c == null || c == 0) || player.isCreative();
    }

    private void consumeAmmo(@NotNull PlayerEntity player, ItemStack weapon) {
        if (player.isCreative()) return;
        Integer c = weapon.get(STORAGE);
        if (c == null || c == 0) return;
        weapon.set(STORAGE, c - 1);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.isOf(RegisterItems.CARTRIDGE);
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }

    public boolean onClicked(@NotNull ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        Integer c = stack.get(STORAGE);
        if (c == null || c == 400 || !otherStack.isOf(RegisterItems.MAGAZINE)) return false;
        Integer sc = otherStack.get(STORAGE);
        if (sc == null) return false;

        int stackCount = otherStack.getCount();
        int p = Math.min(400 - c, stackCount * sc);
        stack.set(STORAGE, c + p - p % stackCount);
        otherStack.set(STORAGE, sc - (p / stackCount));
        cursorStackReference.set(otherStack);
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer count = stack.get(STORAGE);
        if (count == null) return;

        textConsumer.accept(Text.of((count == 0 ? "§4" : count < 100 ? "§e" : "§2") + count + " / 400"));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
