package com.envyclient.core.impl.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.realms.RealmsBridge;

public class DisconnectCommand implements Command {

    @Override
    public boolean onCommand(String[] strings) {

        Minecraft mc = Minecraft.getMinecraft();

        // minecraft code for disconnecting from singleplayer or multiplayer
        boolean flag = mc.isIntegratedServerRunning();
        boolean flag1 = mc.func_181540_al();
        mc.theWorld.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);

        if (flag) {
            mc.displayGuiScreen(new GuiMainMenu());
        } else if (flag1) {
            RealmsBridge realmsbridge = new RealmsBridge();
            realmsbridge.switchToRealms(new GuiMainMenu());
        } else {
            mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
        }

        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "disconnect" + ChatFormatting.GRAY + " ]";
    }
}
