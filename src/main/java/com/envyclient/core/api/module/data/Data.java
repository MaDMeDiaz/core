package com.envyclient.core.api.module.data;

import com.envyclient.core.api.module.type.Category;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {

    String name();

    int keyCode() default Keyboard.KEY_NONE;

    boolean toggled() default false;

    Category category();

    String description() default "";

    String[] alias() default "";

}