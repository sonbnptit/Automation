package net.serenitybdd.junit.runners;


import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import com.vin3s.auto.utils.Constants;
import com.vin3s.auto.utils.GlobalVariable;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import com.vin3s.auto.utils.Constants;
import net.serenitybdd.core.environment.WebDriverConfiguredEnvironment;
import net.thucydides.core.batches.BatchManager;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.JUnitAdapter;
import net.thucydides.core.webdriver.DriverConfiguration;
import net.thucydides.core.webdriver.WebDriverFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbsCustomSerenityParameterizedRunner extends SerenityParameterizedRunner {

    protected DriverConfiguration configuration;

    public AbsCustomSerenityParameterizedRunner(Class<?> klass, DriverConfiguration configuration, WebDriverFactory webDriverFactory, BatchManager batchManager) throws Throwable {
        super(klass, configuration, webDriverFactory, batchManager);
        this.configuration = configuration;

        runBootstrap();

        //Child class: init testClassAnnotations and call loadTestRunners()
        loadTestRunners(webDriverFactory, batchManager);

    }

    public AbsCustomSerenityParameterizedRunner(Class<?> klass) throws Throwable {
        this(klass, WebDriverConfiguredEnvironment.getDriverConfiguration(), new WebDriverFactory(),
                Injectors.getInjector().getInstance(BatchManager.class));

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
        if(!GlobalVariable.isBootstrapFinished) {
            GlobalVariable.isBootstrapFinished = CommonRunnerFeatures.runBootstrapScript();
        }
    }

    protected abstract DataDrivenAnnotations getTestAnnotations();

    public void loadTestRunners(WebDriverFactory webDriverFactory, BatchManager batchManager) throws Throwable {
        //clear parent runner
        super.getRunners().clear();
        DataDrivenAnnotations testClassAnnotations = getTestAnnotations();
        if (testClassAnnotations.hasTestDataDefined()) {
            super.getRunners().addAll(buildTestRunnersForEachDataSetUsing(webDriverFactory, batchManager));
        } else if (testClassAnnotations.hasTestDataSourceDefined()) {
            super.getRunners().addAll(buildTestRunnersFromADataSourceUsing(webDriverFactory, batchManager));
        }
    }

    protected String getQualifierFor(final Object testCase) {
        return QualifierFinder.forTestCase(testCase).getQualifier();
    }

    protected String from(final Collection testData) {
        StringBuffer testDataQualifier = new StringBuffer();
        boolean firstEntry = true;
        for (Object testDataValue : testData) {
            if (!firstEntry) {
                testDataQualifier.append("/");
            }
            testDataQualifier.append(testDataValue);
            firstEntry = false;
        }
        return testDataQualifier.toString();
    }

    protected List<Runner> buildTestRunnersForEachDataSetUsing(final WebDriverFactory webDriverFactory,
                                                               final BatchManager batchManager) throws Throwable {
        /*if (shouldSkipAllTests()) {
            return new ArrayList<>();
        }*/

        List<Runner> runners = new ArrayList<>();
        DataTable parametersTable = getTestAnnotations().getParametersTableFromTestDataAnnotation();
        for (int i = 0; i < parametersTable.getRows().size(); i++) {
            Class<?> testClass = getTestClass().getJavaClass();
            SerenityRunner runner = new TestClassRunnerForParameters(testClass,
                    configuration,
                    webDriverFactory,
                    batchManager,
                    parametersTable,
                    i);
            runner.useQualifier(from(parametersTable.getRows().get(i).getValues()));
            runners.add(runner);
        }
        return runners;
    }

    protected List<Runner> buildTestRunnersFromADataSourceUsing(final WebDriverFactory webDriverFactory,
                                                                final BatchManager batchManager) throws Throwable {
        /*if (shouldSkipAllTests()) {
            return new ArrayList<>();
        }*/

        List<Runner> runners = new ArrayList<>();
        List<?> testCases = getTestAnnotations().getDataAsInstancesOf(getTestClass().getJavaClass());
        DataTable parametersTable = getTestAnnotations().getParametersTableFromTestDataSource();
        for (int i = 0; i < testCases.size(); i++) {
            Object testCase = testCases.get(i);
            SerenityRunner runner = new TestClassRunnerForInstanciatedTestCase(testCase,
                    configuration,
                    webDriverFactory,
                    batchManager,
                    parametersTable,
                    i);
            runner.useQualifier(getQualifierFor(testCase));
            runners.add(runner);
        }
        return runners;
    }

    @Override
    public void run(RunNotifier notifier){
        super.run(notifier);

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
        }
    }

    protected void renameReport(){
        List<TestOutcome> testOutcomes = ParameterizedTestsOutcomeAggregator.from(this).aggregateTestOutcomesByTestMethods();
        CommonRunnerFeatures.doRenameReport(configuration, testOutcomes);
    }

    protected void createOnePageReport(boolean isRenameReport, boolean isPdfReport){
        List<TestOutcome> testOutcomes = ParameterizedTestsOutcomeAggregator.from(this).aggregateTestOutcomesByTestMethods();

        CommonRunnerFeatures.doCreateOnePageReport(configuration, testOutcomes, isRenameReport, isPdfReport);
    }

    protected void JSONReport(){
        List<TestOutcome> testOutcomes = ParameterizedTestsOutcomeAggregator.from(this).aggregateTestOutcomesByTestMethods();
        CommonRunnerFeatures.doJSONReport(configuration, testOutcomes);
    }

}
