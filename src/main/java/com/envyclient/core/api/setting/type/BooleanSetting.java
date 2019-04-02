package com.envyclient.core.api.setting.type;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;

import java.lang.reflect.Field;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Module module, Field field) {
        super(name, module, field);
    }
}