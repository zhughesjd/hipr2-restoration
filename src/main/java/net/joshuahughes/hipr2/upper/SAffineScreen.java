package net.joshuahughes.hipr2.upper;
//package code.iface.affine;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
//import code.iface.utils.*;
//import code.operator.affine.*;

/**
 *
 *SAffineScreen is the user interface to the Affine
 *algorithm.  It is run as an applet
 *embedded in the file affine.htm 
 *@author Timothy Sharman
 */

public class SAffineScreen extends VisionApplet1 {
 
  //The operator class for performing an affine transformation. It's a thread
  Affine affine;

  //The arrays used to store the parameters for the transformation
  float [] a_array = new float[4];
  float [] b_array = new float[2];

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //Two panels for use with the array inputs, and their layout managers
  private JPanel a_panel = new JPanel();
  private JPanel b_panel = new JPanel();
  private GridLayout a_layout = new GridLayout(2,3,5,5);
  private GridLayout b_layout = new GridLayout(2,2,5,5);

  //the components for the interface
  private JButton startbutton = new JButton("Transform");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JTextField a11_box = new JTextField(3);
  private JTextField a12_box = new JTextField(3);
  private JTextField a21_box = new JTextField(3);
  private JTextField a22_box = new JTextField(3);
  private JTextField b1_box = new JTextField(3);
  private JTextField b2_box = new JTextField(3);
  private JLabel a_label = new JLabel("A =");
  private JLabel b_label = new JLabel("B =");
  private JLabel blanka = new JLabel(" ");
  private JLabel blankb = new JLabel(" ");

  public void add_extra() {

    //initialise the operator constructors
    affine = new Affine(i_w, i_h);

    //Add the arrays to their panels.

    a_panel. setLayout(a_layout);
    a_panel. setBackground(Color.white);
    a_panel. add(a_label);
    a_panel. add(a11_box);
    a11_box. setText("1");
    a_panel. add(a12_box);
    a12_box. setText("0");
    a_panel. add(blanka);
    a_panel. add(a21_box);
    a21_box. setText("0");
    a_panel. add(a22_box);
    a22_box. setText("1");

    //And now the second array

    b_panel. setBackground(Color.white);
    b_panel. setLayout(b_layout);
    b_panel. add(b_label);
    b_panel. add(b1_box);
    b1_box. setText("0");
    b_panel. add(blankb);
    b_panel. add(b2_box);
    b2_box. setText("0");

    //Now layout things in the mid panel

    midc. weighty = 0.5;
    midc. weightx = 0.5;
   
    midc. gridheight = 2;
    midc. fill = GridBagConstraints. VERTICAL;
    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(a_panel, midc);
    mid. add(a_panel);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(b_panel, midc);
    mid. add(b_panel);

    midc. weightx = 0;
    midc. gridheight = 1;
    midc. fill = GridBagConstraints.NONE;

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
	try{
	  a_array[0] = Float. valueOf(a11_box. getText()).floatValue();
	  a_array[1] = Float. valueOf(a12_box. getText()).floatValue();
	  a_array[2] = Float. valueOf(a21_box. getText()).floatValue();
	  a_array[3] = Float. valueOf(a22_box. getText()).floatValue();
	  b_array[0] = Float. valueOf(b1_box. getText()).floatValue();
	  b_array[1] = Float. valueOf(b2_box. getText()).floatValue();
	  dest_1d = affine. affine_transform(src_1d, i_w, i_h, a_array,
					     b_array);
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken.setText(new Long(time_msec).toString()+" msecs");

	  dest = createImage(new MemoryImageSource(i_w, i_h, dest_1d, 0, i_w));
	  
	  dest_canvas. updateImage(dest);
	  //Re-add the pixel listener to the new canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(brpl);
	}

	catch (NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid Arrays Specified"),
				    "Error!", JOptionPane.WARNING_MESSAGE);
	}
      }


      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }




  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new affine transformation operator
    affine = new Affine(i_w, i_h); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for performing an affine transformation on an. Written by Timothy Sharman";
  }
}
