package com.envyclient.core.impl.commands;

import com.envyclient.core.util.PlayerUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;

import static com.envyclient.core.Envy.Managers.COMMAND;

public class HelpCommand implements Command {

    @Override
    public boolean onCommand(String[] args) {
        PlayerUtils.sendConsoleReply("Here is a list of all the COMMANDS:");
        COMMAND.getCommandManager().getCommandMap().values().forEach(command -> PlayerUtils.sendConsoleReply(command.usage()));
        return true;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "help" + ChatFormatting.GRAY + " ]";
    }

}
