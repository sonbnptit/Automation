package net.serenitybdd.junit.runners;

import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;

public class SerenityParameterizedSqlRunner extends AbsCustomSerenityParameterizedRunner {
    public SerenityParameterizedSqlRunner(Class<?> klass, DriverConfiguration configuration, WebDriverFactory webDriverFactory, BatchManager batchManager) throws Throwable {
        super(klass, configuration, webDriverFactory, batchManager);
    }

    public SerenityParameterizedSqlRunner(Class<?> klass) throws Throwable {
        super(klass);
    }

    @Override
    protected DataDrivenAnnotations getTestAnnotations() {
        return DataDrivenSqlAnnotations.forClass(getTestClass());
    }
}
