package com.wanghz.mymvc.annotation;

import java.lang.annotation.*;

/**
 * 持久化注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    public String value();
}
