package dev.endless.v4.data.provider;

import dev.endless.v4.init.BlockInit;
import dev.endless.v4.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NoctyraRecipeProvider extends FabricRecipeProvider {
    public NoctyraRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, BlockInit.EXAMPLE_BLOCK, 1)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemInit.EXAMPLE_ITEM, 9)
                .input(BlockInit.EXAMPLE_BLOCK)
                .criterion(hasItem(BlockInit.EXAMPLE_BLOCK), conditionsFromItem(BlockInit.EXAMPLE_BLOCK))
                .offerTo(recipeExporter);

        List<ItemConvertible> exampleOres = List.of(BlockInit.EXAMPLE_OVERWORLD_ORE, BlockInit.EXAMPLE_DEEPSLATE_ORE, BlockInit.EXAMPLE_NETHER_ORE, BlockInit.EXAMPLE_END_ORE);

        RecipeProvider.offerBlasting(recipeExporter,
                exampleOres,
                RecipeCategory.MISC,
                ItemInit.EXAMPLE_ITEM,
                0.2f,
                100,
                "example");

        RecipeProvider.offerSmelting(recipeExporter,
                exampleOres,
                RecipeCategory.MISC,
                ItemInit.EXAMPLE_ITEM,
                0.2f,
                200,
                "example");

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.EXAMPLE_SWORD)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("E")
                .pattern("E")
                .pattern("S")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemInit.EXAMPLE_PICKAXE)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("EEE")
                .pattern(" S ")
                .pattern(" S ")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemInit.EXAMPLE_AXE)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("EE")
                .pattern("ES")
                .pattern(" S")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemInit.EXAMPLE_SHOVEL)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("E")
                .pattern("S")
                .pattern("S")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemInit.EXAMPLE_HOE)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .input('S', ConventionalItemTags.WOODEN_RODS)
                .pattern("EE")
                .pattern(" S")
                .pattern(" S")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .criterion(hasTag(ConventionalItemTags.WOODEN_RODS), conditionsFromTag(ConventionalItemTags.WOODEN_RODS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.EXAMPLE_HELMET)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .pattern("EEE")
                .pattern("E E")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.EXAMPLE_CHESTPLATE)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .pattern("E E")
                .pattern("EEE")
                .pattern("EEE")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.EXAMPLE_LEGGINGS)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .pattern("EEE")
                .pattern("E E")
                .pattern("E E")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ItemInit.EXAMPLE_BOOTS)
                .input('E', ItemInit.EXAMPLE_ITEM)
                .pattern("E E")
                .pattern("E E")
                .criterion(hasItem(ItemInit.EXAMPLE_ITEM), conditionsFromItem(ItemInit.EXAMPLE_ITEM))
                .offerTo(recipeExporter);
    }

    private static String hasTag(TagKey<Item> tag) {
        return "has_" + tag.id().toString();
    }
}
