package net.joshuahughes.hipr2.upper;
//package code.iface.fft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class containing the GUI for the DefinableNotch operator, for inclusion
 * in the FFTInterface.
 *
 * @author Simon Horne.
 */
public class DefinableNotchInterface {

  private JPanel p;
  private JLabel x1l,y1l,x2l,y2l;
  public JTextField x1tf,y1tf,x2tf,y2tf;
  public JButton notchButton, notchKeepButton;

  /**
   * Method to create the panel for inclusion in the GUI.
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);
    x1l = new JLabel("X1 ");
    x1l.setAlignmentY(Component.CENTER_ALIGNMENT);
    y1l = new JLabel("Y1 ");
    y1l.setAlignmentY(Component.CENTER_ALIGNMENT);
    x2l = new JLabel("X2 ");
    x2l.setAlignmentY(Component.CENTER_ALIGNMENT);
    y2l = new JLabel("Y2 ");
    y2l.setAlignmentY(Component.CENTER_ALIGNMENT);
    x1tf = new JTextField("10",5);
    x1tf.setAlignmentY(Component.CENTER_ALIGNMENT);
    y1tf = new JTextField("10",5);
    y1tf.setAlignmentY(Component.CENTER_ALIGNMENT);
    x2tf = new JTextField("20",5);
    x2tf.setAlignmentY(Component.CENTER_ALIGNMENT);
    y2tf = new JTextField("20",5);
    y2tf.setAlignmentY(Component.CENTER_ALIGNMENT);
    notchButton = new JButton("Remove Area");
    notchButton.setAlignmentY(Component.CENTER_ALIGNMENT);
    notchKeepButton = new JButton("Keep Area");
    notchKeepButton.setAlignmentY(Component.CENTER_ALIGNMENT);

    p.add(notchButton);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(notchKeepButton);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(x1l);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(x1tf);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(y1l);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(y1tf);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(x2l);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(x2tf);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(y2l);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(y2tf);
    return p;
  }

  /**
   * @return The top-left x coordinate.
   */
  public int getX1(){
    int c = 0;
    try{
      c = Integer.valueOf(x1tf.getText()).intValue();
    }catch(NumberFormatException e){
      c=-10000;
    }
    return c;
  }
  /**
   * @return The top-left y coordinate.
   */
  public int getY1(){
    int c = 0;
    try{
      c = Integer.valueOf(y1tf.getText()).intValue();
    }catch(NumberFormatException e){
      c=-10000;
    }
    return c;
  }
  /**
   * @return The bottom-right x coordinate.
   */
  public int getX2(){
    int c = 0;
    try{
      c = Integer.valueOf(x2tf.getText()).intValue();
    }catch(NumberFormatException e){
      c=-10000;
    }
    return c;
  }
  /**
   * @return The bottom-right y coordinate.
   */
  public int getY2(){
 int c = 0;
    try{
      c = Integer.valueOf(y2tf.getText()).intValue();
    }catch(NumberFormatException e){
      c=-10000;
    }
    return c;
  }
  /**
   * Gets the coordinates of the selected area as an array of four ints
   * between 0 and size of image (negative if error in data entry).
   *
   * @return An array of coordinates {x1,y1,x2,y2}.
   */
  public int [] getCoords(int s){
    int [] coords = new int [4];
    coords[0] = getX1();
    coords[1] = -getY1();
    coords[2] = getX2();
    coords[3] = -getY2();
    return coords;
  }
}
