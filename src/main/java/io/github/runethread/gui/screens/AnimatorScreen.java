package io.github.runethread.gui.screens;

import io.github.runethread.gui.Screen;
import io.github.runethread.gui.menus.AnimatorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AnimatorScreen extends Screen<AnimatorMenu> {
    int fuel = menu.getFuelBurnTime();
    int maxFuel = 20000; //Equivalent to one lava bucket
    int burn = menu.getBurnTime();
    int totalBurn = menu.getRecipeBurnTime();

    int fuelBarHeight = (int) (fuel * 14/ (float) maxFuel);
    int cookBarWidth = (int) (burn * 24/ (float) totalBurn);

    int fuelBarTextureY = 13 - fuelBarHeight;

    public AnimatorScreen(AnimatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        GUI_TEXTURE = menu.getBackgroundTexture();
    }

    protected void drawExtras(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        fuel = menu.getFuelBurnTime();
        maxFuel = 20000; //Equivalent to one lava bucket
        burn = menu.getBurnTime();
        totalBurn = menu.getRecipeBurnTime();

        fuelBarHeight = (int) (fuel * 14/ (float) maxFuel);
        cookBarWidth = (int) (burn * 24/ (float) totalBurn);

        fuelBarTextureY = 13 - fuelBarHeight;

        // Draw the fuel bar
        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, leftPos + 18, topPos + 40 + fuelBarTextureY, 176, fuelBarTextureY, 14, fuelBarHeight, textureWidth, textureHeight);

        // Draw the cook progress bar
        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, leftPos + 68, topPos + 39, 176, 14, cookBarWidth, 17, textureWidth, textureHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int fuelBarX = leftPos + 18;
        int FuelBarY = topPos + 40;
        int progressBarX = leftPos + 68;
        int progressBarY = topPos + 39;

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (isMouseOverFuelBar(mouseX, mouseY, fuelBarX, FuelBarY)) {
            Component tooltip = Component.literal("Fuel: " + fuel + "/" + maxFuel + " ticks");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
        if (isMouseOverProgressBar(mouseX, mouseY, progressBarX, progressBarY)) {
            Component tooltip = Component.literal("Burning: " + burn + "/" + totalBurn + " ticks");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    // Helper to check if mouse is over the fuel bar
    private boolean isMouseOverFuelBar(double mouseX, double mouseY, int barX, int barY) {
        return mouseX >= barX && mouseX < barX + 14 &&
                mouseY >= barY && mouseY < barY + 14;
    }

    private boolean isMouseOverProgressBar(double mouseX, double mouseY, int barX, int barY) {
        return mouseX >= barX && mouseX < barX + 24 &&
                mouseY >= barY && mouseY < barY + 17;
    }
}
