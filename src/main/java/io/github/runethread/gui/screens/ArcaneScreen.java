package io.github.runethread.gui.screens;

import io.github.runethread.RuneThread;
import io.github.runethread.gui.Screen;
import io.github.runethread.gui.menus.ArcaneMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneScreen extends Screen<ArcaneMenu> {
    public ArcaneScreen(ArcaneMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(RuneThread.MODID, "textures/gui/arcane_table.png");
    }
}