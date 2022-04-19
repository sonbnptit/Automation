package com.tcb.auto.serenity;

import net.thucydides.junit.annotations.UseTestDataFrom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface UseTestDataFromExcel {
    String value();
    String sheet();
    boolean runAll() default false;
}
