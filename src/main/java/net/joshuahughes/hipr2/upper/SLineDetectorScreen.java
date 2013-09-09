package net.joshuahughes.hipr2.upper;
//package code.iface.linedet;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
//import code.iface.utils.*;
//import code.operator.linedet.*;
//import code.operator.utils.*;

/**
 *
 *SLineDetectorScreen is the user interface to the
 *Line Detector algorithm.  It is run as an applet
 *embedded in the file linedet.htm 
 *@author Timothy Sharman
 *@see code.operator.linedet
 */

public class SLineDetectorScreen extends VisionApplet3 {

  //The operator class for performing line detection. It's a thread
  LineDetector linedetector;

  int thresh = 0;
  int lines = 15;
  boolean choice = false;
  float scale = 1;
  float offset = 0;

  //The two image answer returned by the compass
  TwoImages answer;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  private CheckBoxListener cbl = new CheckBoxListener();
  private ComboListener thcl = new ComboListener();

  //The components for the GUI
  private JLabel threshlabel = new JLabel("Threshold Value");
  private JTextField threshbox = new JTextField(5);
  private JLabel cblabel = new JLabel("Select which lines to view :");
  private JCheckBox cb1 = new JCheckBox("Horizontal Lines", true);
  private JCheckBox cb2 = new JCheckBox("45 Degree Lines", true);
  private JCheckBox cb3 = new JCheckBox("135 Degree Lines", true);
  private JCheckBox cb4 = new JCheckBox("Vertical Lines", true);
  private JLabel choicelabel = new JLabel("Apply Thresholding");
  private JComboBox threshchoice = new JComboBox();
  private JLabel scaleLabel = new JLabel("Scale value");
  private JTextField scaleBox = new JTextField(5);
  private JLabel offsetLabel = new JLabel("Offset value");
  private JTextField offsetBox = new JTextField(5);
  private JButton startbutton = new JButton("Detect Lines");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel out2 = new JLabel("Pixel Labelling");
  private JLabel out3 = new JLabel("Edge Image");
  
  public void add_extra() {

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
    
    //initialise the operator constructors
    linedetector = new LineDetector();
    
    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(cblabel, midc);
    mid. add(cblabel);

    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(cb1, midc);
    mid. add(cb1);
    cb1. setBackground(Color.yellow);
    cb1. addActionListener(cbl);

    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(cb2, midc);
    mid. add(cb2);
    cb2. setBackground(Color.red);
    cb2. addActionListener(cbl);

    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(cb3, midc);
    mid. add(cb3);
    cb3. setBackground(Color.green);
    cb3. addActionListener(cbl);

    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(cb4, midc);
    mid. add(cb4);
    cb4. setBackground(Color.blue);
    cb4. addActionListener(cbl);

    midc. gridx = 0;
    midc. gridy = 4;
    midlayout. setConstraints(choicelabel, midc);
    mid. add(choicelabel);

    midc. gridx = 1;
    midc. gridy = 4;
    midlayout. setConstraints(threshchoice, midc);
    threshchoice. addItem("No");
    threshchoice. addItem("Yes");
    mid. add(threshchoice);
    threshchoice. addActionListener(thcl);

    midc. gridx = 0;
    midc. gridy = 5;
    midlayout. setConstraints(threshlabel, midc);
    mid. add(threshlabel);

    midc. gridx = 1;
    midc. gridy = 5;
    midlayout. setConstraints(threshbox, midc);
    mid. add(threshbox);
    threshbox. setText("0");
    threshbox. setEditable(false);

    midc. gridx = 0;
    midc. gridy = 6;
    midlayout. setConstraints(scaleLabel, midc);
    mid. add(scaleLabel);
    
    midc. gridx = 1;
    midc. gridy = 6;
    midlayout. setConstraints(scaleBox, midc);
    mid. add(scaleBox);
    scaleBox. setText("1");

    midc. gridx = 0;
    midc. gridy = 7;
    midlayout. setConstraints(offsetLabel, midc);
    mid. add(offsetLabel);

    midc. gridx = 1;
    midc. gridy = 7;
    midlayout. setConstraints(offsetBox, midc);
    mid. add(offsetBox);
    offsetBox. setText("0");

    midc. gridx = 0;
    midc. gridy = 8;
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
    midc. gridy = 9;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();

    //Load a different default image
    input. setText("hse2sub2.gif");
    load_image. doClick();
    
  }

/**
   *
   *Handles the actions performed when combo lists on the interface 
   *are selected.
   *
   *@param evt The event which caused this object to be called
   *
   */
  
  class ComboListener implements ActionListener {
    
    public void actionPerformed(ActionEvent evt) {
      JComboBox cb = (JComboBox)evt.getSource();
     
      if( cb.getSelectedItem().equals("No") ) {
	choice = false;
	threshbox. setEditable(false);
      }
      if( cb.getSelectedItem().equals("Yes") ) {
	choice = true;
	threshbox. setEditable(true);
      }
    }
  }


   /**
   *
   *Handles the actions performed when toggle buttons on the 
   *interface are pressed.
   *
   *@param evt The event which caused this object to be called
   *
   */
  
  
  class CheckBoxListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      JCheckBox but = (JCheckBox) e.getSource();

      if(but == cb1){
	if(but. isSelected()){
	  lines = lines + 1;
	}
	else {
	  lines = lines - 1;
	}
      }
      if(but == cb2){
	if(but. isSelected()){
	  lines = lines + 2;
	}
	else {
	  lines = lines - 2;
	}
      }
      if(but == cb3){
	if(but. isSelected()){
	  lines = lines + 4;
	}
	else {
	  lines = lines - 4;
	}
      }
      if(but == cb4){
	if(but. isSelected()){
	  lines = lines + 8;
	}
	else {
	  lines = lines - 8;
	}
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
	  scale = Float. valueOf(scaleBox. getText()). floatValue();
	  offset = Float. valueOf(offsetBox. getText()). floatValue();
	  thresh = (Integer.valueOf(threshbox. getText()).intValue()); 
	  if(thresh < 0){
	    JOptionPane.showMessageDialog(null,("Threshold Value must be positive"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	  else{
	    answer = linedetector. apply_lineDetect(src_1d, d_w2, d_h2,
						    lines, thresh, choice,
						    scale, offset);
	    
	    dest_1d2 = answer. image1;
	    dest_1d = answer. image2;

	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken.setText(new Long(time_msec).toString()+" msecs");
	    dest = createImage(new MemoryImageSource(d_w2,d_h2,dest_1d,0,d_w2));
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
	catch (NumberFormatException e) {
	  JOptionPane.showMessageDialog(null,("Invalid Threshold Value"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	}
      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
    }
  }
  
  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new line detector operator
    linedetector = new LineDetector(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for detecting lines in an image. Written by Timothy Sharman";
  }
} 

