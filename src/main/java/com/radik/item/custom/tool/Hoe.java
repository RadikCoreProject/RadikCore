package com.radik.item.custom.tool;

import com.radik.Radik;
import com.radik.block.RegisterBlocks;
import com.radik.connecting.event.ChallengeEvent;
import com.radik.item.RegisterItems;
import com.radik.property.base.EventProperty;
import net.minecraft.block.*;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

import static com.radik.Data.BOOL;
import static com.radik.Data.EVENT_TYPE;
import static com.radik.property.base.BaseProperties.EVENT_PROPERTY;
import static net.minecraft.item.BoneMealItem.useOnFertilizable;
import static net.minecraft.item.BoneMealItem.useOnGround;

public class Hoe extends HoeItem implements Tools {
    private static final HashMap<Block, Item> CROPS = new HashMap<>();
    private static final HashMap<Block, Item> LEAVES = new HashMap<>();

    static {
        CROPS.put(Blocks.WHEAT, Items.WHEAT_SEEDS);
        CROPS.put(Blocks.BEETROOTS, Items.BEETROOT_SEEDS);
        CROPS.put(Blocks.POTATOES, Items.POTATO);
        CROPS.put(Blocks.CARROTS, Items.CARROT);

        LEAVES.put(Blocks.OAK_LEAVES, RegisterItems.LEAVE_OAK);
        LEAVES.put(Blocks.ACACIA_LEAVES, RegisterItems.LEAVE_ACACIA);
        LEAVES.put(Blocks.PALE_OAK_LEAVES, RegisterItems.LEAVE_PALE_OAK);
        LEAVES.put(Blocks.DARK_OAK_LEAVES, RegisterItems.LEAVE_DARK_OAK);
        LEAVES.put(Blocks.BIRCH_LEAVES, RegisterItems.LEAVE_BIRCH);
        LEAVES.put(Blocks.SPRUCE_LEAVES, RegisterItems.LEAVE_SPRUCE);
        LEAVES.put(Blocks.AZALEA_LEAVES, RegisterItems.LEAVE_AZALEA);
        LEAVES.put(Blocks.JUNGLE_LEAVES, RegisterItems.LEAVE_JUNGLE);
        LEAVES.put(Blocks.CHERRY_LEAVES, RegisterItems.LEAVE_CHERRY);
        LEAVES.put(Blocks.MANGROVE_LEAVES, RegisterItems.LEAVE_MANGROVE);
        LEAVES.put(RegisterBlocks.RADIOACTIVE_LEAVES, RegisterItems.LEAVE_DEAD);
    }

    public Hoe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings.component(BOOL, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendTooltip(@NotNull ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Tools.appendTooltip(stack, textConsumer, Tool.HOE);
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return super.canMine(stack, state, world, pos, user) && (state.getBlock() instanceof CropBlock && state.get(CropBlock.AGE) == 7 || !(state.getBlock() instanceof CropBlock));
    }

    public boolean postMine(@NotNull ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (Tools.activedPower(stack) && !world.isClient()) {
            Block block = state.getBlock();
            ChallengeEvent event = stack.get(EVENT_TYPE);
            if (event == null) return super.postMine(stack, world, state, pos, miner);
            switch (event) {
                case HALLOWEEN -> {
                    Item needItem = CROPS.get(block);
                    if (needItem != null) {
                        ((PlayerEntity) miner).getInventory().forEach(stack1 -> {
                            if (stack1.getItem().equals(needItem)) {
                                stack1.decrement(1);
                                world.setBlockState(pos, block.getDefaultState());
                                Radik.sendEventToPlayers(0, pos, 0, (ServerWorld) world);
                                if (Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.GREEN_CANDY_DROP_CHANCE) + 1) == 1) {
                                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                        new ItemStack(RegisterItems.ORANGE, 1)));
                                }
                            }
                        });
                    }
                }
                case SUMMER -> {
                    Item item = LEAVES.get(block);
                    if (item == null) break;
                    if (Radik.RANDOM.nextInt(1, EVENT_PROPERTY.getInt(EventProperty.LEAVE_DROP_CHANCE) + 1) == 1) {
                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                new ItemStack(item, 1)));
                    }
                }
                case WINTER -> {}
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        ActionResult result = super.useOnBlock(context);
        if (!(context.getWorld() instanceof ServerWorld world)) return result;
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        Block block = world.getBlockState(pos).getBlock();
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        if (Tools.activedPower(stack) && !context.getWorld().isClient()) {
            switch (stack.get(EVENT_TYPE)) {
                case HALLOWEEN -> {
                    if (block.equals(Blocks.DRAGON_HEAD)) {
                        if (Radik.RANDOM.nextInt(64) == 5) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DRAGON_BREATH)));
                        stack.damage(2, player);
                        Radik.sendEventToPlayers(0, pos, 0, world);
                    }
                }
                case WINTER -> {}
                case FLOWERY -> {
                    if (block instanceof Fertilizable) {
                        useMeal(context, 0, 0);
                        useMeal(context, 1, 0);
                        useMeal(context, -1, 0);
                        useMeal(context, 0, 1);
                        useMeal(context, 0, -1);
                        stack.damage(10, player);
                    }
                }
                case SUMMER -> {}
                case null, default -> {}
            }
        }
        return ActionResult.SUCCESS;
    }

    private static void useMeal(ItemUsageContext ctx, int x, int z) {
        if (ctx.getPlayer() == null) return;
        BlockPos pos = ctx.getBlockPos().add(x, 0, z);

        meal(x == 0 && z == 0 ? ctx : new ItemUsageContext(
            ctx.getWorld(),
            ctx.getPlayer(),
            null,
            null,
            new BlockHitResult(
                ctx.getHitPos().add(x, 0, z),
                ctx.getSide(),
                ctx.getBlockPos().add(x, 0, z),
                ctx.hitsInsideBlock()
            )
        ));
        Radik.sendEventToPlayers(2, pos, 0, (ServerWorld) ctx.getWorld());
    }

    private static void meal(ItemUsageContext context) {
        Item meal = Items.BONE_MEAL;
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(context.getSide());
        boolean b = useOnFertilizable(new ItemStack(meal), world, blockPos);
        if (!b) useOnGround(new ItemStack(meal), world, blockPos2, Direction.UP);
    }
}
