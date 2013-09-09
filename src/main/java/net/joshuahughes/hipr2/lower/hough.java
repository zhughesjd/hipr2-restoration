package net.joshuahughes.hipr2.lower;

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

import net.joshuahughes.hipr2.upper.Hough;
import net.joshuahughes.hipr2.upper.TwoImages;


public class hough extends operator1DInt{

  Hough houghOp = new Hough();
  static int number=0;
  String type = new String("Hough");
  JLabel thresholdLabel;
  JTextField thresholdBox;
  JButton applyButton;

  public hough(){
  }

  public hough(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,2);
    box.getOut1().setText("LINES");
    box.getOut2().setText("TRANSFORM");
    box.getIn1().setText("BININ1");
    box.setBounds(0,0,box.outs.getPreferredSize().width,80);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    saveData = thresholdBox. getText();
    
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
    float threshValue = (float) tokenizer.nval;
    
    thresholdBox. setText(String. valueOf(threshValue));

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
    //Add the interface components
    panel = new JPanel();
    thresholdLabel = new JLabel("Threshold Value: ");
    thresholdBox = new JTextField(5);
    thresholdBox. setText("0.5");
    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 
    //Add the components to the panel
    panel. add(thresholdLabel);
    panel. add(thresholdBox);
    panel. add(applyButton);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    TwoImages answers;
    float thresh = 0;
    System.out.println(name);
    if(getInput1()!=null){

      System.out.println("Input Width 1 "+getInput1().getWidth());
      //Grab the threshold
      try {
      thresh = Float. valueOf(thresholdBox. getText()). floatValue();
      }
      catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Threshold Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
      thresh = 0;
      thresholdBox.setText("0");
      }
      //Calculate the two output images
      answers = houghOp. apply_hough(imageConversions.gs2pix(input1.getValues()), 
				     input1.getWidth(), 
				     input1.getHeight(), 
				     thresh, 1,0);      
      
      //Send out the first output image
      output1 = new image1DInt(input1.getWidth(),
			       input1.getHeight(), 
			       imageConversions.pix2gs(answers.image2));
      
      //Size of hough image must be calculated for proper viewing
      int hough_w = 500;
      int hough_h = 2* (int) (Math.max(
				       input1.getHeight(),
				       input1.getWidth())*Math.sqrt(2));

      //Send out the second output image
      output2 = new image1DInt(hough_w,hough_h,
			       imageConversions.pix2gs(answers.image1));
      
      System.out.println("Output width 1 "+getOutput1().getWidth());
      System.out.println("Output width 2 "+getOutput2().getWidth());
      
      propagate();
    }
    
  }
}

















