package com.envyclient.core.impl.managers;

import com.envyclient.core.api.manager.Manager;
import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;
import com.envyclient.core.api.setting.data.Clamp;
import com.envyclient.core.api.setting.data.Modes;
import com.envyclient.core.api.setting.data.Name;
import com.envyclient.core.api.setting.type.BooleanSetting;
import com.envyclient.core.api.setting.type.ClampedSetting;
import com.envyclient.core.api.setting.type.ModeSetting;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.envyclient.core.Envy.Managers.MODULE;

public class SettingManager extends Manager<Setting> {

    @Override
    public void enable() {
        MODULE.registerModuleModes();
        MODULE.getContents()
                .forEach(module -> Arrays.stream(module.getClass().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(Name.class))
                        .forEach(field -> registerSetting(module, field)));
    }

    private void registerSetting(Module module, Field field) {

        String name = field.getAnnotation(Name.class).value();

        field.setAccessible(true);

        if (field.getType().isAssignableFrom(String.class) && field.isAnnotationPresent(Modes.class)) {
            add(new ModeSetting(name, module, field, Arrays.asList(field.getAnnotation(Modes.class).value())));
        } else if (field.getType().isAssignableFrom(boolean.class)) {
            add(new BooleanSetting(name, module, field));
        } else if (field.isAnnotationPresent(Clamp.class)) {
            add(new ClampedSetting(name, module, field, field.getAnnotation(Clamp.class).min(), field.getAnnotation(Clamp.class).max()));
        }
    }

    public List<Setting> getSettings(Module module) {
        return getContents().stream()
                .filter(setting -> setting.getModule() == module)
                .collect(Collectors.toList());
    }

    @Nullable
    public Setting getSetting(Module module, String name) {
        return getContents().stream()
                .filter(setting -> setting.getModule() == module)
                .filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    @Nullable
    public Setting getSetting(Module module, Class<? extends Setting> clazz, String name) {
        return getContents().stream()
                .filter(setting -> setting.getModule() == module)
                .filter(setting -> setting.getClass() == clazz)
                .filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public void capClampedSettings() {
        getContents().stream()
                .filter(setting -> setting instanceof ClampedSetting)
                .forEach(setting -> capClampedSetting((ClampedSetting) setting));
    }

    public void capClampedSetting(ClampedSetting setting) {
        double current = setting.getValue();
        double min = setting.getMin();
        double max = setting.getMax();

        if (current > max) {
            setting.setValue(max);
        } else if (current < min) {
            setting.setValue(min);
        }
    }

}