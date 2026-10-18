package com.spoonypanda.ceramiccrocks.datagen;

import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.item.Items.CLAY_BALL;

public class ModRecipeProvider extends RecipeProvider{
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries){
        super(pOutput,pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pOutput) {
        oreSmelting(pOutput,ModItems.UNFIRED_LARGE_CROCK.get(), RecipeCategory.MISC, ModBlocks.LARGE_CROCK.get(),0.25f,200,"large_crock");
        oreSmelting(pOutput,ModItems.UNFIRED_SMALL_CROCK.get(), RecipeCategory.MISC, ModBlocks.SMALL_CROCK.get(),0.25f,200,"small_crock");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.UNFIRED_LARGE_CROCK.get())
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', CLAY_BALL)
                .unlockedBy(getHasName(CLAY_BALL), has(CLAY_BALL))
                .save(pOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.UNFIRED_SMALL_CROCK.get())
                .pattern(" C ")
                .pattern("C C")
                .pattern(" C ")
                .define('C', CLAY_BALL)
                .unlockedBy(getHasName(CLAY_BALL), has(CLAY_BALL))
                .save(pOutput);
    }

    protected static void oreSmelting(RecipeOutput pOutput, ItemLike pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        String name = pGroup + "_from_smelting";
        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(pIngredient), pCategory, pResult, pExperience, pCookingTime)
                .unlockedBy("has_" + pGroup, has(pIngredient))
                .save(pOutput, ResourceLocation.fromNamespaceAndPath("ceramiccrocks",name));
    }
}
