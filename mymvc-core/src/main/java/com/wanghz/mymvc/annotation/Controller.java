package com.wanghz.mymvc.annotation;

import java.lang.annotation.*;

/**
 * 控制层注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

    String value() default "";
}
