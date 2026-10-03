package com.radik.item.custom.weapon;

import com.radik.connecting.event.ChallengeEvent;
import com.radik.entity.RegisterEntities;
import com.radik.entity.projictile.water_drop.WaterDropEntity;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.radik.Data.EVENT_TYPE;
import static com.radik.Data.STORAGE;

public class WaterPistol extends RangedWeaponItem {
    public static final float DROP_SPEED = 2f;
    public static final int FIRE_RATE = 3;
    private boolean isFiring = false;

    public WaterPistol(Settings settings) {
        super(settings.maxCount(1).component(STORAGE, 0).component(EVENT_TYPE, ChallengeEvent.SUMMER));
    }

    @Override
    public ActionResult use(World world, @NotNull PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        isFiring = true;
        return ActionResult.CONSUME;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        isFiring = false;
        return false;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;
        if (isFiring) {
            if (world.getTime() % FIRE_RATE == 0)
                fireBullet(world, player, stack);
        }
    }

    private void fireBullet(World world, PlayerEntity player, ItemStack stack) {
        if (!hasAmmo(player, stack) && !player.isCreative()) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 0.5f, 1.0f);
            return;
        }

        if (!world.isClient()) {
            WaterDropEntity entity = createBullet(world, player);
            Vec3d rot = player.getRotationVec(1.0f);
            entity.setVelocity(rot.x, rot.y, rot.z, DROP_SPEED, 0);
            player.addVelocity(-rot.x * 0.05, -rot.y * 0.05, -rot.z * 0.05);
            world.spawnEntity(entity);
            if (!player.isCreative()) consumeAmmo(player, stack);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,
            0.8f, 1.5f + world.random.nextFloat() * 0.2f);

        player.getItemCooldownManager().set(new ItemStack(this), 1);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    private @NotNull WaterDropEntity createBullet(World world, @NotNull PlayerEntity player) {
        WaterDropEntity entity = new WaterDropEntity(RegisterEntities.WATER_DROP, world);
        entity.setPos(player.getX(), player.getEyeY() - 0.5F, player.getZ());
        entity.setDamage(0.0f);
        entity.setCritical(false);

        entity.setSound(SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE);

        return entity;
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
        return stack -> stack.isOf(Items.AIR);
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }

    public boolean onClicked(@NotNull ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (otherStack.isOf(Items.WATER_BUCKET)) {
            stack.set(STORAGE, 400);
            otherStack.withItem(Items.BUCKET);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Integer count = stack.get(STORAGE);
        if (count == null) return;

        textConsumer.accept(Text.of((count == 0 ? "§4" : count < 100 ? "§e" : "§2") + count + " / 400"));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
