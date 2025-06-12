package com.radik.logic;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.radik.Data.getDimension;
import static com.radik.logic.RadikCoreDEFEND.Logger;

public class OnEntityUse {
    public static void initialize() {
        UseEntityCallback.EVENT.register(OnEntityUse::use);
    }

    private static ActionResult use(PlayerEntity playerEntity, World world, Hand hand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (entity instanceof ItemFrameEntity) {
            Logger(playerEntity.getName().getString(), entity.getName().getString(), getDimension(world), (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(), "use");
        }
        return ActionResult.PASS;
    }

}
