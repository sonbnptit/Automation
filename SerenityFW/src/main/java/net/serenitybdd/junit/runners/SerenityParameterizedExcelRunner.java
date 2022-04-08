package net.serenitybdd.junit.runners;


import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.util.JUnitAdapter;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.apache.commons.lang3.reflect.FieldUtils;

public class SerenityParameterizedExcelRunner extends AbsCustomSerenityParameterizedRunner {
    public SerenityParameterizedExcelRunner(Class<?> klass, DriverConfiguration configuration, WebDriverFactory webDriverFactory, BatchManager batchManager) throws Throwable {
        super(klass, configuration, webDriverFactory, batchManager);
    }

    public SerenityParameterizedExcelRunner(Class<?> klass) throws Throwable {
        super(klass);
    }

    @Override
    protected DataDrivenAnnotations getTestAnnotations() {
        return net.serenitybdd.junit.runners.DataDrivenExcelAnnotations.forClass(getTestClass());
    }

    @Override
    protected String getQualifierFor(final Object testCase) {
        return QualifierFinder.forTestCase(testCase).getQualifier();
    }
}
