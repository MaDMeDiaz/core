package com.envyclient.core.api.setting.type;

import com.envyclient.core.api.module.Module;
import com.envyclient.core.api.setting.Setting;

import java.lang.reflect.Field;

public class ClampedSetting extends Setting<Double> {

    private double min, max;

    public ClampedSetting(String name, Module module, Field field, double min, double max) {
        super(name, module, field);
        this.min = min;
        this.max = max;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public boolean onlyInt() {
        return getField().getType() == Integer.TYPE;
    }

    @Override
    public Double getValue() {
        try {
            if (onlyInt()) {
                return (double) getField().getInt(getModule());
            } else {
                return getField().getDouble(getModule());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValue(Double value) {
        try {
            if (onlyInt()) { // int
                getField().setInt(getModule(), value.intValue());
            } else { // double
                getField().setDouble(getModule(), value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setValue(String value) {
        setValue(Double.parseDouble(value));
    }

}