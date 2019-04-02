package com.envyclient.core.impl.components;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.util.render.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class ComponentButton extends GuiComponent {

    private boolean hovered;
    private boolean disabled;
    private Color color;

    public ComponentButton(String name, double x, double y, int width, int height, Color color) {
        super(name, x, y, width, height);
        this.hovered = false;
        this.disabled = false;
        this.color = color;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        hovered = (mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height));

        Gui.drawRect(x, y, x + width, y + height,
                disabled ? Integer.MIN_VALUE : hovered ?
                        color.brighter().getRGB() : color.getRGB());
        FontUtils.drawTotalCenteredStringWithShadow(name, x + (width / 2), y + (height / 2) + 1, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!disabled && hovered && mouseButton == 0) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1));
        }
    }

    public boolean isHovered() {
        if (disabled) {
            return false;
        }
        return hovered;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}