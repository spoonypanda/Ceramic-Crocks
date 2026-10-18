package com.spoonypanda.ceramiccrocks.datagen;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CeramicCrocks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.UNFIRED_LARGE_CROCK);
        simpleItem(ModItems.UNFIRED_SMALL_CROCK);
        itemModel(ModBlocks.LARGE_CROCK.get().asItem());
        itemModel(ModBlocks.SMALL_CROCK.get().asItem());
    }

    private ItemModelBuilder simpleItem(DeferredHolder<Item, Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(CeramicCrocks.MOD_ID, "item/" + item.getId().getPath()));

    }

    private void itemModel(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }
}
