package com.wanghz.mymvc.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

    public String value();
}
