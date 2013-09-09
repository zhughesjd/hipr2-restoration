package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 * Interface for the laplacian kernel type selection.
 */
public class KernelType {

  private JPanel p;
  private JLabel l;
  private int t = 1;

  /**
   * Gets the kernel type selected by the classifyMenuItem.setEnabled(false);user.
   *
   * @return The kernel type.
   */
  public int type(){
    return t;
  }

  /**
   * Creates the panel for inclusion in the laplacian GUI.
   */
  public JPanel createPanel(URL docbase){
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);
    ButtonGroup group = new ButtonGroup();
    JRadioButton type1 = new JRadioButton("1");
    JRadioButton type2 = new JRadioButton("2");
    JRadioButton type3 = new JRadioButton("3");

    // make icons

//    JLabel label1 =       new JLabel(new ImageIcon("kernel1.gif"));
//    JLabel label2 =       new JLabel(new ImageIcon("kernel2.gif"));
//    JLabel label3 =       new JLabel(new ImageIcon("kernel3.gif"));
    ImageIcon ker1Icon;
    try {
      URL theker1URL = new URL(docbase,"kernel1.gif");
      ker1Icon = new ImageIcon(theker1URL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find kernel1.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p;
    }
    JLabel label1 = new JLabel(ker1Icon);
    ImageIcon ker2Icon;
    try {
      URL theker2URL = new URL(docbase,"kernel2.gif");
      ker2Icon = new ImageIcon(theker2URL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find kernel2.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p;
    }
    JLabel label2 = new JLabel(ker2Icon);
    ImageIcon ker3Icon;
    try {
      URL theker3URL = new URL(docbase,"kernel3.gif");
      ker3Icon = new ImageIcon(theker3URL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find kernel3.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p;
    }
    JLabel label3 = new JLabel(ker3Icon);

    // fix up panel
    type1.setOpaque(false);
    type2.setOpaque(false);
    type3.setOpaque(false);
    group.add(type1);
    group.add(type2);
    group.add(type3);
    type1.setSelected(true);

    l = new JLabel("Kernel Selection");

    type1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=1;
      }
    });
    type2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=2;
      }
    });
    type3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=3;
      }
    });


    p.add(l);
    p.add(type1);
    p.add(label1);
    p.add(Box.createRigidArea(new Dimension(20,0)));
    p.add(type2);
    p.add(label2);
    p.add(Box.createRigidArea(new Dimension(20,0)));
    p.add(type3);
    p.add(label3);
    return p;
  }

}
