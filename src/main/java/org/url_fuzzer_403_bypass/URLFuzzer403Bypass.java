package org.url_fuzzer_403_bypass;


import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;
public class URLFuzzer403Bypass implements BurpExtension {

    private MontoyaApi api;
    @Override
    public void initialize(MontoyaApi api) {
        this.api  = api;

        String extensionName = "URL Fuzzer 401/403 Bypass";
        api.extension().setName(extensionName);

        //Register our http handler with Burp.
        var myURLFuzzer = new MyURLFuzzer(api);
        api.scanner().registerScanCheck(myURLFuzzer);

        MyOptionsTab tableModel = new MyOptionsTab();
        api.userInterface().registerSuiteTab(extensionName, constructTab(tableModel));


    }
    private Component constructTab(MyOptionsTab tableModel)
    {
        JPanel tabUI = new TabUI();
        return tabUI;
    }


}


