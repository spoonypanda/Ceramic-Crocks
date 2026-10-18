package com.spoonypanda.ceramiccrocks.item;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, CeramicCrocks.MOD_ID);

    public static final DeferredHolder<Item,Item> UNFIRED_LARGE_CROCK = ITEMS.register("unfired_large_crock",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item,Item> UNFIRED_SMALL_CROCK = ITEMS.register("unfired_small_crock",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
