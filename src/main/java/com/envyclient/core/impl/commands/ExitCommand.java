package com.envyclient.core.impl.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;
import net.minecraft.client.Minecraft;

public class ExitCommand implements Command {

    @Override
    public boolean onCommand(String[] strings) {
        Minecraft.getMinecraft().shutdown();
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "exit" + ChatFormatting.GRAY + " ]";
    }
}
