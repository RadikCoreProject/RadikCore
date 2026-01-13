package com.radik.block.custom.winter;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.radik.Data.command;

public class Freezer extends Block {
    public Freezer(AbstractBlock.@NotNull Settings settings) {
        super(settings.luminance((t) -> 13).strength(-1, 330000000).dropsNothing().ticksRandomly().nonOpaque());
    }

    @Override
    public void onPlaced(@NotNull World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            String[] poses = new String[]{
                (pos.getX() + 10) + " " + pos.getY() + " " + (pos.getZ() + 10),
                (pos.getX() + 10) + " " + pos.getY() + " " + (pos.getZ() - 10),
                (pos.getX() - 10) + " " + pos.getY() + " " + (pos.getZ() + 10),
                (pos.getX() - 10) + " " + pos.getY() + " " + (pos.getZ() - 10)
            };
            for (String i : poses) {
                command(String.format("execute positioned %s run fillbiome ~-10 ~-20 ~-10 ~10 ~20 ~10 minecraft:snowy_taiga", i));
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int c = 30;
        double speed = 0.4;

        for (int i = 0; i < c; i++) {
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = random.nextDouble() * Math.PI;

            double vx = speed * Math.sin(phi) * Math.cos(theta);
            double vy = speed * Math.sin(phi) * Math.sin(theta);
            double vz = speed * Math.cos(phi);

            world.addParticleClient(
                ParticleTypes.SNOWFLAKE,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                vx, vy, vz
            );
        }
    }
}
