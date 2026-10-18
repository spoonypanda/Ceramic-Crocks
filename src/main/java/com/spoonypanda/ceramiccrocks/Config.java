package com.spoonypanda.ceramiccrocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = CeramicCrocks.MOD_ID)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ALLOW_DYE = BUILDER
            .comment("Allow the dyeing of Crocks (set to false if you hate fun)")
            .define("allowDye", true);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> CROCK_BLACKLIST = BUILDER
            .comment("List the items you don't want to allow in your crocks. Format: modid:item_name")
            .comment("CROCK_BLACKLISTED tag is also a thing, if you prefer to do it through datapacks.")
            .comment("Remove the default crocks at your own risk, your minecraft universe may collapse due to crockception.")
            .defineListAllowEmpty(
                    "crockBlacklist",
                    List.of(
                            "ceramiccrocks:small_crock",
                            "ceramiccrocks:large_crock"
                    ),
                    Config::validateItemName
            );


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean allowDye;
    public static Set<ResourceLocation> crockBlacklist = Set.of();


    private static boolean validateItemName(final Object obj) {
        if (!(obj instanceof String s)) return false;
        ResourceLocation id = ResourceLocation.tryParse(s);
        return id != null && BuiltInRegistries.ITEM.containsKey(id);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        allowDye = ALLOW_DYE.get();

        crockBlacklist = CROCK_BLACKLIST.get().stream()
                .map(ResourceLocation::parse)
                .filter(BuiltInRegistries.ITEM::containsKey)
                .collect(Collectors.toUnmodifiableSet());
    }
}
