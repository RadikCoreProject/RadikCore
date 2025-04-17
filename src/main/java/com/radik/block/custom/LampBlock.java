package com.radik.block.custom;

import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.*;
import net.minecraft.world.BlockView;

public class LampBlock extends Block {
    public LampBlock() {
        super(AbstractBlock.Settings.create().strength(0.2f).sounds(BlockSoundGroup.GLASS).luminance(state -> 15));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0.875, 0, 1, 1, 1);
    }
}
