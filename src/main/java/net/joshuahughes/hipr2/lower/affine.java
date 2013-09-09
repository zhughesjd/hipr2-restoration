package net.joshuahughes.hipr2.lower;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import net.joshuahughes.hipr2.upper.Affine;


public class affine extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JTextField a11 = new JTextField("1",4);
  JTextField a12 = new JTextField("0",4);
  JTextField a21 = new JTextField("0",4);
  JTextField a22 = new JTextField("1",4);
  JTextField b1 = new JTextField("0",4);
  JTextField b2 = new JTextField("0",4);
  static int number=0;
  float [] a_array;     
  float [] b_array;
  String type = new String("AffineTransformation");

  public affine(){
  }

  public affine(JPanel panel, linkData links){
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
    saveData = a11.getText() + " " + a12.getText() + " " + a21.getText()
         + " " + a22.getText() + " " + b1.getText() + " " + b2.getText();
    return saveData;
  }

  /**
   *Used to load all the parameters for this particular operator and reset the 
   *interface so that it contains these loaded parameters
   */

  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    
    //Grab the parameters
    int tokenType;
    double value;
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    a11. setText(String. valueOf(value));
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    a12. setText(String. valueOf(value));
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    a21. setText(String. valueOf(value));
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    a22. setText(String. valueOf(value));
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    b1. setText(String. valueOf(value));
    tokenType = tokenizer.nextToken();
    value = (double) tokenizer.nval;
    b2. setText(String. valueOf(value));
    
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
    JLabel aLabel = new JLabel("A:");
    JLabel bLabel = new JLabel("B:");
    JButton applyButton = new JButton("Apply");

    panel.setLayout(panellayout);

    panelc.gridx = 0;
    panelc.gridy = 0;
    panellayout.setConstraints(aLabel, panelc);
    panel.add(aLabel);    
    
    panelc.gridx = 1;
    panelc.gridy = 0;
    panellayout.setConstraints(a11, panelc);
    panel.add(a11);    
    panelc.gridx = 2;
    panelc.gridy = 0;
    panellayout.setConstraints(a12, panelc);
    panel.add(a12);    
    panelc.gridx = 1;
    panelc.gridy = 1;
    panellayout.setConstraints(a21, panelc);
    panel.add(a21);    
    panelc.gridx = 2;
    panelc.gridy = 1;
    panellayout.setConstraints(a22, panelc);
    panel.add(a22);

    panelc.gridx = 3;
    panelc.gridy = 0;
    panellayout.setConstraints(bLabel, panelc);
    panel.add(bLabel);    

    panelc.gridx = 4;
    panelc.gridy = 0;
    panellayout.setConstraints(b1, panelc);
    panel.add(b1);
    panelc.gridx = 4;
    panelc.gridy = 1;
    panellayout.setConstraints(b2, panelc);
    panel.add(b2);

    panelc.gridx = 5;
    panelc.gridy = 0;
    panellayout.setConstraints(applyButton, panelc);
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

    /* Arrays  used for the affine transformation: a11,a12,a21,a22   b1,b2 */
    try {
      a_array = new float[4];     
      b_array = new float[2];
      a_array[0] = new Float(a11.getText()).floatValue();
      a_array[1] = new Float(a12.getText()).floatValue();
      a_array[2] = new Float(a21.getText()).floatValue();
      a_array[3] = new Float(a22.getText()).floatValue();
      b_array[0] = new Float(b1.getText()).floatValue();
      b_array[1] = new Float(b2.getText()).floatValue();
      }
      catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
      }
      System.out.println(name);
    
  
      if(getInput1()!=null){
        Affine affineOp = new Affine(input1.getWidth(),input1.getHeight());
        System.out.println("Input Width 1 "+getInput1().getWidth());
        output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(affineOp.affine_transform(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),a_array,b_array)));
        System.out.println("Output Width 1 "+getOutput1().getWidth());
        propagate();
    }
  }
}
