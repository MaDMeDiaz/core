package com.envyclient.core.api.component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GuiComponent {

    protected List<GuiComponent> children = new CopyOnWriteArrayList<>();

    protected String name;
    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected boolean hovered;

    public GuiComponent(double x, double y, int width, int height) {
        this("", x, y, width, height);
    }

    public GuiComponent(String name, double x, double y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void initGui() {
        children.forEach(GuiComponent::initGui);
    }

    public void updateScreen() {
        children.forEach(GuiComponent::updateScreen);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // hover check
        hovered = mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);

        children.forEach(guiComponent -> guiComponent.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        children.forEach(guiComponent -> guiComponent.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        children.forEach(guiComponent -> guiComponent.mouseReleased(mouseX, mouseY, state));
    }

    public void mouseClickMove(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick) {
        children.forEach(guiComponent -> guiComponent.mouseClickMove(mouseX, mouseY, mouseButton, timeSinceLastClick));
    }

    public void keyTyped(char typedChar, int keyCode) {
        children.forEach(guiComponent -> guiComponent.keyTyped(typedChar, keyCode));
    }

    public void onGuiClosed() {
        children.forEach(GuiComponent::onGuiClosed);
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHovered() {
        return hovered;
    }

    public List<GuiComponent> getChildren() {
        return children;
    }
}