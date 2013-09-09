package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interface for selecting the required size of kernel (5 buttons 3-7).
 *
 * @author Simon Horne.
 */
public class KernelSize {

  private JPanel p;
  private JLabel l;
  private int s = 3;

  /**
   * Gets the current selected size.
   * 
   * @return The size from the interface
   */
  public int size(){
    return s;
  }

  /**
   * Sets up the interface panel for inclusion in a GUI.
   */
  public JPanel createPanel(){
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
    p.setOpaque(false);
    ButtonGroup group = new ButtonGroup();
    JRadioButton size3 = new JRadioButton("3x3");
    JRadioButton size4 = new JRadioButton("4x4");
    JRadioButton size5 = new JRadioButton("5x5");
    JRadioButton size6 = new JRadioButton("6x6");
    JRadioButton size7 = new JRadioButton("7x7");
    size3.setOpaque(false);
    size4.setOpaque(false);
    size5.setOpaque(false);
    size6.setOpaque(false);
    size7.setOpaque(false);
    size3.setSelected(true);
    group.add(size3);
    group.add(size4);
    group.add(size5);
    group.add(size6);
    group.add(size7);

    l = new JLabel("Kernel Size");

    size3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 3;
      }
    });
    size4.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 4;
      }
    });
    size5.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 5;
      }
    });
    size6.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 6;
      }
    });
    size7.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 7;
      }
    });

    p.add(l);
    p.add(size3);
    p.add(size4);
    p.add(size5);
    p.add(size6);
    p.add(size7);
    return p;
  }

}
