package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 *SHistEqualizeScreen is the user interface to the HistEqualize
 *algorithm.  It is run as an applet
 *embedded in the file histeq.htm 
 *@author Timothy Sharman
 */

public class SHistEqualizeScreen extends VisionApplet3{

  //The operator class for performing histogram equalization. It's a thread
  HistEqualize histequalize;

  //The operator class for generating a histogram. It's a thread
  Histogram hist;

  ImageCanvas test_canvas;

  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //the components for the interface
  private JButton startbutton = new JButton("Equalize");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel hist_label = new JLabel("Histogram");

  //histogram label. A bit artificial for 256x256 images...
  JLabel hist_range = new JLabel("0          64"+
			       "           128"+
			       "           192"+
			       "           256");

  public void add_extra() {
    
    //initialise the operator constructors
    hist = new Histogram();

    histequalize = new HistEqualize();

    dest_1d = hist. histogram(src_1d, i_w, i_h);
    d_w = (dest_1d.length)/i_h;
    d_h = i_h;
    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
    dest_canvas = new ImageCanvas(dest);

    //Add the histogram to the bottom mid panel

    bm. remove(output);
    bm. remove(outsize1);

    bmc. gridx = 0;
    bmc. gridy = 0;
    bmlayout. setConstraints(hist_label, bmc);
    bm. add(hist_label);

    bmc. gridx = 0;
    bmc. gridy = 1;
    bmlayout. setConstraints(dest_canvas, bmc);
    bm. add(dest_canvas);
    
    bmc. gridx = 0;
    bmc. gridy = 2;
    bmlayout. setConstraints(hist_range, bmc);
    bm. add(hist_range);

    bm. repaint();

    //Now sort the rest of the layout

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

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();

    //Load different image
    input. setText("cam1.gif");
    load_image. doClick();

    
  } 

    /**
   *gui_add_image is used to add the default images to the canvas
   *along with their respective pixel listeners
   */
  
  public void gui_add_image() {
    
    //Create and add the pixel listeners to the canvases
    PixelListener blpl = new PixelListener();
    PixelListener brpl = new PixelListener();

    //add the source image
    blc. gridx = 0;
    blc. gridy = 1;
    bllayout. setConstraints(src_canvas, blc);
    bl. add(src_canvas); 
    src_canvas. addMouseMotionListener(blpl);
  
    //Add the destination images
    bmc. gridx = 0;
    bmc. gridy = 1;
    bmlayout. setConstraints(dest_canvas, bmc);
    bm. add(dest_canvas);
    bm. remove(bmPosition);
    bm. remove(bmIntensity);

    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(dest_canvas2, brc);
    br. add(dest_canvas2);
    dest_canvas2. addMouseMotionListener(brpl);

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
	dest_1d2 = histequalize. equalize(src_1d, i_w, i_h);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken. setText(new Long(time_msec).toString()+" msecs");
	dest2 = createImage(new MemoryImageSource(d_w2,d_h2,dest_1d2,0,d_w2));

	dest_1d = hist. histogram(dest_1d2, i_w, i_h);
	d_w = (dest_1d.length)/i_h;
	d_h = i_h;
	dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	dest_canvas. updateImage(dest);
	dest_canvas2. updateImage(dest2);
	//Add a new pixel listener to the canvas
	PixelListener brpl = new PixelListener();
	dest_canvas2. addMouseMotionListener(brpl);
      }

      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
	bm. remove(dest_canvas);

	dest_1d = hist. histogram(src_1d, i_w, i_h);
	d_w = (dest_1d.length)/i_h;
	d_h = i_h;
	dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
	dest_canvas = new ImageCanvas(dest);

	bmc. gridx = 0;
	bmc. gridy = 1;
	bmlayout. setConstraints(dest_canvas, bmc);
	bm. add(dest_canvas);
	bm. validate();
	bm. repaint();
	
      }
      
    }
  }




  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    set_src_image2(input_img, w, h, name);

    //create a new equalization operator
    histequalize = new HistEqualize(); 
    
    hist = new Histogram();

    //redo the histogram
    dest_1d = hist. histogram(src_1d, i_w, i_h);
    d_w = (dest_1d.length)/i_h;
    d_h = i_h;
    dest = createImage(new MemoryImageSource(d_w, d_h, dest_1d, 0, d_w));
    bm. remove(bmPosition);
    bm. remove(bmIntensity);
    dest_canvas. updateImage(dest);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for histogram equalizing an image. Written by Timothy Sharman";
  }
}
