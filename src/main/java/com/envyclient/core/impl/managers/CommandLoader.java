package com.envyclient.core.impl.managers;

import com.envyclient.core.util.Loader;
import com.envyclient.core.impl.commands.*;
import me.ihaq.commandmanager.CommandManager;

public class CommandLoader implements Loader {

    private final CommandManager commandManager = new CommandManager("-");

    @Override
    public void enable() {
        commandManager.register(new String[]{"help", "h"}, new HelpCommand());
        commandManager.register(new String[]{"bind", "b"}, new BindCommand());
        commandManager.register(new String[]{"toggle", "t"}, new ToggleCommand());
        commandManager.register(new String[]{"friend", "f"}, new FriendCommand());
        commandManager.register(new String[]{"setting", "value", "val", "set", "v"}, new SettingCommand());
        commandManager.register(new String[]{"clear", "cls"}, new ClearCommand());
        commandManager.register(new String[]{"disconnect", "dc"}, new DisconnectCommand());
        commandManager.register(new String[]{"exit", "quit"}, new ExitCommand());
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}