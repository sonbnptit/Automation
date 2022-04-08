package com.tcb.auto.utils;

import com.tcb.auto.subprocess.web.WebElementController;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.function.Supplier;

public class LazyBy implements Supplier<By> {
    Compiler compiler;
    String byStr;

    public LazyBy(Compiler compiler, String byStr) {
        this.compiler = compiler;
        this.byStr = byStr;
    }

    @Override
    public By get() {
        String comByStr = compiler.compileWithProperties(byStr);
        return WebElementController.getBy(comByStr);
    }

    public By by() {
        String comByStr = compiler.compileWithProperties(byStr);
        return WebElementController.getBy(comByStr);
    }

    public By by(String... params) {
        String comByStr = compiler.compileWithPropertiesParams(byStr, params);
        return WebElementController.getBy(comByStr);
    }
}
