package net.joshuahughes.hipr2.upper;
//package code.iface.imagelabel;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import code.iface.utils.*;
//import code.operator.imagelabel.*;


/**
 *
 *SImageLabelScreen is the user interface to the connect component labelling
 *algorithm (code.operator.ImageLabel.java) It is run as an applet
 *embedded in the file pixLabel.htm.
 *
 *@author Judy Robertson SELLIC OnLine
 *@author Neil Brown, DAI
 *@author Timothy Sharman
 */
public class SImageLabelScreen extends VisionApplet1 {

  //The operator class for performing image logical Label. It's a thread
  ImageLabel imageLabel;
  
  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the GUI
  private JLabel time = new JLabel("Time");
  private JTextField time_taken = new JTextField(20);
  private JButton startbutton = new JButton("Label");
  private JButton abortbutton = new JButton("Stop");
 
  public void add_extra() {
 
    //initialise the operator constructors
    imageLabel = new ImageLabel( i_w );
    
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

    midc. gridwidth = 1;
    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();


    //This should hopefully load a binary image as the default    
    
    image_url = "art3.gif";
    input. setText("art3.gif");
      try {
	theURL = new URL(image_url);
      } catch (MalformedURLException e) {
	
	// if a file rather than a URL, then try local file space
	// If still not a URL and not a file then report error
	
	try {
	  
	  theURL = new URL(getDocumentBase(),"images/"+image_url);
	} 
	catch (MalformedURLException e4) {
	  JOptionPane.showMessageDialog(null,("Unable to load default file"),
					("Load Error"), JOptionPane.WARNING_MESSAGE);
	  return;
	}
      }
      String filename = theURL.getFile();
      apply_send_image();                 
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
	dest_1d = imageLabel.doLabel( src_1d, i_w, i_h);
	dest = createImage(new MemoryImageSource(i_w, i_h,dest_1d,0, i_w)); 
	
	dest_canvas. updateImage(dest);
	//Add a pixel listener to the canvas
	PixelListener brpl = new PixelListener();
	dest_canvas. addMouseMotionListener(brpl);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken.setText(new Long(time_msec).toString()+" msecs");
	JOptionPane.showMessageDialog(null,("Objects counted "+imageLabel.getColours()),
				      ("Info"), JOptionPane.INFORMATION_MESSAGE);
	
      }
      
    
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }


  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new inversion operator
    imageLabel = new ImageLabel(i_w); 
    
    set_src_image2(input_img, w, h, name);
  }


  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "Image Labelling Applet :part of the code.iface package."+
      "\nWritten by Judy Robertson, SELLIC OnLine\nand Neil Brown, DAI\n"+
      "and Timothy Sharman";
  }
}
