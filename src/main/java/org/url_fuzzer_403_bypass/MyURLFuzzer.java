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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static burp.api.montoya.scanner.AuditResult.auditResult;
import static burp.api.montoya.scanner.ConsolidationAction.*;
import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;



public class MyURLFuzzer implements ScanCheck {

    private final Logging logging;
    private final MontoyaApi api;
    public static short NUM_OF_THREADS = 2;
    public ArrayList<String> scannedURLs = new ArrayList<>();

    // In milliseconds
    public static long TIMEOUT = 100;
    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUM_OF_THREADS);
    public MyURLFuzzer(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
        logging.logToOutput("Created MyURLFuzzer");
    }

    public boolean isScanned = false;
    @Override
    public AuditResult activeAudit(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint )
    {

        ArrayList<AuditIssue> auditIssueList = new ArrayList<>();

        String requestURL = baseRequestResponse.request().url();
        logging.logToOutput("Active Audit ongoing for " + requestURL);
        // We don't utilize insertion points, so no point scanning multiple times
        // Check if item is audited before
        // A hack to disable the same check for multiple insertion points
        if(isURLScannedBefore(requestURL)){
            return new AuditResult() {
                @Override
                public List<AuditIssue> auditIssues() {
                    return null;
                }
            };
        }
        else{
            scannedURLs.add(requestURL);
        }


        if( isRequestUnAuthorized(baseRequestResponse) ) {
            logging.logToOutput("Found 401/403 Request: " + baseRequestResponse.request().url());
                HttpRequest initiatingRequest = baseRequestResponse.request();
                String originalPath = initiatingRequest.path();
                ArrayList<String> paths = generatePaths(originalPath);
                for (String path : paths) {
//                    Runnable sendModifiedRequest = new SendModifiedRequest(baseRequestResponse, path, auditIssueList);
//                    executor.submit(sendModifiedRequest);
                    new SendModifiedRequest(baseRequestResponse, path, auditIssueList);
                }
            }


        try {
            executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logging.logToError(e);
            throw new RuntimeException(e);
        }
        logging.logToOutput("Triaging Result");
        return auditResult(auditIssueList);
    }

    private boolean isURLScannedBefore(String URL) {
        if(scannedURLs.contains(URL)){
            return true;
        }
        else{
            return false;
        }
    }


    public class SendModifiedRequest implements  Runnable {
        HttpRequestResponse baseRequestResponse;
        String path;
        ArrayList<AuditIssue> auditIssueList;
        public SendModifiedRequest(HttpRequestResponse baseRequestResponse, String path, ArrayList<AuditIssue> auditIssueList){
            this.baseRequestResponse = baseRequestResponse;
            this.path = path;
            this.auditIssueList = auditIssueList;
        }

        public void run() {
            HttpRequest modifiedRequest = baseRequestResponse.request().withPath(path);
            HttpRequestResponse modifiedRequestResponse = api.http().sendRequest(modifiedRequest);

            // Improve: Scan for non-default behavior - scan for clues
            if (isBehaviorChanged(baseRequestResponse, modifiedRequestResponse)){

                // Bypassed
                List<HttpRequestResponse> requestResponses = new ArrayList<>();
                var highlightedBaseRequestResponse = baseRequestResponse.withResponseMarkers(getResponseHighlights(baseRequestResponse)).withRequestMarkers(getRequestHighlights(baseRequestResponse));
                var highlightedModifiedRequestResponse = modifiedRequestResponse.withResponseMarkers(getResponseHighlights(modifiedRequestResponse)).withRequestMarkers(getRequestHighlights(modifiedRequestResponse));

                requestResponses.add(highlightedBaseRequestResponse);
                requestResponses.add(highlightedModifiedRequestResponse);
                logging.logToOutput("Found bypass " + path);


                auditIssueList.add(
                        auditIssue(
                                "401/403 Bypass",
                                String.format("The Path: `%s` prompted a different response", path) ,
                                null,
                                baseRequestResponse.request().url(),
                                AuditIssueSeverity.HIGH,
                                AuditIssueConfidence.FIRM,
                                "Sends every available characters at pre-defined place in the URL to to find HTTP Desync bugs between reverse proxies and web servers.\r\n\r\nBased on the research of Rafael da Costa Santos (https://rafa.hashnode.dev/exploiting-http-parsers-inconsistencies)",
                                null,
                                AuditIssueSeverity.HIGH,
                                requestResponses
                        )
                );
            }
        }

        private boolean isBehaviorChanged(HttpRequestResponse baseRequestResponse, HttpRequestResponse modifiedRequestResponse) {
            // 404 or 400
            short[] badStatusCodes = { 404, 400 };

            // Check for 404 or 400
            for (short statusCode: badStatusCodes){
                if(modifiedRequestResponse.response().statusCode() == statusCode){
                    // Behavior did change, it just errors out
                    return false;

                }
            }

            // Check if a response is sent, in case you \r\n, the web server waits forever
            if(modifiedRequestResponse.response().body().length() <= 0){
                return  false;
            }

            // Check for change in status code
            if (baseRequestResponse.response().statusCode() != modifiedRequestResponse.response().statusCode()){
                return true;
            }


            // I don't even know how the code can reach here...
            return false;
        }
    }

    private Marker getResponseHighlights(HttpRequestResponse requestResponse)
    {
        String response = requestResponse.response().toString();

        int CRLFIndex =  response.indexOf("\r\n");

        return Marker.marker(0, CRLFIndex);
    }

    private Marker getRequestHighlights(HttpRequestResponse requestResponse)
    {
        String request = requestResponse.request().toString();

        int pathLength =  requestResponse.request().path().length();
        int pathIndex = request.indexOf(requestResponse.request().path());

        return Marker.marker(pathIndex, pathIndex + pathLength);
    }

    @Override
    public AuditResult passiveAudit(HttpRequestResponse baseRequestResponse)
    {
        return new AuditResult() {
            @Override
            public List<AuditIssue> auditIssues() {
                return null;
            }
        };
    }

    @Override
    public ConsolidationAction consolidateIssues(AuditIssue newIssue, AuditIssue existingIssue)
    {
        return existingIssue.name().equals(newIssue.name())
                ? KEEP_EXISTING : KEEP_BOTH;


    }
    public ArrayList<String> generatePaths(String originalPath){
        ArrayList<String> paths = new ArrayList<String>();

        for(int x = 0; x < 255 ; x++){
            // Generate unicode character payload from char code
            String payload = Character.toString((char)x);


            String[] tokenizedPath =  originalPath.split("/");
            int slashCount = tokenizedPath.length;

            // Insert payload before first slash
            paths.add(payload + originalPath);

            // Insert payload in the middle of each word
            /* Test Cases
                /admin --> [ 'admin' ]
                /admin/admin -->  [ 'admin' , 'admin' ]
                /admin/admin/admin  --> ['admin', 'admin', 'admin' ]
             */

            for (int j = 0; j < slashCount; j++){
                StringBuilder sb =  new StringBuilder();
                for(int z = 0 ; z < slashCount; z++) {
                    if (originalPath.startsWith("/") && sb.indexOf("/") != 0 ){
                        sb.append("/");
                    }
                    if( !tokenizedPath[z].isEmpty()) {
                        sb.append(tokenizedPath[z]);
                        if(z == j){
                            sb.append(payload);
                        }
                        sb.append("/");
                    }
                }
                paths.add(sb.toString());
            }

            // Insert Payload At the end of the path
            paths.add(originalPath + payload);
        }


        return paths;
    }

    public boolean isRequestUnAuthorized(HttpRequestResponse responseReceived){
        boolean isStatusCodeUnAuthorized = false;
        boolean isGetRequest = false;
        short responseStatusCode = responseReceived.response().statusCode();
        short[] unAuthorizedStatusCodes = { 403, 401 };


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
