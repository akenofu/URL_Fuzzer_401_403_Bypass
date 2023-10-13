package org.url_fuzzer_403_bypass;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class TabUI extends JPanel {


    public TabUI() {
        initComponents();
        this.numOfFuzzedCharsTextField.setText(String.valueOf(MyURLFuzzer.numberOfFuzzedCharsInURL));
        this.supportOtherMethodsCheckBox.setSelected(MyURLFuzzer.areOtherHTTPMethodsSupported);

        updateEstimatedNumberOfRequests();
    }

    private void applyConfigurationButtonMouseClicked(MouseEvent e) {
        MyURLFuzzer.numberOfFuzzedCharsInURL = Short.parseShort(numOfFuzzedCharsTextField.getText());
        MyURLFuzzer.areOtherHTTPMethodsSupported = this.supportOtherMethodsCheckBox.isSelected();
        MyURLFuzzer.scannedURLs = new ArrayList<>();
    }

    private void numOfFuzzedCharsTextFieldPropertyChange(PropertyChangeEvent e) {
        updateEstimatedNumberOfRequests();
    }


    private void updateEstimatedNumberOfRequests(){
        int numOfFuzzedChars = Short.parseShort(numOfFuzzedCharsTextField.getText());
        long estimate = (long)pow(MyURLFuzzer.charRange, numOfFuzzedChars) * 7 ;
        estimatedRequestsPerPathTextField.setText("~" +  estimate);
    }


    private void initComponents() {


        numOfFuzzedCharsLabel = new JLabel();
        numOfFuzzedCharsTextField = new JTextField();
        supportOtherMethodsCheckLabel = new JLabel();
        supportOtherMethodsCheckBox = new JCheckBox();
        applyConfigurationButton = new JButton();
        estimatedRequestsPerPathLabel = new JLabel();
        estimatedRequestsPerPathTextField = new JTextField();


        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border
                .EmptyBorder ( 0, 0 ,0 , 0) ,  "" , javax. swing .border . TitledBorder. CENTER ,javax
                . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dialog", java .awt . Font. BOLD ,
                12 ) ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans
                .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "border" .equals ( e.
                getPropertyName () ) )throw new RuntimeException( ) ;} } );
        setLayout(null);


        numOfFuzzedCharsLabel.setText("Fuzzed characters per URL");
        add(numOfFuzzedCharsLabel);
        numOfFuzzedCharsLabel.setBounds(45, 25, 155, 30);


        numOfFuzzedCharsTextField.getDocument().addDocumentListener(new DocumentListener() {public void changedUpdate(DocumentEvent e) {
                updateEstimatedNumberOfRequests();

            }

            public void insertUpdate(DocumentEvent e) {
                updateEstimatedNumberOfRequests();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

        });

        add(numOfFuzzedCharsTextField);
        numOfFuzzedCharsTextField.setBounds(205, 30, 75, 22);


        supportOtherMethodsCheckLabel.setText("Support other methods (POST,PATCH,etc..)");
        add(supportOtherMethodsCheckLabel);
        supportOtherMethodsCheckLabel.setBounds(new Rectangle(new Point(45, 65), supportOtherMethodsCheckLabel.getPreferredSize()));
        add(supportOtherMethodsCheckBox);
        supportOtherMethodsCheckBox.setBounds(280, 65, 25, supportOtherMethodsCheckBox.getPreferredSize().height);


        applyConfigurationButton.setText("Apply Configuration");
        applyConfigurationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                applyConfigurationButtonMouseClicked(e);
            }
        });
        add(applyConfigurationButton);
        applyConfigurationButton.setBounds(new Rectangle(new Point(45, 185), applyConfigurationButton.getPreferredSize()));


        estimatedRequestsPerPathLabel.setText("Estimated Requests per Path");
        add(estimatedRequestsPerPathLabel);
        estimatedRequestsPerPathLabel.setBounds(45, 90, 155, 30);


        estimatedRequestsPerPathTextField.setEditable(false);
        add(estimatedRequestsPerPathTextField);
        estimatedRequestsPerPathTextField.setBounds(205, 95, 75, estimatedRequestsPerPathTextField.getPreferredSize().height);

        {

            Dimension preferredSize = new Dimension();
            for(int i = 0; i < getComponentCount(); i++) {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }

    }



    private JLabel numOfFuzzedCharsLabel;
    private JTextField numOfFuzzedCharsTextField;
    private JLabel supportOtherMethodsCheckLabel;
    private JCheckBox supportOtherMethodsCheckBox;
    private JButton applyConfigurationButton;
    private JLabel estimatedRequestsPerPathLabel;
    private JTextField estimatedRequestsPerPathTextField;
}
