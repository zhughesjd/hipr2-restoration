package net.joshuahughes.hipr2.upper;
//package code.iface.laplacian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The interface for the laplacian of gaussian kernel size 
 * and theta selection.
 */
public class KernelInterface {

  private JPanel p;
  private JLabel kl,tl;
  private JTextField ktf,ttf;

  /**
   * Creates the interface panel for inclusion in the laplacian of
   * gaussian GUI.
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setLayout(new FlowLayout());
    p.setOpaque(false);
    kl = new JLabel("Kernel Size ");
    tl = new JLabel("Theta ");
    ktf = new JTextField("7",5);
    ttf = new JTextField("0.6",5);

    p.add(kl);
    p.add(ktf);
    p.add(tl);
    p.add(ttf);
    return p;
  }
  /**
   * @return The kernel size (-1 if something wrong with input).
   */
  public int getKernelSize(){
    int k;
      try{
	k = Integer.valueOf(ktf.getText()).intValue();
	}catch(NumberFormatException e2){
	  k = -1;
	}
    return k;
  }
  /**
   * @return The theta value (-1 if something wrong with input).
   */
  public double getTheta(){
    double t;
    try{
	t = Double.valueOf(ttf.getText()).doubleValue();
	}catch(NumberFormatException e2){
	  t = -1;
	}
    return t;
  }
  
}
