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

import net.joshuahughes.hipr2.upper.Rotate;


public class rotate extends operator1DInt{

  JTextField angleText = new JTextField("0.0",5);
  Rotate rotateOp = new Rotate();
  static int number=0;
  String type = new String("Rotation");

  public rotate(){
  }

  public rotate(JPanel panel, linkData links){
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
    saveData = angleText.getText();
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
    double ang = (double) tokenizer.nval;
    angleText. setText(String. valueOf(ang));

    //Get all parameters here

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
    JLabel angleLabel = new JLabel("Angle");
    panel.add(angleLabel);
    JButton applyButton = new JButton("Apply");
    panel.add(angleText);
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

      if(getInput1()!=null){
        System.out.println("Input Width 1 "+getInput1().getWidth());
        try {
          output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(rotateOp.rotate(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),(new Double(angleText.getText()).doubleValue()))));
        }
        catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Angle Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
        }
        System.out.println("Output Width 1 "+getOutput1().getWidth());
      }
      propagate();
  }
}
  

















