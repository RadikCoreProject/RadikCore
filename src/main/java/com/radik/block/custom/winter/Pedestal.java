package com.radik.block.custom.winter;

import com.radik.block.custom.blockentity.event.EventBlockEntity;
import com.radik.connecting.event.ChallengeEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.radik.Data.command;
import static com.radik.block.custom.data.BlockData.*;

public class Pedestal extends Block {
    public static final EnumProperty<ChallengeEvent> PEDESTAL = EnumProperty.of("pedestal", ChallengeEvent.class);

    public Pedestal(AbstractBlock.@NotNull Settings settings) {
        super(settings.luminance((t) -> 13).strength(-1, 330000000).dropsNothing().ticksRandomly().nonOpaque());
        setDefaultState(getDefaultState().with(PEDESTAL, ChallengeEvent.NONE));
    }

    @Override
    public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            ChallengeEvent name = state.get(PEDESTAL);
            if (name == null || name == ChallengeEvent.NONE) return;

            String[] poses = new String[]{
                (pos.getX() + 10) + " " + pos.getY() + " " + (pos.getZ() + 10),
                (pos.getX() + 10) + " " + pos.getY() + " " + (pos.getZ() - 10),
                (pos.getX() - 10) + " " + pos.getY() + " " + (pos.getZ() + 10),
                (pos.getX() - 10) + " " + pos.getY() + " " + (pos.getZ() - 10)
            };
            for (String i : poses) {
                command(String.format("execute positioned %s run fillbiome ~-10 ~-20 ~-10 ~10 ~20 ~10 %s", i, name.id()));
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        ChallengeEvent type = state.get(PEDESTAL);
        if (type == null || type == ChallengeEvent.NONE) return;
        int c = 30;
        double speed = 0.4;

        for (int i = 0; i < c; i++) {
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = random.nextDouble() * Math.PI;

            double vx = speed * Math.sin(phi) * Math.cos(theta);
            double vy = speed * Math.sin(phi) * Math.sin(theta);
            double vz = speed * Math.cos(phi);

            world.addParticleClient(
                type.particle(),
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                vx, vy, vz
            );
        }
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        builder.add(PEDESTAL);
    }

    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(PEDESTAL, EventBlockEntity.getEventType());
    }
}
