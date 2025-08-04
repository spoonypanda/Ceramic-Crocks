package com.spoonypanda.ceramiccrocks.util;

import com.spoonypanda.ceramiccrocks.CeramicCrocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> STORAGE_CROCK = tag("storage_crock");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CeramicCrocks.MOD_ID, name));
        }
    }

    public static class Items{
        public static final TagKey<Item> UNFIRED_CROCK = tag("unfired_crock");
        public static final TagKey<Item> CROCK_BLACKLISTED = tag("crock_blacklisted");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CeramicCrocks.MOD_ID, name));
        }
    }
}
