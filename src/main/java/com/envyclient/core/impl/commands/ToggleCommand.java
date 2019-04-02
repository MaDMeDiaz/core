package com.envyclient.core.impl.commands;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.util.PlayerUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;

import static com.envyclient.core.Envy.Managers.MODULE;

public class ToggleCommand implements Command {

    @Override
    public boolean onCommand(String[] args) {

        if (args.length == 1) {
            Module m = MODULE.getModule(args[0]);
            if (m != null) {
                m.toggle();
                PlayerUtils.sendConsoleReply(m.getName() + (m.isToggled() ? ChatFormatting.GREEN + " on." : ChatFormatting.RED + " off."));
            } else {
                PlayerUtils.sendConsoleReply("Module not found.");
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "toggle <module>" + ChatFormatting.GRAY + " ]";
    }
}
