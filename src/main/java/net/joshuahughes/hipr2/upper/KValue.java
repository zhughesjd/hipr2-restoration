package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interface for selecting the unsharp k value.
 *
 * @author Simon Horne.
 */
public class KValue {

  private JPanel p;
  private JLabel l;
  private JTextField t;
  /**
   * Gets the k value.
   *
   * @return the k value from the interface
   */
  public double value(){
    double k = 0.7;
    try{
        k = Double.valueOf(t.getText()).doubleValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
				    "Invalid k value - "+t.getText(),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
      k = 0;//Don't do anything if bad number entered
    }
    if(k<0){
      JOptionPane.showMessageDialog(null,
				    "K must be >= 0 : "+t.getText(),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
    }
    return k;
  }

  /**
   * Sets up the panel for inclusion in the unsharp interface.
   * @return the panel
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setOpaque(false);
    l = new JLabel("K Parameter ");
    t = new JTextField("0.7",5);

    p.add(l);
    p.add(t);
    return p;
  }

}
