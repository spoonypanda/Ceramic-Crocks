package com.spoonypanda.ceramiccrocks.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;


public class CrockBlockItem extends BlockItem {
    public CrockBlockItem(Supplier<Block> blockSupplier, Item.Properties properties) {
        super(blockSupplier.get(), properties);
    }

//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new IClientItemExtensions() {
//            @Override
//            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
//                return new CrockItemRenderer(); // or cache it if desired
//            }
//        });
//    }
}