package org.url_fuzzer_403_bypass;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Marker;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ConsolidationAction;
import burp.api.montoya.scanner.ScanCheck;
import burp.api.montoya.scanner.audit.insertionpoint.AuditInsertionPoint;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;

import java.util.*;

import static burp.api.montoya.core.ByteArray.byteArray;
import static burp.api.montoya.scanner.AuditResult.auditResult;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_BOTH;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_EXISTING;
import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class MyURLFuzzer implements ScanCheck {
    private final Logging logging;
    private final MontoyaApi api;

    public MyURLFuzzer(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }

    @Override
    public AuditResult activeAudit(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint)
    {

        ArrayList<AuditIssue> auditIssueList = new ArrayList<>();

        if( isRequestUnAuthorized(baseRequestResponse) ) {
            logging.logToOutput("Found unauthorized Request: " + baseRequestResponse.request().url());
            if( isRequestUnAuthorized(baseRequestResponse) ) {
                logging.logToOutput("Found unauthorized Request: " + baseRequestResponse.request().url());

                HttpRequest initiatingRequest = baseRequestResponse.request();
                String originalPath = initiatingRequest.path();
                ArrayList<String> paths = generatePaths(originalPath);

                for (String path : paths) {
                    HttpRequest modifiedRequest = initiatingRequest.withPath(path);

                    logging.logToOutput("Modified Request: " + modifiedRequest.toString());
                    HttpRequestResponse modifiedRequestResponse = api.http().sendRequest(modifiedRequest);

                    if (modifiedRequestResponse.response().statusCode() != baseRequestResponse.response().statusCode()){
                        logging.logToOutput("Bypassed !");

                        List<HttpRequestResponse> requestResponses = new ArrayList<>();
                        requestResponses.add(baseRequestResponse);
                        requestResponses.add(modifiedRequestResponse);

                        // Add Audit Result
                        auditIssueList.add(
                                auditIssue(
                                        "403 Bypass",
                                        "The response contains the string: ",
                                        null,
                                        baseRequestResponse.request().url(),
                                        AuditIssueSeverity.HIGH,
                                        AuditIssueConfidence.CERTAIN,
                                        null,
                                        null,
                                        AuditIssueSeverity.HIGH,
                                        requestResponses
                                        )
                        );
                    }
                }
            }
        }

        return auditResult(auditIssueList);
    }

    @Override
    public AuditResult passiveAudit(HttpRequestResponse baseRequestResponse)
    {
        return null;
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue newIssue, AuditIssue existingIssue)
    {
        return existingIssue.name().equals(newIssue.name()) ? KEEP_EXISTING : KEEP_BOTH;
    }

    public ArrayList<String> generatePaths(String originalPath){
        ArrayList<String> paths = new ArrayList<String>();

        logging.logToOutput("Original Path: " + originalPath);

        // TODO Fix payload generation
        paths.add(originalPath + "wow");

        return paths;
    }

    public boolean isRequestUnAuthorized(HttpRequestResponse responseReceived){
        boolean isStatusCodeUnAuthorized = false;
        boolean isGetRequest = false;
        short responseStatusCode = responseReceived.response().statusCode();
        short unAuthorizedStatusCodes[] = { 403, 401 };


        for (short statusCode: unAuthorizedStatusCodes){
            if(statusCode == responseStatusCode){
                isStatusCodeUnAuthorized = true;
                break;
            }
        }

        if(responseReceived.request().method() == "GET") {
            isGetRequest = true;
        }


        return  (isStatusCodeUnAuthorized && isGetRequest) ;

    }





}
