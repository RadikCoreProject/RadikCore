package com.radik.block.custom.blockentity.event;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class EventBlock extends BlockWithEntity {
    public static final MapCodec<EventBlock> CODEC = createCodec(EventBlock::new);
    public static final IntProperty EVENT_TYPE = IntProperty.of("event_type", 0, 4);

    public EventBlock(@NotNull Settings settings) {
        super(settings.nonOpaque().strength(-1, 99999999).luminance(t -> t.get(EVENT_TYPE) == 0 ? 6 : 15));
        setDefaultState(getStateManager().getDefaultState().with(EVENT_TYPE, 0));
    }

    @Override
    protected MapCodec<? extends EventBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EventBlockEntity(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(EVENT_TYPE, 0);
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(EVENT_TYPE);
    }
}
