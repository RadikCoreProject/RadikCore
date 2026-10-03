package com.radik.block.custom.blockentity.event;

import com.mojang.serialization.MapCodec;
import com.radik.block.custom.blockentity.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EventBlock extends BlockWithEntity {
    public static final MapCodec<EventBlock> CODEC = createCodec(EventBlock::new);
    public static final IntProperty EVENT_TYPE = IntProperty.of("event_type", 0, 4);

    public EventBlock(Settings settings) {
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) EventBlockServerBridge.getHandler().onUse(world, player, pos);
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) EventBlockServerBridge.getHandler().onPlaced(world, pos, placer, itemStack);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(EVENT_TYPE, 0);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EVENT_TYPE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world instanceof ServerWorld serverWorld) {
            if (type == BlockEntities.EVENT_BLOCK_ENTITY) {
                return (world1, pos, state1, blockEntity) ->
                    EventBlockServerBridge.getHandler().tickEventBlockEntity(
                        serverWorld, pos, state1, (EventBlockEntity) blockEntity
                    );
            }
        }
        return null;
    }

    public interface EventBlockServerHandler {
        void onUse(World world, PlayerEntity player, BlockPos pos);
        void onPlaced(World world, BlockPos pos, @Nullable LivingEntity placer, ItemStack itemStack);
        void tickEventBlockEntity(ServerWorld world, BlockPos pos, BlockState state, EventBlockEntity blockEntity);
    }

    static class NoopEventBlockServerHandler implements EventBlockServerHandler {
        @Override
        public void onUse(World world, PlayerEntity player, BlockPos pos) {}

        @Override
        public void onPlaced(World world, BlockPos pos, LivingEntity placer, ItemStack itemStack) {}

        @Override
        public void tickEventBlockEntity(ServerWorld world, BlockPos pos, BlockState state, EventBlockEntity blockEntity) {}
    }

    public static class EventBlockServerBridge {
        private static EventBlockServerHandler handler = new NoopEventBlockServerHandler();

        public static void setHandler(EventBlockServerHandler newHandler) {
            handler = newHandler;
        }

        public static EventBlockServerHandler getHandler() {
            return handler;
        }
    }
}
