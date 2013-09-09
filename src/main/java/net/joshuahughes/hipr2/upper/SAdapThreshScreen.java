package net.joshuahughes.hipr2.upper;
//package code.iface.adapthresh;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import code.iface.utils.*;
//import code.operator.utils.*;
//import code.operator.adapthresh.*;

/**
 *
 *SAdapThreshScreen is the user interface to the adaptive thresholding
 *algorithm (code.operator.AdapThresh.java) It is run as an applet
 *embedded in the file adpthrsh.htm
 *
 *
 *@author Timothy Sharman
 *@see code.operator.adapthresh
 */

public class SAdapThreshScreen extends VisionApplet1 {

  //The operator class for performing adaptive thresholding. It's a thread
  AdapThresh adapthresh;

  //Local Variables for passing to the operator
  int size = 0;
  int con = 0;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the interface
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JButton startbutton = new JButton("Threshold");
  private JButton abortbutton = new JButton("Stop");
  private JLabel neighboursize = new JLabel("Neighbourhood Size");
  private JTextField sizebox = new JTextField(5);
  private JLabel funclabel = new JLabel("Threshold Function");
  private JComboBox funcbox = new JComboBox();
  private JLabel conlabel = new JLabel("Constant Value");
  private JTextField conbox = new JTextField(5);

  public void add_extra() {
 
    //initialise the operator constructors
    adapthresh = new AdapThresh();

    //Set up the mid panel

    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(neighboursize, midc);
    mid. add(neighboursize);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(sizebox, midc);
    mid. add(sizebox);
    sizebox. setText("7");
    
    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(funclabel, midc);
    mid. add(funclabel);

    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(funcbox, midc);
    funcbox. addItem("Mean");
    funcbox. addItem("Median");
    funcbox. addItem("Mean of Min & Max");
    mid. add(funcbox);

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(conlabel, midc);
    mid. add(conlabel);

    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(conbox, midc);
    conbox. setText("7");
    mid. add(conbox);

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
    
    mid. doLayout();
    mid. repaint();

    //Load a different default image
    input. setText("son1.gif");
    load_image. doClick();

    
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
	try{
	  size = Integer. valueOf (sizebox.getText()). intValue();
	  con = Integer. valueOf (conbox.getText()). intValue();
	  if(size > 0){
	    time_msec = System.currentTimeMillis();
	    if(funcbox. getSelectedItem().equals("Mean")){
	      dest_1d = adapthresh. mean_thresh(src_1d, i_w, i_h, size, con);
	    }
	    else if(funcbox. getSelectedItem().equals("Median")){
	      dest_1d = adapthresh. median_thresh(src_1d, i_w, i_h, size, con);
	    }
	    else{
	      dest_1d = adapthresh. meanMaxMin_thresh(src_1d, i_w, i_h, size, con);
	    }

	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken. setText(new Long(time_msec).toString()+" msecs");
	    
	    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	    
	    dest_canvas. updateImage(dest);
	    //Add a new pixel listener to the new dest canvas
	    PixelListener brpl = new PixelListener();
	    dest_canvas. addMouseMotionListener(brpl);
	  }
	  else{
	    JOptionPane.showMessageDialog(null,
	    ("Neighbourhood size must be greater than zero"),
	    ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameters specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	}
	
      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }     
    }
  }
  
  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new adapthresh operator
    adapthresh = new AdapThresh();

    set_src_image2(input_img, w, h, name);
   }

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet to perform Adaptive Thresholding to an image. Written by Timothy Sharman";
  }
}


