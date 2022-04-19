package net.serenitybdd.junit.runners;

import com.google.inject.Injector;
import com.google.inject.Module;
import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.core.webdriver.WebdriverManager;
import org.junit.runners.model.InitializationError;

public class SerenityNoDataRunner extends AbsCustomSerenityRunner {
    public SerenityNoDataRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    public SerenityNoDataRunner(Class<?> klass, Module module) throws InitializationError {
        super(klass, module);
    }

    public SerenityNoDataRunner(Class<?> klass, Injector injector) throws InitializationError {
        super(klass, injector);
    }

    public SerenityNoDataRunner(Class<?> klass, WebDriverFactory webDriverFactory) throws InitializationError {
        super(klass, webDriverFactory);
    }

    public SerenityNoDataRunner(Class<?> klass, WebDriverFactory webDriverFactory, DriverConfiguration configuration) throws InitializationError {
        super(klass, webDriverFactory, configuration);
    }

    public SerenityNoDataRunner(Class<?> klass, WebDriverFactory webDriverFactory, DriverConfiguration configuration, BatchManager batchManager) throws InitializationError {
        super(klass, webDriverFactory, configuration, batchManager);
    }

    public SerenityNoDataRunner(Class<?> klass, BatchManager batchManager) throws InitializationError {
        super(klass, batchManager);
    }

    public SerenityNoDataRunner(Class<?> klass, WebdriverManager webDriverManager, DriverConfiguration configuration, BatchManager batchManager) throws InitializationError {
        super(klass, webDriverManager, configuration, batchManager);
    }
}
