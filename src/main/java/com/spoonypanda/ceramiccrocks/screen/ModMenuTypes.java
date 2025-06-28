package com.spoonypanda.ceramiccrocks.screen;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, CeramicCrocks.MOD_ID);

    public static final RegistryObject<MenuType<CrockMenu>> LARGE_CROCK_MENU =
            registerMenuType("large_crock_menu", (windowId, inv, data) ->
                    new CrockMenu(windowId, inv, data, CrockSize.LARGE));

    public static final RegistryObject<MenuType<CrockMenu>> SMALL_CROCK_MENU =
            registerMenuType("small_crock_menu", (windowId, inv, data) ->
                    new CrockMenu(windowId, inv, data, CrockSize.SMALL));

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, () -> IForgeMenuType.create((factory)));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
