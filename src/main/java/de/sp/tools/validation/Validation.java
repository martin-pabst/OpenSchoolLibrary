package de.sp.tools.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Martin on 07.06.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Validation {

    // for all objects
    boolean notNull() default false;

    // for Strings and Lists of Strings (=> all elements are validated!)
    boolean notEmpty() default false;
    SanitizingStrategy sanitizingStrategy() default SanitizingStrategy.jsoupWhitelist;
    String regEx() default "";
    int maxLength() default Integer.MAX_VALUE;
    String[] acceptedValues() default {};

    // for int attributes
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;

    // for all attributes
    String message() default "";

}
