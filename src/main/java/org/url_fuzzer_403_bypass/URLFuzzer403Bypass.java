package org.url_fuzzer_403_bypass;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;
import static burp.api.montoya.http.message.params.HttpParameter.urlParameter;

public class URL_Fuzzer_403_Bypass  implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("URL Fuzzer - 403 Bypass");

        //Register our http handler with Burp.
        api.http().registerHttpHandler(new MyURLFuzzer(api));
    }
}
