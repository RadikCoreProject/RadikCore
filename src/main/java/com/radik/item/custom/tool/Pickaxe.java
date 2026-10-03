package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.base.EventProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;

public class Pickaxe extends Item implements Tools {
    private static final Map<Block, Integer> ORBS = new HashMap<>();

    static {
        ORBS.put(Blocks.COAL_ORE, 1);
        ORBS.put(Blocks.DEEPSLATE_COAL_ORE, 1);
        ORBS.put(Blocks.IRON_ORE, 2);
        ORBS.put(Blocks.DEEPSLATE_IRON_ORE, 2);
        ORBS.put(Blocks.GOLD_ORE, 3);
        ORBS.put(Blocks.DEEPSLATE_GOLD_ORE, 3);
        ORBS.put(Blocks.COPPER_ORE, 1);
        ORBS.put(Blocks.DEEPSLATE_COPPER_ORE, 1);
        ORBS.put(Blocks.EMERALD_ORE, 8);
        ORBS.put(Blocks.DEEPSLATE_EMERALD_ORE, 8);
        ORBS.put(Blocks.DIAMOND_ORE, 5);
        ORBS.put(Blocks.DEEPSLATE_DIAMOND_ORE, 5);
        ORBS.put(Blocks.REDSTONE_ORE, 3);
        ORBS.put(Blocks.DEEPSLATE_REDSTONE_ORE, 3);
        ORBS.put(Blocks.LAPIS_ORE, 3);
        ORBS.put(Blocks.DEEPSLATE_LAPIS_ORE, 3);
        ORBS.put(Blocks.NETHER_GOLD_ORE, 2);
        ORBS.put(Blocks.NETHER_QUARTZ_ORE, 3);
        ORBS.put(RegisterBlocks.LEAD_ORE, 5);
        ORBS.put(RegisterBlocks.DEEPSLATE_LEAD_ORE, 5);
        ORBS.put(RegisterBlocks.URANUS_ORE, 10);
        ORBS.put(RegisterBlocks.DEEPSLATE_URANUS_ORE, 10);
    }

    public Pickaxe(ToolMaterial material, float attackDamage, float attackSpeed, Item.@NotNull Settings settings) {
        super(settings.fireproof().pickaxe(material, attackDamage, attackSpeed).component(BOOL, false));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean result = super.postMine(stack, world, state, pos, miner);
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Boolean bool = stack.get(BOOL);
        Block block = state.getBlock();
        if (event == null || bool == null) return result;

        if (!world.isClient() && Tools.activedPower(stack)) {
            switch (stack.get(EVENT_TYPE)) {
                case HALLOWEEN -> Tools.mine3x1(stack, (ServerWorld) world, state, pos, (ServerPlayerEntity) miner);
                case WINTER -> {}
                case FLOWERY -> {
                    if (Tools.hasSilkTouch(stack) || !ORBS.containsKey(block)) return result;
                    int k = ORBS.get(block) * 2;
                    if (Radik.RANDOM.nextInt(100) <= EVENT_PROPERTY.getInt(EventProperty.FLOWERY_PICKAXE_ULTA_CHANCE)) {
                        stack.setDamage(stack.getDamage() + k);
                        miner.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100, 1, true, false));
                    }
                }
                case null, default -> {}
            }
        }
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Tools.appendTooltip(stack, textConsumer, Tool.PICKAXE);
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
}
