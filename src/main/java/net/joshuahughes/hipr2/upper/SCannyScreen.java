package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 *SCannyScreen is the user interface to the Canny edge detection
 *algorithm (code.operator.Canny.java) It is run as an applet
 *embedded in the file canny.htm
 *
 *
 *@author Timothy Sharman
 */

public class SCannyScreen extends VisionApplet1 {
  
  //The operator class for performing Canny Edge Detection. It's a thread
  Canny canny;

  //Local variables to be passed to the operator

  int kersize = 3;
  float std_dev = 1;
  int hight = 10;
  int lowt = 1;
  float scale = 1;
  int offset = 0;

  //The listeners for the GUI
  private ComboListener clsize = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the interface
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JComboBox sizebox = new JComboBox();
  private JLabel sizelabel = new JLabel( "Select Kernel Size" );
  private JLabel thetalabel = new JLabel("Standard Deviation");
  private JTextField thetaval = new JTextField(5);
  private JLabel highlabel = new JLabel("Upper Threshold");
  private JTextField highthresh = new JTextField(5);
  private JLabel lowlabel = new JLabel("Lower Threshold");
  private JLabel scalelabel = new JLabel("Scale Value");
  private JTextField scalebox = new JTextField(5);
  private JLabel offsetlabel = new JLabel("Offset Value");
  private JTextField offsetbox = new JTextField(5);
  private JTextField lowthresh = new JTextField(5);
  private JButton startbutton = new JButton("Canny");
  private JButton abortbutton = new JButton("Stop");

   public void add_extra() {
 
     //initialise the operator constructors
     canny = new Canny();
    
     midc. weighty = 0.5;
     
     midc. gridx = 0;
     midc. gridy = 0;
     midlayout. setConstraints(sizelabel, midc);
     mid. add(sizelabel);
     
     midc. gridx = 1;
     midc. gridy = 0;
     sizebox. addItem("3x3 Kernel");
     sizebox. addItem("4x4 Kernel");
     sizebox. addItem("5X5 Kernel");
     sizebox. addItem("6x6 Kernel");
     sizebox. addItem("7x7 Kernel");
     midlayout. setConstraints(sizebox, midc);
     mid. add(sizebox);
     sizebox. addActionListener(clsize);
     
     midc. gridx = 0;
     midc. gridy = 1;
     midlayout. setConstraints(thetalabel, midc);
     mid. add(thetalabel);
     
     midc. gridx = 1;
     midc. gridy = 1;
     midlayout. setConstraints(thetaval, midc);
     thetaval. setText("0.45");
     mid. add(thetaval);
     
     midc. gridx = 0;
     midc. gridy = 2;
     midlayout. setConstraints(highlabel, midc);
     mid. add(highlabel);
     
     midc. gridx = 1;
     midc. gridy = 2;
     midlayout. setConstraints(highthresh, midc);
     highthresh. setText("10");
     mid. add(highthresh);
     
     midc. gridx = 0;
     midc. gridy = 3;
     midlayout. setConstraints(lowlabel, midc);
     mid. add(lowlabel);
     
     midc. gridx = 1;
     midc. gridy = 3;
     midlayout. setConstraints(lowthresh, midc);
     lowthresh. setText("1");
     mid. add(lowthresh);
     
     midc. gridx = 0;
     midc. gridy = 4;
     midlayout. setConstraints(scalelabel, midc);
     mid. add(scalelabel);
     
     midc. gridx = 1;
     midc. gridy = 4;
     midlayout. setConstraints(scalebox, midc);
     scalebox. setText("1.0");
     mid. add(scalebox);
     
     midc. gridx = 0;
     midc. gridy = 5;
     midlayout. setConstraints(offsetlabel, midc);
     mid. add(offsetlabel);
     
     midc. gridx = 1;
     midc. gridy = 5;
     midlayout. setConstraints(offsetbox, midc);
     offsetbox. setText("0");
     mid. add(offsetbox);
     
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
     
     midc. fill = GridBagConstraints. NONE;
     midc. gridx = 0;
     midc. gridy = 7;
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
	  scale = Float. valueOf(scalebox. getText()). floatValue();
	  offset = Integer. valueOf(offsetbox. getText()). intValue();
	  std_dev = Float. valueOf (thetaval.getText()). floatValue();
	  if((std_dev < 0.4) || (std_dev > ((float)kersize / 6))){
	    JOptionPane.showMessageDialog(null,("Theta value must be 0.4 < theta < kersize/6"),
					  "Error!", JOptionPane.WARNING_MESSAGE);
	  }
	  else{
	    hight = Integer. valueOf(highthresh. getText()). intValue();
	    lowt = Integer. valueOf(lowthresh. getText()). intValue();
	    dest_1d = canny. apply_canny(src_1d, i_w, i_h, kersize, std_dev, 
					 lowt, hight, scale, offset);
	    
	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken. setText(new Long(time_msec).toString()+" msecs");
	    
	    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	    
	    dest_canvas. updateImage(dest);
	    //Add a pixel listener to the new canvas
	    PixelListener brpl = new PixelListener();
	    dest_canvas. addMouseMotionListener(brpl);
	  }
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameter values specified"),
				    "Error!", JOptionPane.WARNING_MESSAGE);
	}
      }

      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }    
    }
  }


  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new canny operator
    canny = new Canny();

    set_src_image2(input_img, w, h, name);
   }

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet to perform Canny Edge Detection on an image. Written by Timothy Sharman";
  }
}

