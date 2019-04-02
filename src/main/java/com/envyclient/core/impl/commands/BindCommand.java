package com.envyclient.core.impl.commands;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.impl.files.ModulesFile;
import com.envyclient.core.impl.modules.ClickGUI;
import com.envyclient.core.util.PlayerUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.ihaq.commandmanager.Command;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static com.envyclient.core.Envy.Managers.FILE;
import static com.envyclient.core.Envy.Managers.MODULE;

public class BindCommand implements Command {

    @Override
    public boolean onCommand(String[] args) {

        if (args.length == 3 && args[0].equalsIgnoreCase("add")) { // add module key

            String command = args[0];
            Module module = MODULE.getModule(args[1]);
            String key = args[2];

            if (module != null)
                return handleBind(command, module, key);

        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) { // remove module

            String command = args[0];
            Module module = MODULE.getModule(args[1]);

            if (module != null)
                return handleBind(command, module, null);

        } else if (args.length == 1 && args[0].equalsIgnoreCase("clear")) { // clear

            String command = args[0];
            return handleBind(command, null, null);

        }

        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "bind <add/remove/clear> <module> <key>" + ChatFormatting.GRAY + " ]";
    }

    private boolean handleBind(String command, Module module, String key) {
        switch (command.toLowerCase()) {
            case "add": {
                module.setKeyCode(Keyboard.getKeyIndex(key.toUpperCase()));
                PlayerUtils.sendConsoleReply("The Bind for " + module.getName() + " has been set to " + Keyboard.getKeyName(module.getKeyCode()) + ".");
                saveFile();
                return true;
            }
            case "remove": {
                module.setKeyCode(Keyboard.KEY_NONE);
                PlayerUtils.sendConsoleReply("The Bind for " + module.getName() + " has been set to " + Keyboard.getKeyName(module.getKeyCode()) + ".");
                saveFile();
                return true;
            }
            case "clear": {
                MODULE.getContents().forEach(module1 -> {
                    if (module1.getClass() != ClickGUI.class) {
                        module1.setKeyCode(Keyboard.KEY_NONE);
                    }
                });
                PlayerUtils.sendConsoleReply("All binds have been cleared.");
                saveFile();
                return true;
            }
            default: {
                return false;
            }
        }
    }


    private void saveFile() {
        try {
            FILE.getFile(ModulesFile.class).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
