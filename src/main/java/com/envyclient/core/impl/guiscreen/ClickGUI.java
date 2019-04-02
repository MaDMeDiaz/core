package com.envyclient.core.impl.guiscreen;

import com.envyclient.core.Envy;
import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.api.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGUI extends GuiScreen {

    private List<GuiComponent> componentList = new CopyOnWriteArrayList<>();

    @Override
    public void initGui() {
        componentList.forEach(GuiComponent::initGui);
    }

    @Override
    public void updateScreen() {
        componentList.forEach(GuiComponent::updateScreen);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        componentList.forEach(c -> c.drawScreen(mouseX, mouseY, partialTicks));
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        componentList.forEach(c -> c.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        componentList.forEach(c -> c.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        componentList.forEach(c -> c.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }
        componentList.forEach(c -> c.keyTyped(typedChar, keyCode));
    }

    @Override
    public void onGuiClosed() {
        componentList.forEach(GuiComponent::onGuiClosed);

        // turning off the clickgui module
        Module clickGUI = Envy.Managers.MODULE.getModule(com.envyclient.core.impl.modules.ClickGUI.class);
        if (clickGUI != null && clickGUI.isToggled()) {
            clickGUI.toggle();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public List<GuiComponent> getComponentList() {
        return componentList;
    }
}