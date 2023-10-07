package org.url_fuzzer_403_bypass;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
public class URLFuzzer403Bypass implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("URL Fuzzer 401/403 Bypass");

        //Register our http handler with Burp.
        var myURLFuzzer = new MyURLFuzzer(api);
        api.scanner().registerScanCheck(myURLFuzzer);

    }
}
