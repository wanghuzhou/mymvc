package com.wanghz.mymvc.annotation;

import java.lang.annotation.*;

/**
 * 地址映射处理注解
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    public String value();
}
