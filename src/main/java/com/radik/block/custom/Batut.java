package com.radik.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Batut extends SlimeBlock {
    public Batut(Settings settings, byte type) {
        super(settings);
        TYPE = type;
    }
    private final byte TYPE;

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (this.TYPE) {
            case 0: return VoxelShapes.cuboid(-0.5, 0.5, -0.5, 1.5, 0.625, 1.5);
            default: return VoxelShapes.empty();
        }
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounce(entity);
        }

    }

    private void bounce(@NotNull Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0D && Math.abs(vec3d.y) < 200.0D) {
            double d = entity instanceof LivingEntity ? 2.0D : 0.8D;
            entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
        }
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        double d = Math.abs(entity.getVelocity().y);
        if (d < 0.1 && !entity.bypassesSteppingEffects()) {
            double e = 0.4 + d * 0.2;
            entity.setVelocity(entity.getVelocity().multiply(e, (double)1.0F, e));
        }

        super.onSteppedOn(world, pos, state, entity);
    }
}
