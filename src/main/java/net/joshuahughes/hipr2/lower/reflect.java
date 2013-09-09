package net.joshuahughes.hipr2.lower;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import net.joshuahughes.hipr2.upper.Reflect;
import net.joshuahughes.hipr2.upper.ReflectImageCanvas;


public class reflect extends operator1DInt{

  /* Angle parameter */
  JTextField angleText = new JTextField("0",5);

  // To place component in histogram panel and main panel
  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();

  Reflect reflectOp = new Reflect();
  static int number=0;
  String type = new String("Reflection");

  /* To display the input image in the parameter's window */
  image1DInt display;
  Image displayImage;
  ReflectImageCanvas src_canvas;
  Point point = new Point(0,0); 

  public reflect(){
  }

  public reflect(JPanel panel, linkData links){
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
    saveData = angleText.getText() + " " + point.x + " " + point.y;
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
    double clickangle = (double) tokenizer.nval;
    angleText. setText(String. valueOf(clickangle));
    tokenType = tokenizer.nextToken();
    point.x = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    point.y = (int) tokenizer.nval;
         
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
    panel.setLayout(panellayout);
    JLabel angleLabel = new JLabel("Angle of Axis");
    JButton applyButton = new JButton("Apply");
    panelc. gridx = 0;
    panelc. gridy = 0;
    panellayout. setConstraints(angleLabel, panelc);
    panel.add(angleLabel);
    panelc. gridx = 1;
    panelc. gridy = 0;
    panellayout. setConstraints(angleText, panelc);
    angleText. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
          double val;
	  try {
	      val = Double. valueOf (angleText. getText()). doubleValue();
	  }
	  catch(NumberFormatException e){
	      JOptionPane.showMessageDialog(null,("Reflect: Invalid angle specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
	      return;
	  } 
	  if(val >= 0 && val <= 180){
             if(src_canvas != null) {
	       src_canvas. angle = new Double(angleText.getText()).doubleValue();
	       src_canvas. paint(src_canvas. getGraphics());
	     }
	  }
	  else{
	    JOptionPane.showMessageDialog(null,("Reflection angle must lie between 0 and 180"),("Error!"), JOptionPane.WARNING_MESSAGE);
            return;
	  }
      }
    });
    panel.add(angleText);
    panelc. gridx = 0;
    panelc. gridy = 2;
    panellayout. setConstraints(applyButton, panelc);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        doreflect();
    }});

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
   
    if(getInput1()!=null){
        
      // set default image position
      point.x = input1.getWidth()/2;
      point.y = input1.getHeight()/2;
      doreflect();
    }
  }    

   
  // routine to do actual reflection
  void doreflect() {
        
    if(getInput1()!=null){
      try {
        System.out.println("Input Width 1 "+getInput1().getWidth());
     
        display = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                input1.getValues());
        updateDisplay();
        output1 = new image1DInt(
                        input1.getWidth(),
                        input1.getHeight(),
                        imageConversions.pix2gs(
                                reflectOp.reflect_image(
                                        imageConversions.gs2pix(input1.getValues()),
                                        input1.getWidth(),
                                        input1.getHeight(),
                                        src_canvas.x,
                                        src_canvas.y,
                                        src_canvas.angle,
                                        false)));
      }
      catch(NumberFormatException e){
        JOptionPane.showMessageDialog(null,("Invalid Angle Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
        return;
      }
      System.out.println("Output Width 1 "+getOutput1().getWidth());
      propagate();
    }
  }

void updateDisplay(){
    int [] pixels = (int []) display.getValues().clone();

    for(int i=0;i<pixels.length;++i){
	//Get value and do scaling and offset
	int grey = (int)pixels[i];
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
     if(src_canvas != null) {
        panel.remove(src_canvas);
     }
     src_canvas = new ReflectImageCanvas(displayImage);
     src_canvas.angle = new Double(angleText.getText()).doubleValue();
     panelc. gridx = 0;
     panelc. gridy = 1;
     panellayout. setConstraints(src_canvas, panelc);
     panel.add(src_canvas);
     MyMouseListener mousel = new MyMouseListener();
     src_canvas. addMouseListener(mousel);
     src_canvas. addMouseMotionListener(mousel);
     src_canvas.x = point.x;
     src_canvas.y = point.y;
     updateParameters();
}

  class MyMouseListener extends MouseInputAdapter {

    public void mousePressed(MouseEvent e) {
      ReflectImageCanvas ric = (ReflectImageCanvas)e. getSource();
      int x = e. getX();
      int y = e. getY();
      point.x = x;
      point.y = y;
      src_canvas.x = x;
      src_canvas.y = y;
      ric. setLineCenter(x ,y);
    }
  }

}
