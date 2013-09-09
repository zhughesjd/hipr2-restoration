package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 *SClassifyScreen is the user interface to the Classify
 *algorithm.  It is run as an applet
 *embedded in the file classify.htm 
 *@author Timothy Sharman
 */

public class SClassifyScreen extends VisionApplet1 {

  //The operator class for performing the classification. It's a thread
  Classify classify;

  int numClasses = 16;

  //Co-ordiante points for finding the intensity value at a selected point
  int clickx, clicky, actualx, actualy;
  
  //An array which the textfields are stored in
  JTextField [][] boxes = new JTextField[2][16];
  
  //The arrays used to store the parameters for the classification
  Limits [] limArray;
  
  //The colours which the classes will be coloured with
  int[] colourArray = new int[17];
  
  //The listeners for the GUI
  private ButtonListener startbl = new ButtonListener();
  private ButtonListener abortbl = new ButtonListener();

  //A panel onto which the classification list will be put
  private JPanel limPanel = new JPanel();
  private GridLayout limLayout = new GridLayout(17,3,0,0);

  //A scroll pane into which the above panel will be added
  private JScrollPane scroll;

  //the components for the interface
  private JButton startbutton = new JButton("Classify");
  private JButton abortbutton = new JButton("Stop");
  private JTextField time_taken = new JTextField(20);
  private JLabel time = new JLabel("Time");
  private JLabel classLabel = new JLabel("Class");
  private JLabel lowLabel = new JLabel("Lower Limit");
  private JLabel hiLabel = new JLabel("Upper Limit");
  private JTextField classes = new JTextField(5);
  private JLabel classNum = new JLabel("Number of Classes");
  private JLabel listLabel = new JLabel("Class Limits :");
  private JLabel box1 = new JLabel("Class 1");
  private JLabel box2 = new JLabel("Class 2");
  private JLabel box3 = new JLabel("Class 3");
  private JLabel box4 = new JLabel("Class 4");
  private JLabel box5 = new JLabel("Class 5");
  private JLabel box6 = new JLabel("Class 6");
  private JLabel box7 = new JLabel("Class 7");
  private JLabel box8 = new JLabel("Class 8");
  private JLabel box9 = new JLabel("Class 9");
  private JLabel box10 = new JLabel("Class 10");
  private JLabel box11 = new JLabel("Class 11");
  private JLabel box12 = new JLabel("Class 12");
  private JLabel box13 = new JLabel("Class 13");
  private JLabel box14 = new JLabel("Class 14");
  private JLabel box15 = new JLabel("Class 15");
  private JLabel box16 = new JLabel("Class 16");

  public void add_extra() {

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
        
    //Create the textfields in the list    
    for(int j = 0; j < 16; j++){
      for(int i = 0; i < 2; i++){
	boxes[i][j] = new JTextField("0",3);
      }
    }

    //initialise the operator constructors
    classify = new Classify();
    
    //Add the list to their panel
    
    limPanel. setLayout(limLayout);
    
    limPanel. add(classLabel);
    limPanel. add(lowLabel);
    limPanel. add(hiLabel);
    limPanel. add(box1);
    box1. setForeground(new Color(0,0,255));
    limPanel. add(boxes[0][0]);
    limPanel. add(boxes[1][0]);
    limPanel. add(box2);
    box2. setForeground(new Color(0,255,0));
    limPanel. add(boxes[0][1]);
    limPanel. add(boxes[1][1]);
    limPanel. add(box3);
    box3. setForeground(new Color(255,0,0));
    limPanel. add(boxes[0][2]);
    limPanel. add(boxes[1][2]);
    limPanel. add(box4);
    box4. setForeground(new Color(0,255,255));
    limPanel. add(boxes[0][3]);
    limPanel. add(boxes[1][3]);
    limPanel. add(box5);
    box5. setForeground(new Color(255,255,0));
    limPanel. add(boxes[0][4]);
    limPanel. add(boxes[1][4]);
    limPanel. add(box6);
    box6. setForeground(new Color(255,0,255)); 
    limPanel. add(boxes[0][5]);
    limPanel. add(boxes[1][5]);
    limPanel. add(box7);
    box7. setForeground(new Color(255,255,255));
    limPanel. add(boxes[0][6]);
    limPanel. add(boxes[1][6]);
    limPanel. add(box8);
    box8. setForeground(new Color(0,0,128));
    limPanel. add(boxes[0][7]);
    limPanel. add(boxes[1][7]);
    limPanel. add(box9);
    box9. setForeground(new Color(0,128,0));
    limPanel. add(boxes[0][8]);
    limPanel. add(boxes[1][8]);
    limPanel. add(box10);
    box10. setForeground(new Color(128,0,0));
    limPanel. add(boxes[0][9]);
    limPanel. add(boxes[1][9]);
    limPanel. add(box11);
    box11. setForeground(new Color(0,128,128));
    limPanel. add(boxes[0][10]);
    limPanel. add(boxes[1][10]);
    limPanel. add(box12);
    box12. setForeground(new Color(128,128,0));
    limPanel. add(boxes[0][11]);
    limPanel. add(boxes[1][11]);
    limPanel. add(box13);
    box13. setForeground(new Color(128,0,128));
    limPanel. add(boxes[0][12]);
    limPanel. add(boxes[1][12]);
    limPanel. add(box14);
    box14. setForeground(new Color(128,128,128));
    limPanel. add(boxes[0][13]);
    limPanel. add(boxes[1][13]);
    limPanel. add(box15);
    box15. setForeground(new Color(0,128,255));
    limPanel. add(boxes[0][14]);
    limPanel. add(boxes[1][14]);
    limPanel. add(box16);
    box16. setForeground(new Color(255,128,0));
    limPanel. add(boxes[0][15]);
    limPanel. add(boxes[1][15]);

    //Now add the list panel to the scroll pane

    scroll = new JScrollPane(limPanel, 
			     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll. setPreferredSize(new Dimension(400, 200));

    //Now add eveythind to the mid panel
    
    midc. weighty = 0.5;
    
    midc. gridx = 0;
    midc. gridy = 0;
    midlayout. setConstraints(classNum, midc);
    mid. add(classNum);
    
    midc. gridx = 1;
    midc. gridy = 0;
    midlayout. setConstraints(classes, midc);
    classes. setText("16");
    mid. add(classes);

    midc. gridx = 0;
    midc. gridy = 1;
    midlayout. setConstraints(listLabel,midc);
    mid. add(listLabel);

    midc. gridx = 1;
    midc. gridy = 1;
    midlayout. setConstraints(scroll, midc);
    mid. add(scroll);

    midc. gridx = 0;
    midc. gridy = 2;
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
    midc. gridy = 3;
    midlayout. setConstraints(time, midc);   
    mid. add(time);
    
    midc. gridx = 1;
    midlayout. setConstraints(time_taken, midc);
    time_taken. setEditable(false);
    mid. add(time_taken);

    mid. repaint();
    
    //This should hopefully load a different default    
    
    input. setText("pap2.gif");
    load_image. doClick();

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
	try{
	 numClasses = Integer. valueOf (classes.getText()). intValue();
	 if(numClasses < 1 || numClasses > 16){
	   JOptionPane.showMessageDialog(null,("Number of classes must be between 1 and 16"),
					 ("Error!"), JOptionPane.WARNING_MESSAGE);    
	 }
	 else{
	   time_msec = System.currentTimeMillis();
	   limArray = new Limits[numClasses];
	   if(getLimits()){
	     dest_1d = classify. classifyImage(src_1d, i_w, i_h, limArray);
	     setColours();
	     time_msec = System.currentTimeMillis() - time_msec;
	     time_taken. setText(new Long(time_msec).toString()+" msecs");
	     dest = createImage(new MemoryImageSource(d_w,d_h,dest_1d,0,d_w));

	     dest_canvas. updateImage(dest);
	     PixelListener brpl = new PixelListener();
	     dest_canvas. addMouseMotionListener(brpl);
	   }
	 }
	 
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid parameters specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE); 
	}     
      }
      
      else if( b == abortbutton ) {
	
	time_taken. setText("");
	
      }
      
    }
  }

  private boolean getLimits(){
    int lo;
    int hi;

    //Grabs all the limits out of the boxes and puts them in an array

    for(int j = 0; j < numClasses; j++){
	try{
	  Limits tmp = new Limits();
	  lo = Integer. valueOf (boxes[0][j].getText()). intValue();
	  hi = Integer. valueOf (boxes[1][j].getText()). intValue();
	  if(lo > hi){
	    JOptionPane.showMessageDialog(null,("Class "+(j+1)+
	                                        " must have high limit"+
     						" higher than low limit"),
					  ("Error!"),
                                           JOptionPane.WARNING_MESSAGE);	
	    return false;
          }

	  if(lo < 0 || lo > 255 || hi < 0 || hi > 255){
	    JOptionPane.showMessageDialog(null,("Class limits must lie between 0 and 255"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);   
	    return false;
	  }
	  tmp. lolim = lo;
	  tmp. uplim = hi;
	  limArray[j] = tmp;
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid class limits specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);    
	  return false;
	}
    }
    return true;
  }
    

  //Sets the colours of all the different pixels according to their classes
  private void setColours(){
    
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = colourArray[dest_1d[i]];
    }

  }

  public void set_src_image(int [] input_img , int w, int h, String name) {
    
    //create a new classification operator
    classify = new Classify(); 
    
    set_src_image2(input_img, w, h, name);
  }
  
  /**
   *Used by the browser to find out what the applet is for
   *
   *@return the function of the applet
   */
  public String getAppletInfo() {
    return "An applet for performing image classification. Written by Timothy Sharman";
  }
}
