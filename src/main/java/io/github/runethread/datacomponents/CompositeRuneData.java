package io.github.runethread.datacomponents;

import io.github.runethread.util.runeutils.ModifierMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public record CompositeRuneData(ItemStack mainRune, ModifierMap modifiers) {
        public CompositeRuneData(ItemStack itemStack, Map<ResourceLocation, ItemStack> modifiers){
        this(
                itemStack,
                (ModifierMap) modifiers
        );
    }
}
