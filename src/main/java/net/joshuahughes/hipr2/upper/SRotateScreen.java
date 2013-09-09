package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 *SRotateScreen is the user interface to the Rotate
 *algorithm.  It is run as an applet
 *embedded in the file rotate.htm 
 *@author Timothy Sharman
 */


public class SRotateScreen extends VisionApplet1 {

  //The operator class for performing image rotation. It's a thread
  Rotate rotateob;

  //The amount of rotation
  int degrees = 0;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //the components for the interface
  private JLabel angle = new JLabel("Enter Rotation Angle: ");
  private JTextField angle_val = new JTextField(4);
  private JButton startbutton = new JButton("Rotate");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");

  public void add_extra() {

    //initialise the operator constructors
    rotateob = new Rotate();

    midc.weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(angle, midc);
    mid. add(angle);

    midc. gridx = 1;
    midc. gridy = 0;
    angle_val. setText("0");
    midlayout. setConstraints(angle_val, midc);
    mid. add(angle_val);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(startbutton, midc);
    startbutton .setBackground(Color.green);
    mid. add(startbutton);
    startbutton. addActionListener(startbl);
    
    midc. gridx = GridBagConstraints.RELATIVE;
    abortbutton. setBackground(Color.red);
    midlayout. setConstraints(abortbutton, midc);
    mid. add(abortbutton);
    abortbutton. addActionListener(abortbl);

    midc. gridwidth = 1;
    midc. gridx = 0;
    midc. gridy = 2;
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
	try{
	degrees = Integer.parseInt(angle_val. getText()); 
	}
	catch (NumberFormatException e) {
	  JOptionPane.showMessageDialog(null,("Invalid Rotation Angle. Setting to zero"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	  angle_val. setText("0");
	  degrees = 0;
	}
	time_msec = System.currentTimeMillis();
	
	dest_1d = rotateob. rotate(src_1d, i_w, i_h, degrees);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken. setText(new Long(time_msec).toString()+" msecs");
	dest = createImage(new MemoryImageSource(i_w,i_h,dest_1d,0,i_w));

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
    rotateob = new Rotate(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for rotating an image. Written by Timothy Sharman";
  }
}

