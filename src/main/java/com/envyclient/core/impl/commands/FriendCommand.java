package com.envyclient.core.impl.commands;

import com.envyclient.core.util.PlayerUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;

import static com.envyclient.core.Envy.Managers.FRIEND;

public class FriendCommand implements Command {

    @Override
    public boolean onCommand(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
            FRIEND.clear();
            PlayerUtils.sendConsoleReply("All friends removed.");
            return true;
        } else if (args.length == 2) {
            String command = args[0];
            String name = args[1];
            return handleFriend(command, name, name);
        } else if (args.length == 3) {
            String command = args[0];
            String name = args[1];
            String nickname = args[2];
            return handleFriend(command, name, nickname);
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "friend <add/remove/clear> <name> <alias>" + ChatFormatting.GRAY + " ]";
    }

    private boolean handleFriend(String command, String name, String nickname) {
        switch (command.toLowerCase()) {
            case "add": {
                FRIEND.addFriend(name, nickname);
                PlayerUtils.sendConsoleReply("Added '" + name + "'.");
                return true;
            }
            case "remove": {
                FRIEND.deleteFriend(name);
                PlayerUtils.sendConsoleReply("Deleted '" + name + "'.");
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
