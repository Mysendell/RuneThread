package io.github.runethread.gui.screens;

import io.github.runethread.gui.AbstractScreen;
import io.github.runethread.gui.menus.RuneEtcherMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RuneEtcherScreen extends AbstractScreen<RuneEtcherMenu> {
    public RuneEtcherScreen(RuneEtcherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 224;
        imageHeight = 209;
        inventoryLabelY = 114;
    }
}
