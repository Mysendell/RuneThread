package io.github.runethread.util;

import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ModifierMap extends HashMap<String, Object> {
    public ItemStack getModifierRune(Modifiers modifierName) {
        Object modifierRune = this.get(modifierName.toString());
        if (modifierRune instanceof ItemStack stack)
            return stack;
        return null;
    }

    public <T> T getModifier(Modifiers modifierName, Class<T> clazz) {
        Object modifier = this.get(modifierName.toString());
        if (clazz.isInstance(modifier))
            return clazz.cast(modifier);
        return null;
    }

    public <T> List<T> getAllOfType(Class<T> clazz) {
        List<T> results = this.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
        return results;
    }
}
