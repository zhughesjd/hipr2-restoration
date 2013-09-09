package net.joshuahughes.hipr2.lower;

import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.joshuahughes.hipr2.upper.ImageNot;


public class not extends operator1DInt{

  ImageNot notOp = new ImageNot();
  static int number=0;
  String type = new String("NOT");

  public not(){
  }

  public not(JPanel panel, linkData links){
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
    
    //No parameters to save
    
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

    //No parameters to add back in

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
    JLabel label = new JLabel("No Parameters");
    panel.add(label);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    /**
     *Most of this function is left blank at the moment. When the operator
     *is ready to be added to the tableau, this function will contain the code
     *to run the operator that was written in the hipr package. One of the already
     *implemented operators is left here as a guide, however some operators could 
     *look quite different.
     */
    
    System.out.println(name);
    
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      output1 = new image1DInt(input1.getWidth(),
			       input1.getHeight(),
			       imageConversions.pix2gs(notOp.apply_invert(
			       imageConversions.gs2pix(input1.getValues()),
			       input1.getWidth(),
			       input1.getHeight())));
      System.out.println("Output Width 1 "+getOutput1().getWidth());
      
      propagate();
    }
  }
}

















