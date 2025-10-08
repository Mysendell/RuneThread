package io.github.runethread.gui.screens;

import io.github.runethread.gui.AbstractScreen;
import io.github.runethread.gui.menus.AnimatorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AnimatorScreen extends AbstractScreen<AnimatorMenu> {
    private final int MAX_FUEL = 20000; //Equivalent to one lava bucket
    private final int FUELBAR_BASE_HEIGHT = 14;
    private final int COOKBAR_BASE_WIDTH = 24;
    private final int COOKBAR_BASE_HEIGHT = 17;


    public AnimatorScreen(AnimatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = menu.getBackgroundTexture();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int fuelBarX = leftPos + 18;
        int FuelBarY = topPos + 40;
        int progressBarX = leftPos + 68;
        int progressBarY = topPos + 39;
        int fuel = menu.getFuelBurnTime();
        int burn = menu.getBurnTime();
        int totalBurn = menu.getRecipeBurnTime();

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int fuelBarHeight = (int) (fuel * FUELBAR_BASE_HEIGHT/ (float) MAX_FUEL);
        int cookBarWidth = (int) (burn * COOKBAR_BASE_WIDTH/ (float) totalBurn);
        int fuelBarTextureY = (FUELBAR_BASE_HEIGHT - 1) - fuelBarHeight;

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, leftPos + 18, topPos + 40 + fuelBarTextureY, 176, fuelBarTextureY, FUELBAR_BASE_HEIGHT, fuelBarHeight, textureWidth, textureHeight);

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, leftPos + 68, topPos + 39, 176, 14, cookBarWidth, COOKBAR_BASE_HEIGHT, textureWidth, textureHeight);

        if (isMouseOverFuelBar(mouseX, mouseY, fuelBarX, FuelBarY)) {
            Component tooltip = Component.literal("Fuel: " + fuel + "/" + MAX_FUEL + " ticks");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverProgressBar(mouseX, mouseY, progressBarX, progressBarY)) {
            Component tooltip = Component.literal("Burning: " + burn + "/" + totalBurn + " ticks");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    private boolean isMouseOverFuelBar(double mouseX, double mouseY, int barX, int barY) {
        return mouseX >= barX && mouseX < barX + FUELBAR_BASE_HEIGHT &&
                mouseY >= barY && mouseY < barY + FUELBAR_BASE_HEIGHT;
    }

    private boolean isMouseOverProgressBar(double mouseX, double mouseY, int barX, int barY) {
        return mouseX >= barX && mouseX < barX + COOKBAR_BASE_WIDTH &&
                mouseY >= barY && mouseY < barY + COOKBAR_BASE_HEIGHT;
    }
}
