package org.url_fuzzer_403_bypass;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
public class URLFuzzer403Bypass implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("URL Fuzzer - 403 Bypass");

        //Register our http handler with Burp.
        api.http().registerHttpHandler(new MyURLFuzzer(api));
    }
}
