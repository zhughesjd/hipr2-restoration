package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.Convolution;


/**
 * The convolution operator, all input/output images are
 * image2DDouble images, there are 2 inputs - an image and a kernel,
 * there is 1 output, the actual convolution algorithm code is located
 * in code/operator/convolution/Convolution.java.
 */
public class convolution extends operator2DDouble{
  /**
   * The convolution algorithm.
   */
  Convolution convolutionOp = new Convolution();
  /**
   * The textfield for entering the number of iterations in the 
   * parameters frame.
   */
  JTextField iterationsText;
  /**
   * Initialise the unique numbering system for all convolution operators.
   */
  static int number=0;
  /**
   * Set the type of this operator.
   */
  String type = new String("Convolution");
  JLabel scaleLabel;
  JTextField scaleText;
  JLabel offsetLabel;
  JTextField offsetText;
  double scale = 1.0;
  double offset = 0.0;
  int iterations;
  
            
  /**
   * No arguments constructor.
   */
  public convolution(){
  }

  /**
   * Constructor taking the parent panel for the graphical representation
   * of this operator and a representation of the links in the system.
   * @param panel the parent panel for the operatorBox
   * @param links the representation af the links in the system
   */
  public convolution(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setBox(panel,links,2,1);
    box.getIn2().setText("KR");
  }

  /**
   * Returns the number of this operator.
   * @return the number of the operator
   */
  public int getNumber(){
    return number;
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    saveData = iterationsText.getText() +" "+ scaleText.getText() + " " + offsetText.getText();
    return saveData;
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
    int value = (int) tokenizer.nval;
    iterationsText.setText((new Integer(value)).toString());

    tokenType = tokenizer.nextToken();
    scale = (double ) tokenizer.nval;
    scaleText.setText(String. valueOf(scale));

    tokenType = tokenizer.nextToken();
    offset = (double) tokenizer.nval;
    offsetText.setText(String. valueOf(offset));

    parameters.pack();
    parameters.setVisible(false);
  }
      
  /**
   * Sets up the parameters frame for this operator.
   */
  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    JLabel iterationsLabel = new JLabel("Number Of Iterations");
    iterationsText = new JTextField("1",5);
    JButton applyButton = new JButton("Apply");

    scaleLabel = new JLabel("Scale");
    scaleText = new JTextField("1.0",5);
    scale = 1.0;
    offsetLabel = new JLabel("Offset");
    offsetText = new JTextField("0.0",5);
    offset = 0.0;

    panel.add(iterationsLabel);
    panel.add(iterationsText);
    panel. add(scaleLabel);
    panel. add(scaleText);
    panel. add(offsetLabel);
    panel. add(offsetText);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
/*
    iterationsText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
*/    
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  /**
   * scales/offsets and clips an image
   */ 
  public double [][] scaleclip(int width, int height, double scale, double offset, double [][] input) {

      // scale/offset and clip
      double value;
      double [][] output = new double[width][height];                
      for (int i=0; i<width; i++) {
        for (int j=0; j<height; j++) {
                value = input[i][j]*scale + offset;
                if (value < 0) value = 0.0;
                if (value > 255.0) value = 255.0;
                output[i][j] = value;
        }}
      return output;
  }
  public int [] intscaleclip(int width, int height, double scale, double offset, int [] input) {

      // scale/offset and clip
      double value;
      int [] output = new int[width*height];
      for (int i=0; i<width*height; i++) {
                value = input[i]*scale + offset;
                if (value < 0) value = 0.0;
                if (value > 255.0) value = 255.0;
                output[i] = (int) value;
      }
      return output;
}
          
  /**
   * Runs the operator if both inputs contain data and propagates
   * it's output to the next operators in the system (operators that
   * this operator links to).
   */
  public void go(){

    System.out.println(name);

      // get and check iteration count
      try {
              iterations = (new Integer(iterationsText.getText())).intValue();
              scale = Double. valueOf (scaleText. getText()). doubleValue();
              offset = Double. valueOf (offsetText. getText()). doubleValue();
      }
      catch(NumberFormatException e){
              JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
      }
      if (iterations <= 0) {
              JOptionPane.showMessageDialog(null,("Number of iterations must be > 0"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
      }
                        
    if(getInput1()!=null && getInput2()!=null 
       && getInput1().getWidth()>=getInput2().getWidth()
       && getInput1().getHeight()>=getInput2().getHeight()){
      System.out.println("Width 1 "+getInput1().getWidth());
      System.out.println("Width 2 "+getInput2().getWidth());
      output1 = 
	new image2DDouble(
                input1.getWidth()-(input2.getWidth()*iterations)+1,
	        input1.getHeight()-(input2.getHeight()*iterations)+1,
		scaleclip(
		        input1.getWidth()-(input2.getWidth()*iterations)+1,
		        input1.getHeight()-(input2.getHeight()*iterations)+1,
		        scale,
		        offset,
		        convolutionOp.convolutionType1(
                                 input1.getValues2D(),
				 input1.getWidth(),
				 input1.getHeight(),
				 input2.getValues2D(),
				 input2.getWidth(),
				 input2.getHeight(),
                		 iterations)));
      System.out.println("Output width "+getOutput1().getWidth());
      propagate();
    }
  }
}
