package net.joshuahughes.hipr2.upper;
//package code.iface.imageor;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
//import code.iface.utils.*;
//import code.operator.imageor.*;

/**
 *
 *ImageOrScreen is the user interface to the pixel logical OR
 *algorithm (hiprjava.operator.ImageOr.java) It is run as an applet
 *embedded in the file pixor.htm.
 *
 *@author Judy Robertson SELLIC OnLine
 *@author Neil Brown, DAI
 *@author Timothy Sharman
 */

public class SImageOrScreen extends VisionApplet2{

  //The operator class for performing image logical OR. It's a thread
  ImageOr imageor;

  //should we use a constant or an image?
  private boolean constant;

  //Do we do NOR or OR ?
  private boolean nor;

  //what is the constant value?
  private int constantvalue = 1;

  //The final scaling to be appilied to result image
  private float scalevalue =1;
  
  //The offset value to add to the result image 
  private float offsetvalue =0;

  //The listeners for the GUI
  private ComboListener clnor = new ComboListener();
  private ComboListener cltoggle = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  // The components for the interface
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel execution_time = new JLabel("Execution time: ");
  private JLabel source2 = new JLabel("Source 2");
  private JComboBox sourcetoggle = new JComboBox();
  private JLabel norlabel = new JLabel( "Operator" );
  private JComboBox nortoggle = new JComboBox();
  private JLabel constantlabel = new JLabel("Constant value");
  private JTextField constantbox = new JTextField(10);
  private JTextField offsetbox = new JTextField(10);
  private JLabel offset = new JLabel("Offset");
  private JTextField scalebox = new JTextField(10);
  private JLabel scaling = new JLabel("Scaling");
  private JButton startbutton = new JButton("OR");
  private JButton abortbutton = new JButton("Stop");

  public void add_extra() {
 
    //initialise the operator constructors
    imageor= new ImageOr(i1_w, i2_w);
    
    midc.weighty = 2;
    midlayout.setConstraints(source2, midc);
    mid.add(source2);
    
    midc.gridx = 1;
    sourcetoggle.addItem("Input 2");
    sourcetoggle.addItem("Constant");
    midlayout.setConstraints( sourcetoggle, midc );
    mid.add( sourcetoggle );
    sourcetoggle. addActionListener(cltoggle);

    midc.gridx = 0;
    midc.gridy = 1;
    midlayout.setConstraints(constantlabel, midc);
    mid.add(constantlabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(constantbox, midc);
    mid. add(constantbox);
    constantbox. setText("1");
    constantbox. setEditable(false);
    
    
    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints( norlabel, midc );
    mid. add( norlabel );
    
    nortoggle. addItem("OR");
    nortoggle. addItem("NOR");
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints( nortoggle, midc);
    mid. add( nortoggle );
    nortoggle. addActionListener(clnor);

    midc. gridx = 0;
    midc. gridy = 3;
    midlayout. setConstraints(scaling, midc);
    mid. add(scaling);
    
    midc. gridx = 1;
    midlayout. setConstraints(scalebox, midc);
    scalebox. setText("1.0");
    mid. add(scalebox);
    
    midc. gridx = 0;
    midc. gridy = 4;
    
    midlayout. setConstraints(offset, midc);
    mid. add(offset);
    
    midc. gridx = 1;
    midc. gridy = 4;
    midlayout. setConstraints(offsetbox, midc);
    offsetbox. setText("0");
    mid. add(offsetbox);
    
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

    midc. gridwidth = 1;
    midc. fill = GridBagConstraints. NONE;
    midc. gridx = 0;
    midc. gridy = 6;
    midlayout. setConstraints(time, midc);
    
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);
    
    mid. repaint();

    //This should hopefully load a different default    
    
    input. setText("wdg2thr3.gif");
    input2. setText("wdg3adp2.gif");
    load_image. doClick();
    load_image2. doClick();

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
      
      if( cb.getSelectedItem().equals("Input 2") ) {
	constantbox. setEditable(false);
	constant = false;
      }
      else if( cb.getSelectedItem().equals("Constant")){
	constantbox. setEditable(true);
	constant = true;
      }
      else if( cb.getSelectedItem().equals("NOR")){
	startbutton.setText( "NOR" );
      }
      else if( cb.getSelectedItem().equals("OR")){
	startbutton.setText("OR");
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
	constant = sourcetoggle.getSelectedItem().equals("Constant");
	try{
	  constantvalue =  Integer.valueOf( constantbox.getText() ).intValue();
	  offsetvalue = Float. valueOf (offsetbox.getText()). floatValue();
	  scalevalue = Float. valueOf(scalebox.getText()).floatValue();
	  nor = nortoggle.getSelectedItem().equals( "NOR" );
	  //We want to OR a constant value with image 1
	  
	  if (constant) {
	    dest_1d = imageor.doOr( src1_1d, constantvalue, nor, i1_w, i1_h,
				    offsetvalue, scalevalue );
	    dest = createImage(new MemoryImageSource(i1_w, i1_h,dest_1d,0, i1_w));
	    outsize. setText(i1_w+" x "+i1_h);
	  } else{
	    dest_1d = imageor.doOr( src1_1d, src2_1d, nor, d_w, d_h,
				    offsetvalue, scalevalue );
	    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	    outsize. setText(d_w+" x "+d_h);
	  }
	  
	  dest_canvas. updateImage(dest);
	  PixelListener outpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(outpl);
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken.setText(new Long(time_msec).toString()+" msecs");
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

 
  public void set_src1_image(int [] input_img , int w, int h, String name) {
    
    //create a new oring operator
    imageor = new ImageOr(i1_w, i2_w);

    set_src1_image2(input_img, w, h, name);
  }

  public void set_src2_image(int [] input_img , int w, int h, String name) {
    
    //create a new oring operator
    imageor = new ImageOr(i1_w, i2_w);
    
    set_src2_image2(input_img, w, h, name);
  }

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "Image Logical (N)OR Applet"+
     "\nWritten by Judy Robertson, SELLIC OnLine\nand Neil Brown, DAI, Timothy Sharman\n";
  }
}
