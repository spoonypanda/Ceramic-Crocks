package com.spoonypanda.ceramiccrocks.datagen.loot;

import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider lookupProvider) {
        super(
                Set.of(),
                FeatureFlags.REGISTRY.allFlags(),
                lookupProvider
        );
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.LARGE_CROCK.get(), block -> createCrockLoot(block));
        this.add(ModBlocks.SMALL_CROCK.get(), block -> createCrockLoot(block));
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    private LootTable.Builder createCrockLoot(Block block) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                .include(DataComponents.CONTAINER)
                                                .include(DataComponents.BASE_COLOR)
                                )));
    }
}
