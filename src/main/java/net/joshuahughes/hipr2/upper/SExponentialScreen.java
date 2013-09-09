package net.joshuahughes.hipr2.upper;
//package code.iface.exponential;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
//import code.iface.utils.*;
//import code.operator.exponential.*;

/**
 *
 *SExponentialScreen is the user interface to the Log
 *algorithm.  It is run as an applet
 *embedded in the file pixexp.htm 
 *@author Timothy Sharman
 */

public class SExponentialScreen extends VisionApplet1 {
 
  //The operator class for performing image exponentiation. It's a thread
  Exponential exponential;

  //Loacal variables
  double expo = 1.03;
  float scaleval = 0;
  float offsetval = 0;
  
 
  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //The components for the GUI
 
  private JTextField gamma  = new JTextField(10);
  private JLabel instructions = new JLabel("Enter gamma value: ");
  private JLabel scaleLabel = new JLabel("Enter scale value: ");
  private JTextField scaleBox = new JTextField(5);
  private JLabel offsetLabel = new JLabel("Enter offset value: ");
  private JTextField offsetBox = new JTextField(5);
  private JButton startbutton = new JButton("Exponentiate");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  public void add_extra() {


    //initialise the operator constructors
    exponential = new Exponential();
 
    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(instructions, midc);
    mid. add(instructions);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(gamma, midc);
    gamma. setText("1.03");
    mid. add(gamma);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(scaleLabel, midc);
    mid. add(scaleLabel);

    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(scaleBox, midc);
    mid. add(scaleBox);
    scaleBox. setText("1");

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(offsetLabel, midc);
    mid. add(offsetLabel);

    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(offsetBox, midc);
    mid. add(offsetBox);
    offsetBox. setText("0");

    midc. gridx = 0;
    midc. gridy = 3;
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
    midc. gridy = 4;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();
    
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
	time_msec = System.currentTimeMillis();
	try {
	  scaleval = Float. valueOf(scaleBox. getText()). floatValue();
	  offsetval = Float. valueOf(offsetBox. getText()). floatValue();
	  expo = (Double.valueOf(gamma. getText()).doubleValue()); 
	  dest_1d = exponential. apply_expo(src_1d, i_w, i_h, expo, 
					    scaleval, offsetval);
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken.setText(new Long(time_msec).toString()+" msecs");
	  dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	  
	  dest_canvas. updateImage(dest);
	  //Add a pixel listener to the new canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(brpl);
	}
	catch (NumberFormatException e) {
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
    
    //create a new exponential operator
    exponential = new Exponential(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for compressing an image using a logarithm. Written by Timothy Sharman";
  }
}

