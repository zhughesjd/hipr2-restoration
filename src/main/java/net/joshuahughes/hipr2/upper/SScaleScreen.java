package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.event.*;



/**
 *
 *ScaleScreen is the user interface to the Scaling algorithm
 *(code.operator.Scale.java) It is run as an applet embedded in
 *the file scale.htm @author Craig Strachan DAI 
 *@author Judy Robertson SELLIC OnLine
 *@author Timothy Sharman 
 */

public class SScaleScreen extends VisionApplet1 {

  
  //The input picture. Overrides the input canvas in VisionApplet.
  //This allow the use of the specially tailored ScaleImageCanvas.
  public ScaleImageCanvas src_canvas;

  //Variables used to decide which version of the operator to execute
  private int scalefactor;

  boolean interpolate;

  boolean shrink;

  //Coordinates of sample region
  private int sample_x;
  private int sample_y;

  //The operator class for performing image difference. It's a thread
  Scale scale;

  Point point = null;

  //The listeners for the components of the GUI
  private ComboListener clmethod = new ComboListener();
  private ComboListener cldirection = new ComboListener();
  private ComboListener clscale = new ComboListener();
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();
  
  //The components for the GUI
  private JLabel timelabel = new JLabel("Elapsed Time");
  private JButton startbutton = new JButton("Scale");
  private JButton abortbutton = new JButton("Stop");
  private JLabel time = new JLabel("Time");
  private JTextField time_taken = new JTextField(20);
  private JComboBox methodtoggle = new JComboBox();
  private JComboBox directiontoggle = new JComboBox();
  private JComboBox scaletoggle = new JComboBox();
  private JLabel method = new JLabel("Method :");
  private JLabel direction = new JLabel("Direction :");
  private JLabel scalelab = new JLabel("Scale :");
  
  /**
   * Called automatically when the applet is started. Initialises the
   *interface components ready for drawing on screen.  
   */

  public void add_extra() {
   
//    set_image();

    sample_x = 0;
    sample_y = 0;
    
    scalefactor = 2;

    interpolate = false;
    
    shrink = true;
    
    
    //initialise the operator constructors
    scale = new Scale();
    
    //Set the layout for the mid panel
    
    midc. weighty = 0.5;
    
    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(method, midc);
    mid. add(method);

    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(direction, midc);
    mid. add(direction);

    midc. gridx = 2;
    midc. gridy = 0;
    midlayout. setConstraints(scalelab, midc);
    mid. add(scalelab);

    midc. gridx = 0;
    midc. gridy = 1;
    methodtoggle. addItem("Selection/Replication");
    methodtoggle. addItem("Interpolation");
    midlayout. setConstraints(methodtoggle, midc);
    mid. add(methodtoggle);
    methodtoggle. addActionListener(clmethod);

    midc. gridx = 1;
    midc. gridy = 1;
    directiontoggle. addItem("Shrink");
    directiontoggle. addItem("Expand");
    midlayout. setConstraints(directiontoggle, midc);
    mid. add(directiontoggle);
    directiontoggle. addActionListener(cldirection);

    midc. gridx = 2;
    midc. gridy = 1;
    scaletoggle.addItem("2");
    scaletoggle.addItem("3");
    scaletoggle.addItem("4");
    midlayout. setConstraints(scaletoggle, midc);
    mid. add(scaletoggle);
    scaletoggle. addActionListener(clscale);

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
    container. validate();
  }


  public int [] get_rect(int [] src, int src_w, int x, int y, 
			 int width, int height) {
    int [] dest = new int [width * height];
    int k = 0;
    for (int j = y; j < y + height; j++) {
      for (int i = x; i < x + width; i++) {
	dest[k++] = src[(j * src_w) + i];
      }
    }
    return dest;
  } /* get_rect */


  class MyMouseListener extends MouseInputAdapter {
    
    public void mouseDragged(MouseEvent e) {
//      ImageCanvas ic = (ImageCanvas)e. getSource();
      ScaleImageCanvas ic = (ScaleImageCanvas)e. getSource();
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
      ScaleImageCanvas sic = (ScaleImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      if (point == null) {
	point = new Point(x, y);
      } else {
	point.x = x;
	point.y = y;
      }
      sic. x_Y(point.x ,point.y);
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
      
      if( cb.getSelectedItem().equals("Interpolation") ) {
	interpolate = true;
      }
      else if( cb. getSelectedItem().equals("Selection/Replication") ) {
	interpolate = false;
      }
      else if( cb. getSelectedItem(). equals("Expand") ) {
	src_canvas. x_Y(1,1);
	shrink = false;
	src_canvas.width = i_w / scalefactor;
	src_canvas.height = i_h / scalefactor;
	src_canvas.paint(src_canvas.getGraphics());
      }
      else if( cb. getSelectedItem(). equals("Shrink") ) {
	src_canvas. x_Y(1,1);
	shrink = true;
	src_canvas.width = 0;
	src_canvas.height = 0;
	src_canvas.paint(src_canvas.getGraphics());
      }
      else if( cb == scaletoggle ) {
	if (scaletoggle.getSelectedItem().equals("2"))
	  scalefactor = 2;
	else if (scaletoggle.getSelectedItem().equals("3"))
	  scalefactor = 3;
	else scalefactor = 4;
	if (directiontoggle.getSelectedItem().equals("Expand")) {
	  src_canvas.width = i_w / scalefactor;
	  if (src_canvas.x + src_canvas.width >
	      src_canvas.getImageWidth())
	    src_canvas.x = src_canvas.getImageWidth() - src_canvas.width;
	  src_canvas.height = i_h / scalefactor;
	  if (src_canvas.y + src_canvas.height >
	      src_canvas.getImageHeight())
	    src_canvas.y = src_canvas.getImageHeight() - src_canvas.height;
	  src_canvas.paint(src_canvas.getGraphics());
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
	time_msec = System. currentTimeMillis();
	if (shrink == true) {
	  if (interpolate == true) {
	    dest_1d = scale.shrink_average (src_1d, i_w, i_h, scalefactor);
	  }
	  else {
	    dest_1d = scale.shrink_sample (src_1d, i_w, i_h, scalefactor);
	  }
	}
	else {
	  if (interpolate == false) {
	    dest_1d = scale.grow_replicate( get_rect(src_1d, src_canvas.getImageWidth(), 
						     src_canvas.x, src_canvas.y, 
						     src_canvas.width, src_canvas.height), 
					    src_canvas.width, src_canvas.height, 
					    scalefactor);
	  }
	  else {
	    dest_1d = scale.grow_interpolate(get_rect(src_1d, src_canvas.getImageWidth(), 
						      src_canvas.x, src_canvas.y, 
						      src_canvas.width, src_canvas.height), 
					     src_canvas.width, src_canvas.height, 
					     scalefactor);
	  }
	}

	time_msec = System.currentTimeMillis() - time_msec;
	time_taken. setText(new Long(time_msec).toString()+" msecs");
	

	if (shrink == true) {
	  dest = createImage(new MemoryImageSource(i_w / scalefactor, i_h / scalefactor, 
						   dest_1d, 0, i_w / scalefactor));
	  d_w = i_w / scalefactor;
	  d_h = i_h / scalefactor;
	  outsize. setText(d_w+" x "+d_h);
	}
	else {
	  dest = createImage(new MemoryImageSource(src_canvas.width * scalefactor, 
						   src_canvas.height * scalefactor, 
						   dest_1d, 0, src_canvas.width * 
						   scalefactor));
	  d_w = src_canvas.width * scalefactor;
	  d_h = src_canvas.height * scalefactor;
	  outsize. setText(d_w+" x "+d_h);
	}

	
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
    i_h = h;
    src_1d = new int[i_w*i_h]; 
    src_1d = input_img;
    src = createImage(new MemoryImageSource(i_w,i_h,src_1d,0,i_w));
    if(src_canvas != null){
      bl. remove(src_canvas);
    }
    src_canvas  = new ScaleImageCanvas( src);
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
    
    if (shrink){
      src_canvas.width = 0;
      src_canvas.height = 0;
      src_canvas.paint(src_canvas.getGraphics());
    }
    
    //create a new threshold operator
    scale = new Scale(); 

    //blank the destination image
    d_w = i_w;
    d_h = i_h;

    dest_1d = new int [i_w* i_h];
    dest_1d = input_img;
    dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));
    if(dest_canvas !=null){
      br. remove(dest_canvas);
    }
    dest_canvas = new ImageCanvas(dest);
    //Add a new pixel listener to the canvas
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
    
    src_canvas = new ScaleImageCanvas(src);
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
      JOptionPane.showMessageDialog(null,("The default file image cannot be loaded"),
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

    container. doLayout();
    container. repaint();
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
    ScaleImageCanvas c = new ScaleImageCanvas(image);
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
    return "An applet for applying scaling to an image :part of the code.iface package. Written by Craig Strachan, heavily based on code by Judy Robertson, SELLIC OnLine, Timothy Sharman";
              }
}















