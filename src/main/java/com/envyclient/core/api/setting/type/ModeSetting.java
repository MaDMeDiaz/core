package com.envyclient.core.api.setting.type;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;

import java.lang.reflect.Field;
import java.util.List;

public class ModeSetting extends Setting<String> {

    private List<String> modes;

    public ModeSetting(String name, Module module, Field field, List<String> modes) {
        super(name, module, field);
        this.modes = modes;
    }

    public List<String> getModes() {
        return modes;
    }

    @Override
    public void setValue(String value) {

        if (!contains(modes, value)) {
            value = modes.get(0);
        }

        super.setValue(value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase());
    }

    public int currentIndex() {
        return modes.indexOf(getValue());
    }

    private boolean contains(List<String> list, String text) {
        return list.stream().filter(string -> string.equalsIgnoreCase(text)).count() >= 1;
    }
}
