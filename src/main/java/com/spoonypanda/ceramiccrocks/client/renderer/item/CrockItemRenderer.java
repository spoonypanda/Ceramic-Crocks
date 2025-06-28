package com.spoonypanda.ceramiccrocks.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.spoonypanda.ceramiccrocks.block.custom.CrockBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrockItemRenderer extends BlockEntityWithoutLevelRenderer {
    public CrockItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack,
                             MultiBufferSource bufferSource, int light, int overlay) {
        Block block = ((BlockItem) stack.getItem()).getBlock();
        BlockState state = block.defaultBlockState();

        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        DyeColor dye = tag != null && tag.contains("dyecolor") ? DyeColor.byName(tag.getString("dyecolor"), null) : null;

        if (dye != null && state.hasProperty(CrockBlock.DYE_COLOR)) {
            state = state.setValue(CrockBlock.DYE_COLOR, dye);
        }

        poseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, light, overlay);
        poseStack.popPose();
    }
}