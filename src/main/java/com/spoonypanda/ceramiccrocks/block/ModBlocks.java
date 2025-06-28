package com.spoonypanda.ceramiccrocks.block;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.custom.CrockBlock;
import com.spoonypanda.ceramiccrocks.block.entity.CrockBlockEntity;
import com.spoonypanda.ceramiccrocks.item.CrockBlockItem;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CeramicCrocks.MOD_ID);

    // Old registers, leaving in commented so I can go back and see how this was original done before updating
//    public static final RegistryObject<Block> LARGE_CROCK = registerBlock("large_crock",
//            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.STONE).noOcclusion()));
//    public static final RegistryObject<Block> SMALL_CROCK = registerBlock("small_crock",
//            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.STONE).noOcclusion()));

    public static final RegistryObject<Block> LARGE_CROCK = registerBlock("large_crock",
            () -> new CrockBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.DECORATED_POT)
                    .noOcclusion(),CrockSize.LARGE),
            () -> new CrockBlockItem(() -> ModBlocks.LARGE_CROCK.get(), new Item.Properties())


    );

    public static final RegistryObject<Block> SMALL_CROCK = registerBlock("small_crock",
            () -> new CrockBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.DECORATED_POT)
                    .noOcclusion(),CrockSize.SMALL),
            () -> new CrockBlockItem(() -> ModBlocks.SMALL_CROCK.get(), new Item.Properties())
    );


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockSupplier, Supplier<Item> itemSupplier) {
        RegistryObject<T> blockReg = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> {
            System.out.println("[DEBUG] Registering CrockBlockItem for: " + ForgeRegistries.BLOCKS.getKey(blockReg.get()));
            return itemSupplier.get();
        });

        return blockReg;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }


}
