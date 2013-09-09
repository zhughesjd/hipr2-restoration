package net.joshuahughes.hipr2.upper;
//package code.iface.fft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The interface for the notch filter (horizontal and vertical line removal)
 * operator.
 *
 * @author Simon Horne.
 */
public class NotchFilterInterface {

  /**
   * Width is the width of the horizontal and vertical notches.
   */
  private int width;
  /**
   * Radius is the radius around (0,0) where the pixels are unaffected.
   */
  private int radius;
  private JPanel p;
  private JLabel widthLabel,radiusLabel;
  private JTextField widthText,radiusText;
  public JButton notchButton;

  /**
   * Method to create the panel for inclusion in the FFT GUI.
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);
    widthLabel = new JLabel("Width ");
    widthLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
    radiusLabel = new JLabel("Radius ");
    radiusLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
    widthText = new JTextField("5",5);
    widthText.setAlignmentY(Component.CENTER_ALIGNMENT);
    radiusText = new JTextField("15",5);
    radiusText.setAlignmentY(Component.CENTER_ALIGNMENT);
    notchButton = new JButton("Apply Notch Filter");
    notchButton.setAlignmentY(Component.CENTER_ALIGNMENT);

    p.add(notchButton);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(widthLabel);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(widthText);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(radiusLabel);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(radiusText);
    return p;
  }

  /**
   * @return The value of width from the width text box.
   */
  public int getWidth(){
    int w = 0;
    try{
      w = Integer.valueOf(widthText.getText()).intValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
				    ("Invalid width - "+widthText.getText()),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
      w=0;
    }
    return w;
  }

  /**
   * @return The value of radius from the radius text box.
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
