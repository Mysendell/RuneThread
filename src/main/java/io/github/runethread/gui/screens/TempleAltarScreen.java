package io.github.runethread.gui.screens;

import io.github.runethread.gui.Screen;
import io.github.runethread.gui.menus.TempleAltarMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TempleAltarScreen extends Screen<TempleAltarMenu> {
    public TempleAltarScreen(TempleAltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = menu.getBackgroundTexture();
        offsetX = 16;
        offsetY = 2;
        imageWidth = 224;
        imageHeight = 209;
        inventoryLabelY = 114;
    }
}
