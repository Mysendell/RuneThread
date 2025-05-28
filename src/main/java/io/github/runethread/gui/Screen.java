package io.github.runethread.gui;

import io.github.runethread.RuneThread;
import io.github.runethread.gui.menus.ArcaneMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class Screen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected static ResourceLocation GUI_TEXTURE;
    protected static int textureWidth = 256;
    protected static int textureHeight = 256;
    protected static int offsetX = 0;
    protected static int offsetY = 0;

    public Screen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x, y, offsetX, offsetY, imageWidth, imageHeight, textureWidth, textureHeight);
        drawExtras(guiGraphics, pMouseX, pMouseY);
    }

    protected void drawExtras(GuiGraphics guiGraphics, int mouseX, int mouseY){
        // Override this method in subclasses to draw additional elements
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
