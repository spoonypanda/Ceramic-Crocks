package com.spoonypanda.ceramiccrocks.datagen;

import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.item.Items.CLAY_BALL;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> CROCK_SMELTABLES = List.of(ModItems.UNFIRED_LARGE_CROCK.get(),ModItems.UNFIRED_SMALL_CROCK.get());
    public ModRecipeProvider(PackOutput pOutput){
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter,ModItems.UNFIRED_LARGE_CROCK.get(), RecipeCategory.MISC, ModBlocks.LARGE_CROCK.get(),0.25f,200,"large_crock");
        oreSmelting(pWriter,ModItems.UNFIRED_SMALL_CROCK.get(), RecipeCategory.MISC, ModBlocks.SMALL_CROCK.get(),0.25f,200,"small_crock");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.UNFIRED_LARGE_CROCK.get())
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', CLAY_BALL)
                .unlockedBy(getHasName(CLAY_BALL), has(CLAY_BALL))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.UNFIRED_SMALL_CROCK.get())
                .pattern(" C ")
                .pattern("C C")
                .pattern(" C ")
                .define('C', CLAY_BALL)
                .unlockedBy(getHasName(CLAY_BALL), has(CLAY_BALL))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        String name = pGroup + "_from_smelting";
        //oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(pIngredient), pCategory, pResult, pExperience, pCookingTime)
                .unlockedBy("has_" + pGroup, has(pIngredient))
                .save(pFinishedRecipeConsumer, ResourceLocation.fromNamespaceAndPath("ceramiccrocks",name));
    }
}
