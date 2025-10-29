package io.github.runethread.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;


/**
 * An abstract class for creating custom GUI screens.
 * Provides basic defaults for rendering the GUI background.
 * @param <T> the type of the related menu
 */
public class AbstractScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected static ResourceLocation GUI_TEXTURE;
    /** The width of the texture file. Default is 256.
     */
    protected static int textureWidth;
    /** The height of the texture file. Default is 256.
     */
    protected static int textureHeight;
    /** The width of the GUI image inside the texture. Default is 176.
     */
    protected static int imageWidth;
    /** The height of the GUI image inside the texture. Default is 166.
     */
    protected static int imageHeight;
    /** The x offset of the GUI image inside the texture. Default is 0.
     */
    protected static int offsetX;
    /** The y offset of the GUI image inside the texture. Default is 0.
     */
    protected static int offsetY;

    /**
     * Constructor for the AbstractScreen.
     * @param menu the related menu
     * @param playerInventory the player's inventory
     * @param title the title of the GUI
     */
    public AbstractScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        offsetX = 0;
        offsetY = 0;
        textureWidth = 256;
        textureHeight = 256;
    }


    /**
     * Renders the background of the GUI, including the texture.
     * @param guiGraphics the GuiGraphics instance
     * @param pPartialTick partial tick time
     * @param pMouseX mouse X position
     * @param pMouseY mouse Y position
     */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x, y, offsetX, offsetY, imageWidth, imageHeight, textureWidth, textureHeight);
    }

    /**
     * Renders the screen and all the non-background components in it, including the tooltips.
     * @param guiGraphics the GuiGraphics instance
     * @param mouseX mouse X position
     * @param mouseY mouse Y position
     * @param partialTick partial tick time
     */
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
