package com.michael.beans;

import java.lang.annotation.*;

/**
 * 自动注入
 *
 * @author Michael Chu
 * @since 2019-09-09 15:02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
