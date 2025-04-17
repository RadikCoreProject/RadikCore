package com.radik.block.custom;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;

public class Bicycle extends PillarBlock {
    public Bicycle() {
        super(AbstractBlock.Settings.create().sounds(BlockSoundGroup.GLASS).strength(3, 9999999).noBlockBreakParticles());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.5, 0, -0.6125, 0.61125, 1.0235, 1.6125);
    }
}
