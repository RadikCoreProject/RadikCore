package com.radik.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import org.jetbrains.annotations.NotNull;

import static com.radik.block.custom.BlockData.FACING2;

public class RotatableBlock extends Block {
    public RotatableBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING2, 0));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING2);
    }
}
