package com.spoonypanda.ceramiccrocks.block.entity;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CeramicCrocks.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrockBlockEntity>> LARGE_CROCK_BE =
            BLOCK_ENTITIES.register("large_crock_be", () ->
                    BlockEntityType.Builder.of(
                            CrockBlockEntity::createLarge,
                            ModBlocks.LARGE_CROCK.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrockBlockEntity>> SMALL_CROCK_BE =
            BLOCK_ENTITIES.register("small_crock_be", () ->
                    BlockEntityType.Builder.of(
                            CrockBlockEntity::createSmall,
                            ModBlocks.SMALL_CROCK.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

}
