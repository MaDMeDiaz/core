package com.envyclient.core.api.setting;

import com.envyclient.core.api.module.Module;

import java.lang.reflect.Field;

public abstract class Setting<T> {

    private String name;
    private Module module;
    private Field field;

    public Setting(String name, Module module, Field field) {
        this.name = name;
        this.module = module;
        this.field = field;
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        try {
            return (T) field.get(module);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(T value) {
        try {
            field.set(module, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public Field getField() {
        return field;
    }

}