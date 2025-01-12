package entity.report;

import entity.ScriptInformation;

import java.util.ArrayList;
import java.util.List;

public class ScriptReportGenerator implements ReportGenerator {
    private final String headerPrefix = "> Starting: ";
    private final String spendingTimePrefix = "> Spending Time: ";
    private final String statusPrefix = "> Status: ";
    private final String assertErrorPrefix = "> Assert Error: ";
    private final String totalTimePrefix = "Total: ";

    private ColorHelper colorHelper = new ColorHelper();

    @Override
    public String generateScriptInfoHeader(ScriptInformation scriptInformation) {
        String header = "+-----------------------------------------------------+\n";
        String scriptName = scriptInformation.getScriptName() + "\n";
        return header + headerPrefix + scriptName;
    }

    @Override
    public String generateScriptInfoBody(ScriptInformation scriptInformation) {
        String errorMessage = scriptInformation.getErrorMessage() + "\n";
        return assertErrorPrefix + errorMessage;
    }

    @Override
    public String generateScriptInfoExceptionBody(ScriptInformation scriptInformation) {
        final String errorMessage = scriptInformation.getErrorMessage() + "\n";
        final String exceptionErrorPrefix = "> Exception Error: ";
        return exceptionErrorPrefix + errorMessage;
    }

    @Override
    public String generateScriptInfoFooter(ScriptInformation scriptInformation) {
        String spendingTime = transformTimeToSecond(scriptInformation.getExecutionTime()) + "s\n";
        String status = scriptInformation.resultStatus() + "\n";
        return spendingTimePrefix + spendingTime + statusPrefix + decorateStatus(status);
    }

    private String decorateStatus(String status) {
        if (status.contains("PASS") || status.contains("pass"))
            return colorHelper.paintingGreen(status);
        return colorHelper.paintingRed(status);
    }

    @Override
    public String generateScriptSummary(List<ScriptInformation> scriptInformationList) {
        String header = generatorHeader();
        String body = generateBody(scriptInformationList);
        String footer = generateFooter(scriptInformationList);

        return header + body + footer;
    }


    private String generateFooter(List<ScriptInformation> scriptInformationList) {
        String header = "+--------------------------------------------------------------------+-----------------+-----------------+\n";
        double totalTime = scriptInformationList.stream().mapToDouble(ScriptInformation::getExecutionTime).sum();
        int pass = (int) scriptInformationList.stream().filter(scriptInformation -> scriptInformation.resultStatus().equalsIgnoreCase("PASS")).count();
        int fail = (int) scriptInformationList.stream().filter(scriptInformation -> scriptInformation.resultStatus().equalsIgnoreCase("FAILED")).count();
        String footer = "+--------------------------------------------------------------------------------------------------------+\n";

        String totalTimeSummary = totalTimePrefix + TimeHelper.transformToMinute(totalTime) + " min";
        String statusSummary = pass + " " + colorHelper.paintingGreen("passed") + ", " + fail + " " + colorHelper.paintingRed("failed");
        String leftAlignFormat = "| %-76s | %-41s |%n";
        return header + String.format(leftAlignFormat, totalTimeSummary, statusSummary) + footer;
    }

    private double transformTimeToMinute(double time) {
        return TimeHelper.transformToMinute(time);
    }

    private String transformTimeToSecond(double executionTime) {
        return TimeHelper.transformToSecondFormat(executionTime);
    }

    private String generateBody(List<ScriptInformation> scriptInformationList) {
        List<String> scriptBodyList = getScriptBodyList(scriptInformationList);
        String result = "";
        for (String s : scriptBodyList)
            result += s;
        return result;
    }

    private String generatorHeader() {
        String header = "+--------------------------------------------------------------------+-----------------+-----------------+\n" +
                        "| ScriptName                                                         | Times           | Status          |\n" +
                        "+--------------------------------------------------------------------+-----------------+-----------------+\n";
        return header;
    }

    public List<String> getScriptBodyList(List<ScriptInformation> scriptInformations) {
        List<String> result = new ArrayList<>();
        String leftAlignFormat = "| %-66s | %-15s | %-24s |%n";

        for(ScriptInformation s : scriptInformations)
            result.add(String.format(leftAlignFormat, s.getScriptName(), TimeHelper.transformToSecondFormat(s.getExecutionTime()) + " s", decorateStatus(s.resultStatus())));
        return result;
    }



}
