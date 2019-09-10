package com.michael.web.mvc;

import java.lang.annotation.*;

/**
 * @author Michael Chu
 * @since 2019-09-09 15:38
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    String value();
}
