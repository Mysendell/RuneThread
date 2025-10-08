package io.github.runethread.gui.screens;

import io.github.runethread.gui.AbstractScreen;
import io.github.runethread.gui.menus.TempleAltarMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TempleAltarScreen extends AbstractScreen<TempleAltarMenu> {
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
