package com.spoonypanda.ceramiccrocks.datagen;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(CeramicCrocks.MOD_ID, "item/" + item.getId().getPath()));

    }

    private void itemModel(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();
        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }
}
