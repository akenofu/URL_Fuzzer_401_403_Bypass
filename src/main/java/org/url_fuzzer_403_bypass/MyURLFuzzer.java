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

import static burp.api.montoya.scanner.AuditResult.auditResult;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_BOTH;
import static burp.api.montoya.scanner.ConsolidationAction.KEEP_EXISTING;
import static burp.api.montoya.scanner.audit.issues.AuditIssue.auditIssue;



public class MyURLFuzzer implements ScanCheck {
    private final Logging logging;
    private final MontoyaApi api;
    private ArrayList<String> scannedURLs = new ArrayList<>();

    public static short numberOfFuzzedCharsInURL = 1;
    public static boolean areOtherHTTPMethodsSupported = false;

    public MyURLFuzzer(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }

    @Override
    public AuditResult activeAudit(HttpRequestResponse baseRequestResponse, AuditInsertionPoint auditInsertionPoint)
    {
        ArrayList<AuditIssue> auditIssueList = new ArrayList<>();
        String requestURL = baseRequestResponse.request().url();


        // Scan every URL only once, regardless of how many insertion points
        if(isURLScannedBefore(requestURL)){
            return auditResult(auditIssueList);
        }
        else{
            scannedURLs.add(requestURL);
            logging.logToOutput("Scanning: " + requestURL);
        }

        if( isRequestUnAuthorized(baseRequestResponse) ) {
            logging.logToOutput(String.format("Found %d request: %s", baseRequestResponse.response().statusCode(), baseRequestResponse.request().url()));
                HttpRequest initiatingRequest = baseRequestResponse.request();
                String originalPath = initiatingRequest.path();
                ArrayList<String> paths = generatePaths(originalPath);
                for (String path : paths) {
                    scanPath(baseRequestResponse, path, initiatingRequest, auditIssueList);
                }
            }

        return auditResult(auditIssueList);
    }

    private void scanPath(HttpRequestResponse baseRequestResponse, String path, HttpRequest initiatingRequest, ArrayList<AuditIssue> auditIssueList) {
        HttpRequest modifiedRequest = initiatingRequest.withPath(path);
        HttpRequestResponse modifiedRequestResponse = api.http().sendRequest(modifiedRequest);

        // Improve: Scan for non-default behavior - scan for clues
        if (isBehaviorChanged(baseRequestResponse, modifiedRequestResponse)){

            // Bypassed
            List<HttpRequestResponse> requestResponses = new ArrayList<>();
            var highlightedBaseRequestResponse = baseRequestResponse.withResponseMarkers(getResponseHighlights(baseRequestResponse)).withRequestMarkers(getRequestHighlights(baseRequestResponse));
            var highlightedModifiedRequestResponse = modifiedRequestResponse.withResponseMarkers(getResponseHighlights(modifiedRequestResponse)).withRequestMarkers(getRequestHighlights(modifiedRequestResponse));

            requestResponses.add(highlightedBaseRequestResponse);
            requestResponses.add(highlightedModifiedRequestResponse);

            // Add Audit Result
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

    private boolean isURLScannedBefore(String URL) {
        if(scannedURLs.contains(URL)){
            return true;
        }
        else{
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
        return existingIssue.name().equals(newIssue.name()) ? KEEP_EXISTING : KEEP_BOTH;

    }

    
    
    public ArrayList<String> generatePaths(String originalPath){
        ArrayList<String> paths = new ArrayList<String>();
        String[] tokenizedPath =  originalPath.split("/");
        int slashCount = tokenizedPath.length;

        generateAndAddPayloads(originalPath, paths, tokenizedPath, slashCount);


        return paths;
    }

    private void generateAndAddPayloads(String originalPath, ArrayList<String> paths, String[] tokenizedPath, int slashCount) {

        short charRange = 255;
        char[] charArray = new char[charRange];
        for(int i = 0 ; i < charRange; i++){
            charArray[i] = (char) i;
        }
        char[] current = new char[numberOfFuzzedCharsInURL];
        generatePermutations(charArray, current, 0, numberOfFuzzedCharsInURL, originalPath, paths, tokenizedPath, slashCount);

    }

    public void generatePermutations(char[] charArray, char[] current, int index, int n, String originalPath, ArrayList<String> paths, String[] tokenizedPath, int slashCount) {
        if (index == n) {
            // Print the current permutation
            addPayloadToPaths(originalPath, paths, new String(current), tokenizedPath, slashCount);
            return;
        }

        for (int i = 0; i < charArray.length; i++) {
            current[index] = charArray[i];
            generatePermutations(charArray, current, index + 1, n, originalPath, paths, tokenizedPath, slashCount);
        }
    }

    private void addPayloadToPaths(String originalPath, ArrayList<String> paths, String payload,  String[] tokenizedPath , int slashCount) {
        // Insert payload before first slash
        paths.add(payload + originalPath);

        // Insert `/payload/` before original path
        if(!originalPath.startsWith("/")){
            paths.add("/" + payload + "/" + originalPath);
        }
        paths.add( payload + "/" + originalPath);


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

        // Insert `/payload/` after original path
        // Test cases:
        // /admin --> /admin/;/
        // /Admin/ --> /admin/;/
        if(!originalPath.endsWith("/")){
            paths.add( originalPath + "/"  + payload + "/");
        }
        paths.add( originalPath + payload + "/");

        // Insert Payload At the end of the path
        paths.add(originalPath + payload);
    }

    public boolean isRequestUnAuthorized(HttpRequestResponse responseReceived){
        boolean isStatusCodeUnAuthorized = false;
        short responseStatusCode = responseReceived.response().statusCode();
        short[] unAuthorizedStatusCodes = { 403, 401 };


        for (short statusCode: unAuthorizedStatusCodes){
            if(statusCode == responseStatusCode){
                isStatusCodeUnAuthorized = true;
                break;
            }
        }

        if(MyURLFuzzer.areOtherHTTPMethodsSupported) {
            return isStatusCodeUnAuthorized;
        }
        else{
               return isStatusCodeUnAuthorized & responseReceived.request().method() == "GET" ;

        }
    }
}
