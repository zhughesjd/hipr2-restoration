package net.joshuahughes.hipr2.upper;
//package code.iface.fft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The interace for the frequency filter operator, for inclusion in the 
 * FFTInterface.
 *
 * @author Simon Horne.
 */
public class FreqFilterInterface {

  /**
   * false = high pass filter, true = low pass filter.
   */
  private boolean removeHigh;
  private JPanel p;
  private JLabel radiusLabel;
  private JTextField radiusText;
  private JComboBox highLowBox;
  public JButton filterButton;
  String [] highLowStrings = {"High Pass Filter", "Low Pass Filter"};

  /**
   * Method to create panel for inclusion in GUI.
   */
  public JPanel createPanel(){
    removeHigh = false;
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);
    radiusLabel = new JLabel("Radius ");
    radiusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
    highLowBox = new JComboBox(highLowStrings);
    highLowBox.setAlignmentY(Component.CENTER_ALIGNMENT);
    highLowBox.setSelectedIndex(0);//Default is high pass filter
    radiusText = new JTextField("50",5);
    radiusText.setAlignmentY(Component.CENTER_ALIGNMENT);
    filterButton = new JButton("Apply Frequency Filter");
    filterButton.setAlignmentY(Component.CENTER_ALIGNMENT);
    
    highLowBox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
  
	String choice = (String) highLowBox.getSelectedItem();
	if(choice == highLowStrings[0]) removeHigh=false;
	else if(choice == highLowStrings[1]) removeHigh=true;
      }
    });

    p.add(filterButton);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(highLowBox);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(radiusLabel);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(radiusText);
    return p;
  }

  /**
   * @return True or false (high pass, low pass).
   */
  public boolean getHighLow(){
    return removeHigh;
  }

  /**
   * @return Integer representing radius of filter.
   */
  public int getRadius(){
    int r = 0;
    try{
      r = Integer.valueOf(radiusText.getText()).intValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
				    ("Invalid radius - "+radiusText.getText()),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
      r=0;
    }
    return r;
  }
}
