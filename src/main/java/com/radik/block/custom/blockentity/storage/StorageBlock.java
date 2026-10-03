package com.radik.block.custom.blockentity.storage;

import com.mojang.serialization.MapCodec;
import com.radik.Data;
import com.radik.connecting.game.StorageBlockData;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageBlock extends BlockWithEntity {
    public static final MapCodec<StorageBlock> CODEC = createCodec(StorageBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public StorageBlock(@NotNull Settings settings) {
        super(settings.mapColor(MapColor.IRON_GRAY).nonOpaque().strength(3, 99999999));
    }

    @Override
    protected MapCodec<? extends StorageBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StorageBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            StorageBlockEntity block = (StorageBlockEntity) world.getBlockEntity(pos);
            if (block != null && placer != null) {
                block.owner = placer.getName().getString();
                Integer l = itemStack.get(Data.STORAGE_LEVEL);
                if (l != null) block.storage = StorageBlockData.storageById(l);
                block.markDirty();
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);

        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof StorageBlockEntity storageEntity) {
            int count = storageEntity.filling;
            ItemStack itemType = storageEntity.type;

            if (itemType != null && !itemType.isEmpty()) {
                while (count > 0) {
                    int dropAmount = Math.min(count, itemType.getMaxCount());
                    drops.add(itemType.copyWithCount(dropAmount));
                    count -= dropAmount;
                }
            }
        }
        return drops;
    }
}
