package com.spoonypanda.ceramiccrocks.item;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CeramicCrocks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CERAMICCROCKS_TAB = CREATIVE_MODE_TABS.register("ceramiccrocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.UNFIRED_LARGE_CROCK.get()))
                    .title(Component.translatable("creativetab.ceramiccrocks_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.UNFIRED_LARGE_CROCK.get());
                        output.accept(ModItems.UNFIRED_SMALL_CROCK.get());
                        output.accept(ModBlocks.LARGE_CROCK.get());
                        output.accept(ModBlocks.SMALL_CROCK.get());
                    })
                    .build()
    );
    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
