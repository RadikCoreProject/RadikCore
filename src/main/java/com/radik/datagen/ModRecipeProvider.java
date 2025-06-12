//package com.radik.datagen;
//
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
//import net.minecraft.block.Blocks;
//import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
//import net.minecraft.data.server.recipe.RecipeExporter;
//import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
//import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
//import net.minecraft.item.ItemConvertible;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemGroups;
//import net.minecraft.item.Items;
//import net.minecraft.recipe.book.RecipeCategory;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.util.Identifier;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static com.radik.block.RegisterBlocks.*;
//import static com.radik.item.RegisterItems.*;
//
//public class ModRecipeProvider extends FabricRecipeProvider {
//    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
//        super(output, registriesFuture);
//    }
//
//    @Override
//    public void generate(RecipeExporter exporter) {
//        List<ItemConvertible> PINK_GARNET_SMELTABLES = List.of(Items.RED_DYE, Items.ORANGE_DYE, Items.YELLOW_DYE, Items.GREEN_DYE, Items.BLUE_DYE, Items.PURPLE_DYE);
//
////        offerSmelting(exporter, PINK_GARNET_SMELTABLES, RecipeCategory.MISC, RegisterItems.SUGAR_BROWN, 0.25f, 200, "sugar");
////        offerBlasting(exporter, PINK_GARNET_SMELTABLES, RecipeCategory.MISC, ModItems.PINK_GARNET, 0.25f, 100, "pink_garnet");
//
////        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.PINK_GARNET, RecipeCategory.DECORATIONS, ModBlocks.PINK_GARNET_BLOCK);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE1, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.WHITE_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE2, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.LIGHT_GRAY_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE3, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.GRAY_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE4, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.BLACK_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE5, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.BROWN_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE6, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.RED_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE7, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.ORANGE_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE8, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.YELLOW_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE9, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.LIME_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE10, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.GREEN_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE11, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.CYAN_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE12, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.LIGHT_BLUE_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE13, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.BLUE_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE14, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.PURPLE_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE15, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.MAGENTA_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WINTER_STONE16, 8)
//                .pattern("AAA")
//                .pattern("ABA")
//                .pattern("AAA")
//                .input('A', Items.SMOOTH_STONE)
//                .input('B', Items.PINK_DYE)
//                .group("ledenets")
//                .criterion(hasItem(Items.SMOOTH_STONE), conditionsFromItem(Items.SMOOTH_STONE))
//                .offerTo(exporter);
//
//
////        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_PINK_GARNET, 32)
////                .input(ModBlocks.MAGIC_BLOCK)
////                .criterion(hasItem(ModBlocks.MAGIC_BLOCK), conditionsFromItem(ModBlocks.MAGIC_BLOCK))
////                .offerTo(exporter, Identifier.of(TutorialMod.MOD_ID, "raw_pink_garnet_from_magic_block"));
//
////        offerSmithingTrimRecipe(exporter, ModItems.KAUPEN_SMITHING_TEMPLATE, Identifier.of(TutorialMod.MOD_ID, "kaupen"));
//    }
//}