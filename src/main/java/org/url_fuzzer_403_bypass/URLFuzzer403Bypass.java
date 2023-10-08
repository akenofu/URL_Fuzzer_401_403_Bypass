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
        int labelHeight = 200;
        int labelWidth = 100;

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel vboxPanel = new JPanel();
        vboxPanel.setLayout(new BoxLayout(vboxPanel, BoxLayout.PAGE_AXIS));

        // Original HBox without the button
        JPanel hboxPanel = new JPanel();
        hboxPanel.setLayout(new BoxLayout(hboxPanel, BoxLayout.LINE_AXIS));

        JPanel textFieldPannel = new JPanel();
        JLabel label = new JLabel("Label");
        textFieldPannel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0,0 ));

        label.setMaximumSize(new Dimension(labelHeight,labelWidth));
        int textFieldMaxWidth  = 150;
        JTextField textField = new FixedSizeTextField(textFieldMaxWidth,80);
        textField.setColumns(1);

        textFieldPannel.add(textField);
        textFieldPannel.setMaximumSize(new Dimension(textFieldMaxWidth, 50));

        hboxPanel.add(label);
        hboxPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        hboxPanel.add(textFieldPannel);

        // Set the preferred height for the original HBox
        hboxPanel.setPreferredSize(new Dimension(textFieldMaxWidth, 50));

        vboxPanel.add(hboxPanel);
        vboxPanel.add(Box.createVerticalStrut(10)); // Add vertical separation

        // First additional HBox with Label and CheckBox
        JPanel checkBoxHBox1 = new JPanel();
        checkBoxHBox1.setLayout(new BoxLayout(checkBoxHBox1, BoxLayout.LINE_AXIS));

        JLabel checkBoxLabel1 = new JLabel("CheckBox 1");
        checkBoxLabel1.setMaximumSize(new Dimension(labelHeight,labelWidth));

        JCheckBox checkBox1 = new JCheckBox();
        checkBox1.setText("");

        checkBoxHBox1.add(checkBoxLabel1);
        checkBoxHBox1.add(Box.createRigidArea(new Dimension(10, 0)));
        checkBoxHBox1.add(checkBox1);

        // Set the preferred height for the first additional HBox
        checkBoxHBox1.setPreferredSize(new Dimension(checkBoxHBox1.getPreferredSize().width, 30));

        vboxPanel.add(checkBoxHBox1);
        vboxPanel.add(Box.createVerticalStrut(10)); // Add vertical separation

        // Second additional HBox with Label and CheckBox
        JPanel checkBoxHBox2 = new JPanel();
        checkBoxHBox2.setLayout(new BoxLayout(checkBoxHBox2, BoxLayout.LINE_AXIS));

        JLabel checkBoxLabel2 = new JLabel("CheckBox 2");
        JCheckBox checkBox2 = new JCheckBox();
        checkBoxLabel2.setMaximumSize(new Dimension(labelHeight,labelWidth));
        checkBox2.setText("");

        checkBoxHBox2.add(checkBoxLabel2);
        checkBoxHBox2.add(Box.createRigidArea(new Dimension(10, 0)));
        checkBoxHBox2.add(checkBox2);

        // Set the preferred height for the second additional HBox
        checkBoxHBox2.setPreferredSize(new Dimension(checkBoxHBox2.getPreferredSize().width, 30));

        vboxPanel.add(checkBoxHBox2);
        vboxPanel.add(Box.createVerticalStrut(10)); // Add vertical separation

        // Button HBox with CENTER-BOTTOM orientation
        JPanel buttonHBox = new JPanel();
        buttonHBox.setLayout(new BoxLayout(buttonHBox, BoxLayout.Y_AXIS));

        JButton bottomButton = new JButton("Bottom Button");

        buttonHBox.add(Box.createVerticalGlue());
        buttonHBox.add(bottomButton);

        // Set the preferred height for the button HBox
        buttonHBox.setPreferredSize(new Dimension(buttonHBox.getPreferredSize().width, 50));

        vboxPanel.add(buttonHBox);

        panel.add(vboxPanel, BorderLayout.WEST);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 300, 10));
        return panel;
    }

    class FixedSizeTextField extends JTextField {
        private final Dimension fixedSize;

        public FixedSizeTextField(int width, int height) {
            super();

            this.fixedSize = new Dimension(width, height);
        }

        @Override
        public Dimension getMaximumSize() {
            return fixedSize;
        }
    }
}


