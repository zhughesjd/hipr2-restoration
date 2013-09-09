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

import net.joshuahughes.hipr2.upper.Log;


public class lapOfGauss extends operator1DInt{


  JLabel kernelLabel = new JLabel("Kernel Size");
  JTextField kernelText = new JTextField("7",5);
  JLabel thetaLabel = new JLabel("Theta Value");
  JTextField thetaText = new JTextField("0.6",5);
  JButton applyButton = new JButton("Apply");
  JLabel scaleLabel = new JLabel("Output Display Scale Value");
  JTextField scaleText = new JTextField("1.0",5);
  JLabel offsetLabel = new JLabel("Output Display Offset Value");
  JTextField offsetText = new JTextField("0.0",5);
  static int number=0;
  String type = new String("LaplacianOfGaussian");
  int kernelsize;
  double theta;
  public lapOfGauss(){
  }

  public lapOfGauss(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
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
    kernelsize = (int) tokenizer.nval;
    kernelText.setText(String. valueOf(kernelsize));
    tokenType = tokenizer.nextToken();
    theta = (double) tokenizer.nval;
    thetaText.setText(String. valueOf(theta));
    tokenType = tokenizer.nextToken();
    double scale = (double) tokenizer.nval;
    scaleText.setText(String. valueOf(scale));
    tokenType = tokenizer.nextToken();
    double offset = (double) tokenizer.nval;
    offsetText.setText(String. valueOf(offset));

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = kernelsize + " " + theta + " " + scaleText.getText() + " " + offsetText.getText();
    return saveData;
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
    panel.add(kernelLabel);
    panel.add(kernelText);
    panel.add(thetaLabel);
    panel.add(thetaText);
    panel.add(scaleLabel);
    panel.add(scaleText);
    panel.add(offsetLabel);
    panel.add(offsetText);
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
    try {

      // get parameters
      try {
          kernelsize = new Integer(kernelText.getText()).intValue();
          theta = new Double(thetaText.getText()).doubleValue();
      }
      catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
      }

      // process image
      if(getInput1()!=null){
        Log logOp = new Log(kernelsize,theta);
        System.out.println("Input Width 1 "+getInput1().getWidth());
        if (!(logOp.kernelValid(theta))) {
	   JOptionPane.showMessageDialog(null,"Theta and size cannot generate a valid kernel. 0.5 < Theta < 0.09*size must hold.","Invalid kernel",JOptionPane.WARNING_MESSAGE);
	   return;
        }
        else {
          output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    logOp.log_image(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        (new Double(scaleText.getText()).doubleValue()),
                        (new Double(offsetText.getText()).doubleValue()))));
          System.out.println("Output width "+getOutput1().getWidth());
          propagate();
	}
      }
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
  }
}
