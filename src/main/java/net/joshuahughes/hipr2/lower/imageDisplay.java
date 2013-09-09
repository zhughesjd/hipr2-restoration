package net.joshuahughes.hipr2.lower;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;

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
 * This operator displays the image input, the output is as the same 
 * as the input.
 */
public class imageDisplay extends operator1DInt{
   /**
   * The scrollpane for displaying the loaded image, active when the
   * frame is too small to fit the image in.
   */
  JScrollPane scroll;
  /**
   * The panels for displaying the image and the parameters
   */
  JPanel bottomPanel;
  JPanel middlePanel;
  /**
   * The label for displaying the image
   */
  JLabel imageLabel;
  /**
   * The label for the scale, applyied to the image displayed
   */
  JLabel scaleLabel;
   /**
   * The label for the offset, applyied to the image displayed
   */
  JLabel offsetLabel;
   /**
   * The label for  displaying the pixel's position 
   */
  JLabel position = new JLabel("Point X:  Y: ");
  /**
   * The label for  displaying the pixel's intensity 
   */
  JLabel intensity = new JLabel("Intensity: ");
   /**
   * The pixel listener for getting the current pixel
   */
  PixelListener brpl = new PixelListener();
  /**
   * The text box for the scale, applyied to the image displayed
   */
  JTextField scaleBox;
  /**
   * The text box for the offset, applyied to the image displayed
   */
  JTextField offsetBox;
  /**
   * The apply button, for applying scale and offset to the image.
   */
  JButton applyButton;
  /**
   * The save button, for saving the image.
   */
  JButton saveButton;
   /** 
   * The text box for giving the URL
   */
//***  JTextField textbox;
   /**
   * The file for storing the image
   **/
  File file;
  /**
   * The label info
   */
//***  JLabel info = new JLabel("Full URL");
  /**
   * The displayed image
   */
  Image displayImage;
  /**
   * The icon, for the displayed image 
   */
  ImageIcon imageIcon;
  /**
   * The position of the current pixel
   */
  int actualx, actualy;
   /**
   * The canvas for displaying the image.
   */
  NoScaleImageCanvas dest_canvas;
  /** 
   * The array for storing the input values
   */
  int [] pixels;
  /**
   * Initialise the numbering for all imageLoads.
   */
  static int number=0;
  /** 
   *Identification for the operator
   */
  String type = new String("ImageDisplay");
  /**
   * Constructor for creating a new imageLoad when one is selected from
   * the menu system.
   * @param panel the parent of all operatorBoxes on the UI
   * @param links all the links in the system
   */
  public imageDisplay(JPanel panel, linkData links){
    ++number;
    setName(type+'_'+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
  }

  /**
   * Returns the number of this operator.
   * @return the number of the operator
   */
  public int getNumber(){
    return number;
  }
  /**
  * Loads the required parameters (number of iterations)
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    int tokenType;
    tokenType = tokenizer.nextToken();
    double scaleValue = tokenizer.nval;
    scaleBox.setText((new Double(scaleValue)).toString());
    tokenType = tokenizer.nextToken();
    double offsetValue = tokenizer.nval;
    offsetBox.setText((new Double(offsetValue)).toString());
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date), there are no parameters with imageDisplay
   * that require saving.
   */
  public String saveParameters(){
    String saveData = scaleBox.getText()+" "+offsetBox.getText();
    return saveData;
  }
  /**
   * Sets up the parameters for the new imageLoad operator.
   */
  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    //Now create the scale and offset boxes and apply button
    bottomPanel = new JPanel();
    middlePanel = new JPanel();
    JPanel otherPanel = new JPanel();
    JPanel savePanel = new JPanel();
    scaleLabel = new JLabel("Scale ");
    offsetLabel = new JLabel("Offset ");
    scaleBox = new JTextField(5);
    scaleBox.setText("1.0");
    offsetBox = new JTextField(5);
    offsetBox.setText("0");
//***    textbox = new JTextField(20);
    applyButton = new JButton("Apply");
    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	updateDisplay();
      }
    });
//***    saveButton = new JButton("Save Image");
//***    saveButton. addActionListener(new ActionListener(){
//***      public void actionPerformed(ActionEvent e){
//***	save_Image();
//***      }
//***    });
    panel.setLayout(new BorderLayout());
    
    middlePanel. add(position);
    middlePanel. add(intensity);
    bottomPanel. add(scaleLabel);
    bottomPanel. add(scaleBox);
    bottomPanel. add(offsetLabel);
    bottomPanel. add(offsetBox);
    bottomPanel. add(applyButton);
//**    savePanel. add(info);
//**    savePanel. add(textbox);
//***    savePanel. add(saveButton);
    otherPanel.setLayout(new BorderLayout());
    otherPanel.add(bottomPanel,"North");
    otherPanel.add(savePanel,"South");
    panel.add(middlePanel, "North");
    panel.add(otherPanel,"South");
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
    parameters.setLocation(new Point(100,100));
  }

  /**
   * Saves the current image displayed.
   */
/*
  
    void save_Image(){
    try {
      URL testURL = new URL(textbox.getText());
    }
    catch (MalformedURLException e4) {
      JOptionPane.showMessageDialog(null,("Malformed URL"),"Load Error", JOptionPane.WARNING_MESSAGE);
      
    }						
      file = new File(textbox.getText());

      FileWriter saveFile;
      String data = new String();
      String header = new String("systemlayout\n//Automatically generated save file.  Do not edit manually unless you know what you are doing.  You could break something.\n");
      int [] array = (int []) input1.getValues().clone();
      try {
      saveFile = new FileWriter(file);
      }
      catch(IOException ioe){
	System.out.println("IO error"+ioe);
      }
    }
*/

  /**
   * Displays the image in the parameter's window
   */
  void updateDisplay(){
      try{
	  pixels = (int []) input1.getValues().clone();
	  try{
	      //Grab the scale and offset values
	      float scale = Float.valueOf( scaleBox.getText() ).floatValue();
	      float offset = Float.valueOf( offsetBox.getText() ).floatValue();
	      for(int i=0;i<pixels.length;++i){
		  //Get value and do scaling and offset
		  int grey = (int)((pixels[i]*scale)+offset);
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
		  panel. remove(dest_canvas);
	      }
	      dest_canvas = new NoScaleImageCanvas(displayImage);
	      dest_canvas. addMouseMotionListener(brpl);
	      panel. add(dest_canvas, "Center");
	      //imageIcon = new ImageIcon(displayImage);
	      //imageLabel.setIcon(imageIcon);
	      updateParameters();
	  }
	  catch(NumberFormatException e){
	      //The scale or offset values aren't valid so pop up a window
	      JOptionPane.showMessageDialog(null,("Invalid scale or offset"),
					    ("Error!"), JOptionPane.WARNING_MESSAGE);
	      scaleBox. setText("1.0");
	      offsetBox. setText("0");
	  }
      }
      catch(NullPointerException e) {
	  // Binary operator must only have one image
	  JOptionPane.showMessageDialog(null,("Probably binary operator does not have 2 image"),
					("Error!"), JOptionPane.WARNING_MESSAGE);
	  scaleBox. setText("1.0");
	  offsetBox. setText("0");
      }
  }
    
 /**
   * Runs the operator 
   * and propagates the image to all the operators that it links
   * to.
   */
  public void go(){
    if(getInput1()!=null){
      System.out.println(name);
      
      System.out.println("Input width "+getInput1().getWidth());
      output1 = new image1DInt(getInput1());
      System.out.println("Output width "+getOutput1().getWidth());
      updateDisplay();
      
      propagate();
       
    }
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
	//if ((image_width >= 256) || (image_height >= 256)){
	  
	  // if (image_width > image_height){
	    
// 	  float ratio = (float) image_width / (float) image_height;
// 	  scalex = 256;
// 	  scaley = (int) ((float) 256/ ratio);
// 	  actualx = (int)(((float)image_width / (float)scalex)*clickx);
// 	  actualy = (int)(((float)image_height / (float)scaley)*clicky);
// 	  }  
	
// 	  else if (image_height > image_width){
	    
// 	    float ratio = (float) image_height /  (float) image_width;
// 	    scalex = image_width;
// 	    scaley = 256;
// 	    scalex = (int) ( (float) 256 / ratio);
// 	    actualx = (int)(((float)image_width / (float)scalex)*clickx);
// 	    actualy = (int)(((float)image_height / (float)scaley)*clicky);
// 	  } 
// 	  else {  
// 	    actualx = (int)(((float)image_width / (float)256)*clickx);
// 	    actualy = (int)(((float)image_height / (float)256)*clicky);
// 	  }
	  
	//}
	//else {
	  actualx = clickx;
	  actualy = clicky;
	  //	}
	
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

















