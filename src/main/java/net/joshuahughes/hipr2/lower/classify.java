package net.joshuahughes.hipr2.lower;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;
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

import net.joshuahughes.hipr2.upper.Classify;
import net.joshuahughes.hipr2.upper.Limits;


public class classify extends operator1DInt{

  
  /* image displayed in the parameters window with colors */
  int [] display;
  Image displayImage;
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JScrollPane scroll;

 //The colours which the classes will be coloured with
  Limits [] classification;
  int[] colourArray = new int[17];
  JPanel limPanel = new JPanel(); //Panel for the Limits array
  JTextField [][]classText;
  JTextField numclassText;
  JLabel numclassLabel = new JLabel("Classes");
  JLabel uplimLabel = new JLabel("Upper Limit");
  JLabel lolimLabel = new JLabel("Lower Limit");
  JLabel classlimLabel = new JLabel("Class Limits: ");
  JLabel classLabel = new JLabel("Class ");
  JLabel class1Label = new JLabel("Class 1 ");

  Classify classifyOp = new Classify();
  static int number=0;
  int numclass;
  String type = new String("Classification");

  public classify(){
  }

  public classify(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = numclassText.getText();
    for(int i = 0; i < 16 ; i ++) {
      saveData = saveData + " " + classText[0][i];
      saveData = saveData + " " + classText[1][i];
    }
    return saveData;
  }

  /**
   *Used to load all the parameters for this particular operator and reset the 
   *interface so that it contains these loaded parameters
   */

  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    
    //Grab the parameters
    int tokenType;
    tokenType = tokenizer.nextToken();
    numclass = (int) tokenizer.nval;
    numclassText.setText(String. valueOf(numclass));
    for(int i = 0; i < 16 ; i ++) {
        tokenType = tokenizer.nextToken();
        int tmplow = (int) tokenizer.nval;
        classText[0][i].setText(String. valueOf(tmplow));
        tokenType = tokenizer.nextToken();
        int tmphigh = (int) tokenizer.nval;
        classText[1][i].setText(String. valueOf(tmphigh));
    }

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
    JButton applyButton = new JButton("Apply");
    setClassInterface();
    
    scroll = new JScrollPane(imageLabel);
    /* Add others parameters and Limits array to the main panel */
    panel.add(scroll);
    panel.add(numclassLabel);
    panel.add(numclassText);
//***    panel.add(classlimLabel);
    panel.add(limPanel);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       go();
     }});

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
    
    getClassify();

    if(getInput1()!=null){
       System.out.println("Width 1 "+getInput1().getWidth());
       display = new int[input1.getWidth()*input1.getHeight()];
      
       // Sets the colours of all the different pixels according to their classes  
       int [] tmpout = new int[input1.getWidth()*input1.getHeight()];

       tmpout = classifyOp.classifyImage(
            imageConversions.gs2pix(
                input1.getValues()),
                input1.getWidth(),
                input1.getHeight(),
                classification);
       for(int i = 0; i < tmpout.length; i++){
	 display[i] = colourArray[tmpout[i]];
       }
       output1 =  new image1DInt(input1.getWidth(),input1.getHeight(),tmpout);
       updateDisplay();
       propagate();
  
       System.out.println("Output width "+getOutput1().getWidth());
     }
  }
  /**
   * Converts an array of color pixels into a greylevel representation
   * @param the array of pixels
   * @return the array of greylevels
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
   * Updates the display of the classified image in the parameters popup
   */
 void updateDisplay(){
    int [] pixels = (int []) display.clone();
   
      displayImage = 
	parameters.createImage(
                new MemoryImageSource(
                     input1.getWidth(), 
		     input1.getHeight(), 
		     pixels,
                     0, 
		     input1.getWidth()));
      imageIcon = new ImageIcon(displayImage);
      imageLabel.setIcon(imageIcon);
      updateParameters();
 }

  /**
   * Creates the interface of the classes used for classifying the image
   */
  void setClassInterface() {
     
    classText = new JTextField[2][16];
    for(int i = 0; i < 16 ; i ++) {
      classText[0][i] = new JTextField("0",3);
      classText[1][i] = new JTextField("0",3);
    }
    numclassText = new JTextField("16",2);
    class1Label.setForeground(new Color(0,0,255));
    JLabel class2Label = new JLabel("Class 2");
    class2Label.setForeground(new Color(0,255,0));
    JLabel class3Label = new JLabel("Class 3");
    class3Label.setForeground(new Color(255,0,0));
    JLabel class4Label = new JLabel("Class 4");
    class4Label.setForeground(new Color(0,255,255));
    JLabel class5Label = new JLabel("Class 5");
    class5Label.setForeground(new Color(255,255,0));
    JLabel class6Label = new JLabel("Class 6");
    class6Label.setForeground(new Color(255,0,255));
    JLabel class7Label = new JLabel("Class 7");
    class7Label.setForeground(new Color(255,255,255));
    JLabel class8Label = new JLabel("Class 8");
    class8Label.setForeground(new Color(0,0,128));
    JLabel class9Label = new JLabel("Class 9");
    class9Label.setForeground(new Color(0,128,0));
    JLabel class10Label = new JLabel("Class 10");
    class10Label.setForeground(new Color(128,0,0));
    JLabel class11Label = new JLabel("Class 11");
    class11Label.setForeground(new Color(0,128,128));
    JLabel class12Label = new JLabel("Class 12");
    class12Label.setForeground(new Color(128,128,0));
    JLabel class13Label = new JLabel("Class 13");
    class13Label.setForeground(new Color(128,0,128));
    JLabel class14Label = new JLabel("Class 14");
    class14Label.setForeground(new Color(128,128,128));
    JLabel class15Label = new JLabel("Class 15");
    class15Label.setForeground(new Color(0,128,255));
    JLabel class16Label = new JLabel("Class 16 ");
    class16Label.setForeground(new Color(255,128,0));


    //Create the Colour array for use in displaying different classes
    colourArray[0] = (new Color(0,0,0)).getRGB();
    colourArray[1] = (new Color(0,0,255)).getRGB();
    colourArray[2] = (new Color(0,255,0)).getRGB();
    colourArray[3] = (new Color(255,0,0)).getRGB();
    colourArray[4] = (new Color(0,255,255)).getRGB();
    colourArray[5] = (new Color(255,255,0)).getRGB();
    colourArray[6] = (new Color(255,0,255)).getRGB();
    colourArray[7] = (new Color(255,255,255)).getRGB();
    colourArray[8] = (new Color(0,0,128)).getRGB();
    colourArray[9] = (new Color(0,128,0)).getRGB();
    colourArray[10] = (new Color(128,0,0)).getRGB();
    colourArray[11] = (new Color(0,128,128)).getRGB();
    colourArray[12] = (new Color(128,128,0)).getRGB();
    colourArray[13] = (new Color(128,0,128)).getRGB();
    colourArray[14] = (new Color(128,128,128)).getRGB();
    colourArray[15] = (new Color(0,128,255)).getRGB();
    colourArray[16] = (new Color(255,128,0)).getRGB();

    /* Add the Limits to their panel */
    GridLayout limLayout = new GridLayout(17,3,0,0);
    limPanel.setLayout(limLayout);
    limPanel.add(classLabel);
    limPanel.add(lolimLabel);
    limPanel.add(uplimLabel);
    limPanel.add(class1Label);
    limPanel.add(classText[0][0]);
    limPanel.add(classText[1][0]);
    limPanel.add(class2Label);
    limPanel.add(classText[0][1]);
    limPanel.add(classText[1][1]);
    limPanel.add(class3Label);
    limPanel.add(classText[0][2]);
    limPanel.add(classText[1][2]);
    limPanel.add(class4Label);
    limPanel.add(classText[0][3]);
    limPanel.add(classText[1][3]);
    limPanel.add(class5Label);
    limPanel.add(classText[0][4]);
    limPanel.add(classText[1][4]);
    limPanel.add(class6Label);
    limPanel.add(classText[0][5]);
    limPanel.add(classText[1][5]);
    limPanel.add(class7Label);
    limPanel.add(classText[0][6]);
    limPanel.add(classText[1][6]);
    limPanel.add(class8Label);
    limPanel.add(classText[0][7]);
    limPanel.add(classText[1][7]);
    limPanel.add(class9Label);
    limPanel.add(classText[0][8]);
    limPanel.add(classText[1][8]);
    limPanel.add(class10Label);
    limPanel.add(classText[0][9]);
    limPanel.add(classText[1][9]);
    limPanel.add(class11Label);
    limPanel.add(classText[0][10]);
    limPanel.add(classText[1][10]);
    limPanel.add(class12Label);
    limPanel.add(classText[0][11]);
    limPanel.add(classText[1][11]);
    limPanel.add(class13Label);
    limPanel.add(classText[0][12]);
    limPanel.add(classText[1][12]);
    limPanel.add(class14Label);
    limPanel.add(classText[0][13]);
    limPanel.add(classText[1][13]);
    limPanel.add(class15Label);
    limPanel.add(classText[0][14]);
    limPanel.add(classText[1][14]);
    limPanel.add(class16Label);
    limPanel.add(classText[0][15]);
    limPanel.add(classText[1][15]);
  }

  /**
   * Gets the classes' limits from the parameters popup
   */
  void getClassify() {
     /* Creation of Limits array with Textfield values */
    try{
	numclass = new Integer(numclassText.getText()).intValue();
	if(numclass < 1 || numclass > 16){
	    JOptionPane.showMessageDialog(null,("Number of classes must be between 1 and 16"),
					 ("Error!"), JOptionPane.WARNING_MESSAGE);
	    return;
	}

        classification = new Limits[numclass];
        for(int j = 0; j < numclass ; j ++ ) {  
      
	  Limits limit = new Limits();
	  limit.lolim = new Integer(classText[0][j].getText()).intValue();
	  limit.uplim = new Integer(classText[1][j].getText()).intValue();
	  if(limit.lolim > limit.uplim){
	    JOptionPane.showMessageDialog(null,("Class "+(j+1)+
 	                        " must have high limit higher than low limit"),
				("Error!"),
				JOptionPane.WARNING_MESSAGE); 
            return;
	  }
	 
	  if(limit.lolim < 0 || limit.lolim > 255 || limit.uplim < 0 || limit.uplim > 255){
	    JOptionPane.showMessageDialog(null,("Class limits must lie between 0 and 255"),("Error!"), JOptionPane.WARNING_MESSAGE);
            return;
	  }
	  classification[j] = limit ;
        }
    }
    catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid class limits specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
  }
}
