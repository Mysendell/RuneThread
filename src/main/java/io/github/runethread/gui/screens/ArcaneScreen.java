package io.github.runethread.gui.screens;

import io.github.runethread.gui.AbstractScreen;
import io.github.runethread.gui.menus.ArcaneMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneScreen extends AbstractScreen<ArcaneMenu> {
    public ArcaneScreen(ArcaneMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = menu.getBackgroundTexture();
    }
}