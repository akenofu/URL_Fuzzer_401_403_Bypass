package org.url_fuzzer_403_bypass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class TabUI extends JPanel {
    public TabUI() {
        initComponents();
        this.numOfFuzzedCharsTextField.setText(String.valueOf(MyURLFuzzer.numberOfFuzzedCharsInURL));
        this.supportOtherMethodsCheckBox.setSelected(MyURLFuzzer.areOtherHTTPMethodsSupported);
    }

    private void applyConfigurationButtonMouseClicked(MouseEvent e) {
        MyURLFuzzer.numberOfFuzzedCharsInURL = Short.parseShort(numOfFuzzedCharsTextField.getText());
        MyURLFuzzer.areOtherHTTPMethodsSupported = this.supportOtherMethodsCheckBox.isSelected();
    }

    private void initComponents() {


        numOfFuzzedCharsLabel = new JLabel();
        numOfFuzzedCharsTextField = new JTextField();
        supportOtherMethodsCheckLabel = new JLabel();
        supportOtherMethodsCheckBox = new JCheckBox();
        applyConfigurationButton = new JButton();


        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder
                ( 0, 0 ,0 , 0) ,  "" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border
                .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .awt . Font. BOLD ,12 ) ,java . awt
                . Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void
        propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
                ;} } );
        setLayout(null);


        numOfFuzzedCharsLabel.setText("Fuzzed characters per URL");
        add(numOfFuzzedCharsLabel);
        numOfFuzzedCharsLabel.setBounds(45, 25, 155, 30);
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

}
