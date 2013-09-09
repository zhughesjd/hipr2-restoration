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
 *STranslateScreen is the user interface to the Translation algorithm
 *(code.operator.Translate.java) It is run as an applet embedded in
 *the file trans.htm @author Craig Strachan DAI after Judy Robertson
 *SELLIC OnLine, Timothy Sharman */

public class STranslateScreen extends VisionApplet1 {

  //The input picture. Overrides the input canvas in VisionApplet.
  //This allow the use of the specially tailored TranslateImageCanvas. 
  private TranslateImageCanvas src_canvas;

  private boolean wrap;

  public Point point = null;

  int startx, starty, currx, curry;

  boolean clicked = false;

  //The operator class for performing translation. It's a thread
  Translate translate;

  //The listeners for the GUI
  private ComboListener clwrap = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  private MyMouseListener mousel = new MyMouseListener();
  private TextListener transtl = new TextListener();

  //The components for the GUI
  private JButton startbutton = new JButton("Translate");
  private JButton abortbutton = new JButton("Stop");
  private JLabel time = new JLabel("Time");
  private JTextField time_taken = new JTextField(20);
  private JLabel wrap_label = new JLabel("Wrap/NoWrap :");
  private JComboBox  wrap_chooser = new JComboBox ();
  private JLabel xLabel = new JLabel("X Translation:");
  private JLabel yLabel = new JLabel("Y Translation:");
  private JTextField xBox = new JTextField(5);
  private JTextField yBox = new JTextField(5);

  public void add_extra() {
    
    wrap = true;
    
    //initialise the operator constructors
    translate = new Translate();
  
    //Set the layout for the mid panel

    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(wrap_label, midc);
    mid. add(wrap_label);

    midc. gridx = 1;
    midc. gridy = 0;
    wrap_chooser. addItem("Wrap");
    wrap_chooser. addItem("No Wrap");
    midlayout. setConstraints(wrap_chooser, midc);
    mid. add(wrap_chooser);
    wrap_chooser. addActionListener(clwrap);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(xLabel, midc);
    mid. add(xLabel);
    
    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(xBox, midc);
    mid. add(xBox);
    xBox. setText("0");
    xBox. addKeyListener(transtl);

    midc. gridx = 0;
    midc. gridy = 2;
    midlayout. setConstraints(yLabel, midc);
    mid. add(yLabel);
    
    midc. gridx = 1;
    midc. gridy = 2;
    midlayout. setConstraints(yBox, midc);
    mid. add(yBox);
    yBox. setText("0");
    yBox. addKeyListener(transtl);

    midc. gridx = 0;
    midc. gridy = 3;
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
    midc. gridy = 4;
    midlayout. setConstraints(time, midc);
    mid. add(time);
    
    midc. gridx = 1;
    midc. gridy = 4;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);
    
    mid. repaint();
  }

  class TextListener extends KeyAdapter {
    public void keyReleased(KeyEvent evt){
      JTextField jtf = (JTextField)evt. getSource();
      try{
	//If no start point has been selected so far set it to 1,1
	if(!clicked){
	  src_canvas. setStart(1,1);
	  startx = 1;
	  starty = 1;
	}
	int val = Integer. valueOf (jtf. getText()). intValue();
	if(jtf == xBox){
	  int yVal = Integer. valueOf(yBox. getText()). intValue();
	  src_canvas. endLine((val+startx) ,(yVal+starty));  
	}
	if(jtf == yBox){
	  int xVal = Integer. valueOf(xBox. getText()). intValue();
	  src_canvas. endLine((xVal+startx), (val+starty));
	}
      }
      catch(NumberFormatException e){
	if(jtf. getText(). equals("")){
	  //Do nothing
	}
	else{
	  JOptionPane.showMessageDialog(null,("Invalid translation specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);  
	}
      }    
    }
  }
  
  /**
   *
   *Handles the actions performed when the mouse is used in the interface.
   *
   *@param e The event which caused this object to be called
   *
   */


  class MyMouseListener extends MouseInputAdapter {
    
    public void mousePressed(MouseEvent e) {
      TranslateImageCanvas tic = (TranslateImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      startx = x;
      starty = y;
      if (point == null) {
	point = new Point(x, y);
      } else {
	point.x = x;
	point.y = y;
      }
      tic. setStart(point.x ,point.y);
      clicked = true;
      
    }

    public void mouseDragged(MouseEvent e) {
      TranslateImageCanvas tic = (TranslateImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      if (point == null) {
	point = new Point(x, y);
      } else {
	point.x = x;
	point.y = y;
      }
      tic. updateLine(point.x ,point.y);
      xBox. setText(""+(x-startx)+"");
      yBox. setText(""+(y-starty)+"");

//      ImageCanvas ic = (ImageCanvas)e. getSource();
      int clickx = e. getX();
      int clicky = e. getY();
      int scalex = 0;
      int scaley = 0;
//      int image_width = ic. getImageWidth();
//      int image_height = ic. getImageHeight();
      int image_width = tic. getImageWidth();
      int image_height = tic. getImageHeight();
    
      if ((image_width >= 256) || (image_height >= 256)){
	
	if (image_width > image_height){
	  
	  float ratio = (float) image_width / (float) image_height;
	  scalex = 256;
	  scaley = (int) ((float) 256/ ratio);
	  actualx = (int)(((float)image_width / (float)scalex)*clickx);
	  actualy = (int)(((float)image_height / (float)scaley)*clicky);
	}  
	
	else if (image_height > image_width){
	
	  float ratio = (float) image_height /  (float) image_width;
	  scalex = image_width;
	  scaley = 256;
	  scalex = (int) ( (float) 256 / ratio);
	  actualx = (int)(((float)image_width / (float)scalex)*clickx);
	  actualy = (int)(((float)image_height / (float)scaley)*clicky);
	} 
	else {  
	  actualx = (int)(((float)image_width / (float)256)*clickx);
	  actualy = (int)(((float)image_height / (float)256)*clicky);
	}
	
      }
      else {
	actualx = clickx;
	actualy = clicky;
      }
      if(actualx < 0){
	actualx = 0;
      }
      if(actualy < 0){
	actualy = 0;
      }
      if(actualx > image_width - 1){
	actualx = image_width - 1;
      }
      if(actualy > image_height - 1){
	actualy = image_height - 1;
      }
      int intense = src_1d[actualx+(actualy*image_width)] & 0x000000ff;
      blPosition. setText("Point X:"+actualx+"  Y:"+actualy);
      blIntensity. setText("Intensity:"+intense);

    }

    public void mouseReleased(MouseEvent e) {
     TranslateImageCanvas tic = (TranslateImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      if (point == null) {
	point = new Point(x, y);
      } else {
	point.x = x;
	point.y = y;
      }
      tic. endLine(point.x ,point.y);
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
      JComboBox cb = (JComboBox) evt.getSource();
      
      if( cb.getSelectedItem().equals("Wrap") ) {
	wrap = true;
      }
      else if (cb. getSelectedItem(). equals("No Wrap")) {
	wrap = false;
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
	xBox. setText(Integer. toString(src_canvas.end_x - src_canvas. start_x));
	yBox. setText(Integer. toString(src_canvas.end_y - src_canvas. start_y));
	time_msec = System. currentTimeMillis();
	dest_1d = translate. translate_image(src_1d, i_h, i_w, src_canvas.end_x - 
					     src_canvas.start_x, src_canvas.end_y - 
					     src_canvas.start_y, wrap);
	time_msec = System.currentTimeMillis() - time_msec;
	time_taken.setText(new Long(time_msec).toString()+" msecs");

	dest = createImage(new MemoryImageSource(i_w, i_h, dest_1d, 0, i_w));
	
	if (dest_canvas != null) {
	  br. remove(dest_canvas);
	}
	
	dest_canvas = new ImageCanvas(dest);
	//Add a new pixel listener to the canvas
	PixelListener brpl = new PixelListener();
	dest_canvas. addMouseMotionListener(brpl);
	brc. gridx = 0;
	brc. gridy = 1;
	brlayout. setConstraints(dest_canvas, brc);
	br. add(dest_canvas); 
	br. validate();
	br. doLayout();
	container. validate();

      }
      
      else if ( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }       

  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //reset the input image
    i_w = w;
    i_h =h;
    src_1d = new int[i_w*i_h]; 
    src_1d = input_img;
    src = createImage(new MemoryImageSource(i_w,i_h,src_1d,0,i_w));
    if(src_canvas != null){
      bl. remove(src_canvas);
    }
    src_canvas  = new TranslateImageCanvas( src);
    blc. gridx = 0;
    blc. gridy = 1;
    bllayout.setConstraints(src_canvas, blc);
    bl. add(src_canvas);
    bl. validate();
    bl. repaint();
    insize. setText(i_w+" x "+i_h);

    MyMouseListener mousel = new MyMouseListener();
    src_canvas. addMouseListener(mousel);
    src_canvas. addMouseMotionListener(mousel);
    
    //create a new threshold operator
    translate = new Translate(); 

    //blank the destination image
    d_w = i_w;
    d_h = i_h;

    dest_1d = new int [i_w* i_h];
    dest_1d = input_img;
    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
    if(dest_canvas != null){
      br. remove(dest_canvas);
    }
    dest_canvas = new ImageCanvas(dest);
    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(dest_canvas, brc);
    br. add(dest_canvas);
    outsize. setText(d_w+" x "+d_h);
    
    container. validate();
    container. repaint();
    clicked = false;
    xBox. setText("0");
    yBox. setText("0");
    startx = 0;
    starty = 0;
    src_canvas. endLine(0,0);

    //Add new pixel listeners
    PixelListener brpl = new PixelListener();
    dest_canvas. addMouseMotionListener(brpl);
    blPosition. setText("Point X:  Y: ");
    blIntensity. setText("Intensity: ");
    brPosition. setText("Point X:  Y: ");
    brIntensity. setText("Intensity: ");
    
  }

  public void set_image() {
    
    //Get image name
    String image_name = "brg1.gif";    
    
    try {
      URL theURL = new URL(getDocumentBase(),"images/"+image_name);
      src = this.getImage(theURL);          
    }  catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("The default file is not present"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    src_canvas = new TranslateImageCanvas(src);
    dest_canvas = new ImageCanvas(src);
    i_w = src_canvas.getImageWidth();
    i_h = src_canvas.getImageHeight();
    
    //Fetch the image and turn it into a pixel array
    src_1d = new int[i_w * i_h];
    PixelGrabber pg = new PixelGrabber(src,0,0,i_w,i_h,src_1d,0,i_w);
    try {
      pg.grabPixels();
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null,("Unable to handle default file"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    if ((pg.status() & ImageObserver.ABORT) != 0) {
      JOptionPane.showMessageDialog(null,("The default image file cannot be loaded"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    d_w = i_w;
    d_h = i_h;
    
    //make a blank destination array 
    dest_1d = new int [d_w* d_h];
    
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = src_1d[i];
    }

  }
  
  public void gui_add_image() {
    
    //add the source image
    blc. gridx = 0;
    blc. gridy = 1;
    bllayout. setConstraints(src_canvas, blc);
    bl. add(src_canvas); 
    MyMouseListener mousel = new MyMouseListener();
    src_canvas. addMouseListener(mousel);
    src_canvas. addMouseMotionListener(mousel);
  
    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(dest_canvas, brc);
    br. add(dest_canvas);
    dest_canvas. addMouseMotionListener(brpl);
    
  }


  /*apply_send_image grabs the image from the URL and turns it into an
   *array of pixels which the operators can manipulate
   *This version overides the version in VisionApplet so as to make
   *use of the ScaleImageCanvas
   */
  
  public void apply_send_image(){
    
    //Turn the specified user image file name into a pixel array
    
    MediaTracker tracker;
    tracker = new MediaTracker(this);
    
    Image image = getImage(theURL);  
    tracker.addImage(image, 0);
    
    //this is to make sure the image loads without an error
    try {
      tracker. waitForID(0);
    } catch(InterruptedException e){
      JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (tracker. statusID(0, false) == tracker.ERRORED){
     JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				   ("Load Error"), JOptionPane.WARNING_MESSAGE); 
      return;
    }
    TranslateImageCanvas c = new TranslateImageCanvas(image);
    int i_w = c.getImageWidth();
    int i_h = c.getImageHeight();
    int[] src_1d = new int[i_w*i_h];
    PixelGrabber pg1 = new PixelGrabber(image,0,0,i_w,i_h,src_1d,0,i_w);
    try {
      pg1.grabPixels();
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if ((pg1.status() & ImageObserver.ABORT) != 0) {
      JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    set_src_image(src_1d, i_w, i_h, image_url);
    
  }
  

   /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for applying translation to an image. Written by Craig Strachan, heavily based on code by Judy Robertson, SELLIC OnLine, Timothy Sharman";
              }
}















