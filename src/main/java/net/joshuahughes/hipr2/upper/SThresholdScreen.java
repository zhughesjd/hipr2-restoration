package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;

/**
 *
 *SThresholdScreen is the user interface to the thresholding algorithm
 *(newjavasrc.operator.Threshold.java) It is run as an applet embedded in
 *the file threshld.htm
 *
 *@author Konstantinos Koryllos, Judy Robertson SELLIC OnLine, Timothy Sharman
 */

public class SThresholdScreen extends HistVisionApplet {
  
  /**
   *The current low threshold for the threshold algorithm
   */ 
  
  public int lowthresh = 64;
  
  /**
   *The current high threshold for the threshold algorithm
   */ 
  public int highthresh = 192;
  
  //To siginify if the thresholded image is to be inverted or not.
  private boolean inverted = false;

  //vision operators required for thresholding
  private Threshold thresholdalgorithm;
  
  //The listeners for the GUI
  private SliderListener slhigh = new SliderListener();
  private SliderListener sllow = new SliderListener();
  private ComboListener cltoggle = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  private TextListener textListen = new TextListener();
  
  //The layout managers
  private GridBagLayout mllayout = new GridBagLayout(); 
  private GridBagLayout mrlayout = new GridBagLayout();
  private GridBagConstraints midlc = new GridBagConstraints();
  private GridBagConstraints midrc = new GridBagConstraints();
  
  //Interface components
  private JLabel highlabel = new JLabel("High threshold value");
  private JLabel lowlabel = new JLabel("Low threshold value");
  private JTextField lowbox = new JTextField(4);
  private JTextField highbox = new JTextField(4);
  private JSlider highthreshold = new JSlider(JSlider.HORIZONTAL, 0, 255, highthresh); 
  private JSlider lowthreshold = new JSlider(JSlider.HORIZONTAL, 0, 255, lowthresh);
  private JComboBox toggle = new JComboBox();
  private JButton startbutton = new JButton("Apply Threshold");  
  private JButton abortbutton = new JButton("Abort");
  private JLabel time = new JLabel("Time");
  private JTextField time_taken = new JTextField(20);
  
  public void add_extra() {
    
    //initialise the operator constructors
    thresholdalgorithm = new Threshold();
    
    //Set the layout for the mid panel
    
    midc. weighty = 0.5;
    
    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(lowlabel, midc);
    mid. add(lowlabel);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(lowbox, midc);
    mid. add(lowbox);
    lowbox. addActionListener(textListen);
    lowbox. setText("64");
    
    midc. gridx = 1;
    midc. gridy = 1;
    midc. weightx = 0.4;
    midc. fill = GridBagConstraints. HORIZONTAL;
    midlayout. setConstraints(lowthreshold, midc);
    mid. add(lowthreshold);
    lowthreshold. addChangeListener(sllow);
    lowthreshold. setMajorTickSpacing(50);
    lowthreshold. setMinorTickSpacing(5);
    lowthreshold. setPaintTicks(true);
    lowthreshold. setPaintLabels(true);

    midc. weightx = 0;
    midc. fill = GridBagConstraints.NONE;
    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(highlabel, midc);
    mid. add(highlabel);
    
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(highbox, midc);
    mid. add(highbox);
    highbox. addActionListener(textListen);
    highbox. setText("192");
    
    midc. gridx = 1;
    midc. gridy = 3;
    midc. weightx = 0.4;
    midc. fill = GridBagConstraints. HORIZONTAL;
    midlayout. setConstraints(highthreshold, midc);
    mid. add(highthreshold);
    highthreshold. setMajorTickSpacing(50);
    highthreshold. setMinorTickSpacing(5);
    highthreshold. setPaintTicks(true);
    highthreshold. setPaintLabels(true);
    highthreshold. addChangeListener(slhigh);
    
    midc. weightx = 0;
    midc. fill = GridBagConstraints.NONE;
    midc. gridwidth = 1;
    midc. gridx = 0;
    midc. gridy = 4;
    toggle. addItem("Normal");
    toggle. addItem("Inverted");
    midlayout. setConstraints(toggle, midc);
    mid. add(toggle);
    toggle. addActionListener(cltoggle);
    
    midc. gridx = 0;
    midc. gridy = 5;
    midlayout. setConstraints(startbutton, midc);
    startbutton. setBackground(Color.green);
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
    midc. gridy = 6;
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
      JComboBox cb = (JComboBox) evt.getSource();
      
      if( cb.getSelectedItem().equals("Normal") ) {
	inverted = false;
      }
      else if( cb.getSelectedItem().equals("Inverted") ) {
	inverted = true;
      }
    }
  }

   class TextListener implements ActionListener {
     public void actionPerformed(ActionEvent evt){
       JTextField jtf = (JTextField)evt. getSource();
       try{
	 int val = Integer. valueOf (jtf. getText()). intValue();
	 if(val >= 0 && val <= 255){
	   if(jtf == lowbox){
	     lowthreshold. setValue(val);
	   }
	   else if(jtf == highbox){
	     highthreshold. setValue(val);
	   }
	 }
	 else{
	   JOptionPane.showMessageDialog(null,("Threshold must lie between 0 and 255"),
					 ("Error!"), JOptionPane.WARNING_MESSAGE);     
	   if(jtf == lowbox){
	     lowthreshold. setValue(64);
	     lowbox. setText("64");
	   }
	   else if(jtf == highbox){
	     highthreshold. setValue(192);
	     highbox. setText("192");
	   }
	 }
       }
       catch(NumberFormatException e){
	 JOptionPane.showMessageDialog(null,("Invalid threshold specified"),
				       ("Error!"), JOptionPane.WARNING_MESSAGE);  
       }    
     }
   }



  /**
   *
   *Handles the actions performed when sliders on the interface are selected.
   *
   *@param evt The event which caused this object to be called
   *
   */

  class SliderListener implements ChangeListener {
    
    public void stateChanged(ChangeEvent evt) {
      JSlider slide = (JSlider) evt. getSource();
      
      if (histogramalgorithm != null) {
	if (slide == highthreshold) {
	  highthresh = highthreshold. getValue();
	  highbox. setText(String. valueOf(highthreshold. getValue()));
	  hist_canvas. highthresh = highthresh;
	  hist_canvas. invalidate();
	  hist_canvas. repaint();
	}
	if (slide == lowthreshold) {
	  lowthresh = lowthreshold. getValue();
	  lowbox. setText(String. valueOf(lowthreshold. getValue()));
	  hist_canvas. lowthresh = lowthresh;
	  hist_canvas. invalidate();
	  hist_canvas. repaint();
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
	if(lowthresh > highthresh){
	  JOptionPane.showMessageDialog(null,("Low threshold must be smaller than high threshold"),
					("Error!"), JOptionPane.WARNING_MESSAGE);   
	}
	else{
	  dest_1d = thresholdalgorithm. twothreshold(src_1d ,i_w,
						     i_h, lowthresh, highthresh);
	  
	  if (inverted) {
	    dest_1d = thresholdalgorithm. flip_the_pixels(dest_1d, i_w, i_h);
	  }
	  
	  time_msec = System.currentTimeMillis() - time_msec;
	  time_taken.setText(new Long(time_msec).toString()+" msecs");
	  dest = createImage(new MemoryImageSource(i_w, i_h, dest_1d, 0, i_w));
	  
	  dest_canvas. updateImage(dest);
	  outsize. setText(d_w+ " x "+d_h);
	  container. validate();
	  //Add a pixel listener to the new output canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(brpl);

	}
      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");

      }
      
    }
  }
  
 public void set_src_image(int [] input_img , int w, int h, String name) {
    
    set_src_image2(input_img, w, h, name);

    //create a new inversion operator
    thresholdalgorithm = new Threshold(); 
    
    lowbox. setText("64");
    highbox. setText("192");
    highthreshold. setValue(192);
    lowthreshold. setValue(64);
        
  }

  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for thresholding an image :part of the hiprjava.iface package. Written by Judy Robertson, SELLIC OnLine, Timothy Sharman";
  }
}
  













 


