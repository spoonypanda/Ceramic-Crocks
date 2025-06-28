package com.spoonypanda.ceramiccrocks.item;

import com.spoonypanda.ceramiccrocks.client.renderer.item.CrockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;


public class CrockBlockItem extends BlockItem {
    public CrockBlockItem(Supplier<Block> blockSupplier, Item.Properties properties) {
        super(blockSupplier.get(), properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CrockItemRenderer(); // or cache it if desired
            }
        });
    }
}