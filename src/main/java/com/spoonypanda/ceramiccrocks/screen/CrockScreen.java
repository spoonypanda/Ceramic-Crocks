package com.spoonypanda.ceramiccrocks.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import com.spoonypanda.ceramiccrocks.util.CrockSize;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrockScreen extends AbstractContainerScreen<CrockMenu> {
    private static final ResourceLocation LARGE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CeramicCrocks.MOD_ID, "textures/gui/large_crock_gui.png");
    private static final ResourceLocation SMALL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CeramicCrocks.MOD_ID, "textures/gui/small_crock_gui.png");

    private final ResourceLocation guiTexture;
    private final CrockSize size;

    public CrockScreen(CrockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        size = pMenu.getSize();

        this.guiTexture = switch (size) {
            case SMALL -> SMALL_TEXTURE;
            case LARGE -> LARGE_TEXTURE;
        };
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelX = 10000;
        this.inventoryLabelY = 10000;
        this.titleLabelX = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        imageWidth = 176;
        imageHeight = switch (size) {
            case SMALL -> 165;
            case LARGE -> 193;
        };

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F,1.0F,1.0F);
        RenderSystem.setShaderTexture(0,guiTexture);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(guiTexture,leftPos,topPos,0,0,imageWidth,imageHeight,256,256);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
    }
}
