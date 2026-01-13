package com.radik.item.custom.projectile;

import com.radik.entity.projictile.ice_shard.IceShardEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.jetbrains.annotations.NotNull;

public class IceShard extends Item implements ProjectileItem {
    private static final float POWER = 1.8F;

    public IceShard(Item.@NotNull Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(@NotNull World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        world.playSound(
            null,
            user.getX(),
            user.getY(),
            user.getZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW,
            SoundCategory.NEUTRAL,
            0.5F,
            0.6F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!world.isClient()) {
            IceShardEntity iceShard = new IceShardEntity(world, user, itemStack, itemStack);
            iceShard.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, POWER, 1.0F);
            world.spawnEntity(iceShard);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new IceShardEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack, stack);
    }
}