package de.sp.main.services.settings;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Martin on 07.06.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SettingPanel {

    String name() default "";
    String description() default "";
    String[] keywords() default {};

}
