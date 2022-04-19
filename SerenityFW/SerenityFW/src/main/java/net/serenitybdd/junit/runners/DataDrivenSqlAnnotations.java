package net.serenitybdd.junit.runners;

import com.tcb.auto.serenity.ExcelTestDataSource;
import com.tcb.auto.serenity.SqlTestDataSource;
import com.tcb.auto.serenity.UseTestDataFromSql;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.Constants;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.steps.FilePathParser;
import net.thucydides.core.steps.stepdata.TestDataSource;
import net.thucydides.core.util.EnvironmentVariables;
import org.junit.runners.model.TestClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataDrivenSqlAnnotations extends DataDrivenAnnotations {
    private final EnvironmentVariables environmentVariables;
    private final TestClass testClass;

    DataDrivenSqlAnnotations(Class testClass) {
        this(new TestClass(testClass));
    }

    DataDrivenSqlAnnotations(TestClass testClass) {
        this(testClass, Injectors.getInjector().getProvider(EnvironmentVariables.class).get());
    }

    DataDrivenSqlAnnotations(TestClass testClass, EnvironmentVariables environmentVariables) {
        super(testClass, environmentVariables);
        this.testClass = testClass;
        this.environmentVariables = environmentVariables;
    }

    public static DataDrivenSqlAnnotations forClass(final Class testClass) {
        return new DataDrivenSqlAnnotations(testClass);
    }

    public static DataDrivenSqlAnnotations forClass(final TestClass testClass) {
        return new DataDrivenSqlAnnotations(testClass);
    }

    private UseTestDataFromSql findUseTestDataFromAnnotation() {
        return (UseTestDataFromSql)this.testClass.getJavaClass().getAnnotation(UseTestDataFromSql.class);
    }

    @Override
    public boolean hasTestDataSourceDefined() {
        return this.findUseTestDataFromAnnotation() != null;
    }

    @Override
    public DataTable getParametersTableFromTestDataSource() throws Throwable {
        UseTestDataFromSql sqlAnnotation = this.findUseTestDataFromAnnotation();
        TestDataSource testDataSource = new SqlTestDataSource(sqlAnnotation.connection(), sqlAnnotation.table(), sqlAnnotation.query(), sqlAnnotation.runAll());
        List<Map<String, String>> testData = testDataSource.getData();
        List<String> headers = testDataSource.getHeaders();
        return DataTable.withHeaders(headers).andMappedRows(testData).build();
    }

    @Override
    public DataTable getParametersTableFromTestDataAnnotation() {
        return super.getParametersTableFromTestDataAnnotation();
    }

    @Override
    public <T> List<T> getDataAsInstancesOf(Class<T> clazz) {
        ConfigController cc = new ConfigController();
        UseTestDataFromSql sqlAnnotation = this.findUseTestDataFromAnnotation();
        String dbConnection = Commons.isBlankOrEmpty(cc.getProperty(Constants.MAVEN_TEST_DATA_CONNECTION)) ? sqlAnnotation.connection() : cc.getProperty(Constants.MAVEN_TEST_DATA_CONNECTION);
        String dbTable = Commons.isBlankOrEmpty(cc.getProperty(Constants.MAVEN_TEST_DATA_TABLE)) ? sqlAnnotation.table() : cc.getProperty(Constants.MAVEN_TEST_DATA_TABLE);
        String dbQuery = Commons.isBlankOrEmpty(cc.getProperty(Constants.MAVEN_TEST_DATA_QUERY)) ? sqlAnnotation.query() : cc.getProperty(Constants.MAVEN_TEST_DATA_QUERY);
        TestDataSource testdata = new SqlTestDataSource(dbConnection, dbTable, dbQuery, sqlAnnotation.runAll());
        return testdata.getDataAsInstancesOf(clazz);
    }
}
