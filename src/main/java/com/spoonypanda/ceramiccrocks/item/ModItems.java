package com.spoonypanda.ceramiccrocks.item;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CeramicCrocks.MOD_ID);

    public static final RegistryObject<Item> UNFIRED_LARGE_CROCK = ITEMS.register("unfired_large_crock",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> UNFIRED_SMALL_CROCK = ITEMS.register("unfired_small_crock",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
