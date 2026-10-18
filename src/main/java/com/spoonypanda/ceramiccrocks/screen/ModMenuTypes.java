package com.spoonypanda.ceramiccrocks.screen;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, CeramicCrocks.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CrockMenu>> LARGE_CROCK_MENU =
            registerMenuType("large_crock_menu", (windowId, inv, data) ->
                    new CrockMenu(windowId, inv, data, CrockSize.LARGE));

    public static final DeferredHolder<MenuType<?>, MenuType<CrockMenu>> SMALL_CROCK_MENU =
            registerMenuType("small_crock_menu", (windowId, inv, data) ->
                    new CrockMenu(windowId, inv, data, CrockSize.SMALL));

    private static <T extends AbstractContainerMenu>
    DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(
            String name,
            IContainerFactory<T> factory
    ) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
