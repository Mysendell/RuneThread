package io.github.runethread.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

import static io.github.runethread.util.ModifierRune.*;

public class ModifierMap extends HashMap<String, ItemStack> {
    public ItemStack getModifierRune(ModifierRune modifierName) {
        return this.get(modifierName.toString());
    }

    public <T> T getModifier(ModifierRune modifierName, Class<T> clazz) {
        Object modifier = this.get(modifierName.toString());
        if (clazz.isInstance(modifier))
            return clazz.cast(modifier);
        return null;
    }

    public boolean hasModifier(ModifierRune modifierName) {
        return this.containsKey(modifierName.toString());
    }

    public <T extends Item> List<T> getAllOfType(Class<T> clazz) {
        return this.values().stream()
                .map(ItemStack::getItem)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public <T extends Item> List<ResourceLocation> getAllIdentifiersOfType(Class<T> clazz) {
        return this.values().stream()
                .map(ItemStack::getItem)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .map(BuiltInRegistries.ITEM::getKey)
                .toList();
    }

    public ModifierRune getShape(){
        if(hasModifier(SQUARE_RUNE))
            return SQUARE_RUNE;
        if(hasModifier(CIRCLE_RUNE))
            return CIRCLE_RUNE;
        return SPHERE_RUNE;
    }
}
