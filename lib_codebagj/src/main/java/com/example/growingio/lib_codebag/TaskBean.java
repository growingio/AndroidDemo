package com.example.growingio.lib_codebag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author WangYing
 * time   2018/04/2018/4/16:下午1:49
 * email  wangying@growingio.com
 */


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TaskBean {
    String value() default "BugBean";
}
