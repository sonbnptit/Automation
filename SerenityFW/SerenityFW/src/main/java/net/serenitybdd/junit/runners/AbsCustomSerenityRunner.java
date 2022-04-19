package net.serenitybdd.junit.runners;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.Constants;
import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.JUnitAdapter;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.core.webdriver.WebdriverManager;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class AbsCustomSerenityRunner extends SerenityRunner {

    private static boolean IS_BOOTSTRAP_FINISHED = false;

    public AbsCustomSerenityRunner(Class<?> klass) throws InitializationError {
        super(klass);
        runBootstrap();
    }

    public AbsCustomSerenityRunner(Class<?> klass, Module module) throws InitializationError {
        super(klass, module);
        runBootstrap();
    }

    public AbsCustomSerenityRunner(Class<?> klass, Injector injector) throws InitializationError {
        super(klass, injector);
        runBootstrap();
    }

    public AbsCustomSerenityRunner(Class<?> klass, WebDriverFactory webDriverFactory) throws InitializationError {
        super(klass, webDriverFactory);
        runBootstrap();
    }

    public AbsCustomSerenityRunner(Class<?> klass, WebDriverFactory webDriverFactory, DriverConfiguration configuration) throws InitializationError {
        super(klass, webDriverFactory, configuration);
        runBootstrap();
    }

    private void runBootstrap(){
        try {
            Object strategies = FieldUtils.readStaticField(JUnitAdapter.class, "strategies", true);
            List<String> runnerName = (List<String>) FieldUtils.readField(((List) strategies).get(0), "LEGAL_SERENITY_RUNNER_NAMES", true);
            runnerName.add(this.getClass().getSimpleName());
        }catch (IllegalAccessException e){
            //do nothing
        }

        Commons.getLogger().debug("Started test");

        //run bootstrap script
        if(!IS_BOOTSTRAP_FINISHED) {
            IS_BOOTSTRAP_FINISHED = true;
            CommonRunnerFeatures.runBootstrapScript();
        }
    }

    public AbsCustomSerenityRunner(Class<?> klass, WebDriverFactory webDriverFactory, DriverConfiguration configuration, BatchManager batchManager) throws InitializationError {
        super(klass, webDriverFactory, configuration, batchManager);
    }

    public AbsCustomSerenityRunner(Class<?> klass, BatchManager batchManager) throws InitializationError {
        super(klass, batchManager);
    }

    public AbsCustomSerenityRunner(Class<?> klass, WebdriverManager webDriverManager, DriverConfiguration configuration, BatchManager batchManager) throws InitializationError {
        super(klass, webDriverManager, configuration, batchManager);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);

        //After run
        ConfigController cc = new ConfigController();
        String propRenameReport = cc.getProperty(Constants.IS_RENAME_REPORT);
        boolean isRenameReport = !Commons.isBlankOrEmpty(propRenameReport) && "Y".equalsIgnoreCase(propRenameReport);

        String propOnePageReport = cc.getProperty(Constants.IS_CREATE_ONE_PAGE_REPORT);
        String propPdfReport = cc.getProperty(Constants.IS_CREATE_PDF_REPORT);
        String propJSONReport = cc.getProperty(Constants.IS_CREATE_JSON_REPORT);

        boolean isOnePageReport = !Commons.isBlankOrEmpty(propOnePageReport) && "Y".equalsIgnoreCase(propOnePageReport);
        boolean isPdfReport = !Commons.isBlankOrEmpty(propPdfReport) && "Y".equalsIgnoreCase(propPdfReport);
        boolean isJSONReport = !Commons.isBlankOrEmpty(propJSONReport) && "Y".equalsIgnoreCase(propJSONReport);
        if(isOnePageReport){
            //create one page report and rename if need
            createOnePageReport(isRenameReport, isPdfReport);
        }else{
            if(isRenameReport){
                //rename report only
                renameReport();
            }
        }

        if (isJSONReport){
            JSONReport();
            logIssue();
        }
    }

    protected void renameReport(){
        List<TestOutcome> testOutcomes = getStepListener().getTestOutcomes();
        CommonRunnerFeatures.doRenameReport(getConfiguration(), testOutcomes);
    }

    protected void createOnePageReport(boolean isRenameReport, boolean isPdfReport){
        List<TestOutcome> testOutcomes = getStepListener().getTestOutcomes();

        CommonRunnerFeatures.doCreateOnePageReport(getConfiguration(), testOutcomes, isRenameReport, isPdfReport);
    }

    protected void JSONReport(){
        List<TestOutcome> testOutcomes = getStepListener().getTestOutcomes();
        CommonRunnerFeatures.doJSONReport(getConfiguration(), testOutcomes);
    }

    public void logIssue(){
        CommonRunnerFeatures.doLogIssue(getTestClass());
    }
}
