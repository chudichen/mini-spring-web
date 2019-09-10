package com.michael.web.mvc;

import java.lang.annotation.*;

/**
 * @author Michael Chu
 * @since 2019-09-09 15:37
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}
