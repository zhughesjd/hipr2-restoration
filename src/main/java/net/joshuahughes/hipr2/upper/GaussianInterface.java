package net.joshuahughes.hipr2.upper;
//package code.iface.fft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The interface for the Gaussian smoothing operator for inclusion in the
 * FFT interface.
 *
 * @author Simon Horne.
 */
public class GaussianInterface {

  private JPanel p;
  private JLabel sizel,xl,yl;
  private JTextField sizetf,xtf,ytf;
  JButton smooth;

  /**
   * Method for creating the interface panel for inclusion in the FFT GUI.
   */
  public JPanel createPanel(){
    p = new JPanel();

    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);

    sizel = new JLabel("Size ");
    sizel.setAlignmentY(Component.CENTER_ALIGNMENT);
    sizetf = new JTextField("30",3);
    sizetf.setAlignmentY(Component.CENTER_ALIGNMENT);
    xl = new JLabel("X ");
    xl.setAlignmentY(Component.CENTER_ALIGNMENT);
    xtf = new JTextField("0",3);
    xtf.setAlignmentY(Component.CENTER_ALIGNMENT);
    yl = new JLabel("Y ");
    yl.setAlignmentY(Component.CENTER_ALIGNMENT);
    ytf = new JTextField("0",3);
    ytf.setAlignmentY(Component.CENTER_ALIGNMENT);
    smooth = new JButton("Gaussian Smooth");
    smooth.setAlignmentY(Component.CENTER_ALIGNMENT);

    p.add(smooth);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(sizel);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(sizetf);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(xl);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(xtf);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(yl);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(ytf);
    return p;
  }

  /**
   * @return The size selected or -1 if faulty input.
   */
  public double getSize(){
    double size = 0;
    try{
      size = Double.valueOf(sizetf.getText()).doubleValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
				    ("Invalid size - " +
				     sizetf.getText()),
				    "Invalid Value",
				    JOptionPane.WARNING_MESSAGE); 
      size = -1;
    }
    return size;
  }
  /**
   * @return the x value selected
   */
  public int getX(){
    int x = 0;
    try{
      x = Integer.valueOf(xtf.getText()).intValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
			("Invalid x value - "+xtf.getText()),
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
      x = 1000000;
    }
    return x;
  }
  /**
   * @return the y value selected
   */
  public int getY(){
    int y = 0;
    try{
      y = -Integer.valueOf(ytf.getText()).intValue();
    }catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,
			("Invalid y value - "+ytf.getText()),
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
      y = 1000000;
    }
    return y;
  }
}
