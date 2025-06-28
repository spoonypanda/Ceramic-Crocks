package com.spoonypanda.ceramiccrocks;

import com.mojang.logging.LogUtils;
import com.spoonypanda.ceramiccrocks.block.custom.CrockBlock;
import com.spoonypanda.ceramiccrocks.block.entity.CrockBlockEntity;
import com.spoonypanda.ceramiccrocks.block.entity.ModBlockEntities;
import com.spoonypanda.ceramiccrocks.item.ModCreativeModeTabs;
import com.spoonypanda.ceramiccrocks.item.ModItems;
import com.spoonypanda.ceramiccrocks.block.ModBlocks;
import com.spoonypanda.ceramiccrocks.screen.CrockScreen;
import com.spoonypanda.ceramiccrocks.screen.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@Mod(CeramicCrocks.MOD_ID)
public class CeramicCrocks
{
    public static final String MOD_ID = "ceramiccrocks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CeramicCrocks(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();


        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.UNFIRED_LARGE_CROCK);
            event.accept(ModItems.UNFIRED_SMALL_CROCK);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        @SuppressWarnings("deprecation")
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.LARGE_CROCK_MENU.get(), CrockScreen::new);
            MenuScreens.register(ModMenuTypes.SMALL_CROCK_MENU.get(), CrockScreen::new);

            event.enqueueWork(() -> {
                // Block color
                Minecraft.getInstance().getBlockColors().register(
                        (state, level, pos, tintIndex) -> {
                            if (tintIndex == 0 && level != null && pos != null) {
                                BlockEntity be = level.getBlockEntity(pos);
                                if (be instanceof CrockBlockEntity crock) {
                                    return getBlendedDyeColor(crock.getDyeColor(), 0.5f);
                                }
                            }
                            return 0xFFFFFF;
                        },
                        ModBlocks.LARGE_CROCK.get(),
                        ModBlocks.SMALL_CROCK.get()
                );


                Minecraft.getInstance().getItemColors().register(
                        (stack, tintIndex) -> {
                            if (tintIndex == 0) {
                                CompoundTag tag = BlockItem.getBlockEntityData(stack);
                                if (tag != null && tag.contains("dyecolor")) {
                                    DyeColor dye = DyeColor.byName(tag.getString("dyecolor"), null);
                                    return getBlendedDyeColor(dye, 0.5f);
                                }
                            }
                            return 0xFFFFFF;
                        },
                        ModBlocks.LARGE_CROCK.get().asItem(),
                        ModBlocks.SMALL_CROCK.get().asItem()
                );


                Stream.of(ModBlocks.LARGE_CROCK.get(), ModBlocks.SMALL_CROCK.get())
                        .forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()));

                ItemPropertyFunction dyeColor = dyeColorProperty();

                ItemProperties.register(ModBlocks.LARGE_CROCK.get().asItem(), new ResourceLocation("dyecolor"), dyeColor);
                ItemProperties.register(ModBlocks.SMALL_CROCK.get().asItem(), new ResourceLocation("dyecolor"), dyeColor);
            });
        }
    }

    private static int getBlendedDyeColor(@Nullable DyeColor dye, float mix) {
        if (dye == null) return 0xFFFFFF;

        int dyeColor = dye.getTextColor();
        int clayColor = 0xC0B8B0;

        int r = (int)(((dyeColor >> 16 & 0xFF) * (1 - mix)) + ((clayColor >> 16 & 0xFF) * mix));
        int g = (int)(((dyeColor >> 8 & 0xFF) * (1 - mix)) + ((clayColor >> 8 & 0xFF) * mix));
        int b = (int)(((dyeColor & 0xFF) * (1 - mix)) + ((clayColor & 0xFF) * mix));

        return (r << 16) | (g << 8) | b;
    }

    private static ItemPropertyFunction dyeColorProperty() {
        return (stack, level, entity, seed) -> {
            CompoundTag tag = BlockItem.getBlockEntityData(stack);
            if (tag != null && tag.contains("dyecolor")) {
                return switch (tag.getString("dyecolor")) {
                    case "orange" -> 1f;
                    case "magenta" -> 2f;
                    case "light_blue" -> 3f;
                    case "yellow" -> 4f;
                    case "lime" -> 5f;
                    case "pink" -> 6f;
                    case "gray" -> 7f;
                    case "light_gray" -> 8f;
                    case "cyan" -> 9f;
                    case "purple" -> 10f;
                    case "blue" -> 11f;
                    case "brown" -> 12f;
                    case "green" -> 13f;
                    case "red" -> 14f;
                    case "black" -> 15f;
                    default -> 0f; // white
                };
            }
            return 0f;
        };
    }

    private void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().isShiftKeyDown()) {
            BlockPos pos = event.getPos();
            Level level = event.getLevel();
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof CrockBlock) {
                InteractionResult result = state.use(level, event.getEntity(), event.getHand(), event.getHitVec());
                if (result.consumesAction()) {
                    event.setCancellationResult(result);
                    event.setCanceled(true);
                }
            }
        }
    }


}
