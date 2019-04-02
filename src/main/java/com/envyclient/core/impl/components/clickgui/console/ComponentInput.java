package com.envyclient.core.impl.components.clickgui.console;

import com.envyclient.core.api.component.GuiComponent;
import com.envyclient.core.impl.modules.ClickGUI;
import com.envyclient.core.util.render.FontUtils;
import com.envyclient.core.util.PlayerUtils;
import me.ihaq.commandmanager.exception.CommandArgumentException;
import me.ihaq.commandmanager.exception.CommandParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import static com.envyclient.core.Envy.Colors.SECONDARY;
import static com.envyclient.core.Envy.Managers.*;

public class ComponentInput extends GuiComponent {

    private ComponentBody body;
    private GuiTextField input;

    public ComponentInput(ComponentBody body, int width, int height) {
        super(0, 0, width, height);
        this.body = body;

        // making a custom input input box
        input = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, -100, -100, width, height) {

            @Override
            public void drawTextBox() {
                if (this.getVisible()) {

                    int i = this.isEnabled ? this.enabledColor : this.disabledColor;
                    int j = this.cursorPosition - this.lineScrollOffset;
                    int k = this.selectionEnd - this.lineScrollOffset;
                    String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
                    boolean flag = j >= 0 && j <= s.length();
                    boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
                    int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
                    int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 2 : this.yPosition;
                    int j1 = l;

                    if (k > s.length()) {
                        k = s.length();
                    }

                    if (s.length() > 0) {
                        String s1 = flag ? s.substring(0, j) : s;
                        j1 = this.fontRendererInstance.drawStringWithShadow(s1, (float) l, (float) i1, i);
                    }

                    boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
                    int k1 = j1;

                    if (!flag) {
                        k1 = j > 0 ? l + this.width : l;
                    } else if (flag2) {
                        k1 = j1 - 1;
                        --j1;
                    }

                    if (s.length() > 0 && flag && j < s.length()) {
                        j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, i);
                    }

                    if (flag1) {
                        if (flag2) {
                            Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
                        } else {
                            this.fontRendererInstance.drawStringWithShadow("_", (float) k1, (float) i1, i);
                        }
                    }

                    if (k != j) {
                        int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
                        this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
                    }

                }
            }

        }.setMaxStringLength(75);
    }

    @Override
    public void initGui() {
        input.setFocused(true);
        Keyboard.enableRepeatEvents(true);
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        input.setFocused(false);
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        input.setFocused(hovered);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateCoordinates();

        // drawing the input box background
        Gui.drawRect(x, y, x + width, y + height, SECONDARY);

        // drawing the arrow beside the input box text
        FontUtils.drawStringWithShadow(">", x + 4, y + 4, -1);

        // drawing the input box text
        input.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        Keyboard.enableRepeatEvents(true);

        if (keyCode == Keyboard.KEY_RETURN) {
            if (!input.getText().isEmpty()) {

                CLICK_GUI.getConsole().addReply(input.getText(), false);
                PlayerUtils.playButtonSound();

                try {
                    COMMAND.getCommandManager().parseCommand("-" + input.getText());
                } catch (CommandParseException | CommandArgumentException e) {
                    CLICK_GUI.getConsole().addReply(e.getMessage(), true);
                }

                if (CLICK_GUI.getConsole().getContents().size() >= 50) {
                    CLICK_GUI.getConsole().getContents().remove(0);
                    body.setScrollIndex(body.getScrollIndex() - 10);
                }

                input.setText("");
            }
        } else if (keyCode == Keyboard.KEY_UP) {
            for (int i = CLICK_GUI.getConsole().getContents().size() - 1; i >= 0; i--) {
                String command = CLICK_GUI.getConsole().getContents().get(i);
                if (command.startsWith("-")) {
                    input.setText(command.replace("-", ""));
                    break;
                }
            }
        } else if (!(keyCode == MODULE.getModule(ClickGUI.class).getKeyCode())) {
            input.textboxKeyTyped(typedChar, keyCode);
        }

        super.keyTyped(typedChar, keyCode);
    }

    private void updateCoordinates() {
        setPosition(body.getX(), body.getY() + body.getHeight() - 1);

        if (input != null) {
            input.xPosition = (int) x + 8;
            input.yPosition = (int) y + 1;
        }
    }

}