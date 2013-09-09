package net.joshuahughes.hipr2.upper;
// package code.iface.imagenot;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
// import code.iface.utils.*;
//import code.operator.imagenot.*; 

/**
 *
 *ImageNotScreen is the user interface to the ImageNot
 *algorithm.  It is run as an applet
 *embedded in the file imagenot.htm 
 *@author Timothy Sharman
 */

public class SImageNotScreen extends VisionApplet1 {

  //The operator class for performing image inversion. It's a thread
  ImageNot imagenot;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //the components for the interface
  private JButton startbutton = new JButton("Invert");
  private JButton abortbutton = new JButton("Stop");
  private JLabel time = new JLabel("Time");
  private JTextField time_taken = new JTextField(20);

  public void add_extra() {

    //initialise the operator constructors
    imagenot = new ImageNot();

    midc.weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
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
    midc. gridy = 1; 
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
	dest_1d = imagenot. apply_invert(src_1d, i_w, i_h);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken.setText(new Long(time_msec).toString()+" msecs");
	dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));

	dest_canvas. updateImage(dest);
	//Add a pixel listener to the canvas
	PixelListener brpl = new PixelListener();
	dest_canvas. addMouseMotionListener(brpl);
      }

      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }


  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new inversion operator
    imagenot = new ImageNot(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for inverting an image. Written by Timothy Sharman";
  }
}

