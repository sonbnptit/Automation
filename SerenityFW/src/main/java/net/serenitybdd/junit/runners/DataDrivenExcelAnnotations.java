package net.serenitybdd.junit.runners;

import com.vin3s.auto.serenity.ExcelTestDataSource;
import com.vin3s.auto.serenity.UseTestDataFromExcel;
import com.vin3s.auto.utils.Commons;
import com.vin3s.auto.utils.ConfigController;
import com.vin3s.auto.utils.Constants;
import net.serenitybdd.junit.runners.DataDrivenAnnotations;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.steps.FilePathParser;
import net.thucydides.core.steps.stepdata.CSVTestDataSource;
import net.thucydides.core.steps.stepdata.TestDataSource;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.io.FilenameUtils;
import org.junit.runners.model.TestClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataDrivenExcelAnnotations extends DataDrivenAnnotations {
    private final EnvironmentVariables environmentVariables;
    private final Pattern DATASOURCE_PATH_SEPARATORS = Pattern.compile("[;,]");
    private final TestClass testClass;


    DataDrivenExcelAnnotations(final Class testClass) {
        this(new TestClass(testClass));
    }

    DataDrivenExcelAnnotations(final TestClass testClass) {
        this(testClass, Injectors.getInjector().getProvider(EnvironmentVariables.class).get());
    }

    DataDrivenExcelAnnotations(final TestClass testClass, EnvironmentVariables environmentVariables) {
        super(testClass, environmentVariables);
        this.testClass = testClass;
        this.environmentVariables = environmentVariables;
    }

    public static DataDrivenExcelAnnotations forClass(final Class testClass) {
        return new DataDrivenExcelAnnotations(testClass);
    }

    public static DataDrivenExcelAnnotations forClass(final TestClass testClass) {
        return new DataDrivenExcelAnnotations(testClass);
    }

    private UseTestDataFromExcel findUseTestDataFromAnnotation() {
        return (UseTestDataFromExcel)this.testClass.getJavaClass().getAnnotation(UseTestDataFromExcel.class);
    }

    @Override
    public boolean hasTestDataSourceDefined() {
        return this.findUseTestDataFromAnnotation() != null && this.findTestExcelFile() != null && this.findTestExcelSheetName() != null;
    }

    @Override
    public DataTable getParametersTableFromTestDataSource() throws Throwable {
        TestDataSource testDataSource = new ExcelTestDataSource(this.findTestExcelFile(), this.findTestExcelSheetName(), this.findTestExcelRunAll());
        List<Map<String, String>> testData = testDataSource.getData();
        List<String> headers = testDataSource.getHeaders();
        return DataTable.withHeaders(headers).andMappedRows(testData).build();
    }

    @Override
    public DataTable getParametersTableFromTestDataAnnotation() {
        return super.getParametersTableFromTestDataAnnotation();
    }

    @Override
    protected String findTestDataSourcePaths() {
        return (new FilePathParser(this.environmentVariables)).getInstanciatedPath(this.findUseTestDataFromAnnotation().value());
    }

    protected String findTestExcelSheetName() {
        return this.findUseTestDataFromAnnotation().sheet();
    }

    protected boolean findTestExcelRunAll() {
        return this.findUseTestDataFromAnnotation().runAll();
    }

    @Override
    public <T> List<T> getDataAsInstancesOf(Class<T> clazz) throws IOException {
        ConfigController cc = new ConfigController();
        String excelFile = Commons.isBlankOrEmpty(cc.getProperty(Constants.MAVEN_TEST_DATA_FILE)) ? findTestExcelFile() : cc.getProperty(Constants.MAVEN_TEST_DATA_FILE);
        String excelSheet = Commons.isBlankOrEmpty(cc.getProperty(Constants.MAVEN_TEST_DATA_SHEET)) ? findTestExcelSheetName() : cc.getProperty(Constants.MAVEN_TEST_DATA_SHEET);
        excelFile = Commons.getAbsolutePath(excelFile);
        TestDataSource testdata = new ExcelTestDataSource(excelFile, excelSheet, findTestExcelRunAll());
        return testdata.getDataAsInstancesOf(clazz);
    }

    protected String findTestExcelFile() {
        String path = findTestDataSourcePaths();
        File excelFile = new File(path);
        String fileEx = FilenameUtils.getExtension(path).toLowerCase();
        if(excelFile.isFile() && (fileEx.equals("xls") || fileEx.equals("xlsx"))){
            return path;
        }else{
            throw new IllegalArgumentException("No test data file found for path: " + path);
        }
    }
}
