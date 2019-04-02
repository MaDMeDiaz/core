package com.envyclient.core.impl.guiscreen;

import com.envyclient.core.api.alt.Alt;
import com.envyclient.core.util.render.FontUtils;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.Proxy;

import static com.envyclient.core.Envy.ThirdParty.EXECUTOR_SERVICE;

public class AltLogin extends GuiScreen {

    private String status = ChatFormatting.GRAY + "Idle...";
    private GuiTextField username, password, combo;
    private GuiButton add;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        buttonList.add(add = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 92 + 12 + 25, "Back"));

        username = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        password = new GuiTextField(eventButton, fontRendererObj, width / 2 - 100, 94, 200, 20).setPassword(true);
        combo = new GuiTextField(eventButton, mc.fontRendererObj, width / 2 - 100, 128 + 5, 200, 20).setMaxStringLength(200);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 0: {
                if (!combo.getText().isEmpty()) {
                    String[] args = combo.getText().split(":");
                    EXECUTOR_SERVICE.submit(() -> login(new Alt(args[0], args[1])));
                } else {
                    EXECUTOR_SERVICE.submit(() -> login(new Alt(username.getText(), password.getText())));
                }
                break;
            }
            case 1: {
                mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
        }

    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);

        handleAddButton();
        drawDefaultBackground();

        /** Render the buttons. **/
        super.drawScreen(mouseX, mouseY, partialTicks);

        username.drawTextBox();
        password.drawTextBox();
        combo.drawTextBox();

        String statusString = "Status: " + status;
        FontUtils.drawStringWithShadow(statusString, width / 2 - (FontUtils.getStringWidth(statusString) / 2), 8, -1);

        FontUtils.drawCenteredStringWithShadow("Alt Login", this.width / 2, 40, -1);

        if (username.getText().isEmpty())
            FontUtils.drawString("Username / E-Mail", this.width / 2 - 96, 66, Color.GRAY.getRGB());

        if (password.getText().isEmpty())
            FontUtils.drawString("Password", this.width / 2 - 96, 100, Color.GRAY.getRGB());

        if (combo.getText().isEmpty())
            FontUtils.drawString("EMAIL:PASSWORD", this.width / 2 - 96, 134 + 5, Color.GRAY.getRGB());

        GL11.glPopMatrix();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);


        if (keyCode == Keyboard.KEY_TAB) {
            if (!username.isFocused() && !password.isFocused() && !combo.isFocused()) {
                username.setFocused(true);
            } else {
                if (username.isFocused()) {
                    username.setFocused(false);
                    password.setFocused(true);
                } else if (password.isFocused()) {
                    password.setFocused(false);
                    combo.setFocused(true);
                } else if (combo.isFocused()) {
                    combo.setFocused(false);
                    username.setFocused(true);
                }
            }
        }

        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
        combo.textboxKeyTyped(typedChar, keyCode);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
        combo.mouseClicked(mouseX, mouseY, mouseButton);
    }


    private void handleAddButton() {
        if (username.getText().isEmpty() && password.getText().isEmpty() && combo.getText().isEmpty())
            add.enabled = false;
        else if (username.getText().isEmpty() && password.getText().isEmpty() && !combo.getText().isEmpty() && combo.getText().split(":").length >= 2)
            add.enabled = true;
        else if (!username.getText().isEmpty() && combo.getText().isEmpty())
            add.enabled = true;
        else if (!username.getText().isEmpty() && !combo.getText().isEmpty())
            add.enabled = false;
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void login(Alt alt) {

        String username = alt.getEmail();
        String password = alt.getPassword();

        if (password.equals("")) {
            mc.setSession(new Session(username, "", "", "mojang"));
            status = ChatFormatting.GREEN + "Logged in. (" + username + " - offline name)";
            return;
        }

        status = ChatFormatting.YELLOW + "Logging in...";
        Session auth = createSession(username, password);

        if (auth == null) {
            status = ChatFormatting.RED + "Login failed!";
        } else {
            status = ChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            mc.setSession(auth);
        }

    }

}
