package io.github.runethread.customTags;

import io.github.runethread.RuneThread;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class RuneTags {
    public static class Items {
        public static final TagKey<Item> RUNE_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "rune_tag"));
        public static final TagKey<Item> LOCATON_RUNE_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "locator_rune_tag"));
        public static final TagKey<Item> POWER_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "power_tag"));
        public static final TagKey<Item> TARGET_RUNE_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "target_rune_tag"));
        public static final TagKey<Item> NATURE_RUNE_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "nature_rune_tag"));
        public static final TagKey<Item> FUNCTION_RUNE_TAG = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "function_rune_tag"));
    } // TODO Change target to modifier
}
