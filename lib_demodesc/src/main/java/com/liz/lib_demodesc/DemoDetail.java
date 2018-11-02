package com.liz.lib_demodesc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * classDesc:
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DemoDetail {
    String demoDesc() default "demo";
    
    String demoGroup() default "apiconfig";
    
    String demoTitle() default "demoTitle";
    
    String demoSubTitle() default "demoSubTitle";
    
    String demoFeature() default "demoFeature";
    
    String demoIcon() default "demoIcon";
}
