import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Sun Oct 08 22:20:12 BST 2023
 */



/**
 * @author karim
 */
public class TabUI extends JPanel {
	public TabUI() {
		initComponents();
	}

	private void numOfFuzzedCharsTextFieldPropertyChange(PropertyChangeEvent e) {
		
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
		. swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,
		12 ) ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans
		.PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e.
		getPropertyName () ) )throw new RuntimeException( ) ;} } );
		setLayout(null);

		
		numOfFuzzedCharsLabel.setText("Fuzzed characters per URL");
		add(numOfFuzzedCharsLabel);
		numOfFuzzedCharsLabel.setBounds(45, 25, 155, 30);

		
		numOfFuzzedCharsTextField.addPropertyChangeListener("text", e -> numOfFuzzedCharsTextFieldPropertyChange(e));
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
	

	private void applyConfigurationButtonMouseClicked(MouseEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Karim Mohamed
		numOfFuzzedCharsLabel = new JLabel();
		numOfFuzzedCharsTextField = new JTextField();
		supportOtherMethodsCheckLabel = new JLabel();
		supportOtherMethodsCheckBox = new JCheckBox();
		applyConfigurationButton = new JButton();
		estimatedRequestsPerPathLabel = new JLabel();
		estimatedRequestsPerPathTextField = new JTextField();

		//======== this ========
		setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax.
		swing. border. EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border
		. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog"
		,java .awt .Font .BOLD ,12 ), java. awt. Color. red) , getBorder
		( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java
		.beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException
		( ); }} );
		setLayout(null);

		//---- numOfFuzzedCharsLabel ----
		numOfFuzzedCharsLabel.setText("Fuzzed characters per URL");
		add(numOfFuzzedCharsLabel);
		numOfFuzzedCharsLabel.setBounds(45, 25, 155, 30);

		//---- numOfFuzzedCharsTextField ----
		numOfFuzzedCharsTextField.addPropertyChangeListener("text", e -> numOfFuzzedCharsTextFieldPropertyChange(e));
		add(numOfFuzzedCharsTextField);
		numOfFuzzedCharsTextField.setBounds(205, 30, 75, 22);

		//---- supportOtherMethodsCheckLabel ----
		supportOtherMethodsCheckLabel.setText("Support other methods (POST,PATCH,etc..)");
		add(supportOtherMethodsCheckLabel);
		supportOtherMethodsCheckLabel.setBounds(new Rectangle(new Point(45, 65), supportOtherMethodsCheckLabel.getPreferredSize()));
		add(supportOtherMethodsCheckBox);
		supportOtherMethodsCheckBox.setBounds(280, 65, 25, supportOtherMethodsCheckBox.getPreferredSize().height);

		//---- applyConfigurationButton ----
		applyConfigurationButton.setText("Apply Configuration");
		applyConfigurationButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				applyConfigurationButtonMouseClicked(e);
			}
		});
		add(applyConfigurationButton);
		applyConfigurationButton.setBounds(new Rectangle(new Point(45, 185), applyConfigurationButton.getPreferredSize()));

		//---- estimatedRequestsPerPathLabel ----
		estimatedRequestsPerPathLabel.setText("Estimated Requests per Path");
		add(estimatedRequestsPerPathLabel);
		estimatedRequestsPerPathLabel.setBounds(45, 90, 155, 30);

		//---- estimatedRequestsPerPathTextField ----
		estimatedRequestsPerPathTextField.setEditable(false);
		add(estimatedRequestsPerPathTextField);
		estimatedRequestsPerPathTextField.setBounds(205, 95, 75, estimatedRequestsPerPathTextField.getPreferredSize().height);

		{
			// compute preferred size
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
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Karim Mohamed
	private JLabel numOfFuzzedCharsLabel;
	private JTextField numOfFuzzedCharsTextField;
	private JLabel supportOtherMethodsCheckLabel;
	private JCheckBox supportOtherMethodsCheckBox;
	private JButton applyConfigurationButton;
	private JLabel estimatedRequestsPerPathLabel;
	private JTextField estimatedRequestsPerPathTextField;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
