package net.joshuahughes.hipr2.upper;
//package code.iface.compass;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
//import code.iface.utils.*;
//import code.operator.compass.*;
//import code.operator.utils.*;

/**
 *
 *SCompassScreen is the user interface to the Compass
 *algorithm.  It is run as an applet
 *embedded in the file compass.htm 
 *@author Timothy Sharman
 */

public class SCompassScreen extends VisionApplet3{

  //The operator class for performing compass edge detection. It's a thread
  Compass compass;

  //The two image answer returned by the compass
  TwoImages answer;

  //Local variables to signal what parameter to pass to the operator
  boolean coloured = false;
  int kertype = 0;
  float scalevalue = 1;
  int offsetvalue = 0;

  //The listeners for the GUI
  private ComboListener kernelcl = new ComboListener();
  private ComboListener pixcl = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  private WinListener colorwl = new WinListener();
  private ButtonListener colorbl = new ButtonListener();

  //the components for the interface
  private JComboBox kernelchoice = new JComboBox();
  private JLabel kernellabel = new JLabel("Kernel Type");
  private JComboBox labelchoice = new JComboBox();
  private JLabel colourlabel = new JLabel("Labelling Type");
  private JButton startbutton = new JButton("Apply Compass");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel out2 = new JLabel("Pixel Labelling");
  private JLabel out3 = new JLabel("Edge Image");
  private JLabel offsetlabel = new JLabel("Offset");
  private JTextField offset = new JTextField(5);
  private JLabel scalelabel = new JLabel("Scale");
  private JTextField scale = new JTextField(5);
  private JFrame colorFrame = new JFrame("Color Assignment");
  private JLabel viewLabel = new JLabel("View Color Assignments");
  private JButton colorButton = new JButton("Color Map");
  private String picName = "colors.gif";
  private Icon pic;
  private JLabel picLabel;

  public void add_extra() {
    
    //initialise the operator constructors
    compass = new Compass();

    // get colored icon
    try {
              URL thepicURL = new URL(getDocumentBase(),picName);
              pic = new ImageIcon(thepicURL);
    }  catch (MalformedURLException e4) {
              JOptionPane.showMessageDialog(null,("Can't find "+picName),
                                                  "Load Error", JOptionPane.WARNING_MESSAGE);
              return;
    }
    
    //Set up the frame
    picLabel = new JLabel();
    picLabel. setIcon(pic);

    colorFrame. getContentPane(). add(picLabel);

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
    midlayout. setConstraints(kernellabel, midc);
    mid. add(kernellabel);

    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(kernelchoice, midc);
    kernelchoice. addItem("Prewitt");
    kernelchoice. addItem("Sobel");
    kernelchoice. addItem("Kirsch");
    kernelchoice. addItem("Robinson");
    mid. add(kernelchoice);
    kernelchoice. addActionListener(kernelcl);
    
    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(colourlabel, midc);
    mid. add(colourlabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(labelchoice, midc);
    labelchoice. addItem("Greyscale");
    labelchoice. addItem("Color");
    mid. add(labelchoice);
    labelchoice. addActionListener(pixcl);

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(scalelabel, midc);
    mid. add(scalelabel);
    
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(scale, midc);
    mid. add(scale);
    scale. setText("1.0");

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(offsetlabel, midc);
    mid. add(offsetlabel);
    
    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(offset, midc);
    mid. add(offset);
    offset. setText("0");

    midc. gridx = 0;
    midc. gridy = 4;
    midlayout. setConstraints(viewLabel, midc);
    mid. add(viewLabel);

    midc. gridx = 1;
    midc. gridy = 4;
    midlayout. setConstraints(colorButton, midc);
    mid. add(colorButton);
    colorButton. addActionListener(colorbl);

    midc. gridx = 0;
    midc. gridy = 5;
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
    midc. gridy = 6;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();
    
  } 

  /**
   *Handles the actions performed inside any extra windows
   *
   *@param evt The event which caused this object to be called
   *
   */

  class WinListener extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      colorFrame. setVisible(false);
    }
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
     
      if( cb.getSelectedItem().equals("Prewitt") ) {
	kertype = 0;
      }
      if( cb.getSelectedItem().equals("Sobel") ) {
	kertype = 1;
      }
      if( cb.getSelectedItem().equals("Kirsch") ) {
	kertype = 2;
      }
      if( cb.getSelectedItem().equals("Robinson") ) {
	kertype = 3;
      }
      if( cb.getSelectedItem().equals("Greyscale") ) {
	coloured = false;
      }
      if( cb.getSelectedItem().equals("Color") ) {
	coloured = true;
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
	  offsetvalue = Integer. valueOf (offset.getText()). intValue();
	  scalevalue = Float. valueOf(scale.getText()).floatValue();
	  time_msec = System.currentTimeMillis();
	  answer = compass. apply_compass(src_1d, d_w, d_h, kertype, coloured, 
					  scalevalue, offsetvalue);
	  dest_1d2 = answer. image1;
	  dest_1d = answer. image2;
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken. setText(new Long(time_msec).toString()+" msecs");
	  
	  //The sizes must be reduced to allow proper displaying
	  d_w = d_w - 2;
	  d_h = d_h - 2;

	  outsize1. setText(d_w+" x "+d_h);
	  outsize2. setText(d_w+" x "+d_h);

	  dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	  dest2 = createImage(new MemoryImageSource(d_w,d_h,dest_1d2,0,d_w));
	  
	  /* Sizes must now be increased back. If this doesn't happen
	   * then the image becomes slowly distorted each time the operator
	   * is reapplied due to the fact the output canvas is being shrunk 
	   * by two each application
	   */
	  
	  d_w = d_w + 2;
	  d_h = d_h + 2;
	  
	  dest_canvas. updateImage(dest);
	  dest_canvas2. updateImage(dest2);
	  //Re-add the pixel listener to the new canvas
	  PixelListener bmpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(bmpl);
	  PixelListener brpl = new PixelListener();
	  dest_canvas2. addMouseMotionListener(brpl);

	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameters specified"),
	    ("Error!"), JOptionPane.WARNING_MESSAGE);             
	}

	container. validate();

      }

      else if( b == abortbutton ) {
	
	time_taken. setText("");
	
      }

      else if( b == colorButton ) {
	
	colorFrame.pack();
        colorFrame.setVisible(true);

      }

    }
  }



 public void set_src_image(int [] input_img , int w, int h, String name) {
    
    set_src_image2(input_img, w, h, name);

    //create a new compass operator
    compass = new Compass(); 
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for performing compass edge detection on an image. Written by Timothy Sharman";
  }
}

