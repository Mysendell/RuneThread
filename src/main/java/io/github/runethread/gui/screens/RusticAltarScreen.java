package io.github.runethread.gui.screens;

import io.github.runethread.gui.Screen;
import io.github.runethread.gui.menus.RusticAltarMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RusticAltarScreen extends Screen<RusticAltarMenu> {
    public RusticAltarScreen(RusticAltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = menu.getBackgroundTexture();
        offsetX = 40;
        offsetY = 45;
        imageWidth = 176;
        imageHeight = 166;
    }
}
