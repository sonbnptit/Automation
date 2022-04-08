package net.serenitybdd.junit.runners;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;
import com.tcb.auto.IntegrationJira.CreateIssueDemo;
import com.tcb.auto.serenity.JiraTestcase;
import com.tcb.auto.utils.Commons;
import com.tcb.auto.utils.ConfigController;
import com.tcb.auto.utils.Constants;
import com.tcb.auto.utils.ExecuteCMD;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.webdriver.DriverConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class CommonRunnerFeatures {

    public static boolean runBootstrapScript(){

        ConfigController cc = new ConfigController();
        String bootstrapScript = cc.getProperty(Constants.BOOTSTRAP_SCRIPT);
        cc.clearProperty(Constants.BOOTSTRAP_SCRIPT);

        if(!Commons.isBlankOrEmpty(bootstrapScript)) {
            bootstrapScript = Commons.getAbsolutePath(bootstrapScript);
            //run bootstrap script
            Commons.getLogger().debug("Run bootstrap script: " + bootstrapScript);
            ExecuteCMD executeCMD = new ExecuteCMD();
            try {
                executeCMD.doExecuteCmd(bootstrapScript, null);
                return true;
            } catch (Exception e) {
                Commons.getLogger().error(e.getMessage());
            }
        }
        return false;
    }

    public static void doRenameReport(DriverConfiguration configuration, List<TestOutcome> testOutcomes){
        Commons.getLogger().debug("### Rename Report");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

        for(TestOutcome outcomes: testOutcomes){
            if(Commons.isBlankOrEmpty(outcomes.getHtmlReport()) || !outcomes.getHtmlReport().toLowerCase().endsWith("html")) continue;
            //get testcase info
            String path = configuration.getOutputDirectory().getAbsolutePath() + File.separator;

            String reportName = outcomes.getReportName();
            String reportPath = new StringBuilder(path).append(reportName).append(".html").toString();
            String testName = outcomes.getName();
            ZonedDateTime startTime = outcomes.getStartTime();
            //create new report name
            String renameReport = new StringBuilder(startTime.format(formatter))
                    .append('_').append(testName).toString();
            String renameReportPath = path + renameReport + ".html";

            try {
                //load all content in report, change old report name => rename report name
                File reportFile = new File(reportPath);
                String reportContent = new String(Files.readAllBytes(reportFile.toPath()), StandardCharsets.UTF_8);
                reportContent = reportContent.replaceAll(Pattern.quote(reportName), renameReport);

                //save to report
                BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath));
                writer.write(reportContent);
                writer.close();

                //rename report
                File renameReportFile = new File(renameReportPath);
                reportFile.renameTo(renameReportFile);

                //rename report screen shoot
                String reportScPath = new StringBuilder(path).append(reportName).append("_screenshots.html").toString();
                File reportScFile = new File(reportScPath);
                String renameReportScPath = path + renameReport + "_screenshots.html";
                File renameReportScFile = new File(renameReportScPath);
                reportScFile.renameTo(renameReportScFile);

            }catch (IOException e){
                Commons.getLogger().debug(e.getMessage());
            }
        }
    }

    public static void doCreateOnePageReport(DriverConfiguration configuration, List<TestOutcome> testOutcomes, boolean isRenameReport, boolean isPdfReport){
        Commons.getLogger().debug("### Create OnePage Report { 'isRenameReport' : " + isRenameReport + ", 'isPdfReport' : " + isPdfReport + "+ }");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

        for(TestOutcome outcomes: testOutcomes){
            if(Commons.isBlankOrEmpty(outcomes.getHtmlReport()) || !outcomes.getHtmlReport().toLowerCase().endsWith("html")) continue;
            //get testcase info
            String path = configuration.getOutputDirectory().getAbsolutePath() + File.separator;

            String reportName = outcomes.getReportName();
            String reportPath = new StringBuilder(path).append(reportName).append(".html").toString();
            String testName = outcomes.getName();

            String finalReportName = reportName;
            String finalReportPath = reportPath;
            if(isRenameReport) {
                ZonedDateTime startTime = outcomes.getStartTime();
                //create new report name
                finalReportName = new StringBuilder(startTime.format(formatter))
                        .append('_').append(testName).toString();
                finalReportPath = path + finalReportName + ".html";
            }

            try {
                //load all content in report, change old report name => rename report name
                File reportFile = new File(reportPath);
                File finalReportFile = new File(finalReportPath);
                String reportContent = new String(Files.readAllBytes(reportFile.toPath()), StandardCharsets.UTF_8);

                if(isRenameReport) {
                    reportContent = reportContent.replaceAll(Pattern.quote(reportName), finalReportName);
                }

                //add css & js for onepage report
                int headIdx = reportContent.indexOf("</head>");
                String cssContent = Commons.getContentResourceFile("onepage-report.css");
                String jsContent = Commons.getContentResourceFile("onepage-report.js");

                reportContent = reportContent.substring(0, headIdx) +
                        "<style>" + cssContent + "</style>" +
                        "<script>" + jsContent + "</script>" +
                        reportContent.substring(headIdx);


                //save to report
                BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath));
                writer.write(reportContent);
                writer.close();

                if(isRenameReport) {
                    //rename report
                    reportFile.renameTo(finalReportFile);
                    //rename report screen shoot
                    String reportScPath = new StringBuilder(path).append(reportName).append("_screenshots.html").toString();
                    File reportScFile = new File(reportScPath);
                    String renameReportScPath = path + finalReportName + "_screenshots.html";
                    File renameReportScFile = new File(renameReportScPath);
                    reportScFile.renameTo(renameReportScFile);
                }

                //export to pdf report
                if(isPdfReport){
                    String finalReportUrl = new File(finalReportPath).toURI().toURL().toString();
                    String pdfPath = finalReportPath.replace(".html", ".pdf");

                    org.jsoup.nodes.Document doc = Jsoup.parse(new File(finalReportPath), "UTF-8");
                    for(Element elm : doc.getElementsByClass("screenshot")){
                        elm.attr("width", "800");
                        elm.attr("height", "auto");
                    }

                    for(Element elm: doc.select("tr.test-SUCCESS")){
                        if(Commons.isBlankOrEmpty(elm.select("td.step-icon > a[onclick*='toggleDiv']"))) continue;
                        elm.getElementsByClass("SUCCESS-text").remove();
                    }

                    org.w3c.dom.Document w3cDoc = new W3CDom().fromJsoup(doc);

                    XRLog.setLoggingEnabled(false);
                    try (OutputStream os = new FileOutputStream(pdfPath)) {
                        PdfRendererBuilder builder = new PdfRendererBuilder();
                        builder.useDefaultPageSize(420, 297, BaseRendererBuilder.PageSizeUnits.MM); //A3 landscape
                        builder.useFastMode();
                        builder.withW3cDocument(w3cDoc, finalReportUrl);
                        builder.toStream(os);
                        builder.run();
                    }

                    Commons.getLogger().debug("Saved to pdf report: " + pdfPath);
                }


            }catch (IOException e){
                Commons.getLogger().debug(e.getMessage());
            }

        }
    }

    public static void doJSONReport(DriverConfiguration configuration, List<TestOutcome> testOutcomes){
        Commons.getLogger().debug("### JSON Report");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

        for(TestOutcome outcomes: testOutcomes) {
            if (Commons.isBlankOrEmpty(outcomes.getHtmlReport()) || !outcomes.getHtmlReport().toLowerCase().endsWith("html"))
                continue;
            //get testcase info
            String path = configuration.getOutputDirectory().getAbsolutePath() + File.separator;

            String reportName = outcomes.getReportName();
            String reportPath = new StringBuilder(path).append(reportName).append(".html").toString();
            String testName = outcomes.getName();
            ZonedDateTime startTime = outcomes.getStartTime();
            //create new report name
            String renameReport = new StringBuilder(startTime.format(formatter))
                    .append('_').append(testName).toString();
            String renameReportPath = path + renameReport + ".json";

            try {
                //load all content in report, change old report name => rename report name
                File reportFile = new File(reportPath);
                String reportContent = new String(Files.readAllBytes(reportFile.toPath()), StandardCharsets.UTF_8);
                reportContent = reportContent.replaceAll(Pattern.quote(reportName), renameReport);

                //save to report
                BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath));
                writer.write(reportContent);
                writer.close();

                //rename report
                File renameReportFile = new File(renameReportPath);
                reportFile.renameTo(renameReportFile);

                //rename report screen shoot
                String reportScPath = new StringBuilder(path).append(reportName).append("_screenshots.html").toString();
                File reportScFile = new File(reportScPath);
                String renameReportScPath = path + renameReport + "_screenshots.html";
                File renameReportScFile = new File(renameReportScPath);
                reportScFile.renameTo(renameReportScFile);

            } catch (IOException e) {
                Commons.getLogger().debug(e.getMessage());
            }
        }
    }

    public static void doLogIssue(TestClass theTestClass) {

        String className = theTestClass.getName().toLowerCase();

        List<FrameworkMethod> methodName = theTestClass.getAnnotatedMethods(org.junit.Test.class);

        String methodTestName = "";
        String filename = "", fileNamePDF = "";
        CreateIssueDemo demo = new CreateIssueDemo();
        String path = demo.getFolderReportpath();
        for (int t = 0; t < methodName.size(); t++) {
            methodTestName = methodName.get(t).getName().toLowerCase();
            JiraTestcase s = methodName.get(t).getAnnotation(JiraTestcase.class);
            try {

                File file = new File(path);
                for (final File fileEntry : file.listFiles()) {
                    if (fileEntry.getName().contains(".json") & fileEntry.getName().contains(className) & fileEntry.getName().contains(methodTestName)) {
                        filename = fileEntry.getAbsolutePath();
                    }
                    if (fileEntry.getName().contains(".pdf") & fileEntry.getName().contains(className) & fileEntry.getName().contains(methodTestName)) {
                        fileNamePDF = fileEntry.getAbsolutePath();
                    }
                }

                BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
                String currentLine = reader.readLine();

                JSONObject json = new JSONObject(currentLine);
                String result = json.getString("result");
                String summary = json.getJSONObject("featureTag").getString("displayName");
                String description = "";
                String link = "";
                String tc = demo.getListTestcase(s.testID());

                if (result.equalsIgnoreCase("SUCCESS")) {
                    description = json.getJSONObject("userStory").getString("storyName");
                    link = demo.CreateExecutionAndAddToTestcase(tc, result, summary, description);
                    demo.UpdateAttachment(link, filename);
                    demo.UpdateAttachment(link, fileNamePDF);

                } else if (result.equalsIgnoreCase("ERROR") | result.equalsIgnoreCase("FAILURE")) {

                    description = json.getJSONArray("testSteps").getJSONObject(0).getJSONObject("exception").getString("errorType");
                    JSONArray reason = json.getJSONArray("testSteps").getJSONObject(0).getJSONArray("children");
                    String er = "";
                    String fileScreenshot = "";
                    String htmlSource = "";
                    for (int i = 0; i < reason.length(); i++) {
                        JSONObject object = reason.getJSONObject(i);
                        String result1 = object.getString("result");
                        if (result1.equalsIgnoreCase("ERROR") | result1.equalsIgnoreCase("FAILURE")) {
                            er = reason.getJSONObject(i).getJSONObject("exception").getString("errorType");
                            //mes = reason.getJSONObject(i).getJSONObject("exception").getString("message");

                            JSONArray arScreenshot = reason.getJSONObject(i).getJSONArray("screenshots");
                            for (int j = 0; j < arScreenshot.length(); j++) {
                                JSONObject obj = arScreenshot.getJSONObject(j);
                                fileScreenshot = obj.getString("screenshot");
                                htmlSource = obj.getString("htmlSource");
                            }
                        }

//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    JsonParser jp = new JsonParser();
//                    JsonElement je = jp.parse(object.toString());
//                    prettyJsonString = gson.toJson(je);
                    }

                    String newDes = er.concat("\\r\\n(...See more attach file!!)");
                    String newPathScreenshotError = path + "\\" + fileScreenshot;

                    link = demo.CreateExecutionAndAddToTestcase(tc, result, summary, newDes);
                    demo.UpdateAttachment(link, newPathScreenshotError);
                    demo.UpdateAttachment(link, filename);
                    demo.UpdateAttachment(link, fileNamePDF);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
