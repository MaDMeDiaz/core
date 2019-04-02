package net.minecraft.client.gui;

import com.envyclient.core.impl.guiscreen.AltLogin;
import com.envyclient.core.util.PlayerUtils;
import com.envyclient.core.util.Timer;
import com.envyclient.core.util.render.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.envyclient.core.Envy.Colors.MAIN;
import static com.envyclient.core.Envy.Info.UPDATE_TEXT;
import static com.envyclient.core.Envy.Info.ISSUES_TEXT;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private final List<ImageButton> locations = new ArrayList<>();

    @Override
    public void initGui() {
        locations.clear();
        locations.add(new ImageButton(new ResourceLocation("textures/custom/buttons/single.png"), new GuiSelectWorld(this)));
        locations.add(new ImageButton(new ResourceLocation("textures/custom/buttons/multi.png"), new GuiMultiplayer(this)));
        locations.add(new ImageButton(new ResourceLocation("textures/custom/buttons/alt.png"), new AltLogin()));
        locations.add(new ImageButton(new ResourceLocation("textures/custom/buttons/options.png"), new GuiOptions(this, mc.gameSettings)));
        locations.add(new ImageButton(new ResourceLocation("textures/custom/buttons/exit.png"), null));

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int size = width / 4;
        int startX = ((width + size) / 2) - (sr.getScaledWidth() / 10) / 2;
        int startY = ((height - (locations.size() * (sr.getScaledWidth() / 10))) / 2);
        int i = 0;
        for (ImageButton bt : locations) {
            bt.setX(startX);
            bt.setY(startY + (sr.getScaledWidth() / 10) * i);
            i++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // rendering background
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/custom/background.png"));
        drawScaledCustomSizeModalRect(0, 0, 0, 0, width, height, width, height, width, height);

        // rendering the logo
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int size = width / 4;
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/custom/logo.png"));
        drawScaledCustomSizeModalRect((width - size) / 2 - (sr.getScaledWidth() / 10) - size / 5, (height - size) / 2, 0, 0, size, size, size, size, size, size);
        locations.forEach(imageButton -> imageButton.render(mouseX, mouseY));

        // rendering the middle line
        int lineSize = sr.getScaledHeight() / 5 - 15;
        drawGradientRect(width / 2 - 1, ((height - lineSize) / 2 + lineSize / 2) - lineSize, width / 2 + 1, ((height - lineSize) / 2 + lineSize / 2), new Color(0, 255, 0, 0).getRGB(), MAIN);
        drawGradientRect(width / 2 - 1, ((height - lineSize) / 2 + lineSize / 2), width / 2 + 1, ((height - lineSize) / 2 + lineSize / 2) + lineSize, MAIN, new Color(0, 255, 0, 0).getRGB());

        // update text string
        fontRendererObj.drawString(UPDATE_TEXT, 5, height - (FontUtils.getFontHeight() * 2) - 2, -1);
        fontRendererObj.drawString(ISSUES_TEXT, 5, height - FontUtils.getFontHeight() - 2, -1);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        locations.forEach(imageButton -> imageButton.click(mouseX, mouseY, mouseButton));
    }

    private class ImageButton {

        private int x, y, size;
        private ResourceLocation loc;
        private Timer timer;
        private GuiScreen s;
        private float motionAnimation;

        ImageButton(ResourceLocation loc, GuiScreen s) {
            this.loc = loc;
            this.s = s;

            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            x = 0;
            y = 0;
            size = sr.getScaledWidth() / 10;

            timer = new Timer();
            motionAnimation = 0;
        }

        private boolean isHovered(int mouseX, int mouseY) {
            return (mouseX >= x && mouseX <= (x + size) && mouseY >= y && mouseY <= (y + size));
        }

        public void click(int mouseX, int mouseY, int mouseButton) {
            if (isHovered(mouseX, mouseY) && mouseButton == 0) {
                if (s == null) {
                    Minecraft.getMinecraft().shutdown();
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(s);
                }
                PlayerUtils.playButtonSound();
            }
        }

        private void update(int mouseX, int mouseY) {
            if (timer.hasReached(2)) {
                if (isHovered(mouseX, mouseY)) {
                    motionAnimation += 2;
                    if (motionAnimation > 3)
                        motionAnimation = 3;
                } else {
                    motionAnimation--;
                    if (motionAnimation < 0)
                        motionAnimation = 0;
                }
            }
        }

        public void render(int mouseX, int mouseY) {
            update(mouseX, mouseY);

            Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
            Gui.drawModalRectWithCustomSizedTexture(x, (int) (y + motionAnimation), 0, 0, size, size, size, size);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }
}