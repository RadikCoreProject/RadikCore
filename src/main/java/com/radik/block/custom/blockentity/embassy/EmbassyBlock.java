package com.radik.block.custom.blockentity.embassy;

import com.mojang.serialization.MapCodec;
import com.radik.block.custom.blockentity.BlockEntities;
import com.radik.connecting.game.EmbassyData;
import com.radik.packets.EmbassyAction;
import com.radik.packets.payload.EmbassyPayload;
import com.radik.util.Duplet;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class EmbassyBlock extends BlockWithEntity {
    public static final MapCodec<EmbassyBlock> CODEC = createCodec(EmbassyBlock::new);
    public static final IntProperty ACTIVE = IntProperty.of("embassy_type", 0, 2);

    public EmbassyBlock(@NotNull Settings settings) {
        super(settings.nonOpaque().strength(-1, 99999999).luminance(t -> t.get(ACTIVE) * 5 + 5));
        setDefaultState(getStateManager().getDefaultState().with(ACTIVE, 0));
    }

    @Override
    protected MapCodec<? extends EmbassyBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EmbassyBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) openBasedScreen(player, pos, (ServerWorld) world, state);
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            EmbassyBlockEntity block = (EmbassyBlockEntity) world.getBlockEntity(pos);
            if (block != null && placer != null) {
                block.owner = placer.getName().getString();
                block.markDirty();
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
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
            (BlockEntityType<? extends EmbassyBlockEntity>) BlockEntities.EMBASSY_BLOCK_ENTITY,
            (worldx, pos, state, blockEntity) ->
                EmbassyBlockEntity.tick(serverWorld, pos, state, blockEntity))
            : null;
    }

    public static void openBasedScreen(@NotNull PlayerEntity player, BlockPos pos, @NotNull ServerWorld world, BlockState state) {
        EmbassyBlockEntity be = (EmbassyBlockEntity) world.getBlockEntity(pos);
        String name = player.getName().getString();
        if (be == null) return;
        player.openHandledScreen(new EmbassyScreenFactory(be.getData(state, name), Text.translatable("block.radik.embassy_block")));
    }
}
