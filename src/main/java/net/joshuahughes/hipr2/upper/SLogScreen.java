package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 *SLogScreen is the user interface to the Log
 *algorithm.  It is run as an applet
 *embedded in the file pixlog.htm 
 *@author Timothy Sharman
 */

public class SLogScreen extends VisionApplet1 {

  //The operator class for performing image logarithm. It's a thread
  PixLog pixlog;

  double scaleval = 70;

  float offsetval = -160;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //The components for the GUI
  private JTextField logscale  = new JTextField(5);
  private JLabel instructions = new JLabel("Enter scale factor: ");
  private JLabel offsetLabel = new JLabel("Enter offset value: ");
  private JTextField offsetBox = new JTextField(5);
  private JButton startbutton = new JButton("Apply Log");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  public void add_extra() {

    //initialise the operator constructors
    pixlog = new PixLog();

    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(instructions, midc);
    mid. add(instructions);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(logscale, midc);
    logscale. setText("70");
    mid. add(logscale);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(offsetLabel, midc);
    mid. add(offsetLabel);

    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(offsetBox, midc);
    mid. add(offsetBox);
    offsetBox. setText("-160");

    midc. gridx = 0;
    midc. gridy = 2;
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
    midc. gridy = 3;
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
	  scaleval = (Double.valueOf(logscale. getText()).doubleValue()); 
	  offsetval = Float. valueOf(offsetBox. getText()). floatValue();
	  dest_1d = pixlog. apply_log(src_1d, i_w, i_h, scaleval,offsetval);
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken.setText(new Long(time_msec).toString()+" msecs");
	  dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	  
	  dest_canvas. updateImage(dest);
	  //Add a pixel listener to the canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(brpl);
	}
	catch (NumberFormatException e) {
	  JOptionPane.showMessageDialog(null,("Invalid Scale or Offset Value"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	}

      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }

  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new logarithm operator
    pixlog = new PixLog(); 
    
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



