package com.tcb.auto.serenity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface UseTestDataFromSql {
    String connection();
    String table() default "";
    String query() default "";
    boolean runAll() default false;
}
