package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 * The interface for the three-state (foreground, background, either) kernel.
 */
public class ThreeState3x3Kernel{

  private JPanel p1,p2;
  private JLabel l;
  /**
   * Array of 9 buttons for adjusting the kernel.
   */
  private JButton button[] = new JButton[9];
  /**
   * Array of 9 integers representing the kernel.
   */
  private int buttonData[] = new int[9];
  /**
   * Icon representing background on a button.
   */
  ImageIcon blackIcon;// = new ImageIcon("blackicon.gif");
  /**
   * Icon representing foreground on a button.
   */
  ImageIcon whiteIcon;// = new ImageIcon("whiteicon.gif");
  /**
   * Icon representing off/either on a button.
   */
  ImageIcon greyIcon;// = new ImageIcon("greyicon.gif");

  /**
   * Gets the current kernel settings (0 background, 1 foreground, 2 either).
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
      URL theblackURL = new URL(docbase,"blackicon.gif");
      blackIcon = new ImageIcon(theblackURL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find blackicon.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p2;
    }
    try {
      URL thewhiteURL = new URL(docbase,"whiteicon.gif");
      whiteIcon = new ImageIcon(thewhiteURL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find whiteicon.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p2;
    }
    try {
      URL thegreyURL = new URL(docbase,"greyicon.gif");
      greyIcon = new ImageIcon(thegreyURL);
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Can't find greyicon.gif"),
                                    "Load Error", JOptionPane.WARNING_MESSAGE);
      return p2;
    }

    
    for(i=0;i<9;++i){
      button[i] = new JButton("",greyIcon);
//      button[i] = new JButton();
//      button[i].setIcon(greyIcon);
      buttonData[i] = 2;
      p1.add(button[i]);
    }

    button[0].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[0]==2){
	    button[0].setIcon(blackIcon);
	    buttonData[0]=0;
	  }else if(buttonData[0]==0){
	    button[0].setIcon(whiteIcon);
	    buttonData[0]=1;
	  }else{
	    button[0].setIcon(greyIcon);
	    buttonData[0]=2;
	  }
	}});

    button[1].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[1]==2){
	    button[1].setIcon(blackIcon);
	    buttonData[1]=0;
	  }else if(buttonData[1]==0){
	    button[1].setIcon(whiteIcon);
	    buttonData[1]=1;
	  }else{
	    button[1].setIcon(greyIcon);
	    buttonData[1]=2;
	  }
	}});

    button[2].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[2]==2){
	    button[2].setIcon(blackIcon);
	    buttonData[2]=0;
	  }else if(buttonData[2]==0){
	    button[2].setIcon(whiteIcon);
	    buttonData[2]=1;
	  }else{
	    button[2].setIcon(greyIcon);
	    buttonData[2]=2;
	  }
	}});

    button[3].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[3]==2){
	    button[3].setIcon(blackIcon);
	    buttonData[3]=0;
	  }else if(buttonData[3]==0){
	    button[3].setIcon(whiteIcon);
	    buttonData[3]=1;
	  }else{
	    button[3].setIcon(greyIcon);
	    buttonData[3]=2;
	  }
	}});

    button[4].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[4]==2){
	    button[4].setIcon(blackIcon);
	    buttonData[4]=0;
	  }else if(buttonData[4]==0){
	    button[4].setIcon(whiteIcon);
	    buttonData[4]=1;
	  }else{
	    button[4].setIcon(greyIcon);
	    buttonData[4]=2;
	  }
	}});

    button[5].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[5]==2){
	    button[5].setIcon(blackIcon);
	    buttonData[5]=0;
	  }else if(buttonData[5]==0){
	    button[5].setIcon(whiteIcon);
	    buttonData[5]=1;
	  }else{
	    button[5].setIcon(greyIcon);
	    buttonData[5]=2;
	  }
	}});

    button[6].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[6]==2){
	    button[6].setIcon(blackIcon);
	    buttonData[6]=0;
	  }else if(buttonData[6]==0){
	    button[6].setIcon(whiteIcon);
	    buttonData[6]=1;
	  }else{
	    button[6].setIcon(greyIcon);
	    buttonData[6]=2;
	  }
	}});

    button[7].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[7]==2){
	    button[7].setIcon(blackIcon);
	    buttonData[7]=0;
	  }else if(buttonData[7]==0){
	    button[7].setIcon(whiteIcon);
	    buttonData[7]=1;
	  }else{
	    button[7].setIcon(greyIcon);
	    buttonData[7]=2;
	  }
	}});

    button[8].addActionListener(new ActionListener(){
      	public void actionPerformed(ActionEvent e){
	  if(buttonData[8]==2){
	    button[8].setIcon(blackIcon);
	    buttonData[8]=0;
	  }else if(buttonData[8]==0){
	    button[8].setIcon(whiteIcon);
	    buttonData[8]=1;
	  }else{
	    button[8].setIcon(greyIcon);
	    buttonData[8]=2;
	  }
	}});

    p2.add(l);
    p2.add(p1);
    JLabel l1 = new JLabel("Foreground",whiteIcon,JLabel.CENTER);
    JLabel l2 = new JLabel("Background",blackIcon,JLabel.CENTER);
    JLabel l3 = new JLabel("Any",greyIcon,JLabel.CENTER);
    p2.add(l1);
    p2.add(l2);
    p2.add(l3);
    return p2;
  }
}
