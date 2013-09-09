package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.event.*;


/**
 *
 *SReflectScreen is the user interface to the Reflection algorithm
 *(code.operator.Reflect.java) It is run as an applet embedded in
 *the file reflect.htm 
 *@author Craig Strachan DAI 
 *@author Judy Robertson SELLIC OnLine 
 *@author Timothy Sharman 
 */

public class SReflectScreen extends VisionApplet1 {

  //The input picture. Overrides the input canvas in VisionApplet.
  //This allow the use of the specially tailored ReflectImageCanvas./

  public ReflectImageCanvas src_canvas;

  private boolean wrap;

  //The operator class for performing reflection. It's a thread
  private Reflect reflect;

  Point point = null;  

  //The listeners for the components of the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  private SliderListener slangle = new SliderListener();
  private TextListener angletl = new TextListener();

  private JButton startbutton = new JButton("Reflect");
  private JButton abortbutton = new JButton("Stop");
  private JLabel time = new JLabel("Time");
  private JLabel anglelabel = new JLabel("Angle of axis");
  private JTextField angle_text = new JTextField(3);
  private JSlider angle_slide = new JSlider(JSlider.HORIZONTAL, 0, 180, 0);
  private JTextField time_taken = new JTextField(30);


  public void add_extra() {

    wrap = false;
    
    //initialise the operator constructors
    reflect = new Reflect();
    
    //Set the layout for the mid panel

    midc. weighty = 0.5;

    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(anglelabel, midc);
    mid. add(anglelabel);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(angle_text, midc);
    mid. add(angle_text);
    angle_text. setText("0");
    angle_text. addKeyListener(angletl);

    midc. gridx = 1;
    midc. gridy = 1;
    midc. fill = GridBagConstraints.HORIZONTAL;
    midlayout. setConstraints(angle_slide, midc);
    mid. add(angle_slide);
    angle_slide. setBackground(Color.white);
    angle_slide. setMajorTickSpacing(20);
    angle_slide. setMinorTickSpacing(1);
    angle_slide. setPaintTicks(true);
    angle_slide. setPaintLabels(true);
    angle_slide. addChangeListener(slangle);

    midc. fill = GridBagConstraints.NONE;
    midc. gridx = 0;
    midc. gridy = 2;
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
    midc. gridy = 3;
    midlayout. setConstraints(time, midc);
    mid. add(time);
    
    midc. gridx = 1;
    midc. gridy = 3;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);
    
    mid. repaint();
  }


  class TextListener extends KeyAdapter {
    public void keyReleased(KeyEvent evt){
      JTextField jtf = (JTextField)evt. getSource();
      try{
	int val = Integer. valueOf (jtf. getText()). intValue();
	if(val >= 0 && val <= 180){
	  angle_slide. setValue(val);
	}
	else{
	  JOptionPane.showMessageDialog(null,("Angle of reflectance must lie between 0 and 180"),
					("Error!"), JOptionPane.WARNING_MESSAGE);     
	  angle_slide. setValue(0);
	  angle_text. setText("0");
	}
      }
      catch(NumberFormatException e){
	if(jtf.getText().equals("")){
	  //Do nothing
	}
	else{
	  JOptionPane.showMessageDialog(null,("Invalid angle specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);  
	}
      }   
    }
  }


  class MyMouseListener extends MouseInputAdapter {

    public void mouseDragged(MouseEvent e) {
//      ImageCanvas ic = (ImageCanvas)e. getSource();
      ReflectImageCanvas ic = (ReflectImageCanvas)e. getSource();
      int clickx = e. getX();
      int clicky = e. getY();
      int scalex = 0;
      int scaley = 0;
      int image_width = ic. getImageWidth();
      int image_height = ic. getImageHeight();
    
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
    
    public void mousePressed(MouseEvent e) {
      ReflectImageCanvas ric = (ReflectImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      if (point == null) {
	point = new Point(x, y);
      } else {
	point.x = x;
	point.y = y;
      }
      ric. setLineCenter(point.x ,point.y);
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
	angle_text. setText(String.valueOf(angle_slide. getValue()));
	time_msec = System.currentTimeMillis();
	dest_1d = reflect.reflect_image(src_1d, i_w, i_h, src_canvas.x, 
					src_canvas.y, src_canvas.angle, wrap);

	time_msec = System.currentTimeMillis() - time_msec;
	time_taken.setText(new Long(time_msec).toString()+" msecs");
      
	dest = createImage(new MemoryImageSource(i_w, i_h, dest_1d, 0, i_w));
	
	if (dest_canvas != null) {
	  br. remove(dest_canvas);
	}
	
	dest_canvas = new ImageCanvas(dest);
	//Add a new pixel listener
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
      if(slide == angle_slide){
	angle_text. setText(String.valueOf(angle_slide. getValue()));
	src_canvas. angle = angle_slide. getValue();
	src_canvas. paint(src_canvas. getGraphics());
      }
    }
  }


  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //reset the input image
    i_w = w;
    i_h = h;
    src_1d = new int[i_w*i_h]; 
    src_1d = input_img;
    src = createImage(new MemoryImageSource(i_w,i_h,src_1d,0,i_w));
    if(src_canvas != null){
      bl. remove(src_canvas);
    }
    src_canvas  = new ReflectImageCanvas(src);
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
    blPosition. setText("Point X:  Y: ");
    blIntensity. setText("Intensity: ");
    
    angle_slide. setValue(0);
    angle_text. setText("0");

    //create a new threshold operator
    reflect = new Reflect(); 
    
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
    //Add a new pixel listener
    PixelListener brpl = new PixelListener();
    dest_canvas. addMouseMotionListener(brpl);


    brPosition. setText("Point X:  Y: ");
    brIntensity. setText("Intensity: ");

    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(dest_canvas, brc);
    br. add(dest_canvas);
    outsize. setText(d_w+" x "+d_h);
    
    container. validate();
    container. repaint();
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
    
    src_canvas = new ReflectImageCanvas(src);
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
      JOptionPane.showMessageDialog(null,("The default iamge file cannot be loaded"),
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
   *use of the RotateImageCanvas
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
      JOptionPane. showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (tracker. statusID(0, false) == tracker.ERRORED){
      JOptionPane. showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    ReflectImageCanvas c = new ReflectImageCanvas(image);
    int i_w = c.getImageWidth();
    int i_h = c.getImageHeight();
    int[] src_1d = new int[i_w*i_h];
    PixelGrabber pg1 = new PixelGrabber(image,0,0,i_w,i_h,src_1d,0,i_w);
    try {
      pg1.grabPixels();
    } catch (InterruptedException e) {
      JOptionPane. showMessageDialog(null,("Unable to load image - try again"),
				    ("Load Error"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if ((pg1.status() & ImageObserver.ABORT) != 0) {
      JOptionPane. showMessageDialog(null,("Unable to load image - try again"),
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
    return "An applet for applying reflection to an image. Written by Craig Strachan, heavily based on code by Judy Robertson, SELLIC OnLine, Timothy Sharman";
  }
}















