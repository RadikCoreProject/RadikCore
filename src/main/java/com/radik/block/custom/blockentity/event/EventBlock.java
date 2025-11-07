package com.radik.block.custom.blockentity.event;

import com.mojang.serialization.MapCodec;
import com.radik.block.custom.blockentity.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventBlock extends BlockWithEntity {
    public static final MapCodec<EventBlock> CODEC = createCodec(EventBlock::new);
    public static final IntProperty EVENT_TYPE = IntProperty.of("event_type", 0, 1);

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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(world, type);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> validateTicker(World world, BlockEntityType<T> givenType) {
        return world instanceof ServerWorld serverWorld
                ? validateTicker(
                    givenType,
            (BlockEntityType<? extends EventBlockEntity>) BlockEntities.EVENT_BLOCK_ENTITY,
            (worldx, pos, state, blockEntity) ->
                EventBlockEntity.tick(serverWorld, pos, state, blockEntity))
                : null;
    }
}
