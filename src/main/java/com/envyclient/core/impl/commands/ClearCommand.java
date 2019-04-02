package com.envyclient.core.impl.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;

import static com.envyclient.core.Envy.Managers.CLICK_GUI;

public class ClearCommand implements Command {

    @Override
    public boolean onCommand(String[] args) { // clear
        CLICK_GUI.getConsole().getContents().clear();
        CLICK_GUI.getConsole().getBody().setScrollIndex(0);
        return true;
    }


    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "clear" + ChatFormatting.GRAY + " ]";
    }

}
