package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 *SHoughScreen is the user interface to the Hough
 *algorithm.  It is run as an applet
 *embedded in the file hough.htm 
 *@author Timothy Sharman
 */

public class SHoughScreen extends VisionApplet3{

  //The operator class for performing hough edge detection. It's a thread
  Hough hough;

  //The two image answer returned by the hough transform
  TwoImages answer;

  //Local variables to signal what parameter to pass to the operator

  float thresh = 0;
  boolean overlay = false;
  float scale = 1;
  float offset = 0;

  //The listeners for the GUI
  private ComboListener overcl = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the interface
  private JLabel threshlabel = new JLabel("Threshold Value");
  private JTextField threshbox = new JTextField(5);
  private JLabel overlabel = new JLabel("Overlay");
  private JComboBox overbox = new JComboBox();
  private JButton startbutton = new JButton("Apply Hough");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel out2 = new JLabel("Hough Space");
  private JLabel out3 = new JLabel("Edge Image");
  private JLabel scaleLabel = new JLabel("Hough space scale");
  private JTextField scaleBox = new JTextField(5);
  private JLabel offsetLabel = new JLabel("Hough space offset");
  private JTextField offsetBox = new JTextField(5);

  public void add_extra() {
    
    //initialise the operator constructors
    hough = new Hough();
    
    //Set up the bottom mid panel
    
    bm. remove(output);
    
    bmc. gridx = 0;
    bmc. gridy = 0;
    bmlayout. setConstraints(out2, bmc);
    bm. add(out2);
    
    //Set up the bottom right panel
    br. remove(output2);

    brc. gridx = 0;
    brc. gridy = 0;
    brlayout. setConstraints(out3, brc);
    br. add(out3);

    //Set up the mid layout

    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(threshlabel, midc);
    mid. add(threshlabel);

    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(threshbox, midc);
    mid. add(threshbox);
    threshbox. setText("0.5");

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(overlabel, midc);
    mid. add(overlabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(overbox, midc);
    overbox. addItem("No");
    overbox. addItem("Yes");
    mid. add(overbox);
    overbox. addActionListener(overcl);

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(scaleLabel, midc);
    mid. add(scaleLabel);
    
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(scaleBox, midc);
    mid. add(scaleBox);
    scaleBox. setText("1");

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(offsetLabel, midc);
    mid. add(offsetLabel);
    
    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(offsetBox, midc);
    mid. add(offsetBox);
    offsetBox. setText("0");

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
    
    //Load a different default image
    input. setText("sqr1can1.gif");
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
      
      if( cb.getSelectedItem().equals("No") ) {
	overlay = false;
      }
      if( cb.getSelectedItem().equals("Yes") ) {
	overlay = true;
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
	try{
	  scale = Float. valueOf(scaleBox. getText()). floatValue();
	  offset = Float. valueOf(offsetBox. getText()). floatValue();
	  thresh = Float.valueOf( threshbox.getText() ).floatValue();
	  if(thresh > 1 || thresh < 0){
	    JOptionPane.showMessageDialog(null,("Threshold must be between 0 and 1"),
	    ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	  else{
	  time_msec = System.currentTimeMillis();
	  answer = hough. apply_hough(src_1d, i_w, i_h, thresh, scale, offset);
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken. setText(new Long(time_msec).toString()+" msecs");
	  
	  //Size of hough image must be calculated for proper viewing
	  d_w = 500;
	  d_h = 2* (int) (Math.max(i_h,i_w)*Math.sqrt(2));
	  dest_1d = new int[d_w*d_h];
	  dest_1d = answer. image1;
	  outsize1. setText(d_w+" x "+d_h);

	  dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));

	  dest_1d2 = answer. image2;

	  //Check if the images should be overlaid
	  if(overlay){
	    for(int i = 0; i < dest_1d2.length; i++){
	      if(dest_1d2[i] == 0xff0000ff){
		if(src_1d[i] == 0xffffffff){
		  dest_1d2[i] = 0xffffffff;
		}
		else {
		  dest_1d2[i] = 0xff0000ff;
		}
	      }
	      else {
		dest_1d2[i] = src_1d[i];
	      }
	    }
	  }
	  
	  outsize2. setText(d_w2+" x "+d_h2);
	  
	  dest2 = createImage(new MemoryImageSource(d_w2,d_h2,dest_1d2,0,d_w2));
	  
	  dest_canvas. updateImage(dest);
	  //Add a pixel listener to the canvas
	  PixelListener bmpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(bmpl);

	  dest_canvas2. updateImage(dest2);
	  //Add a pixel listener to the canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas2. addMouseMotionListener(brpl);
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
    
    set_src_image2(input_img, w, h, name);

    //create a new hough operator
    hough = new Hough(); 
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for performing a hough transform on an image. Written by Timothy Sharman";
  }
}
