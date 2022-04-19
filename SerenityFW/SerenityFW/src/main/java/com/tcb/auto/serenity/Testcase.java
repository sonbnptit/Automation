package com.tcb.auto.serenity;

import java.lang.annotation.*;

@Repeatable(Testcase.List.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Testcase {
    String[] listId();
    String release() default "";
    String flow() default "";

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        Testcase[] value();
    }
}
