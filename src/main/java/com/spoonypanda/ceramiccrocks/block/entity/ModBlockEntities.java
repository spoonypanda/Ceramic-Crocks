package com.spoonypanda.ceramiccrocks.block.entity;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CeramicCrocks.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrockBlockEntity>> LARGE_CROCK_BE =
            BLOCK_ENTITIES.register("large_crock_be", () ->
                    BlockEntityType.Builder.of(CrockBlockEntity::createLarge, ModBlocks.LARGE_CROCK.get()).build(null)
            );

    public static final RegistryObject<BlockEntityType<CrockBlockEntity>> SMALL_CROCK_BE =
            BLOCK_ENTITIES.register("small_crock_be", () ->
                    BlockEntityType.Builder.of(CrockBlockEntity::createSmall, ModBlocks.SMALL_CROCK.get()).build(null)
            );

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
