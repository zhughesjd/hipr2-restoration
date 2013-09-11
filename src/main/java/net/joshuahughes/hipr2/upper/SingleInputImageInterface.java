package net.joshuahughes.hipr2.upper;
//package code.iface.highlevel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import java.awt.color.*;
//import code.iface.common.*;
//import code.operator.binaryTools.*;

/**
* The base class for many non-binary image operators and also
* the base class for SingleBinaryImageInterface, containing
* the necessary functionality for displaying images onscreen and
* setting up the basic user interface.
*
* @author Simon Horne, bug fix by Donald Nairn
*/
public class SingleInputImageInterface extends JApplet{
/**
 * The top level GUI container.
 */
public Container container;
/**
 * The panel used for displaying the input/output images.
 */
public JPanel imagesPanel;
public JPanel startstopPanel;
  /**
 * The panel containing the file load and status information and controls.
 */
public JPanel loadPanel;
/**
 * The panel containing the leftPanel and the various FFT output images.
 */
public JPanel operatorPanel;
public JPanel inputPanel;
public JPanel outputPanel;
/**
 * The text box on the interface for displaying/entering the
 * input image name.
 */
public JTextField imageNameField;
/**
 * The filename of the input image.
 */
public String imageName;
/**
 * Label for displaying the current loading status.
 */
public JLabel status;
/**
 * The input image stored as an Image.
 */
public Image inputImage;
/**
 * Keeps track of the image loading.
 */
public MediaTracker tracker;
/**
 * The output image stored as an Image.
 */
public Image outputImage;
/**
 * For displaying the input image on the screen.
 */
public ImageIcon inputIcon;
/**
 * For displaying the input icon and therefore the input image.
 */
public JLabel inputLabel;


JLabel inputPixel;
JLabel outputPixel;

/**
 * For displaying the input image dimensions.
 */
public JLabel inputDims;
/**
 * For displaying the output image.
 */
public ImageIcon outputIcon;
/**
 * For displaying the output image.
 */
public JLabel outputLabel;
/**
 * For displaying the output image dimensions.
 */
public JLabel outputDims;
/**
 * An array of ints representing the input image.
 */
public int inputArray[];
/**
 * An array of ints representing the output image.
 */
public int outputArray[];
/**
 * Height of the input image.
 */
public int inputHeight;
/**
 * Width of the input image.
 */
public int inputWidth;
/**
 * Height of the output image.
 */
public int outputHeight;
/**
 * Width of the output image.
 */
public int outputWidth;
/**
 * For storing a record of the time taken by the operator.
 */
public long time_msec;
/**
 * The button for loading a new image.
 */
public JButton load;
/**
 * The interface for stopping and starting the operator.
 */
public StartStopInterface startstop;
/**
 * Various tools for manipulating images.
 */
public ImageTools imageTools = new ImageTools();

/**
 * Sets the initial image name onscreen and internally.
 *
 * @param name the name of the new image
 */
public void initialImage(String name){
  imageName = new String (name);
  imageNameField.setText(name);
}

class CoordinatesListener extends MouseInputAdapter{
  public void mouseDragged(MouseEvent e){
    double screenX = e.getX();
    double screenY = e.getY();
    double x = screenX/256*inputWidth;
    double y = screenY/256*inputHeight;
    int x1 = (int) Math.round(x);
    int y1 = (int) Math.round(y);
    if(e.getComponent()==inputLabel){
	int g = (new Color(inputArray[y1*inputWidth+x1])).getRed();
	inputPixel.setText("X "+x1+" Y "+y1+" Value "+g);
	//System.out.println(inputWidth+" "+x+" "+y+" "+g);
    }else if(e.getComponent()==outputLabel){
	int g = (new Color(outputArray[y1*inputWidth+x1])).getRed();
	outputPixel.setText("X "+x1+" Y "+y1+" Value "+g);
    }
  }
}

/**
 * Updates the output image onscreen.
 *
 * @param output the new output image
 * @param width the width of the new output image
 * @param height the height of the new output image
 */
public void updateOutput(Image output, int width, int height){
  
  outputIcon = new ImageIcon(output);
  outputLabel.setIcon(imageTools.scaleImage(outputIcon));
  outputWidth = width;
  outputHeight = height;
  if(outputWidth == -1){
    outputDims.setText("No image");
  }else{
    outputDims.setText(outputWidth + " x " + outputHeight);
  }
}

/**
 * Initialises the input image by simply updating it onscreen.
 *
 * @param image the new input image
 */
public void initialiseInput(Image image){
  updateInput(image);
  outputArray = (int []) inputArray.clone();
}

/**
 * Updates the input image onscreen.
 *
 * @param image the new output image
 */
public void updateInput(Image image){
  inputIcon = new ImageIcon(image);
  inputLabel.setIcon(imageTools.scaleImage(inputIcon));
  inputWidth = imageTools.getWidth(inputIcon);
  inputHeight = imageTools.getHeight(inputIcon);
  inputArray = imageTools.examineInput(inputIcon);
  if(inputWidth == -1){
    inputDims.setText("No image");
  }else{
    inputDims.setText(inputWidth + " x " + inputHeight);
  }
  
}

/**
 * Initialises the images by loading the current input image from a file,
 * then updating the input and output onscreen.
 *
 * @return true if there was no problems, false if something went wrong
 * @exception InterruptedException error when loading image file
 * @exception MalformedURLException the url is not correctly entered
 */
public boolean initialiseImages(){

  try{

	URL theURL = null;
	status.setText("Loading");
	
	// Initially try for URL

	try {
	    theURL = new URL(imageName);
	} catch (MalformedURLException e) {
	    
	    // if a file rather than a URL, then try local file space
	    // If still not a URL and not a file then report error
	    
	    try {
	    
		theURL = new URL(getDocumentBase(),"images/"+imageName);
	      
	    } catch (MalformedURLException e1) {
		JOptionPane.showMessageDialog(null,("File not found"),
					      "Load Error", JOptionPane.WARNING_MESSAGE);
		return false;
	    }
	}
	
	
	tracker = new MediaTracker(this);
	tracker.addImage(inputImage, 1);
	inputImage = getImage(theURL);
	tracker.waitForID(1);

	//System.out.println("test");
	initialiseInput(inputImage);
	if(inputHeight == -1){
	    //System.out.println("Not a file");
	    JOptionPane.showMessageDialog(null,
					  ("File not found - "+imageName),
					  "File not found",
					  JOptionPane.INFORMATION_MESSAGE); 
	    status.setText("File not found, or not gif/jpg file");
	    return false;
	}else{
	    status.setText("Image loaded successfully");
	    updateOutput(inputImage,inputWidth,inputHeight);
	    return true;
	}
  }catch(InterruptedException e2){
	status.setText("Error loading file");
	//System.out.println("Error in loading file");
	return false;
  }
}

/**
 * Sets up the panel containing the load button and
 * file loading information.
 */
public JPanel createLoadPanel(){
  JPanel p = new JPanel();
  p.setLayout(new FlowLayout());
  p.setOpaque(false);
  
  JLabel l = new JLabel("Input Image  ");
  imageNameField = new JTextField(20);
  imageNameField.setToolTipText("The input file name");
  load = new JButton("Load Image");
  load.setToolTipText("Click here to load image");
  status = new JLabel("Load complete");
  status.setToolTipText("What's happening with the file?");
  
  imageNameField.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e){
	imageName = e.getActionCommand();
	boolean b = initialiseImages();
    }});
  
  load.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e){
	imageName = imageNameField.getText();
	boolean b = initialiseImages();
    }});

  p.add(load);
  p.add(l);
  p.add(imageNameField);
  p.add(status);
  return p;
}

/** 
 * Sets up the panel displaying the input and output images.
 */
public JPanel createImagesPanel(){
  
  imageName = "brg1.gif";
  JPanel p = new JPanel();
  p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
  p.setOpaque(false);
  inputPanel = new JPanel();
  inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));
  inputPanel.setOpaque(false);
  JLabel l1 = new JLabel("Input Image");
  inputLabel = new JLabel("");
  CoordinatesListener coordinatesListener = new CoordinatesListener();
  inputLabel.addMouseMotionListener(coordinatesListener);
  inputLabel.setToolTipText("The input image");
  inputPanel.add(l1);
  inputPanel.add(inputLabel);
  inputDims = new JLabel(); 
  inputPixel = new JLabel("X  Y  Value");
  inputPanel.add(inputDims);
  inputPanel.add(inputPixel);

  outputPanel = new JPanel();
  outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
  outputPanel.setOpaque(false);
  JLabel l2 = new JLabel("Output Image");
  outputLabel = new JLabel("");
  CoordinatesListener coordinatesListener2 = new CoordinatesListener();
  outputLabel.addMouseMotionListener(coordinatesListener2);
  outputLabel.setToolTipText("The output image");
  outputPanel.add(l2);
  outputPanel.add(outputLabel);
  outputDims = new JLabel();
  outputPixel = new JLabel("X  Y  Value");
  outputPanel.add(outputDims);
  outputPanel.add(outputPixel);
  outputHeight = inputHeight;
  outputWidth = inputWidth;

  boolean b = initialiseImages();
  
  p.add(inputPanel);
  p.add(Box.createRigidArea(new Dimension(10,0)));
  p.add(outputPanel);
  
  return p;
}

/**
 * Sets the look and feel of the interface.
 *
 * @exception Exception if things don't go right
 */
public void setLook(){
  try {
    UIManager.setLookAndFeel(
		UIManager.getCrossPlatformLookAndFeelClassName());
  } catch (Exception e) { }
}

/**
 * Sets up the top-level container for displaying the 
 * interface on the screen.
 */
public void setContainer(){
  container = this.getContentPane();
  container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
  container.setBackground(Color.white);
  startstop = new StartStopInterface();

  loadPanel = createLoadPanel();
  imagesPanel = createImagesPanel();
  startstopPanel = startstop.createPanel();
  operatorPanel = new JPanel();
  operatorPanel.setLayout(new BoxLayout(operatorPanel,BoxLayout.Y_AXIS));
  operatorPanel.setOpaque(false);
  operatorPanel.setBorder(BorderFactory.createEmptyBorder(10,50,10,50));

  container.add(loadPanel);
  container.add(operatorPanel);
  container.add(startstopPanel);
  container.add(imagesPanel);

}

public void init() {


  setLook();
  setContainer();
  
}
/**
 * Used by the browser to obtain details of the applet.
 *
 * @return Brief description of the applet.
 */
public String getAppletInfo() {
  return "The superclass of several non-binary image operators.  The superclass of the superclass of the binary image operators.  Written by Simon Horne.";
}
}
