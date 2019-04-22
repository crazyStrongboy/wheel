package com.mars_jun.client.aspect;

import java.lang.annotation.*;

/**
 * @author hejun
 * @date 2019/04/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyHystrixCommand {
    int value() default 100;
    String fallback() default "";
}
