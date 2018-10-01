package shcm.shsupercm.forge.shcmbackupreborn.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.WorldSummary;

import java.awt.*;
import java.io.IOException;

public class GuiBackups extends GuiScreen {
    private final WorldSummary worldSummary;
    private final GuiScreen parent;

    public GuiBackups(WorldSummary worldSummary, GuiScreen parent) {
        this.worldSummary = worldSummary;
        this.parent = parent;
    }
    @Override public boolean doesGuiPauseGame() { return true; }

    @Override
    public void initGui() {
        addButton(new GuiButton(-1, 5, 5, 50, 20, I18n.format("gui.back")));

    }

    @Override
    public void onGuiClosed() {
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case -1:
                mc.displayGuiScreen(parent);
                return;
        }
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(fontRenderer,worldSummary.getDisplayName(),width/2,height/2-30, Color.white.getRGB());
        drawCenteredString(fontRenderer,worldSummary.getFileName(),width/2,height/2-10, Color.lightGray.getRGB());
    }
}
