package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.color.*;

import java.awt.image.*;
import java.net.*;
import javax.swing.event.*;

/**
 * The interface for the two-state (on/off) 3x3 kernel.
 */
public class TwoState3x3Kernel  extends JApplet {

  private JPanel p1,p2;
  private JLabel l;
  /**
   * Array of 9 buttons for toggling values 0,1.
   */
  private JButton button[] = new JButton[9];
  /**
   * Array of 9 values 0,1 for the 9 kernel positions.
   */
  private int buttonData[] = new int[9];
  /**
   * Icon representing an off setting for a button.
   */
  ImageIcon offIcon;// = new ImageIcon("officon.gif");
  /**
   * Icon representing an on setting for a button.
   */
  ImageIcon onIcon;// = new ImageIcon("onicon.gif");

  /**
   * Gets the array representing the kernel.
   *
   * @return The integer array representing the kernel.
   */
  public int [] array(){
    return buttonData;
  }

  /**
   * Sets up the interface panel for inclusion in a GUI.
   */
  public JPanel createPanel(URL docbase){

    int i;
    p1 = new JPanel();
    p2 = new JPanel();
    p1.setLayout(new GridLayout(3,3,5,5));
    p1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    p1.setOpaque(false);
    p2.setOpaque(false);
    l = new JLabel("3x3 Kernel");

    // create colored icons
    try {
      URL theoffURL = new URL(docbase,"officon.gif");
      offIcon = new ImageIcon(theoffURL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find officon.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p2;
    }

    try {
      URL theonURL = new URL(docbase,"onicon.gif");
      onIcon = new ImageIcon(theonURL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find onicon.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p2;
    }
    
    for(i=0;i<9;++i){
      button[i] = new JButton("",offIcon);
      buttonData[i] = 0;
      p1.add(button[i]);
    }
    button[4].setIcon(onIcon);
    buttonData[4] = 1;

    button[0].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[0]==0){
	    button[0].setIcon(onIcon);
	    buttonData[0]=1;
	  }else{
	    button[0].setIcon(offIcon);
	    buttonData[0]=0;
	  }
	}});

    button[1].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[1]==0){
	    button[1].setIcon(onIcon);
	    buttonData[1]=1;
	  }else{
	    button[1].setIcon(offIcon);
	    buttonData[1]=0;
	  }
	}});

    button[2].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[2]==0){
	    button[2].setIcon(onIcon);
	    buttonData[2]=1;
	  }else{
	    button[2].setIcon(offIcon);
	    buttonData[2]=0;
	  }
	}});

    button[3].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[3]==0){
	    button[3].setIcon(onIcon);
	    buttonData[3]=1;
	  }else{
	    button[3].setIcon(offIcon);
	    buttonData[3]=0;
	  }
	}});

    button[4].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[4]==0){
	    button[4].setIcon(onIcon);
	    buttonData[4]=1;
	  }else{
	    button[4].setIcon(offIcon);
	    buttonData[4]=0;
	  }
	}});

    button[5].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[5]==0){
	    button[5].setIcon(onIcon);
	    buttonData[5]=1;
	  }else{
	    button[5].setIcon(offIcon);
	    buttonData[5]=0;
	  }
	}});

    button[6].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[6]==0){
	    button[6].setIcon(onIcon);
	    buttonData[6]=1;
	  }else{
	    button[6].setIcon(offIcon);
	    buttonData[6]=0;
	  }
	}});

    button[7].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[7]==0){
	    button[7].setIcon(onIcon);
	    buttonData[7]=1;
	  }else{
	    button[7].setIcon(offIcon);
	    buttonData[7]=0;
	  }
	}});

    button[8].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[8]==0){
	    button[8].setIcon(onIcon);
	    buttonData[8]=1;
	  }else{
	    button[8].setIcon(offIcon);
	    buttonData[8]=0;
	  }
	}});

    p2.add(l);
    p2.add(p1);

    return p2;
  }
}
