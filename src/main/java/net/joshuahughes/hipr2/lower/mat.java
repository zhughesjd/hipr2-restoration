package net.joshuahughes.hipr2.lower;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.Mat;


public class mat extends operator1DInt{

  JTextField expoText;
  JTextField scaleText;
  JTextField offsetText;
  JCheckBox choice = new JCheckBox("Threshold", false);
  Mat matOp = new Mat();
  static int number=0;
  boolean thresh = false;
  String type = new String("MedialAxisTransform");

  public mat(){
  }

  public mat(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
    box.getIn1().setText("BININ1");
  }

  /**
   * Loads the required parameters (number of iterations)
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{

    //Grab the parameters
    int tokenType;
    tokenType = tokenizer.nextToken();
    String threshoption = (String) tokenizer.sval;
    if(threshoption. equals("false")){
        thresh = false;
        choice.setSelected(false);
    }
    else {
        thresh = true;
        choice.setSelected(true);
    }
    tokenType = tokenizer.nextToken();
    double scaleValue = (double) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    double offsetValue = (double) tokenizer.nval;

    //Get all parameters here
    scaleText.setText(String. valueOf(scaleValue));
    offsetText.setText(String. valueOf(offsetValue));

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
    if (thresh) { saveData = "true"; }
    else { saveData = "false"; }
    saveData = saveData + " " + scaleText.getText() + " " + offsetText.getText();
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
    JLabel scaleLabel = new JLabel("Scale");
    scaleText = new JTextField("1.0",5);
    JLabel offsetLabel = new JLabel("Offset");
    offsetText = new JTextField("0",5);
    JLabel choiceLabel = new JLabel("Threshold");
    JButton applyButton = new JButton("Apply");
    panel.add(scaleLabel);
    panel.add(scaleText);
    panel.add(offsetLabel);
    panel.add(offsetText);
    //panel.add(choiceLabel);
    panel.add(choice);
    
    panel.add(applyButton);
   
    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});

     choice.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       	if(choice. isSelected()){
	  thresh = true;
	}
	else {
	  thresh = false;
	}
     }});
    parameters.getContentPane().add(panel);
   
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){

    System.out.println(name);
    
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      try {
          output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.binary2gs(
                    matOp.mat_image(
                        imageConversions.gs2binary(
                            input1.getValues(),
                            input1.getWidth(),
                            input1.getHeight()),
                        (new Double(scaleText.getText()).doubleValue()),
                        (new Double(offsetText.getText()).doubleValue()),
                        thresh)));
          System.out.println("Output width "+getOutput1().getWidth());
          propagate();
      }
      catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Scale or Offset Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
       }
     }
  }
}
