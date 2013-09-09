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

import net.joshuahughes.hipr2.upper.Exponential;


public class exponential extends operator1DInt{

  JTextField baseText;
  JTextField scaleText;
  JTextField offsetText;
  Exponential exponentialOp = new Exponential();
  static int number=0;
  String type = new String("Exponential");

  public exponential(){
  }

  public exponential(JPanel panel, linkData links){
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
    saveData = saveData + " " + scaleText.getText() + " " + offsetText.getText();    
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
    double scaleValue = (double) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    double offsetValue = (double) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    double baseValue = (double) tokenizer.nval;

    //Get all parameters here
    scaleText.setText(String. valueOf(scaleValue));
    offsetText.setText(String. valueOf(offsetValue));
    baseText.setText(String. valueOf(baseValue));
        
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
    JLabel scaleLabel = new JLabel("Scale");
    scaleText = new JTextField("1.0",5);
    JLabel offsetLabel = new JLabel("Offset");
    offsetText = new JTextField("0",5);
    JLabel expoLabel = new JLabel("Base");
    baseText = new JTextField("1.0",5);
    JButton applyButton = new JButton("Apply");
    panel.add(scaleLabel);
    panel.add(scaleText);
    panel.add(offsetLabel);
    panel.add(offsetText);
    panel.add(expoLabel);
    panel.add(baseText);
    panel.add(applyButton);
   
    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    scaleText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    offsetText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    baseText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});

    parameters.getContentPane().add(panel);
   
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
    
    
      if(getInput1()!=null){
	try {
          System.out.println("Input Width 1 "+getInput1().getWidth());
          output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    exponentialOp.apply_expo(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
		        input1.getHeight(),
		        (new Double(baseText.getText()).doubleValue()),
		        (new Float(scaleText.getText()).floatValue()),
                        (new Float(offsetText.getText()).floatValue()))));
	}
	catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,("Wrong Format for Scale or Offset"),("Error!"), JOptionPane.WARNING_MESSAGE);
                return;
        }
        System.out.println("Output Width 1 "+getOutput1().getWidth());
        propagate();
    }
  }
}
