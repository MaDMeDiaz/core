package com.envyclient.core.impl.components.clickgui;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.api.module.Module;
import com.envyclient.core.util.render.FontUtils;
import com.envyclient.core.util.PlayerUtils;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.List;

import static com.envyclient.core.Envy.Colors.MAIN;
import static com.envyclient.core.Envy.Colors.SECONDARY;
import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class ComponentModule extends GuiComponent {

    private ComponentPanel parent;
    private Module module;
    private int moduleIndex;

    public ComponentModule(ComponentPanel parent, Module module, int height, int moduleIndex) {
        super(module.getName(), 0, 0, parent.getWidth() - 5, height);
        this.parent = parent;
        this.module = module;
        this.moduleIndex = moduleIndex;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        setPosition(parent.getX(), (parent.getY() + 25 + 2) + ((height + 2) * moduleIndex));

        // extra hover check so you can't toggle modules from to panels at once
        List<GuiComponent> componentsList = CLICK_GUI.getClickGUI().getComponentList();
        if (componentsList.indexOf(parent) != componentsList.size() - 1 && componentsList.get(componentsList.size() - 1).isHovered()) {
            hovered = false;
        }

        // drawing the background
        Gui.drawRect(x + 5, y, x + width, y + height, module.isToggled() ? MAIN : SECONDARY);

        if (hovered) {
            Gui.drawRect(x + 5, y, x + width, y + height, new Color(37, 37, 37, 195).brighter().getRGB());
        }

        // drawing the module name
        FontUtils.drawTotalCenteredStringWithShadow(name, x + (width / 2), y + (height / 2) + 1, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        // toggle the module and play the button sound
        if (hovered && mouseButton == 0) {
            module.toggle();
            PlayerUtils.playButtonSound();
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
