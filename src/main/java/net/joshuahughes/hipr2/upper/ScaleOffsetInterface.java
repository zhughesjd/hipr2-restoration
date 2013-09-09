package net.joshuahughes.hipr2.upper;
// package code.iface.common;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScaleOffsetInterface {

  private JPanel p;
  private JLabel scaleLabel;
  private JLabel offsetLabel;
  private JTextField scaleText;
  private JTextField offsetText;

  public double offset(){
    double offset = 0;
    try{
      offset = Double.valueOf(offsetText.getText()).doubleValue();
    }catch(NumberFormatException e){
      offset=1000000;
    }
    return offset;
  }

  public JPanel createPanel(){
    p = new JPanel();
    p.setOpaque(false);
    scaleLabel = new JLabel("Output Display Scale value ");
    scaleText = new JTextField("1",5);
    offsetLabel = new JLabel("Output Display Offset value ");
    offsetText = new JTextField("0",5);
    p.add(scaleLabel);
    p.add(scaleText);
    p.add(offsetLabel);
    p.add(offsetText);
    return p;
  }

  public double scale(){
    double scale = 1.0;
    try{
      scale = Double.valueOf(scaleText.getText()).doubleValue();
    }catch(NumberFormatException e){
      scale=1000000;
    }
    return scale;
  }

}
