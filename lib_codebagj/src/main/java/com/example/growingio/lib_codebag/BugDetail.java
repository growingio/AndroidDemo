package com.example.growingio.lib_codebag;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface BugDetail {
    String taskNumber() default "PI-xxxx";

    String fixVersion() default "2.x";

    String bugName();

    String tag();
}
