package com.spoonypanda.ceramiccrocks.datagen;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider  extends BlockStateProvider{
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CeramicCrocks.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.LARGE_CROCK.get(), new ModelFile.UncheckedModelFile(modLoc("block/large_crock")));
        simpleBlock(ModBlocks.SMALL_CROCK.get(), new ModelFile.UncheckedModelFile(modLoc("block/small_crock")));
    }
}
