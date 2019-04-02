package com.envyclient.core.util;

import java.util.Arrays;

public class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static void invokeLoader(Class clazz, Object parent, boolean enable) {
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            try {

                field.setAccessible(true);

                Object obj = field.get(parent);
                if (obj instanceof Loader) {
                    if (enable) {
                        ((Loader) obj).enable();
                    } else {
                        ((Loader) obj).disable();
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
