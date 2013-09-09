package net.joshuahughes.hipr2.upper;
//package code.iface.histgram;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
//import code.iface.utils.*;
//import code.operator.histgram.*;

/**
 *
 *SHistogramScreen is the user interface to the Histogram
 *algorithm.  It is run as an applet
 *embedded in the file hist.htm 
 *@author Timothy Sharman
 */


public class SHistogramScreen extends VisionApplet1 {
  
  //The opertor class for making the histogram. It's a thread.
  Histogram hist;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //the components for the interface
  private JButton startbutton = new JButton("Histogram");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  //histogram label. A bit artificial for 256x256 images...
  JLabel hist_range = new JLabel("0         64"+
			       "           128"+
			       "           192"+
			       "           256");
 
  public void add_extra() {
    
    //initialise the operator constructors
    hist = new Histogram();

    brc. gridx = 0;
    brc. gridy = 2;
    brlayout. setConstraints(hist_range, brc);

    br. remove(outsize);
    br. add(hist_range);

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
   *gui_add_image is used to add the default images to the interface 
   */

  public void gui_add_image() {
    
    //add the source image
    blc. gridx = 0;
    blc. gridy = 1;
    bllayout. setConstraints(src_canvas, blc);
    bl. add(src_canvas); 
    src_canvas. addMouseMotionListener(blpl);
    
    //Just added
    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(dest_canvas, brc);
    br. add(dest_canvas);
    br. remove(brPosition);
    br. remove(brIntensity);
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
	dest_1d = hist. histogram(src_1d, i_w, i_h);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken.setText(new Long(time_msec).toString()+" msecs");
	d_w = (dest_1d.length)/i_h;
	d_h = i_h;
	dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));

	dest_canvas. updateImage(dest);
      }

      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }

 public void set_src_image(int [] input_img , int w, int h, String name) {
    
   //create a new histogram operator
   hist = new Histogram(); 

   set_src_image2(input_img, w, h, name);
   br. remove(brPosition);
   br. remove(brIntensity);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for finding the histogram of an image. Written by Timothy Sharman";
  }
} 
