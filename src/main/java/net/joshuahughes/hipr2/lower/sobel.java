package net.joshuahughes.hipr2.lower;

import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.joshuahughes.hipr2.upper.Sobel;


public class sobel extends operator1DInt{

  Sobel sobelOp = new Sobel();
  static int number=0;
  String type = new String("SobelEdgeDetector");

  public sobel(){
  }

  public sobel(JPanel panel, linkData links){
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
    parameters = new JFrame(name);
    panel = new JPanel();
    JLabel label = new JLabel("No Parameters");
    panel.add(label);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    System.out.println(name);
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      output1 = new image1DInt(input1.getWidth(),
			       input1.getHeight(),
			       imageConversions.pix2gs(sobelOp.apply_sobel(imageConversions.gs2pix(input1.getValues()),
					       input1.getWidth(),
					       input1.getHeight(),
					       1,0)));
      System.out.println("Output width "+getOutput1().getWidth());
     
      propagate();
    }
  }
  
}

















