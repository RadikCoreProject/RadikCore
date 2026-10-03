//package com.radik.block.custom.blockentity.chemical.electrolyzer;
//
//import com.google.common.collect.Lists;
//import com.mojang.serialization.Codec;
//import com.radik.block.custom.blockentity.BlockEntities;
//import it.unimi.dsi.fastutil.objects.Reference2IntMap;
//import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
//import net.minecraft.block.AbstractFurnaceBlock;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.block.entity.LockableContainerBlockEntity;
//import net.minecraft.entity.ExperienceOrbEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.Inventories;
//import net.minecraft.inventory.SidedInventory;
//import net.minecraft.item.FuelRegistry;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.nbt.NbtCompound;
//import net.minecraft.recipe.*;
//import net.minecraft.recipe.input.SingleStackRecipeInput;
//import net.minecraft.registry.DynamicRegistryManager;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.screen.FurnaceScreenHandler;
//import net.minecraft.screen.PropertyDelegate;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.text.Text;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//import java.util.Map;
//
//public class ElectrolyzerEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider{
//    protected static final int INPUT_SLOT_INDEX = 0;
//    protected static final int FUEL_SLOT_INDEX = 1;
//    protected static final int OUTPUT_SLOT_INDEX = 2;
//    public static final int BURN_TIME_PROPERTY_INDEX = 0;
//    private static final int[] TOP_SLOTS = new int[]{0};
//    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
//    private static final int[] SIDE_SLOTS = new int[]{1};
//    public static final int FUEL_TIME_PROPERTY_INDEX = 1;
//    public static final int COOK_TIME_PROPERTY_INDEX = 2;
//    public static final int COOK_TIME_TOTAL_PROPERTY_INDEX = 3;
//    public static final int PROPERTY_COUNT = 4;
//    public static final int DEFAULT_COOK_TIME = 200;
//    public static final int field_31295 = 2;
//    private static final Codec<Map<RegistryKey<Recipe<?>>, Integer>> CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
//    private static final short DEFAULT_LIT_TIME_REMAINING = 0;
//    private static final short DEFAULT_LIT_TOTAL_TIME = 0;
//    private static final short DEFAULT_COOKING_TIME_SPENT = 0;
//    private static final short DEFAULT_COOKING_TOTAL_TIME = 0;
//    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
//    int litTimeRemaining;
//    int litTotalTime;
//    int cookingTimeSpent;
//    int cookingTotalTime;
//    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
//        @Override
//        public int get(int index) {
//            return switch (index) {
//                case 0 -> ElectrolyzerEntity.this.litTimeRemaining;
//                case 1 -> ElectrolyzerEntity.this.litTotalTime;
//                case 2 -> ElectrolyzerEntity.this.cookingTimeSpent;
//                case 3 -> ElectrolyzerEntity.this.cookingTotalTime;
//                default -> 0;
//            };
//        }
//
//        @Override
//        public void set(int index, int material) {
//            switch (index) {
//                case 0 -> ElectrolyzerEntity.this.litTimeRemaining = material;
//                case 1 ->ElectrolyzerEntity.this.litTotalTime = material;
//                case 2 ->ElectrolyzerEntity.this.cookingTimeSpent = material;
//                case 3 -> ElectrolyzerEntity.this.cookingTotalTime = material;
//                default -> throw new IllegalStateException("Unexpected material: " + index);
//            };
//        }
//
//        @Override
//        public int size() {
//            return 4;
//        }
//    };
//    private final Reference2IntOpenHashMap<RegistryKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap<>();
//    private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
//
//    public ElectrolyzerEntity(BlockPos pos, BlockState state) {
//        super(BlockEntities.ELECTROLYZER_ENTITY, pos, state);
//        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(RecipeType.SMELTING);
//    }
//
//    @Override
//    protected Text getContainerName() {
//        return Text.translatable("container.furnace");
//    }
//
//    @Override
//    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
//    }
//
//    private boolean isBurning() {
//        return this.litTimeRemaining > 0;
//    }
//
//    @Override
//    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
//        super.readNbt(nbt, registries);
//        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
//        Inventories.readNbt(nbt, this.inventory, registries);
//        this.cookingTimeSpent = nbt.getShort("cooking_time_spent", (short)0);
//        this.cookingTotalTime = nbt.getShort("cooking_total_time", (short)0);
//        this.litTimeRemaining = nbt.getShort("lit_time_remaining", (short)0);
//        this.litTotalTime = nbt.getShort("lit_total_time", (short)0);
//        this.recipesUsed.clear();
//        this.recipesUsed.putAll(nbt.get("RecipesUsed", CODEC).orElse(Map.of()));
//    }
//
//    @Override
//    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
//        super.writeNbt(nbt, registries);
//        nbt.putShort("cooking_time_spent", (short)this.cookingTimeSpent);
//        nbt.putShort("cooking_total_time", (short)this.cookingTotalTime);
//        nbt.putShort("lit_time_remaining", (short)this.litTimeRemaining);
//        nbt.putShort("lit_total_time", (short)this.litTotalTime);
//        Inventories.writeNbt(nbt, this.inventory, registries);
//        nbt.put("RecipesUsed", CODEC, this.recipesUsed);
//    }
//
//    public static void tick(ServerWorld world, BlockPos pos, BlockState state, ElectrolyzerEntity blockEntity) {
//        boolean bl = blockEntity.isBurning();
//        boolean bl2 = false;
//        if (blockEntity.isBurning()) {
//            blockEntity.litTimeRemaining--;
//        }
//
//        ItemStack itemStack = blockEntity.inventory.get(1);
//        ItemStack itemStack2 = blockEntity.inventory.get(0);
//        boolean bl3 = !itemStack2.isEmpty();
//        boolean bl4 = !itemStack.isEmpty();
//        if (blockEntity.isBurning() || bl4 && bl3) {
//            SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(itemStack2);
//            RecipeEntry<? extends AbstractCookingRecipe> recipeEntry;
//            if (bl3) {
//                recipeEntry = blockEntity.matchGetter.getFirstMatch(singleStackRecipeInput, world).orElse(null);
//            } else {
//                recipeEntry = null;
//            }
//
//            int i = blockEntity.getMaxCountPerStack();
//            if (!blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
//                blockEntity.litTimeRemaining = blockEntity.getFuelTime(world.getFuelRegistry(), itemStack);
//                blockEntity.litTotalTime = blockEntity.litTimeRemaining;
//                if (blockEntity.isBurning()) {
//                    bl2 = true;
//                    if (bl4) {
//                        Item item = itemStack.getItem();
//                        itemStack.decrement(1);
//                        if (itemStack.isEmpty()) {
//                            blockEntity.inventory.set(1, item.getRecipeRemainder());
//                        }
//                    }
//                }
//            }
//
//            if (blockEntity.isBurning() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
//                blockEntity.cookingTimeSpent++;
//                if (blockEntity.cookingTimeSpent == blockEntity.cookingTotalTime) {
//                    blockEntity.cookingTimeSpent = 0;
//                    blockEntity.cookingTotalTime = getCookTime(world, blockEntity);
//                    if (craftRecipe(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
//                        blockEntity.setLastRecipe(recipeEntry);
//                    }
//
//                    bl2 = true;
//                }
//            } else {
//                blockEntity.cookingTimeSpent = 0;
//            }
//        } else if (blockEntity.cookingTimeSpent > 0) {
//            blockEntity.cookingTimeSpent = MathHelper.clamp(blockEntity.cookingTimeSpent - 2, 0, blockEntity.cookingTotalTime);
//        }
//
//        if (bl != blockEntity.isBurning()) {
//            bl2 = true;
//            state = state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
//            world.setBlockState(pos, state, Block.NOTIFY_ALL);
//        }
//
//        if (bl2) {
//            markDirty(world, pos, state);
//        }
//    }
//
//    private static boolean canAcceptRecipeOutput(
//            DynamicRegistryManager dynamicRegistryManager,
//            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
//            SingleStackRecipeInput input,
//            DefaultedList<ItemStack> inventory,
//            int maxCount
//    ) {
//        if (!inventory.get(0).isEmpty() && recipe != null) {
//            ItemStack itemStack = recipe.material().craft(input, dynamicRegistryManager);
//            if (itemStack.isEmpty()) {
//                return false;
//            } else {
//                ItemStack itemStack2 = inventory.get(2);
//                if (itemStack2.isEmpty()) {
//                    return true;
//                } else if (!ItemStack.areItemsAndComponentsEqual(itemStack2, itemStack)) {
//                    return false;
//                } else {
//                    return itemStack2.getCount() < maxCount && itemStack2.getCount() < itemStack2.getMaxCount() || itemStack2.getCount() < itemStack.getMaxCount();
//                }
//            }
//        } else {
//            return false;
//        }
//    }
//
//    private static boolean craftRecipe(
//            DynamicRegistryManager dynamicRegistryManager,
//            @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe,
//            SingleStackRecipeInput input,
//            DefaultedList<ItemStack> inventory,
//            int maxCount
//    ) {
//        if (recipe != null && canAcceptRecipeOutput(dynamicRegistryManager, recipe, input, inventory, maxCount)) {
//            ItemStack itemStack = inventory.get(0);
//            ItemStack itemStack2 = recipe.material().craft(input, dynamicRegistryManager);
//            ItemStack itemStack3 = inventory.get(2);
//            if (itemStack3.isEmpty()) {
//                inventory.set(2, itemStack2.copy());
//            } else if (ItemStack.areItemsAndComponentsEqual(itemStack3, itemStack2)) {
//                itemStack3.increment(1);
//            }
//
//            if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !inventory.get(1).isEmpty() && inventory.get(1).isOf(Items.BUCKET)) {
//                inventory.set(1, new ItemStack(Items.WATER_BUCKET));
//            }
//
//            itemStack.decrement(1);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
//        return fuelRegistry.getFuelTicks(stack);
//    }
//
//    private static int getCookTime(ServerWorld world, ElectrolyzerEntity furnace) {
//        SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(0));
//        return (Integer)furnace.matchGetter
//                .getFirstMatch(singleStackRecipeInput, world)
//                .map(recipe -> ((AbstractCookingRecipe)recipe.material()).getCookingTime())
//                .orElse(200);
//    }
//
//    @Override
//    public int[] getAvailableSlots(Direction side) {
//        if (side == Direction.DOWN) {
//            return BOTTOM_SLOTS;
//        } else {
//            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
//        }
//    }
//
//    @Override
//    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
//        return this.isValid(slot, stack);
//    }
//
//    @Override
//    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
//        return dir != Direction.DOWN || slot != 1 || stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
//    }
//
//    @Override
//    public int size() {
//        return this.inventory.size();
//    }
//
//    @Override
//    protected DefaultedList<ItemStack> getHeldStacks() {
//        return this.inventory;
//    }
//
//    @Override
//    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
//        this.inventory = inventory;
//    }
//
//    @Override
//    public void setStack(int slot, ItemStack stack) {
//        ItemStack itemStack = this.inventory.get(slot);
//        boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
//        this.inventory.set(slot, stack);
//        stack.capCount(this.getMaxCount(stack));
//        if (slot == 0 && !bl && this.world instanceof ServerWorld serverWorld) {
//            this.cookingTotalTime = getCookTime(serverWorld, this);
//            this.cookingTimeSpent = 0;
//            this.markDirty();
//        }
//    }
//
//    @Override
//    public boolean isValid(int slot, ItemStack stack) {
//        if (slot == 2) {
//            return false;
//        } else if (slot != 1) {
//            return true;
//        } else {
//            ItemStack itemStack = this.inventory.get(1);
//            if (this.world == null) return false;
//            return this.world.getFuelRegistry().isFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
//        }
//    }
//
//    @Override
//    public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
//        if (recipe != null) {
//            RegistryKey<Recipe<?>> registryKey = recipe.id();
//            this.recipesUsed.addTo(registryKey, 1);
//        }
//    }
//
//    @Nullable
//    @Override
//    public RecipeEntry<?> getLastRecipe() {
//        return null;
//    }
//
//    @Override
//    public void unlockLastRecipe(PlayerEntity player, List<ItemStack> ingredients) {
//    }
//
//    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
//        List<RecipeEntry<?>> list = this.getRecipesUsedAndDropExperience(player.getServerWorld(), player.getPos());
//        player.unlockRecipes(list);
//
//        for (RecipeEntry<?> recipeEntry : list) {
//            if (recipeEntry != null) {
//                player.onRecipeCrafted(recipeEntry, this.inventory);
//            }
//        }
//
//        this.recipesUsed.clear();
//    }
//
//    public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
//        List<RecipeEntry<?>> list = Lists.<RecipeEntry<?>>newArrayList();
//
//        for (Reference2IntMap.Entry<RegistryKey<Recipe<?>>> entry : this.recipesUsed.reference2IntEntrySet()) {
//            world.getRecipeManager().get((RegistryKey<Recipe<?>>)entry.getKey()).ifPresent(recipe -> {
//                list.add(recipe);
//                dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe.material()).getExperience());
//            });
//        }
//
//        return list;
//    }
//
//    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
//        int i = MathHelper.floor(multiplier * experience);
//        float f = MathHelper.fractionalPart(multiplier * experience);
//        if (f != 0.0F && Math.random() < f) {
//            i++;
//        }
//
//        ExperienceOrbEntity.spawn(world, pos, i);
//    }
//
//    @Override
//    public void provideRecipeInputs(RecipeFinder finder) {
//        for (ItemStack itemStack : this.inventory) {
//            finder.addInput(itemStack);
//        }
//    }
//
//    @Override
//    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
//        super.onBlockReplaced(pos, oldState);
//        if (this.world instanceof ServerWorld serverWorld) {
//            this.getRecipesUsedAndDropExperience(serverWorld, Vec3d.ofCenter(pos));
//        }
//    }
//}
