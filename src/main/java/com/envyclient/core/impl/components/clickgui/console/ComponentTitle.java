package com.envyclient.core.impl.components.clickgui.console;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.util.render.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static com.envyclient.core.Envy.Colors.MAIN;
import static com.envyclient.core.Envy.Colors.SECONDARY;
import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class ComponentTitle extends GuiComponent {

    private double dragX, dragY;
    private boolean moving;
    private boolean open;

    public ComponentTitle(String name, int y, int width, int height) {
        super(name, 10, y, width, height);

        // adding the body component
        getChildren().add(new ComponentBody(this, width, 210));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();

        if (moving) {
            setPosition(mouseX - dragX, mouseY - dragY);
        }

        Gui.drawRect(x, y, x + width, y - 2, MAIN);
        Gui.drawRect(x, y, x + width, y + height, SECONDARY);

        // title text
        float scale = 2;
        GlStateManager.scale(scale, scale, scale);
        FontUtils.drawStringWithShadow(name, (x + 5) / scale, (y + 5) / scale, -1);
        GlStateManager.scale(1, 1, 1);

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovered) {
            switch (mouseButton) {
                case 1: {
                    if (open) {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("tile.piston.in"), 1));
                        open = false;
                    } else {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("tile.piston.out"), 1));
                        open = true;
                    }
                    break;
                }
                case 0: {
                    List<GuiComponent> componentsList = CLICK_GUI.getClickGUI().getComponentList();
                    componentsList.remove(this);
                    componentsList.add(this);

                    moving = true;
                    dragX = mouseX - x;
                    dragY = mouseY - y;
                    break;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        moving = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}