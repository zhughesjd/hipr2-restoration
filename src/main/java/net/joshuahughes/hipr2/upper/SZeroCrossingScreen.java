package net.joshuahughes.hipr2.upper;
//package code.iface.zerocross;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import code.iface.utils.*;
//import code.operator.zerocross.*;

/**
 *
 *SZeroCrossingScreen is the user interface to the zero crossing detector
 *algorithm (code.operator.ZeroCrossing.java) It is run as an applet
 *embedded in the file zerocross.htm.
 *
 *@author Timothy Sharman
 */

public class SZeroCrossingScreen extends VisionApplet1 {
  
  //The operator class for performing zero crossing detection. It's a thread
  ZeroCrossing zerocrossing;

  //Local variables for passing to the operator
  int kersize = 3;
  float std_dev = 1;
  boolean limit = false;
  int gradient = 0;

  //The listeners for the GUI
  private ComboListener clkernel = new ComboListener();
  private ComboListener clcross = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the GUI
  private JComboBox kerbox = new JComboBox();
  private JComboBox crossbox = new JComboBox();
  private JLabel kerlabel = new JLabel("Select Kernel Size");
  private JLabel thetalabel = new JLabel("Standard Deviation");
  private JTextField thetaval = new JTextField(5);
  private JLabel crosslabel = new JLabel("Crossing Limiter");
  private JLabel gradlabel = new JLabel("Gradient Limit Value");
  private JTextField gradval = new JTextField(5);
  private JButton startbutton = new JButton("Zero Crossing");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  public void add_extra() {
 
    //initialise the operator constructors
    zerocrossing = new ZeroCrossing();
    
    midc. weighty = 0.5;
    
    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(kerlabel, midc);
    mid. add(kerlabel);

    midc. gridx = 1;
    midc. gridy = 0;
    kerbox. addItem("3x3 Kernel");
    kerbox. addItem("4x4 Kernel");
    kerbox. addItem("5x5 Kernel");
    kerbox. addItem("6x6 Kernel");
    kerbox. addItem("7x7 Kernel");
    midlayout. setConstraints(kerbox, midc);
    mid. add(kerbox);
    kerbox. addActionListener(clkernel);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(thetalabel, midc);
    mid. add(thetalabel);
   
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(thetaval, midc);
    mid. add(thetaval);
    thetaval. setText("0.45");

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(crosslabel, midc);
    mid. add(crosslabel);
    
    midc. gridx = 1;
    midc. gridy = 2;
    crossbox. addItem("All Crossings");
    crossbox. addItem("Gradient Limited Crossings");
    midlayout. setConstraints(crossbox, midc);
    mid. add(crossbox);
    crossbox. addActionListener(clcross);

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(gradlabel, midc);
    mid. add(gradlabel);

    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(gradval, midc);
    mid. add(gradval);
    gradval. setText("0");
    gradval. setEditable(false);

    midc. gridx = 0;
    midc. gridy = 4;
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
    midc. gridy = 5;
    midlayout. setConstraints(time, midc);
    
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();

    //Load a differnt default image
    input. setText("cln1.gif");
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

      if( cb.getSelectedItem().equals("3x3 Kernel") ) {
	kersize = 3;
      }
      if(cb. getSelectedItem().equals("4x4 Kernel") ) {
	kersize = 4;
      }
      if(cb. getSelectedItem().equals("5x5 Kernel") ) {
	kersize = 5;
      }
      if(cb. getSelectedItem().equals("6x6 Kernel") ) {
	kersize = 6;
      }
      if(cb. getSelectedItem().equals("7x7 Kernel") ) {
	kersize = 7;
      }
      if(cb. getSelectedItem().equals("All Crossings") ) {
	limit = false;
	gradval. setEditable(false);
      }
      if(cb. getSelectedItem().equals("Gradient Limited Crossings") ) {
	limit = true;
	gradval. setEditable(true);
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
	time_msec = System.currentTimeMillis();
	try {
	  std_dev = Float. valueOf (thetaval.getText()). floatValue();
	  if((std_dev < 0.4) || (std_dev > ((float)kersize / 6))){
	    JOptionPane.showMessageDialog(null,("Theta value must be 0.4 < theta < kersize/6"),
					  "Error!", JOptionPane.WARNING_MESSAGE);
	  }
	  else{
	    gradient = Integer. valueOf(gradval.getText()). intValue();
	    dest_1d = zerocrossing. apply_zeroCrossing(src_1d, i_w, i_h, 
						       kersize, std_dev, 
						       limit, gradient);
	    
	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken.setText(new Long(time_msec).toString()+" msecs");
	    
	    //Must change the output image sizes
	    d_w = i_w - 3;
	    d_h = i_h - 3;
	    
	    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	    outsize. setText(d_w+" x "+d_h);
	    
	    //Must reset image sizes
	    d_w = i_w + 3;
	    d_h = i_h + 3;
	    
	    dest_canvas. updateImage(dest);
	    //Add a pixel listener to the canvas
	    PixelListener brpl = new PixelListener();
	    dest_canvas. addMouseMotionListener(brpl);
	    container. validate();
	  }
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameters specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	}
	
      }

      else if( b == abortbutton ) {
	
	time_taken. setText("");
	
      }    
    }
  }

  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new zero crossing operator
    zerocrossing = new ZeroCrossing();

    set_src_image2(input_img, w, h, name);
   }

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet to find the zero crossings in an image. Written by Timothy Sharman";
  }
}
