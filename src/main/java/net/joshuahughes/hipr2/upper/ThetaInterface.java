package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ThetaInterface {

  private JPanel p;
  private JLabel l;
  public JTextField t;

  public double number(){
    double theta = 0.7;
    try{
      theta = Double.valueOf(t.getText()).doubleValue();
    }catch(NumberFormatException e){
      theta=-1;
    }
    return theta;
  }

  public JPanel createPanel(){
    p = new JPanel();
    p.setOpaque(false);
    l = new JLabel("Theta value ");
    t = new JTextField("0.45",5);

    p.add(l);
    p.add(t);
    return p;
  }

}
