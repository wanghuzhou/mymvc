package com.wanghz.mymvc.annotation;

import java.lang.annotation.*;

/**
 * 业务层注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    public String value();
}
