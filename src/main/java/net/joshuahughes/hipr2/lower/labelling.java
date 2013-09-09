package net.joshuahughes.hipr2.lower;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.joshuahughes.hipr2.upper.ImageLabel;


public class labelling extends operator1DInt{


   /* image displayed in the parameters window with colors */
  int [] display;
  Image displayImage;
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JScrollPane scroll;
  JLabel colourLabel;
  int colour;

  static int number=0;
  String type = new String("Labelling");

  public labelling(){
  }

  public labelling(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
    box.getIn1().setText("BININ1");
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    
    return saveData;
  }

  /**
   *Used to load all the parameters for this particular operator and reset the 
   *interface so that it contains these loaded parameters
   */

  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    
    //Grab the parameters
    //int tokenType;
    //tokenType = tokenizer.nextToken();

    //Get all parameters here

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }
  
  void setParameters(){
    /**
     *This function is used to set up the parameters window. This window
     *should contain enough parameters to be able to run the operator
     *although parameters like scaling and offset are not required as there
     *is an operator already defined to do this. The interface components
     *should be added to the parameters frame. In this example a single panel
     *is created which is used to hold a label saying there are no paramters.
     *In general this will not be true of most operators.
     */
    parameters = new JFrame(name);
    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    scroll = new JScrollPane(imageLabel);
    panel.add(scroll, "Center");
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
    
    if(getInput1()!=null){

      ImageLabel labelOp = new ImageLabel(input1.getWidth());
      
      System.out.println("Width 1 "+getInput1().getWidth());
      display = new int[input1.getWidth()*input1.getHeight()];
      display = labelOp.doLabel(gs2binarypix(input1.getValues(),input1.getWidth(),input1.getHeight()),input1.getWidth(),input1.getHeight());
      colour = labelOp.getColours();
      updateDisplay();

      output1 = new image1DInt(input1.getWidth(),input1.getHeight(),color2gs2(display));
      System.out.println("Output width "+getOutput1().getWidth());
      propagate();
    }
  }

  /**
   * Converts a greylevel array into a binary pixels array
   * @param the greylevels array
   * @param the width of the image
   * @param the height of the image
   * @return the binary pixels array
   */
  public int[] gs2binarypix(int[] gs, int width, int height) {
  
  int[] bin = new int[width*height];
  int tmp;

  for(int i = 0; i < width; i++) {
    for(int j = 0; j < height; j++) {
    tmp = gs[i+(j*width)];
    if (tmp >= 128) { bin[(j*width)+i] = 0xffffffff;}
    else bin[(j*width)+i] = 0xff000000;
    }}
 
  return bin;
  }
  /**
   * Converts a color representation array into 
   * a greyscale representation array
   * @param the color pixels array
   * @return the greyscale pixels array
   */
 private int [] color2gs(int [] color) {
    int blue;
    int green;
    int red;

    int []gs = new int[color.length];

    for (int i = 0; i <color.length; i++) {
      red = color[i] & 0x000000ff;
      green = color[i] & 0x0000ff00;
      blue = color[i] & 0x00ff0000;
      gs[i] = (red + green + blue)/3;
      if ( gs[i] > 255 ) { 
	gs[i] = 255; }
      if ( gs[i] < 0 ) {
	gs[i] = 0; }
    }
    return gs;
  }

 /**
   * Converts a color representation array into 
   * a greyscale representation array
   * @param the color pixels array
   * @return the greyscale pixels array
   */

 private int [] color2gs2(int [] color) {
    int grey;

    int []gs = new int[color.length];

    for (int i = 0; i <color.length; i++) {
      grey = color[i] & 0x000000ff;
     
      gs[i] = grey;
      if ( gs[i] > 255 ) { 
	gs[i] = 255; }
      if ( gs[i] < 0 ) {
	gs[i] = 0; }
    }
    return gs;
  }

  /**
   * Updates the display of the labelled image in the parameters popup
   */
  void updateDisplay(){
    int [] pixels = (int []) display.clone();
   
      displayImage = 
	parameters.createImage(new MemoryImageSource(input1.getWidth(), 
						     input1.getHeight(), 
						     pixels,0, 
						     input1.getWidth()));
      imageIcon = new ImageIcon(displayImage);
      imageLabel.setIcon(imageIcon);
     
      if (colourLabel!= null) {
        panel.remove(colourLabel);
      }
      colourLabel = new JLabel("Number of components: "+colour);
      panel.add(colourLabel,"South");

      updateParameters();
  }
}
