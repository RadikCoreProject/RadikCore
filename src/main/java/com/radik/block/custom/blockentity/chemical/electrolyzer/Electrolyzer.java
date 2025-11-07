//package com.radik.block.custom.blockentity.chemical.electrolyzer;
//
//import com.mojang.serialization.MapCodec;
//import com.radik.Stats;
//import com.radik.block.custom.blockentity.BlockEntities;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.BlockWithEntity;
//import net.minecraft.block.entity.*;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemPlacementContext;
//import net.minecraft.item.ItemStack;
//import net.minecraft.screen.NamedScreenHandlerFactory;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.state.StateManager;
//import net.minecraft.util.*;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//
//import static com.radik.Data.CAPSULE_FLUID;
//import static com.radik.block.custom.BlockData.WATER_LEVEL;
//
//public class Electrolyzer extends BlockWithEntity {
//    public static final MapCodec<Electrolyzer> CODEC = createCodec(Electrolyzer::new);
//
//    public Electrolyzer(Settings settings) {
//        super(settings.strength(3, 3));
//    }
//
//    @Override
//    protected MapCodec<? extends Electrolyzer> getCodec() {
//        return CODEC;
//    }
//
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new FurnaceBlockEntity(pos, state);
//    }
//
//    @Override
//    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
//        if (!world.isClient) {
//            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
//            Integer fluid = stack.get(CAPSULE_FLUID);
//            Integer level = state.get(WATER_LEVEL);
//
//            if (fluid == null || level == null) {
//                openScreen(world, pos, player);
//            } else if (fluid == 1 && level < 24) {
//                state.with(WATER_LEVEL, level + 1);
//            } else {
//                openScreen(world, pos, player);
//            }
//        }
//
//        return ActionResult.SUCCESS;
//    }
//
//    private void openScreen(World world, BlockPos pos, PlayerEntity player) {
//        BlockEntity blockEntity = world.getBlockEntity(pos);
//        if (blockEntity instanceof FurnaceBlockEntity) {
//            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
//            player.incrementStat(Stats.INTERACT_WITH_ELECTROLYZER);
//        }
//    }
//
//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        return this.getDefaultState().with(WATER_LEVEL, 0);
//    }
//
//    @Override
//    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
//        ItemScatterer.onStateReplaced(state, world, pos);
//    }
//
//    @Override
//    protected boolean hasComparatorOutput(BlockState state) {
//        return true;
//    }
//
//    @Override
//    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
//        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
//    }
//
//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(WATER_LEVEL);
//    }
//
//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        return validateTicker(world, type, BlockEntities.ELECTROLYZER_ENTITY);
//    }
//
//    @Nullable
//    protected static <T extends BlockEntity> BlockEntityTicker<T> validateTicker(
//            World world, BlockEntityType<T> givenType, BlockEntityType<? extends ElectrolyzerEntity> expectedType
//    ) {
//        return world instanceof ServerWorld serverWorld
//                ? validateTicker(givenType, expectedType, (worldx, pos, state, blockEntity) -> ElectrolyzerEntity.tick(serverWorld, pos, state, blockEntity))
//                : null;
//    }
//}
