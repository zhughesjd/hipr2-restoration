package net.joshuahughes.hipr2.upper;
//package code.iface.imagebitshift;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import code.iface.utils.*;
//import code.operator.imagebitshift.*;

/**
 *
 *ImageBitShiftScreen is the user interface to the pixel arithmetic shift
 *algorithm (hiprjava.operator.ImageOr.java) It is run as an applet
 *embedded in the file pixbitshift.htm.
 *
 *@author Judy Robertson SELLIC OnLine
 *@author Neil Brown, DAI
 *@author Timothy Sharman
 */
public class SImageBitShiftScreen extends VisionApplet1 {

  //The operator class for performing image logical BitShift. It's a thread
  ImageBitShift imagebitshift;

  //Do we do shift left or right
  private boolean shiftleft = true;

  //what is the constant value?
  private int constantvalue = 1;

  //The final scaling to be appilied to result image
  private float scalevalue =1;
  
  //The offset value to add to the result image 
  private float offsetvalue =0;

  private boolean wrap = false;

  //The listeners for the GUI
  private ComboListener cldir = new ComboListener();
  private ComboListener clwrap = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //The components for the interface
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel execution_time = new JLabel("Execution time: ");
  private JLabel shiftlabel = new JLabel( "Shift" );
  private JComboBox shifttoggle = new JComboBox();
  private JLabel constantlabel = new JLabel("Shift value");
  private JTextField constantbox = new JTextField(10);
  private JTextField offsetbox = new JTextField(10);
  private JLabel offset = new JLabel("Offset");
  private JTextField scalebox = new JTextField(10);
  private JLabel scaling = new JLabel("Scaling");
  private JButton startbutton = new JButton("Bit Shift Left");
  private JButton abortbutton = new JButton("Stop");
  private JLabel input1label = new JLabel("Input 1");
  private JLabel wrapLabel = new JLabel("Wrap/No Wrap");
  private JComboBox wrapbox = new JComboBox();
 
  public void add_extra() {
 
    //initialise the operator constructors
    imagebitshift = new ImageBitShift( i_w );
    imagebitshift.run();
    
    midc.weighty = 0.5;
    midc.gridx = 0;
    midc.gridy = 1;
    midlayout.setConstraints(constantlabel, midc);
    mid. add(constantlabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(constantbox, midc);
    mid. add(constantbox);
    constantbox. setText("1");
    
    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints( shiftlabel, midc );
    mid. add( shiftlabel );
    
    shifttoggle. addItem("Left");
    shifttoggle. addItem("Right");
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints( shifttoggle, midc);
    mid. add( shifttoggle );
    shifttoggle. addActionListener(cldir);

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(wrapLabel, midc);
    mid. add(wrapLabel);

    wrapbox. addItem("No Wrap");
    wrapbox. addItem("Wrap");
    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(wrapbox, midc);
    mid. add(wrapbox);
    wrapbox. addActionListener(clwrap);

    midc. gridx = 0;
    midc. gridy = 4;
    midlayout. setConstraints(scaling, midc);
    mid. add(scaling);
    
    midc. gridx = 1;
    midlayout. setConstraints(scalebox, midc);
    scalebox. setText("1.0");
    mid. add(scalebox);
    
    midc. gridx = 0;
    midc. gridy = 5;
    midlayout. setConstraints(offset, midc);
    mid. add(offset);
    
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
    
    midc. gridwidth = 1;
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
      
      if( cb.getSelectedItem().equals("Left") ) {
	startbutton.setText( "Bit Shift Left" );
	shiftleft = true;
      }
      else if( cb.getSelectedItem().equals("Right")){
	startbutton.setText( "Bit Shift Right" );
	shiftleft = false;
      }
      else if( cb.getSelectedItem().equals("No Wrap")){
	wrap = false;
      }
      else if( cb.getSelectedItem().equals("Wrap")){
	wrap = true;
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
	try{
	  constantvalue =  Float.valueOf( constantbox.getText() ).intValue();
	  
	  if (constantvalue < 0){
	    JOptionPane.showMessageDialog(null,("Bit Shift value must be positive"),
	    ("Error!"), JOptionPane.WARNING_MESSAGE);
	  }
	  else {
	    offsetvalue = Float.valueOf (offsetbox.getText()). floatValue();
	    scalevalue = Float.valueOf(scalebox.getText()).floatValue();
	    
	    dest_1d = imagebitshift.doBitShift( src_1d, constantvalue, 
						shiftleft, wrap,
						i_w, i_h, offsetvalue, 
						scalevalue);
	    dest = createImage(new MemoryImageSource(d_w, d_h,dest_1d,0, d_w));
	    
	    dest_canvas. updateImage(dest);
	    //Add a pixel listener to the panel
	    PixelListener brpl = new PixelListener();
	    dest_canvas. addMouseMotionListener(brpl);
	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken.setText(new Long(time_msec).toString()+" msecs");
	  }
	}
	catch(NumberFormatException e) {
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

    //create a new bitshift operator
    imagebitshift = new ImageBitShift(i_w); 
    
    set_src_image2(input_img, w, h, name);  

  }

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "ImageBitShift Applet"+
     "\nWritten by Judy Robertson, SELLIC OnLine\nand Neil Brown, DAI, Timothy Sharman\n";
  }
}
