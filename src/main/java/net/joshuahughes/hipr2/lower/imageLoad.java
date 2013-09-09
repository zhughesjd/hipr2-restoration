package net.joshuahughes.hipr2.lower;
        
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.NoScaleImageCanvas;


/**
 * Operator used to load images into the system for further manipulation
 * from files stored somewhere.
 */
public class imageLoad extends operator1DInt{
  /**
   * The default file that gets loaded up automatically.
   */
  File file;
  String default_image = "brg1.GIF";

  /**
   * The button for loading files.
   */
  JButton loadButton;
  /** 
   * The field for displaying the file name.
   */
  JTextField loadText;
  /**
   * The scrollpane for displaying the loaded image, active when the
   * frame is too small to fit the image in.
   */
  JScrollPane scroll;
  /**
   * The newly loaded image.
   */
  Image displayImage;
  /**
   * Icon for displaying the image on the screen.
   */
  ImageIcon imageIcon;
  /**
   * The label for the icon, for displaying the image.
   */
  JLabel imageLabel;
  /**
   * The labels for  displaying the pixel's position and intensity
   */
  JLabel position = new JLabel(" Point X:  Y: ");
  JLabel intensity = new JLabel(" Intensity: ");
  /**
   * The pixel listener for getting the current pixel
   */
  PixelListener brpl = new PixelListener();
  /**
   * The position of the current pixel
   */
  int actualx, actualy;
  /**
   * The canvas for displaying the image.
   */
  NoScaleImageCanvas dest_canvas;
  /**
   * The panels of the parameters
   */
  JPanel bottomPanel;
  JPanel midPanel;
  /** 
   * The array for storing the input values
   */
  int [] pixels;
  /**
   * Initialise the numbering for all imageLoads.
   */
  static int number=0;
  /**
   * Identification for all imageLoads.
   */
  String type = new String("ImageLoad");
  /**
   * tableau document base URL 
   */
  static URL localdocbase;

  /**
   * Constructor for creating a new imageLoad when one is selected from
   * the menu system.
   * @param panel the parent of all operatorBoxes on the UI
   * @param links all the links in the system
   */
  public imageLoad(JPanel panel, linkData links, URL docbase){

    file = new File(default_image);
    ++number;
    setName(type+'_'+number);

    setParameters(docbase);

    setBox(panel,links,0,1);

    go();
  }

   /**
   * Returns the number of this operator.
   * @return the number of the operator
   */
  public int getNumber(){
    return number;
  }

  /**
   * Loads the filename
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
  }

  public void loadParameters(StreamTokenizer tokenizer, URL docbase) throws IOException{
    int tokenType;
    String loadData = new String();
    boolean started = false;
    boolean moretoscan = true;
    boolean underscore = false;
    tokenType = tokenizer.nextToken();
    while (moretoscan) {

        // word case
        if (tokenizer.ttype == tokenizer.TT_WORD) {
            if (tokenizer.sval.equals("END")) moretoscan = false;
            else {
                if (started && !underscore) loadData = loadData + '/' + tokenizer.sval;
                else {
                        loadData = loadData + tokenizer.sval;
                        started = true;
                }
                underscore = false;
            }
        }

        // number case
        else if (tokenizer.ttype == tokenizer.TT_NUMBER) {
                int val = (int)tokenizer.nval;
                if (started) loadData = loadData + '/' + val;
                else {
                        loadData = loadData + val;
                        started = true;
                }
                underscore = false;
        }
        // character case
        else {
                char c = (char)tokenizer.ttype;
                if (c == ':') {
                        loadData = loadData + ":/";
                        started = true;
                        underscore = false;
                }
                else if (c == '_') {
                        loadData = loadData + "_";
                        started = true;
                        underscore = true;
                }
                else {
                    if (started) loadData = loadData + '/' + c;
                    else {
                        loadData = loadData + c;
                        started = true;
                    }
                    underscore = false;
                }
                
        }
        tokenType = tokenizer.nextToken();
    }

    // get parameters
    file = new File(loadData);
    loadText.setText(loadData);

    // open up saved image URL
    URL url;
      try {

        // see if a full URL
        url = new URL(loadData);
      } catch (MalformedURLException e) {

        // if a file rather than a URL, then try local file space
        try {

          url = new URL(docbase,"images/"+loadData);
        } catch (MalformedURLException e4) {

          // If still not a URL and not a file then report error
          JOptionPane.showMessageDialog(null,("imageLoad: File not found"+loadData),
                    "Load Error", JOptionPane.WARNING_MESSAGE);
          return;
        }
      }

      // load image
      setImage(url);

    go();
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    StringBuffer saveDataBuffer;
    saveData = file.getPath();
    saveDataBuffer = new StringBuffer(saveData);
    for(int i=0;i<saveDataBuffer.length();++i){
      // convert to space as slash is not recognised when reading in a token
      // scanner knows to replace spaces by slash in this context
      if(saveDataBuffer.charAt(i)=='/'){
	saveDataBuffer.setCharAt(i,' ');
      }
    }
    saveData = saveDataBuffer.toString();
    return saveData;
  }

  /**
   * Sets up the parameters for the new imageLoad operator.
   */
  void setParameters(URL docbase){

    loadButton = new JButton("Load image");
    loadText = new JTextField(file.getName(),20);
    localdocbase = docbase;
    loadButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	loadImage(localdocbase);
	go();
      }});

    parameters = new JFrame(name);
    panel = new JPanel();
    midPanel = new JPanel();
    midPanel.setLayout(new BorderLayout());
    midPanel.add(position,"Center");
    midPanel.add(intensity,"South");
    bottomPanel = new JPanel();
    bottomPanel.add(loadButton);
    bottomPanel.add(loadText);
    panel.setLayout(new BorderLayout());
    panel.add(midPanel,"North");
    panel.add(bottomPanel,"South");
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
    try {
        URL url = new URL(docbase,"images/"+default_image);
        setImage(url);
    }
     catch (MalformedURLException e4) {
              JOptionPane.showMessageDialog(null,("Can't find file images/brg1.GIF"),"Load Error", JOptionPane.WARNING_MESSAGE);
	      return;
     }
  }

  /**
   *Loads the given image and converts it into an image1dInt
   *@param fileName the file containing the default image
   */
  void setImage(URL imageURL){

    // get image - no exception catching apparently possible, thread just hangs if incorrectly non-esistent URL
    imageIcon = new ImageIcon(imageURL);

    int width = imageIcon.getIconWidth();
    int height = imageIcon.getIconHeight();

    int [] inputImage = new int [width*height];

    // loads image - no exception catching apparently possible, thread just hangs if incorrectly non-esistent URL
    PixelGrabber grabber = new PixelGrabber(imageIcon.getImage(),
					    0,0,
					    width,
					    height,
					    inputImage,
					    0,width);
    try{
      grabber.grabPixels();
      for(int i=0;i<inputImage.length;++i){
	int pixel = inputImage[i];
	inputImage[i] = (new Color(pixel)).getRed();
      }
      input1 = new image1DInt(width,
			 height,
			 inputImage);
    }catch(InterruptedException e){
    }
    
  }

  /**
   * Loads the selected image
   */
  void loadImage(URL docbase){

    try {
      URL theURL = new URL(loadText.getText());
      setImage(theURL);
    } catch (MalformedURLException e) {

	// if a file rather than a URL, then try local file space
	// If still not a URL and not a file then report error

	try {
          String filename = new String(loadText.getText());
	  URL theURL = new URL(docbase,"images/"+filename);
	  setImage(theURL);
	  file = new File(filename);

        } catch (MalformedURLException e4) {
	  JOptionPane.showMessageDialog(null,("File not found"),
				    "Load Error", JOptionPane.WARNING_MESSAGE);
	  return;
        }
      }
  }

  /**
   * Runs the operator (if the currently loaded image contains data)
   * and propagates the image to all the operators that imageLoad links
   * to.
   */
  public void go(){
    if(getInput1().getWidth()>0){

      System.out.println(name);
      System.out.println("Input width "+getInput1().getWidth());
      

      output1 = new image1DInt(getInput1());

      updateDisplay();

      System.out.println("Output width "+getOutput1().getWidth());

      propagate();
    }
  }
  /** 
   * Displays the image in the parameter's window
   */
  public void updateDisplay(){
    pixels = (int []) input1.getValues().clone();
   
      //Grab the scale and offset values
     
      for(int i=0;i<pixels.length;++i){
	//Get value and do scaling and offset
	int grey = (int)(pixels[i]);
	if(grey>255){
	  grey = 255;
	}else if(grey<0){
	  grey = 0;
	}
	pixels[i] = (new Color(grey,grey,grey)).getRGB();
      }
     
      displayImage = 
	parameters.createImage(new MemoryImageSource(input1.getWidth(), 
						     input1.getHeight(), 
						     pixels,0, 
						     input1.getWidth()));
     
      if ( dest_canvas != null ) {
	midPanel. remove(dest_canvas);
      }
      dest_canvas = new NoScaleImageCanvas(displayImage);
      dest_canvas. addMouseMotionListener(brpl);
      midPanel. add(dest_canvas, "North");
      updateParameters();
   
}
  
  /**
   * class for getting the current pixel position in the image canvas
   */
  
 public class PixelListener extends MouseMotionAdapter {
    
    //Used to try and find the intensity value at a point on the interface
    
    public void mouseDragged(MouseEvent e) {
      NoScaleImageCanvas ic = (NoScaleImageCanvas)e. getSource();
      int clickx = e. getX();
      int clicky = e. getY();
      int scalex = 0;
      int scaley = 0;
      int image_width = ic. getImageWidth();
      int image_height = ic. getImageHeight();
    
      try{
	  actualx = clickx;
	  actualy = clicky;
	 
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
	  if(ic == dest_canvas){
	    int intense = pixels[actualx+(actualy*image_width)]&0x000000ff;
	    position. setText("Point X:"+actualx+"  Y:"+actualy);
	    intensity. setText("Intensity:"+intense);	
	  }
	}
      }
      //Must have clicked outside picture so ignore 
      catch(ArrayIndexOutOfBoundsException evt){
	//do nothing
      }
    }
  }


}
