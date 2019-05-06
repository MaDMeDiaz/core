package com.envyclient.core.util;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;
import com.envyclient.core.api.setting.type.BooleanSetting;
import com.envyclient.core.api.setting.type.ClampedSetting;
import com.envyclient.core.api.setting.type.ModeSetting;
import com.google.gson.*;

import java.util.List;

import static com.envyclient.core.Envy.Managers.MODULE;
import static com.envyclient.core.Envy.Managers.SETTING;

public class GsonUtils {

    public static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    // static strings for saving
    private static final String NAME = "name";
    private static final String KEY = "key";
    private static final String TOGGLED = "toggled";
    private static final String SETTINGS = "settings";
    private static final String VALUE = "value";

    private GsonUtils() {
    }

    public static boolean loadModuleData(JsonArray modulesArray) {

        if (modulesArray == null) {
            return false;
        }

        modulesArray.forEach(jsonElement -> {
            JsonObject moduleObject = jsonElement.getAsJsonObject();

            if (!moduleObject.has(NAME)) {
                return;
            }

            Module module = MODULE.getModule(moduleObject.get(NAME).getAsString());

            if (module == null) {
                return;
            }

            if (moduleObject.has(KEY)) {
                module.setKeyCode(moduleObject.get(KEY).getAsInt());
            }

            if (moduleObject.has(TOGGLED)) {
                if (moduleObject.get(TOGGLED).getAsBoolean() && !module.isToggled()) {
                    module.toggle();
                } else if (!moduleObject.get(TOGGLED).getAsBoolean() && module.isToggled()) {
                    module.toggle();
                }
            }

            if (moduleObject.has(SETTINGS)) {

                JsonArray settingsArray = moduleObject.get(SETTINGS).getAsJsonArray();

                settingsArray.forEach(jsonElement1 -> {
                    JsonObject settingObject = jsonElement1.getAsJsonObject();

                    if (!settingObject.has(NAME) || !settingObject.has(VALUE)) {
                        return;
                    }

                    Setting setting = SETTING.getSetting(module, settingObject.get(NAME).getAsString());

                    if (setting == null) {
                        return;
                    }

                    JsonElement value = settingObject.get(VALUE);

                    if (setting instanceof BooleanSetting) {
                        ((BooleanSetting) setting).setValue(value.getAsBoolean());
                    } else if (setting instanceof ModeSetting) {
                        ((ModeSetting) setting).setValue(value.getAsString());
                    } else {
                        ((ClampedSetting) setting).setValue(value.getAsString());
                    }

                });
            }

        });

        // Checking if the clamped setting values are not in bound
        SETTING.capClampedSettings();

        return true;
    }

    public static JsonArray getModuleData(boolean binds) {
        JsonArray modulesArray = new JsonArray();

        MODULE.getContents().forEach(module -> {

            JsonObject modulesObject = new JsonObject();

            modulesObject.addProperty(NAME, module.getName());
            modulesObject.addProperty(TOGGLED, module.isToggled());

            if (binds) {
                modulesObject.addProperty(KEY, module.getKeyCode());
            }

            List<Setting> settings = SETTING.getSettings(module);

            if (!settings.isEmpty()) {

                JsonArray settingsArray = new JsonArray();

                settings.forEach(setting -> {

                    JsonObject settingsObject = new JsonObject();
                    settingsObject.addProperty(NAME, setting.getName());

                    if (setting instanceof BooleanSetting) {
                        settingsObject.addProperty(VALUE, (Boolean) setting.getValue());
                    } else if (setting instanceof ModeSetting) {
                        settingsObject.addProperty(VALUE, (String) setting.getValue());
                    } else {
                        ClampedSetting clampedSetting = (ClampedSetting) setting;
                        settingsObject.addProperty(VALUE, clampedSetting.onlyInt() ?
                                clampedSetting.getValue().intValue() :
                                MathUtils.roundToPlace(clampedSetting.getValue(), 2));
                    }

                    settingsArray.add(settingsObject);
                });

                modulesObject.add(SETTINGS, settingsArray);
            }

            modulesArray.add(modulesObject);
        });

        return modulesArray;
    }

    public static JsonObject toJsonObject(String contents) throws JsonSyntaxException {
        return gson.fromJson(contents, JsonObject.class);
    }

    public static JsonArray toJsonArray(String contents) throws JsonSyntaxException {
        return gson.fromJson(contents, JsonArray.class);
    }
}