package io.github.runethread.datacomponents;

import io.github.runethread.customitems.runes.MainRuneItem;
import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public record CompositeRuneData(MainRuneItem mainRune, ModifierMap modifiers) {
    public ResourceLocation mainRuneString(){
        return BuiltInRegistries.ITEM.getKey(mainRune);
    }
    public CompositeRuneData(ResourceLocation mainRuneLocation, Map<ResourceLocation, ItemStack> modifiers){
        this(
                (MainRuneItem) BuiltInRegistries.ITEM.get(mainRuneLocation).get().value(),
                (ModifierMap) modifiers
        );
    }
}
