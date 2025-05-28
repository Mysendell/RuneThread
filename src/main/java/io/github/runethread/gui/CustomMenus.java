package io.github.runethread.gui;

import io.github.runethread.RuneThread;
import io.github.runethread.gui.menus.AnimatorMenu;
import io.github.runethread.gui.menus.ArcaneMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, RuneThread.MODID);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                              IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneMenu>> ARCANE_MENU =
            registerMenuType("arcane_menu", ArcaneMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<AnimatorMenu>> ANIMATOR_MENU =
            registerMenuType("animator_menu", AnimatorMenu::new);
}
