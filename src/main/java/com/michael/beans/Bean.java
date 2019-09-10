package com.michael.beans;

import java.lang.annotation.*;

/**
 * 注册成为bean
 *
 * @author Michael Chu
 * @since 2019-09-09 15:03
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {
}
