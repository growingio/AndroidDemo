package com.liz.lib_demodesc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * classDesc: 标记 DemoBean 的注解，和一般的 Bean 不一样哦，vip bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DemoBeanTag {
    String value() default "demoBean";
}
