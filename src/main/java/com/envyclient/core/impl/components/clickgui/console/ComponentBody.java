package com.envyclient.core.impl.components.clickgui.console;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.util.render.FontUtils;
import com.envyclient.core.util.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static com.envyclient.core.Envy.Colors.*;
import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class ComponentBody extends GuiComponent {

    private ComponentTitle title;
    private double scrollIndex;

    public ComponentBody(ComponentTitle title, int width, int height) {
        super(0, 0, width, height);
        this.title = title;

        // adding the text input component
        children.add(new ComponentInput(this, width, 15));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(!title.isOpen()){
            return;
        }

        setPosition(title.getX(), title.getY() + title.getHeight());

        checkForScroll();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.prepareScissorBox(x, y, x + width, y + height);

        Gui.drawRect(x, y, x + width, y + height, SECONDARY_BRIGHTER);

        double offset = 1;
        for (String text : CLICK_GUI.getConsole().getContents()) {
            FontUtils.drawStringWithShadow(text, x + 2, y + offset - scrollIndex, -1);
            offset += 10;
        }
        renderScrollBar();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        if(!title.isOpen()){
            return;
        }
        super.initGui();
    }

    @Override
    public void updateScreen() {
        if(!title.isOpen()){
            return;
        }
        super.updateScreen();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(!title.isOpen()){
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if(!title.isOpen()){
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick) {
        if(!title.isOpen()){
            return;
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(!title.isOpen()){
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        if(!title.isOpen()){
            return;
        }
        super.onGuiClosed();
    }

    private void checkForScroll() {
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0 && !this.inInArea(10 + getContentHeight())) {
                this.scrollIndex += 16;
                if (this.scrollIndex < 0) {
                    this.scrollIndex = 0;
                }
            } else if (wheel > 0) {
                this.scrollIndex -= 16;
                if (this.scrollIndex < 0) {
                    this.scrollIndex = 0;
                }
            }
        }
    }

    private boolean inInArea(int y) {
        return y - this.scrollIndex <= this.height + 10;
    }

    private int getContentHeight() {
        return CLICK_GUI.getConsole().getContents().size() * (10);
    }

    private void renderScrollBar() {
        int j1 = Math.max(0, this.getContentHeight() - (int) ((this.y + this.height) - this.y - 4));

        if (j1 > 0) {
            double k1 = ((this.y + this.height) - this.y) * ((this.y + this.height) - this.y) / this.getContentHeight();
            k1 = MathHelper.clamp_int((int) k1, 32, (int) ((this.y + this.height) - this.y - 8));
            double l1 = this.scrollIndex * ((this.y + this.height) - this.y - k1) / j1 + this.y;

            if (l1 < this.y) {
                l1 = this.y;
            }

            Gui.drawRect(this.x + this.width - 3, this.y, this.x + this.width, (this.y + this.height), SECONDARY);
            Gui.drawRect(this.x + this.width - 3, l1, this.x + this.width, l1 + k1, MAIN);
        }
    }

    public boolean canFit() {
        return (CLICK_GUI.getConsole().getContents().size() * 10 > height);
    }

    public double getScrollIndex() {
        return scrollIndex;
    }

    public void setScrollIndex(double scrollIndex) {
        this.scrollIndex = scrollIndex;
    }
}