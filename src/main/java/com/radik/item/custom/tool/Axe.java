package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.property.base.EventProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static com.radik.Data.*;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;

public class Axe extends AxeItem implements Tools {
    public Axe(ToolMaterial material, float attackDamage, float attackSpeed, @NonNull Settings settings) {
        super(material, attackDamage, attackSpeed, settings.component(BOOL, false));
    }

    @Override
    public void postDamageEntity(@NotNull ItemStack stack, @NonNull LivingEntity target, LivingEntity attacker) {
        ServerWorld world = (ServerWorld) target.getEntityWorld();
        BlockPos pos = target.getBlockPos().up();

        if (Tools.activedPower(stack)) {
            switch (stack.get(EVENT_TYPE)) {
                case HALLOWEEN -> {
                    if (Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.AXE_STUN_CHANCE) + 1) == 1) {
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1, true, false));
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 255, true, false));
                        Radik.sendEventToPlayers(0, pos, 0, world);
                    }
                }
                case null, default -> {}
            }
        }
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!(miner instanceof ServerPlayerEntity player)) return super.postMine(stack, world, state, pos, miner);
        boolean result = super.postMine(stack, world, state, pos, miner);
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        Block block = state.getBlock();
        if (event == null || bool == null) return result;

        if (!world.isClient() && Tools.activedPower(stack)) {
            switch (stack.get(EVENT_TYPE)) {
                case FLOWERY -> {
                    if ((block == Blocks.OAK_LEAVES || block == Blocks.DARK_OAK_LEAVES || block == Blocks.PALE_OAK_LEAVES)
                    && !Tools.hasSilkTouch(stack)) {
                        int x = Radik.RANDOM.nextInt(1000);
                        Item item = null;
                        if (x <= EVENT_PROPERTY.getDouble(EventProperty.FLOWERY_AXE_NOTCH_APPLE_CHANCE) * 10) item = Items.ENCHANTED_GOLDEN_APPLE;
                        else if (x <= EVENT_PROPERTY.getDouble(EventProperty.FLOWERY_AXE_GOLDEN_APPLE_CHANCE) * 10) item = Items.GOLDEN_APPLE;
                        else if (x <= EVENT_PROPERTY.getDouble(EventProperty.FLOWERY_AXE_APPLE_CHANCE) * 10) item = Items.APPLE;
                        if (item != null) {
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item)));
                            Radik.sendEventToPlayers(2, pos, 0, (ServerWorld) world);
                        }
                    }
                }
                case SUMMER -> {
                    if (block.getName().contains(Text.literal("log"))) break;
                    int k = 9;
                    Queue<BlockPos> q = new LinkedList<>();
                    BlockPos pos1 = pos;
                    do {
                        for (Direction d : Direction.values()) {
                            BlockPos newpos = pos1.offset(d);
                            Block b = world.getBlockState(newpos).getBlock();
                            if (b.equals(block)) q.add(newpos);
                        }
                        pos1 = q.poll();
                        Tools.breakBlock(pos1, world, player, stack);
                        k--;
                    } while (k > 0 && pos1 != null);
                }
                case null, default -> {}
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Tools.appendTooltip(stack, textConsumer, Tool.AXE);
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
