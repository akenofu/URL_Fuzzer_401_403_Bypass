package org.url_fuzzer_403_bypass;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.http.Http;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.requests.HttpTransformation;
import burp.api.montoya.logging.Logging;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;
import static burp.api.montoya.http.message.params.HttpParameter.urlParameter;
import static burp.api.montoya.http.message.requests.HttpRequest.httpRequestFromUrl;

import java.util.ArrayList;

public class MyURLFuzzer  implements HttpHandler {
    private final Logging logging;
    private final MontoyaApi api;

    public MyURLFuzzer(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }



    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        return continueWith(requestToBeSent);
    }
    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived originalResponseReceived)
    {
        if( isRequestUnAuthorized(originalResponseReceived) ) {
            logging.logToOutput("Found unauthorized Request: " + originalResponseReceived.initiatingRequest().url());

            HttpRequest initiatingRequest = originalResponseReceived.initiatingRequest();
            String originalPath = initiatingRequest.path();
            ArrayList<String> paths = generatePaths(originalPath);

            for (String path : paths) {
                HttpRequest modifiedRequest = initiatingRequest.withPath(path);

                logging.logToOutput("Modified Request: " + modifiedRequest.toString());
                HttpRequestResponse response = api.http().sendRequest(modifiedRequest);

                if (response.response().statusCode() != originalResponseReceived.statusCode()){
                    // TODO add output to target tab in burp scanner
                    logging.logToOutput("Bypassed !");
                }
            }

        }

        return continueWith(originalResponseReceived);
    }

    public ArrayList<String> generatePaths(String originalPath){
        ArrayList<String> paths = new ArrayList<String>();

        logging.logToOutput("Original Path: " + originalPath);

        // TODO Fix payload generation
        paths.add(originalPath + "wow");

        return paths;
    }

    public boolean isRequestUnAuthorized(HttpResponseReceived responseReceived){
        boolean isStatusCodeUnAuthorized = false;
        boolean isGetRequest = false;
        short responseStatusCode = responseReceived.statusCode();
        short unAuthorizedStatusCodes[] = { 403, 401 };


        for (short statusCode: unAuthorizedStatusCodes){
            if(statusCode == responseStatusCode){
                isStatusCodeUnAuthorized = true;
                break;
            }
        }

        if(responseReceived.initiatingRequest().method() == "GET") {
            isGetRequest = true;
        }


        return  (isStatusCodeUnAuthorized && isGetRequest) ;

    }

}
