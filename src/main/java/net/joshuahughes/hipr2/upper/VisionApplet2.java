package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import javax.swing.event.*;

/**
 *
 *VisionApplet2 is a subclass of the JApplet class which is tailored
 *for use in the HIPR application. It is used with operators which 
 *require the use of two input images.  
 *
 *@author Timothy Sharman
 */

public class VisionApplet2 extends JApplet { 

  //The main window
  /**
   *The main container for the applet
   */
  public Container container = null;

  //The images
  /**
   *The first input image
   */
  public Image src1;
  /**
   *The second input image
   */
  public Image src2;
  /**
   *The destination image
   */
  public Image dest;
  
  //Their canvases
  /**
   *The first input image canvas
   */
  public ImageCanvas src1_canvas;
  /**
   *The second input image canvas
   */
  public ImageCanvas src2_canvas;
  /**
   *The destination image canvas
   */
  public ImageCanvas dest_canvas;
  
  //pixel arrays for the images
  /**
   *The first input image array
   */
  public int [] src1_1d;
  /**
   *The second input image array
   */
  public int [] src2_1d;
  /**
   *The destination image array
   */
  public int [] dest_1d;

  //the width and height of the input image
  /**
   *The first input image width
   */
  public int i1_w; 
  /**
   *The first input image height
   */
  public int i1_h;

  //the width and height of the second input image
  /**
   *The second input image width
   */
  public int i2_w;
  /**
   *The second input image height
   */
  public int i2_h;
  
  //the width and height of the output image
  /**
   *The destination image width
   */
  public int d_w;
  /**
   *The destination image height
   */
  public int d_h;
  
  //Co-ordiante points for finding the intensity value at a selected point
  private int actualx, actualy;

  //for calculating execution time 
  /**
   *The execution time
   */
  public long time_msec;
  
  //The layout managers
  private GridBagLayout layout = new GridBagLayout(); 
  private GridBagLayout loadlayout = new GridBagLayout();
  /**
   *The layout of the middle panel
   */
  public GridBagLayout midlayout = new GridBagLayout();
  /**
   *The layout of the output panel
   */
  public GridBagLayout outlayout = new GridBagLayout();
  private GridBagLayout brlayout = new GridBagLayout();
  private GridBagLayout bllayout = new GridBagLayout();
  
  private GridBagConstraints wholec = new GridBagConstraints();    
  private GridBagConstraints loadc = new GridBagConstraints();
  /**
   *The constraints for the middle panel
   */
  public GridBagConstraints midc = new GridBagConstraints();
  /**
   *The constraints for the output panel
   */
  public GridBagConstraints outc = new GridBagConstraints();
  private GridBagConstraints blc = new GridBagConstraints();
  private GridBagConstraints brc = new GridBagConstraints();

  //the interface components
  private JPanel top = new JPanel();
  /**
   *The middle panel
   */
  public JPanel mid = new JPanel();
  /**
   *The output panel
   */
  public JPanel out = new JPanel();
  private JPanel br = new JPanel();
  private JPanel bl = new JPanel();
  /**
   *Load button 1
   */
  public JButton load_image = new JButton("Load image");
  /**
   *Load button 2
   */
  public JButton load_image2 = new JButton("Load Image");
  public JTextField input = new JTextField(30);
  public JTextField input2 = new JTextField(30);
  private JLabel inputlab = new JLabel("Input 1");
  private JLabel inputlab2 = new JLabel("Input 2");
  /**
   *The output image label
   */
  public JLabel output = new JLabel("Output");
  /**
   *The first input image size
   */
  public JLabel insize1 = new JLabel();
  /**
   *The second input image sixe
   */
  public JLabel insize2 = new JLabel();
  /**
   *The output image size
   */
  public JLabel outsize = new JLabel();

  //Labels into which info obtained from clicking on images is placed
  private JLabel blPosition = new JLabel("Point X:  Y: ");
  private JLabel brPosition = new JLabel("Point X:  Y: ");
  private JLabel outPosition = new JLabel("Point X:  Y: ");
  private JLabel blIntensity = new JLabel("Intensity: ");
  private JLabel brIntensity = new JLabel("Intensity: ");
  private JLabel outIntensity = new JLabel("Intensity: ");
  
  //The listeners for finding point positions and intensities
  /**
   *The pixel listener for the first input image
   */
  public PixelListener blpl = new PixelListener();
  
  /**
   *The pixel listener for the second input image
   */
  public PixelListener brpl = new PixelListener();

  /**
   *The pixel listener for the output image
   */
  public PixelListener outpl = new PixelListener();

  //the button listener
  private LoadListener ll = new LoadListener();
  private LoadListener ll2 = new LoadListener();

  //the image address and name
  /**
   *The URL from which to load an image
   */
  public URL theURL;
  public String image_url = "scr1.gif";
  public String image_url2 = "scr2.gif";

  /**
   *Called automatically when the applet is started. Initialises the
   *interface components ready for drawing on screen.  
   */
  
  public void init() {

    //sets up the main window
    container = this.getContentPane();
    container.setLayout(layout);
    container.setBackground(Color.white);

    //set up the default images
    set_images();
 
    //set up the interface
    set_interface();

    //add_extra is implemented by any subclasses to allow them to
    //change any of the interface window. 
    add_extra();
  
    //now the image is created, draw it on the interface
    gui_add_image();
    
  }  

  /**
   *Sets up the default images
   */

  public void set_images() {
    
    try {
      URL theURL = new URL(getDocumentBase(),"images/"+image_url);
      src1 = this.getImage(theURL);          
    } catch (MalformedURLException e4) {
      JOptionPane. showMessageDialog(null,("The default file is not present"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    try {
      URL theURL = new URL(getDocumentBase(),"images/"+image_url2);
      src2 = this.getImage(theURL);          
    } catch (MalformedURLException e4) {
      JOptionPane. showMessageDialog(null,("The default file is not present"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }


    src1_canvas = new ImageCanvas(src1);
    src2_canvas = new ImageCanvas(src2);
    dest_canvas = new ImageCanvas(src1);
    i1_w = src1_canvas.getImageWidth();
    i1_h = src1_canvas.getImageHeight();
    i2_w = src2_canvas.getImageWidth();
    i2_h = src2_canvas.getImageHeight();
    
    //Fetch the first image and turn it into a pixel array
    src1_1d = new int[i1_w * i1_h];
    PixelGrabber pg1 = new PixelGrabber(src1,0,0,i1_w,i1_h,src1_1d,0,i1_w);
    try {
      pg1.grabPixels();
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null,("Unable to handle default file"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    if ((pg1.status() & ImageObserver.ABORT) != 0) {
      JOptionPane.showMessageDialog(null,("The default image file cannot be loaded"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    //Fetch the second image and turn it into a pixel array
    src2_1d = new int[i2_w * i2_h];
    PixelGrabber pg2 = new PixelGrabber(src2,0,0,i2_w,i2_h,src2_1d,0,i2_w);
    try {
      pg2.grabPixels();
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null,("Unable to handle default file"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    if ((pg2.status() & ImageObserver.ABORT) != 0) {
      JOptionPane.showMessageDialog(null,("The default image file cannot be loaded"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
      return;
    }

    //decide the overlap area of the two inputs which will be the output
    //image size.
    
    if (i1_w > i2_w) {
      d_w = i2_w;
    }
    else {
      d_w = i1_w;
    }
    
    if (i1_h > i2_h) {
      d_h = i2_h;
    }
    else {
      d_h = i1_h;
    }


    //make a blank destination array 
    dest_1d = new int [d_w* d_h];
    
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = src1_1d[i];
    }


  }

  /**
   *set_interface sets up the 'standard' interface that is the same
   *regardless of which operator is being run
   */

  public void set_interface(){

    //Add the mouse listeners to the images
    PixelListener blpl = new PixelListener();
    PixelListener brpl = new PixelListener();
    PixelListener outpl = new PixelListener();
    src1_canvas. addMouseMotionListener(blpl);
    src2_canvas. addMouseMotionListener(brpl);
    dest_canvas. addMouseMotionListener(outpl);
   
    //The top panel layout

    top. setLayout(loadlayout);
    top. setBackground(Color.white);

    loadc. fill = GridBagConstraints.BOTH;
    loadc. gridx = 0;
    loadc. gridy = 0;
    loadlayout. setConstraints(load_image,loadc);
    top .add(load_image);

    loadc. gridwidth = GridBagConstraints.REMAINDER;
    loadc. gridx = 1;
    loadc. gridy = 0;
    loadlayout .setConstraints(input,loadc);
    top .add(input);
    input. setText(image_url);

    load_image. addActionListener(ll);

    //The second load button
    loadc. anchor = GridBagConstraints.CENTER;
    loadc. gridwidth = 1;
    loadc. fill = GridBagConstraints.BOTH;
    loadc. gridx = 0;
    loadc. gridy = 1;
    loadlayout. setConstraints(load_image2,loadc);
    top .add(load_image2);

    loadc. gridwidth = GridBagConstraints.REMAINDER;
    loadc. gridx = 1;
    loadc. gridy = 1;
    loadlayout .setConstraints(input2,loadc);
    top .add(input2);
    input2. setText(image_url2);

    load_image2. addActionListener(ll2);

    //The mid panel layout

    mid. setLayout(midlayout);
    mid. setBackground(Color.white);
    
    //The output panel layout

    out. setLayout(outlayout);
    out. setBackground(Color.white);

    outlayout. setConstraints(output, outc);
    out. add(output);

    outc. gridx = 0;
    outc. gridy = 2;
    outlayout. setConstraints(outsize, outc);
    outsize. setText(d_w+" x "+d_h);
    out. add(outsize);

    outc. gridx = 0;
    outc. gridy = 3;
    outlayout. setConstraints(outPosition, outc);
    out. add(outPosition);

    outc. gridx = 0;
    outc. gridy = 4;
    outlayout. setConstraints(outIntensity, outc);
    out. add(outIntensity);

    //The bottom right panel layout

    br. setLayout(brlayout);
    br. setBackground(Color.white);

    brc. gridx = 0;
    brc. gridy = 0;
    brlayout. setConstraints(inputlab2, brc);
    br. add(inputlab2);
    
    brc. gridx = 0;
    brc. gridy = 2;
    brlayout. setConstraints(insize2, brc);
    insize2. setText(i2_w+" x "+i2_h);
    br. add(insize2);

    brc. gridx = 0;
    brc. gridy = 3;
    brlayout. setConstraints(brPosition, brc);
    br. add(brPosition);

    brc. gridx = 0;
    brc. gridy = 4;
    brlayout. setConstraints(brIntensity, brc);
    br. add(brIntensity);
 
    //The bottom left panel layout

    bl. setLayout(bllayout);
    bl. setBackground(Color.white);
    
    blc. gridx = 0;
    blc. gridy = 0;
    bllayout. setConstraints(inputlab, blc);
    bl. add(inputlab); 
    
    blc. gridx = 0;
    blc. gridy = 2;
    bllayout. setConstraints(insize1, blc);
    insize1. setText(i1_w+" x "+i1_h);
    bl. add(insize1);

    blc. gridx = 0;
    blc. gridy = 3;
    bllayout. setConstraints(blPosition, blc);
    bl. add(blPosition);

    blc. gridx = 0;
    blc. gridy = 4;
    bllayout. setConstraints(blIntensity, blc);
    bl. add(blIntensity);
    
    //the applet layout
    wholec. gridx = 0;
    wholec. gridy = 0;
    wholec. gridwidth = 3;
    wholec. fill = GridBagConstraints.BOTH;
    wholec. weightx = 0.3;
    wholec. weighty = 0.2;
    layout. setConstraints(top, wholec);
    container. add(top);

    wholec. gridwidth = 3;
    wholec. gridx = 0;
    wholec. gridy = 1;
    layout. setConstraints(mid, wholec);
    container. add(mid);    

    wholec. gridx = 0;
    wholec. gridy = 2;
    wholec. gridwidth = 1;
    layout. setConstraints(bl, wholec);
    container. add(bl);
    
    wholec. gridx = 1;
    wholec. gridy = 2;
    layout. setConstraints(br, wholec);
    container. add(br);

    wholec. anchor = GridBagConstraints.CENTER;
    wholec. fill = GridBagConstraints.NONE;
    wholec. gridx = 2;
    wholec. gridy = 2;
    layout. setConstraints(out, wholec);
    container. add(out);
    
  } 

 /**
   *
   *Handles the actions performed when the mouse is used in the interface
   *to find information about image pixels. Should be added to an ImageCanvas.
   *
   *@param e The event which caused this object to be called
   *
   */
  
   public class PixelListener extends MouseMotionAdapter {
    
    //Used to try and find the intensity value at a point on the interface
    
    public void mouseDragged(MouseEvent e) {
      ImageCanvas ic = (ImageCanvas)e. getSource();
      int clickx = e. getX();
      int clicky = e. getY();
      int scalex = 0;
      int scaley = 0;
      int image_width = ic. getImageWidth();
      int image_height = ic. getImageHeight();
    
      try{
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
	
	//Need to make some chacks that click is inside the image area
	if((actualx < 0) || (actualy < 0)){
	  //do nothing
	}
	else {
	  if(actualx > image_width - 1){
	    actualx = image_width - 1;
	  }
	  if(actualy > image_height - 1){
	    actualy = image_height - 1;
	  }
	  if(ic == src1_canvas){
	    int intense = src1_1d[actualx+(actualy*image_width)] & 0x000000ff;
	    blPosition. setText("Point X:"+actualx+"  Y:"+actualy);
	    blIntensity. setText("Intensity:"+intense);
	  }
	  if(ic == src2_canvas){
	    int intense = src2_1d[actualx+(actualy*image_width)] & 0x000000ff;
	    brPosition. setText("Point X:"+actualx+"  Y:"+actualy);
	    brIntensity. setText("Intensity:"+intense);
	  }
	  if(ic == dest_canvas){
	    int intense = dest_1d[actualx+(actualy*image_width)] & 0x000000ff;
	    outPosition. setText("Point X:"+actualx+"  Y:"+actualy);
	    outIntensity. setText("Intensity:"+intense);	
	  }
	}
      }
      //Must have clicked outside picture so ignore 
      catch(ArrayIndexOutOfBoundsException evt){
	//do nothing
      }
    }
  }




  /**
   *add_extra is used to add extra components to the window of the operator.
   *This is done by adding extra components into the blank mid panel.
   *This panel is then added to the generic panels to make up the interface.
   *The default is for this panel to be blank.
   */

  public void add_extra(){}

  
  private void gui_add_image() {
    
    //add the first source image
    blc. gridx = 0;
    blc. gridy = 1;
    bllayout. setConstraints(src1_canvas, blc);
    bl. add(src1_canvas);

    //add the second source image
    brc. gridx = 0;
    brc. gridy = 1;
    brlayout. setConstraints(src2_canvas, brc);
    br. add(src2_canvas);

    //add the destination image
    outc. gridx = 0;
    outc. gridy = 1;
    outlayout. setConstraints(dest_canvas, outc);
    out. add(dest_canvas);
 
  }
  
  /**
   *set_src1_image2 is used to add a newly loaded image to the interface
   *It is called by set_src1_image once the operator thread has been 
   *recreated.
   *@param input_img The new imput image array
   *@param w The image width
   *@param h The image height
   *@param name The image name
   */

  public void set_src1_image2(int [] input_img , int w, int h, String name) {
    
    //reset the input image

    i1_w = w;
    i1_h = h;
    src1_1d = new int[i1_w*i1_h]; 
    src1_1d = input_img;
    src1 = createImage(new MemoryImageSource(i1_w,i1_h,src1_1d,0,i1_w));
    src1_canvas. updateImage(src1);
    insize1. setText(i1_w+" x "+i1_h);

    //blank the destination image
    
    if (i1_w > i2_w) {
      d_w = i2_w;
    }
    else {
      d_w = i1_w;
    }
    
    if (i1_h > i2_h) {
      d_h = i2_h;
    }
    else {
      d_h = i1_h;
    }

    dest_1d = new int [d_w* d_h];
    dest_1d = input_img;
    dest = createImage(new MemoryImageSource(i1_w,i1_h,dest_1d,0,i1_w));
    dest_canvas. updateImage(dest);
    outsize. setText(i1_w+" x "+i1_h);

    //Add the new pixel listeners
    PixelListener blpl = new PixelListener();
    PixelListener outpl = new PixelListener();
    src1_canvas. addMouseMotionListener(blpl);
    dest_canvas. addMouseMotionListener(outpl);
    blPosition. setText("Point X:  Y: ");
    outPosition. setText("Point X:  Y: ");
    blIntensity. setText("Intensity: ");
    outIntensity. setText("Intensity: ");

  }

  /**
   *set_src1_image tells the applet of the image to be loaded and displayed
   *on the screen. It also is required to recreate the operator which was
   *running at the time. The method should be implemented by the subclass
   *as this default doesn't do this.
   *@param input_img The new input image array
   *@param w The image width
   *@param h The image height
   *@param name The name of the image
   */
  public void set_src1_image(int [] input_img, int w, int h, String name) {
    
    //Must restart any operator here
    //This is a temporary implementation of this method
    
    set_src1_image2(input_img, w, h, name);
    
  }    



  /**
   *set_src2_image2 is used to add a newly loaded image to the interface
   *It is called by set_src1_image once the operator thread has been 
   *recreated.
   *@param input_img The new input image array
   *@param w The image width
   *@param h The image height
   *@param name The image name
   */

  public void set_src2_image2(int [] input_img , int w, int h, String name) {
    
    //reset the input image

    i2_w = w;
    i2_h = h;
    src2_1d = new int[i2_w*i2_h]; 
    src2_1d = input_img;
    src2 = createImage(new MemoryImageSource(i2_w,i2_h,src2_1d,0,i2_w));
    src2_canvas. updateImage(src2);
    insize2. setText(i2_w+" x "+i2_h);
    
    if (i1_w > i2_w) {
      d_w = i2_w;
    }
    else {
      d_w = i1_w;
    }
    
    if (i1_h > i2_h) {
      d_h = i2_h;
    }
    else {
      d_h = i1_h;
    }

    //dest_1d = new int [d_w* d_h];

    //Add the new pixel listeners
    PixelListener brpl = new PixelListener();
    src2_canvas. addMouseMotionListener(brpl);
    brPosition. setText("Point X:  Y: ");
    brIntensity. setText("Intensity: ");

  }

  /**
   *set_src2_image tells the applet of the image to be loaded and displayed
   *on the screen. It also is required to recreate the operator which was
   *running at the time. The method should be implemented by the subclass
   *as this default doesn't do this.
   *@param input_img The new image array
   *@param w The image width
   *@param h The image height
   *@param name The image name
   */
  public void set_src2_image(int [] input_img, int w, int h, String name) {
    
    //Must restart any operator here
    //This is a temporary implementation of this method
    
    set_src2_image2(input_img, w, h, name);
    
  }


/**
   *
   *Handles the actions performed when load buttons on the interface 
   *are pressed.
   *
   *@param evt The event which caused this object to be called
   *
   */

  class LoadListener implements ActionListener {
    
    public void actionPerformed(ActionEvent evt) {

      if (evt.getSource() == load_image){

	//the load button was pressed
	
	image_url = input.getText();    
	try {
	  theURL = new URL(image_url);
	} catch (MalformedURLException e) {
	  
	  // if a file rather than a URL, then try local file space
	  // If still not a URL and not a file then report error
	  
	  try {
	    
	    theURL = new URL(getDocumentBase(),"images/"+image_url);
	    
	  } catch (MalformedURLException e4) {
	    JOptionPane.showMessageDialog(null,("File not found"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	}
	
	//give the user a preview of the image 
	//getAppletContext().showDocument(theURL);
	
	String filename = theURL.getFile();
	
	//check the file is of the correct type
	if ((!filename.endsWith("jpg"))&& ! (filename.endsWith("gif"))) {
	  JOptionPane.showMessageDialog(null,("Invalid file extension. Use .gif or .jpg"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	  return;
	}
	      
	
	apply_send_image(1);     
       
      }
      
      else if(evt.getSource() == load_image2){
	
	//the load button was pressed
	
	image_url2 = input2.getText();    
	try {
	  theURL = new URL(image_url2);
	} catch (MalformedURLException e) {
	  
	  // if a file rather than a URL, then try local file space
	  // If still not a URL and not a file then report error
	  
	  try {
	    
	    theURL = new URL(getDocumentBase(),"images/"+image_url2);
	    
	  } catch (MalformedURLException e4) {
	    JOptionPane.showMessageDialog(null,("File not found"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	}
	
	//give the user a preview of the image 
	//getAppletContext().showDocument(theURL);
	
	String filename = theURL.getFile();
	
	//check the file is of the correct type
	if ((!filename.endsWith("jpg"))&& ! (filename.endsWith("gif"))) {
	  JOptionPane.showMessageDialog(null,("Invalid file extension. Use .gif or .jpg"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	  return;
	}
	
	
      apply_send_image(2);     


      }

                  
    }
    
  }
  
  /**
   *apply_send_image grabs the image from the URL and turns it into an
   *array of pixels which the operators can manipulate
   */
  
  public void apply_send_image(int button){
    
    //Turn the specified user image file name into a pixel array
    
    MediaTracker tracker;
    tracker = new MediaTracker(this);
    
    Image image = getImage(theURL);  
    tracker.addImage(image, 0);
    
    //this is to make sure the image loads without an error
    if (button == 1){
      try {
	tracker. waitForID(0);
      } catch(InterruptedException e){
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      if (tracker. statusID(0, false) == tracker.ERRORED){
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      ImageCanvas c = new ImageCanvas(image);
      i1_w = c.getImageWidth();
      i1_h = c.getImageHeight();
      int[] src1_1d = new int[i1_w*i1_h];
      PixelGrabber pg1 = new PixelGrabber(image,0,0,i1_w,i1_h,src1_1d,0,i1_w);
      try {
	pg1.grabPixels();
      } catch (InterruptedException e) {
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      if ((pg1.status() & ImageObserver.ABORT) != 0) {
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      
      set_src1_image(src1_1d, i1_w, i1_h, image_url);
    }
  
    else if (button ==2){
      try {
	tracker. waitForID(0);
      } catch(InterruptedException e){
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      if (tracker. statusID(0, false) == tracker.ERRORED){
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      ImageCanvas c = new ImageCanvas(image);
      i2_w = c.getImageWidth();
      i2_h = c.getImageHeight();
      int[] src2_1d = new int[i2_w*i2_h];
      PixelGrabber pg2 = new PixelGrabber(image,0,0,i2_w,i2_h,src2_1d,0,i2_w);
      try {
	pg2.grabPixels();
      } catch (InterruptedException e) {
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      if ((pg2.status() & ImageObserver.ABORT) != 0) {
	JOptionPane.showMessageDialog(null,("Unable to load image - try again"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	return;
      }
      
      set_src2_image(src2_1d, i2_w, i2_h, image_url2);
    } 
    
  }
  
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for running image processing operators on. Written by Timothy Sharman";
  }


}


