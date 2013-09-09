package net.joshuahughes.hipr2.upper;
//package code.iface.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The interface for selecting the number of iterations an operator
 * should run for.
 *
 * @author Simon Horne.
 */
public class IterationsInterface {

  private JPanel p;
  public JLabel l;
  private JTextField t;
  
  /**
   * Gets the number of iterations from the onscreen textbox, the user
   * is alerted to an invalid value with a warning.
   *
   * @return the number of iterations
   * @exception NumberFormatException when an invalid number is entered
   */
  public int number(){
    int iterations=0;
    try{
      iterations = Integer.valueOf(t.getText()).intValue();
      if(iterations<0){
	JOptionPane.showMessageDialog(null,
			("Invalid number of iterations - "+t.getText()),
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
      }
    }catch(NumberFormatException e2){
      JOptionPane.showMessageDialog(null,
		      ("Invalid number of iterations - "+t.getText()),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
      iterations = 0;//Don't do anything if bad number entered
      
    }
    return iterations;
  }
  /**
   * Sets up the interface and displays it on the screen.
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setOpaque(false);
    l = new JLabel("Number Of Iterations: ");
    t = new JTextField("1",5);

    p.add(l);
    p.add(t);
    return p;
  }

}
