package com.envyclient.core.impl.commands;

import com.envyclient.core.util.PlayerUtils;
import me.ihaq.commandmanager.Command;
import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;
import com.envyclient.core.api.setting.type.BooleanSetting;
import com.envyclient.core.api.setting.type.ClampedSetting;
import com.envyclient.core.api.setting.type.ModeSetting;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.List;

import static com.envyclient.core.Envy.Managers.*;

public class SettingCommand implements Command {

    @Override
    public boolean onCommand(String[] args) {
        if (args.length >= 1) {

            Module module = MODULE.getModule(args[0]);

            if (module != null && args.length >= 2) {

                Setting setting = SETTING.getSetting(module, args[1]);

                if (setting != null) {

                    if (setting instanceof BooleanSetting) {

                        if (args.length >= 3 && validSetting(setting, args[2])) {
                            ((BooleanSetting) setting).setValue(Boolean.parseBoolean(args[2]));
                            PlayerUtils.sendConsoleReply(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            PlayerUtils.sendConsoleReply(setting.getName() + ": [TRUE or FALSE].");
                        }


                    } else if (setting instanceof ClampedSetting) {

                        if (args.length >= 3 && validSetting(setting, args[2])) {
                            ((ClampedSetting) setting).setValue(args[2]);
                            PlayerUtils.sendConsoleReply(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            PlayerUtils.sendConsoleReply(setting.getName() + ": [min = " + ((ClampedSetting) setting).getMin() + " , max = " + ((ClampedSetting) setting).getMax() + "].");
                        }

                    } else { // modes setting

                        if (args.length >= 3 && validSetting(setting, args[2])) {

                            // toggling the module off and changing the mode and re-toggling it so it changes modes
                            ((ModeSetting) setting).setValue(args[2]);
                            module.toggle();
                            module.toggle();

                            PlayerUtils.sendConsoleReply(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            PlayerUtils.sendConsoleReply(setting.getName() + ": " + ((ModeSetting) setting).getModes().toString() + ".");
                        }
                    }
                } else {
                    sendModuleSettings(module);
                }
            } else {
                sendModuleSettings(module);
            }
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "setting <module> <setting> <value>" + ChatFormatting.GRAY + " ]";
    }

    private void sendModuleSettings(Module module) {

        if (module == null) {
            PlayerUtils.sendConsoleReply("Module not found.");
            return;
        }

        List<Setting> settings = SETTING.getSettings(module);
        if (settings == null) {
            PlayerUtils.sendConsoleReply("This module has no settings.");
        } else {
            settings.forEach(setting -> {
                if (setting instanceof BooleanSetting)
                    PlayerUtils.sendConsoleReply(setting.getName() + ": [TRUE or FALSE].");
                else if (setting instanceof ClampedSetting)
                    PlayerUtils.sendConsoleReply(setting.getName() + ": [min = " + ((ClampedSetting) setting).getMin() + " , max = " + ((ClampedSetting) setting).getMax() + "].");
                else if (setting instanceof ModeSetting)
                    PlayerUtils.sendConsoleReply(setting.getName() + ": " + ((ModeSetting) setting).getModes().toString() + ".");
            });
        }
    }

    private boolean validSetting(Setting setting, String text) {
        try {
            if (setting instanceof ClampedSetting) {

                if (((ClampedSetting) setting).onlyInt())
                    return Integer.parseInt(text) >= ((ClampedSetting) setting).getMin() && Integer.parseInt(text) <= ((ClampedSetting) setting).getMax();
                else
                    return Double.parseDouble(text) >= ((ClampedSetting) setting).getMin() && Double.parseDouble(text) <= ((ClampedSetting) setting).getMax();

            } else if (setting instanceof ModeSetting) {
                return contains(((ModeSetting) setting).getModes(), text);
            } else if (setting instanceof BooleanSetting) {
                return text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean contains(List<String> list, String text) {
        return list.stream().filter(string -> string.equalsIgnoreCase(text)).count() >= 1;
    }


}
