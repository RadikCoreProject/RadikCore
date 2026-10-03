package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.base.EventProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.Component;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.RpcDiscover;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;

public class Shovel extends ShovelItem implements Tools {
    private static final HashMap<Block, Item[]> proccess = new HashMap<>();
    private static final List<Block> DROPPING_CLEVER = new ArrayList<>();

    static {
        proccess.put(Blocks.SAND, new Item[]{Items.DEAD_BUSH, Items.CACTUS, Items.OAK_SAPLING});
        proccess.put(Blocks.RED_SAND, new Item[]{Items.DEAD_BUSH, Items.CACTUS});
        proccess.put(Blocks.DIRT, new Item[]{Items.POTATO, Items.BEETROOT_SEEDS, Items.CARROT, Items.BUSH, Items.ACACIA_SAPLING, RegisterItems.CUCUMBER_SEEDS});
        proccess.put(Blocks.GRASS_BLOCK, new Item[]{Items.POTATO, Items.BEETROOT_SEEDS, Items.CARROT, Items.WHEAT_SEEDS, Items.BIRCH_SAPLING, Items.CHERRY_SAPLING, Items.OAK_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING});
        proccess.put(Blocks.PODZOL, new Item[]{Items.SPRUCE_SAPLING, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BUSH, Items.WILDFLOWERS});
        proccess.put(Blocks.MYCELIUM, new Item[]{Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.CORNFLOWER, Items.TORCHFLOWER_SEEDS, Items.PALE_OAK_SAPLING});
        proccess.put(Blocks.SNOW_BLOCK, new Item[]{RegisterItems.SNOWFLAKE, RegisterItems.ICE_SHARD});

        DROPPING_CLEVER.add(Blocks.TALL_GRASS);
        DROPPING_CLEVER.add(Blocks.TALL_DRY_GRASS);
        DROPPING_CLEVER.add(Blocks.PODZOL);
        DROPPING_CLEVER.add(RegisterBlocks.OLD_GRASS_BLOCK);
    }

    public Shovel(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings.component(BOOL, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Tools.appendTooltip(stack, textConsumer, Tool.SHOVEL);
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean post = super.postMine(stack, world, state, pos, miner);
        ChallengeEvent event = stack.get(EVENT_TYPE);
        Block block = state.getBlock();
        if (event == null) return post;

        if (!world.isClient() && Tools.activedPower(stack)) {
            switch (stack.get(EVENT_TYPE)) {
                case HALLOWEEN -> {
                    if (proccess.containsKey(block)) {
                        if (Radik.RANDOM.nextInt(100) <= EVENT_PROPERTY.getInt(EventProperty.SHOVEL_DROP_CHANCE)) {
                            Item[] item = proccess.get(block);
                            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item[Radik.RANDOM.nextInt(item.length)])));
                        }
                    }
                }
                case WINTER -> {}
                case FLOWERY -> Tools.mine3x1(stack, (ServerWorld) world, state, pos, (ServerPlayerEntity) miner);
                case SUMMER -> {
                    DynamicRegistryManager registryManager = world.getRegistryManager();
                    RegistryEntry<Enchantment> silkEntry = registryManager
                            .getOrThrow(RegistryKeys.ENCHANTMENT)
                            .getOrThrow(Enchantments.SILK_TOUCH);
                    if (DROPPING_CLEVER.contains(block) || stack.getEnchantments().getLevel(silkEntry) != 0) break;
                    int f = Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.CLEVER_DROP_CHANCE) + 1);
                    if (f == 1) {
                        int s = Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.CLEVER_DROP_CHANCE) + 1);
                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                new ItemStack(s == 1 ? RegisterItems.CLEVER4 : RegisterItems.CLEVER3, 1)));
                    }
                }
                case null, default -> {}
            }
        }
        return post;
    }
}
