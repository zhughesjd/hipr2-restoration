package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;

/**
 *
 *SRandomNoiseScreen is the user interface to the RandomNoise
 *algorithm.  It is run as an applet
 *embedded in the file noise.htm 
 *@author Timothy Sharman
 */
public class SRandomNoiseScreen extends VisionApplet1 {

  //The operator class for performing noise generation. It's a thread
  RandomNoise randomnoise;

  //The listeners for the GUI
  private ComboListener clchoice = new ComboListener();
  private ComboListener clst = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  /**
   *Variables to indicate what the selection in the Combo Boxes are.
   *gauss_shot = 0 means Gaussian selected, = 1 means Shot selected
   *full_partial = 0 means Full selected, = 1 means Partial selected
   */
  
  private int gauss_shot = 0;
  private int full_partial = 0;
  
  //The layout managers
  private GridBagLayout layout = new GridBagLayout(); 
  private GridBagLayout toplayout = new GridBagLayout();
  private GridBagLayout midleftlayout = new GridBagLayout();
  private GridBagLayout midrightlayout = new GridBagLayout();
  private GridBagLayout bottomlayout = new GridBagLayout();
  private GridBagConstraints wholec = new GridBagConstraints();  
  private GridBagConstraints topc = new GridBagConstraints();
  private GridBagConstraints midleftc = new GridBagConstraints();
  private GridBagConstraints midrightc = new GridBagConstraints();
  private GridBagConstraints bottomc = new GridBagConstraints();
  
  //Interface components
  private JPanel rtop = new JPanel(); 
  private JPanel midleft = new JPanel();
  private JPanel midright = new JPanel();
  private JPanel bottom = new JPanel();

  private JLabel ntype = new JLabel("Noise Type ");
  private JComboBox choice = new JComboBox();
  private JLabel gaussian = new JLabel("Gaussian Noise: ");
  private JLabel std_dev = new JLabel("Standard Deviation ");
  private JTextField std_dev_val = new JTextField(10);
  private JLabel shot = new JLabel("Shot Noise: ");
  private JComboBox shot_type = new JComboBox();
  private JLabel prob = new JLabel("Probability ");
  private JTextField prob_val = new JTextField(10);
  private JButton startbutton = new JButton("Apply Noise");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
 
  public void add_extra() {

    //initialise the operator constructors
    randomnoise = new RandomNoise();
  
    
    //Set the layout for the top panel
    rtop. setLayout(toplayout);
    rtop. setBackground(Color.white);
    
    topc. gridx = 0;
    topc. gridy = 0;
    topc. anchor = GridBagConstraints.CENTER;
    toplayout. setConstraints(ntype, topc);
    rtop. add(ntype);
    
    topc. gridx = 1;
    topc. gridy = 0;
    choice. addItem("Gaussian Noise");
    choice. addItem("Shot Noise");
    toplayout. setConstraints(choice, topc);
    rtop. add(choice);
    choice. addActionListener(clchoice);
    
    //Set the layout for the midleft panel

    midleft. setLayout(midleftlayout);
    midleft. setBackground(Color.white);

    midleftc. anchor = GridBagConstraints.CENTER;
    midleftc. gridx = 0;
    midleftc. gridy = 0;
    midleftc. gridwidth = 2;
    midleftc. weighty = 0.1;
    midleftlayout. setConstraints(gaussian, midleftc);
    midleft. add(gaussian);
    
    midleftc. anchor = GridBagConstraints.NORTH;
    midleftc. gridwidth = 1;
    midleftc. gridx = 0;
    midleftc. gridy = 1;
    midleftlayout. setConstraints(std_dev, midleftc);
    midleft. add(std_dev);
        
    midleftc. gridx = 1;
    midleftc. gridy = 1;
    midleftlayout. setConstraints(std_dev_val, midleftc);
    std_dev_val. setText("10");
    midleft. add(std_dev_val);
    
    //Set the layout for the midright panel
    
    midright. setLayout(midrightlayout);
    midright. setBackground(Color.white);

    midrightc.anchor = GridBagConstraints.SOUTH;
    midrightc. gridx = 0;
    midrightc. gridy = 0;
    midrightc. gridwidth = 2;
    midrightc. weighty = 0.1;
    midrightlayout. setConstraints(shot, midrightc);
    midright. add(shot);

    midrightc. anchor = GridBagConstraints.CENTER;
    midrightc.gridx = 0;
    midrightc.gridy = 1;
    shot_type. addItem("Full");
    shot_type. addItem("Partial");
    midrightlayout. setConstraints(shot_type, midrightc); 
    midright. add(shot_type);
    shot_type. addActionListener(clst);

    midrightc. gridwidth = 1;
    midrightc.gridx = 0;
    midrightc.gridy = 2;
    midrightlayout. setConstraints(prob, midrightc);
    midright. add(prob);
    
    midrightc.gridx = 1;
    midrightc.gridy = 2;
    midrightlayout. setConstraints(prob_val, midrightc);
    prob_val. setText("0.1");
    prob_val. setEditable(false);
    midright. add(prob_val);
    
    //Set the layout for the bottom panel

    bottom. setLayout(bottomlayout);
    bottom. setBackground(Color.white);

    bottomc. gridx = 0;
    bottomc. gridy = 0;    
    bottomc. fill = GridBagConstraints.BOTH;
    bottomlayout. setConstraints(startbutton, bottomc);
    startbutton .setBackground(Color.green);
    bottom. add(startbutton);
    startbutton. addActionListener(startbl);
    
    bottomc. gridx = 1;
    bottomc. gridy = 0;
    abortbutton. setBackground(Color.red);
    bottomlayout. setConstraints(abortbutton, bottomc);
    bottom. add(abortbutton);
    abortbutton. addActionListener(abortbl);
    
    bottomc. fill = GridBagConstraints.HORIZONTAL;
    bottomc. weighty = 0.1;

    bottomc. gridx = 0;
    bottomc. gridy = 1;
    bottomlayout. setConstraints(time, bottomc);
    bottom. add(time);
    
    bottomc. gridx = 1;
    bottomc. gridy = 1;
    bottomlayout. setConstraints(time_taken, bottomc);
    time_taken. setEditable(false);
    bottom. add(time_taken);

    //the applet layout
    
    midc. gridx = 0;
    midc. gridy = 0;
    midc. gridwidth = 3;
    midc. fill = GridBagConstraints.BOTH;
    midc. weightx = 0.3;
    midc. weighty = 0.2;
    midlayout. setConstraints(rtop, midc);
    mid. add(rtop);
    
    midc. gridwidth = 1;
    midc .gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(midleft, midc);
    mid. add(midleft);

    midc .gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(midright, midc);
    mid. add(midright);

    midc. gridwidth = 3;
    midc .gridx = 0;
    midc. gridy = 2;   
    midlayout. setConstraints(bottom, midc);
    mid. add(bottom);
     
    container. repaint();

    //Load a different default image
    input. setText("pap2.gif");
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
      
      if( cb.getSelectedItem().equals("Gaussian Noise") ) {
	gauss_shot = 0;
	std_dev_val. setEditable(true);
	prob_val. setEditable(false);
      }
      else if( cb.getSelectedItem().equals("Shot Noise") ) {
	gauss_shot = 1;
	std_dev_val. setEditable(false);
	prob_val. setEditable(true);
      }
      else if( cb.getSelectedItem().equals("Full") ) {
	full_partial = 0;
      }
      else if( cb.getSelectedItem().equals("Partial") ){
	full_partial = 1;
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
    
    float par = 0;
    
    public void actionPerformed(ActionEvent evt) {
      JButton b = (JButton)evt.getSource();
      if( b == startbutton ) {
	try{
	  if( gauss_shot == 1) {
	    if( full_partial == 0 ){	  
	      par = Float. valueOf(prob_val.getText()).floatValue();
	      if( par < 0 || par > 1 ) {
		JOptionPane.showMessageDialog(null,("Invalid Probability Specified"),
					      ("Error!"), JOptionPane.WARNING_MESSAGE);
	      }
	      else{
		time_msec = System.currentTimeMillis();
		dest_1d = randomnoise. ShotFull(src_1d, i_w, i_h, par);
		time_msec = System.currentTimeMillis() - time_msec;
		time_taken.setText(new Long(time_msec).toString()+" msecs");
		dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
		
		dest_canvas. updateImage(dest);
	      }
	    }
	    else if (full_partial == 1) {
	      par = Float. valueOf(prob_val.getText()).floatValue();
	      if( par < 0 || par > 1 ) {
		JOptionPane.showMessageDialog(null,("Invalid Probability Specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	      }
	      else{
		time_msec = System. currentTimeMillis();
		dest_1d = randomnoise. ShotPartial(src_1d, i_w, i_h, par);
		time_msec = System. currentTimeMillis() - time_msec;
		time_taken.setText(new Long(time_msec). toString()+" msecs");
		dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
		
		dest_canvas. updateImage(dest);
	      }
	    }
	  }
	  
	  else if(gauss_shot == 0) {
	    par = Float. valueOf(std_dev_val.getText()).floatValue();
	    time_msec = System.currentTimeMillis();
	    dest_1d = randomnoise. Gaussian(src_1d, i_w, i_h, par);
	    time_msec = System.currentTimeMillis() - time_msec;
	    time_taken.setText(new Long(time_msec).toString()+" msecs");
	    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	    
	    dest_canvas. updateImage(dest);
	  }
	  //Add a pixel listener to the canvas
	  PixelListener brpl = new PixelListener();
	  dest_canvas. addMouseMotionListener(brpl);
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameters specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	}
	outsize. setText(d_w+" x "+d_h);
      }
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
    }
  }
  

  

  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new noise operator
    randomnoise = new RandomNoise(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for creating noise in an image. Written by Timothy Sharman";
  }
}















