package net.joshuahughes.hipr2.upper;
//package code.iface.contrast;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
//import code.iface.utils.*;
//import code.operator.contrast.*;
//import code.operator.histgram.*;

/**
 *
 *SContrastStretchScreen is the user interface to the ImageNot
 *algorithm.  It is run as an applet
 *embedded in the file contrast.htm 
 *@author Timothy Sharman
 */

public class SContrastStretchScreen extends VisionApplet1 {

  //The operator class for performing contrast stretching. It's a thread
  ContrastStretch contraststretch;

  //Variables to be passed to the operator
  float cutoffval = 0;

  float hipval = 0;
  float lopval = 0;
  
  int uplim = 255;
  int lolim = 0;

  boolean flag = true;

  //The listeners for the GUI
  private ComboListener choicecl = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //the components for the interface
  private JLabel stretch_type = new JLabel("Stretching Method");
  private JComboBox choice = new JComboBox();
  private JLabel cutofflabel = new JLabel("Cutoff Value");
  private JTextField cutoff = new JTextField(10);
  private JLabel lowlabel = new JLabel("Lower Limit - A");
  private JTextField lowbox = new JTextField(5);
  private JLabel highlabel = new JLabel("Upper Limit - B");
  private JTextField highbox = new JTextField(5);
  private JLabel hiperlabel = new JLabel("Upper Percentage Boundary");
  private JTextField hipercent = new JTextField(5);
  private JLabel loperlabel = new JLabel("Lower Percentage Boundary");
  private JTextField lopercent = new JTextField(5);
  private JButton startbutton = new JButton("Stretch");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  public void add_extra() {

    //initialise the operator constructors
    contraststretch = new ContrastStretch();

    midc.weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(stretch_type, midc);
    mid. add(stretch_type);

    midc. gridx = 1;
    midc. gridy = 0;
    choice. addItem("Normal");
    choice. addItem("Percentile");
    choice. addItem("Cutoff");
    midlayout. setConstraints(choice, midc);
    mid. add(choice);
    choice. addActionListener(choicecl);
   
    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(lowlabel, midc);
    mid. add(lowlabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(lowbox, midc);
    mid. add(lowbox);
    lowbox. setText("0");
    
    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(highlabel, midc);
    mid. add(highlabel);

    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(highbox, midc);
    mid. add(highbox);
    highbox. setText("255");

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(hiperlabel, midc);
    mid. add(hiperlabel);
    
    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(hipercent, midc);
    mid. add(hipercent);
    hipercent. setText("0.95");
    hipercent. setEditable(false);
    
    midc. gridx = 0;
    midc. gridy = 4;
    midlayout. setConstraints(loperlabel, midc);
    mid. add(loperlabel);

    midc. gridx = 1;
    midc. gridy = 4;
    midlayout. setConstraints(lopercent, midc);
    mid. add(lopercent);
    lopercent. setText("0.05");
    lopercent. setEditable(false);

    midc. gridx = 0;
    midc. gridy = 5;
    midlayout. setConstraints(cutofflabel, midc);
    mid. add(cutofflabel);

    midc. gridx = 1;
    midc. gridy = 5;
    midlayout. setConstraints(cutoff, midc);
    mid. add(cutoff);
    cutoff. setText("0.0");
    cutoff. setEditable(false);

    midc. gridx = 0;
    midc. gridy = 6;
    midlayout. setConstraints(startbutton, midc);
    startbutton .setBackground(Color.green);
    mid. add(startbutton);
    startbutton. addActionListener(startbl);
    
    midc. gridx = GridBagConstraints.RELATIVE;
    abortbutton. setBackground(Color.red);
    midlayout. setConstraints(abortbutton, midc);
    mid. add(abortbutton);
    abortbutton. addActionListener(abortbl);

    midc. gridx = 0;
    midc. gridy = 7;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();
    
    //Load different image
    input. setText("wom1.gif");
    load_image. doClick();


  }

 /**
   *
   *Handles the actions performed when combo lists on the interface are selected.
   *
   *@param evt The event which caused this object to be called
   *
   */
  
  class ComboListener implements ActionListener {
    
    public void actionPerformed(ActionEvent evt) {
      JComboBox cb = (JComboBox)evt.getSource();
      
      if( cb.getSelectedItem().equals("Normal")){
	hipercent. setEditable(false);
	lopercent. setEditable(false);
	cutoff. setEditable(false);
      }
      else if(cb.getSelectedItem().equals("Percentile")){
	cutoff. setEditable(false);
	hipercent. setEditable(true);
	lopercent. setEditable(true);
      }
      else if(cb.getSelectedItem().equals("Cutoff")){
	cutoff. setEditable(true);
	hipercent. setEditable(false);
	lopercent. setEditable(false);
      }
    }
  }

 /**
   *
   *Handles the actions performed when buttons on the interface are pressed.
   *
   *@param evt The event which caused this object to be called
   *
   */
  
  
  class ButtonListener implements ActionListener {
    
    public void actionPerformed(ActionEvent evt) {
      JButton b = (JButton)evt.getSource();
      if( b == startbutton ) {
	flag = true;
	try{
	  time_msec = System.currentTimeMillis();
	  uplim = Integer. valueOf(highbox.getText()). intValue();
	  lolim = Integer. valueOf(lowbox. getText()). intValue();
	  if((uplim > 255) || (lolim < 0)){
	    JOptionPane.showMessageDialog(null,("Upper and lower limits must be"+
                                                "between 0 and 255"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	  else if(uplim <= lolim){
	    JOptionPane.showMessageDialog(null,("Upper limit must be greater"+
						 "than the lower limit"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	  else{

	    if(choice. getSelectedItem().equals("Cutoff")){
	      cutoffval = Float. valueOf (cutoff.getText()). floatValue();
	      if((cutoffval < 0) || (cutoffval > 1)){
		JOptionPane.showMessageDialog(null,("Cutoff value must be between 0 and 1"),
					      ("Error!"), JOptionPane.WARNING_MESSAGE);
		flag = false;
	      }
	      else{
		dest_1d = contraststretch. cutoff_stretch(src_1d, i_w, i_h, 
							  cutoffval, uplim, lolim);
	      }
	    }
	    else if(choice. getSelectedItem().equals("Normal")){
	      dest_1d = contraststretch. normal_stretch(src_1d, i_w, i_h, uplim, lolim);
	    }
	    else if(choice.getSelectedItem().equals("Percentile")){
	      hipval = Float. valueOf (hipercent.getText()). floatValue();
	      lopval = Float. valueOf (lopercent.getText()). floatValue();
	      if((hipval > 1) || (lopval < 0)){
		flag = false;
		JOptionPane.showMessageDialog(null,("Percentage values must be between 0 and 1"),
					      ("Error!"), JOptionPane.WARNING_MESSAGE);
	      }
	      else if(hipval <= lopval){
		flag = false;
		JOptionPane.showMessageDialog(null,("High percentage must be bigger than lower percentage"),
					      ("Error!"), JOptionPane.WARNING_MESSAGE);
	      }
	      else{
		dest_1d = contraststretch. percentile_stretch(src_1d, i_w, i_h, 
							      hipval, lopval, uplim, 
							      lolim);
	      }
	    }
	    
	    if(flag){
	      time_msec = System.currentTimeMillis() - time_msec;
	      time_taken.setText(new Long(time_msec).toString()+" msecs");
	      dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	      
	      dest_canvas. updateImage(dest);
	      //Add a pixel listener to the new canvas
	      PixelListener brpl = new PixelListener();
	      dest_canvas. addMouseMotionListener(brpl);
	    }
	  }
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameter values specified"),
					      ("Error!"), JOptionPane.WARNING_MESSAGE);
	}
      
      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      } 
    }
  }


  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new inversion operator
    contraststretch = new ContrastStretch(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for contrast stretching image. Written by Timothy Sharman";
  }
}
