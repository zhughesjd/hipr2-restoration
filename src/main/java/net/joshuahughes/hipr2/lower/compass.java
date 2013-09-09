package net.joshuahughes.hipr2.lower;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.Compass;


public class compass extends operator1DInt{

  JComboBox kerneltype = new JComboBox();
  JTextField scaleText = new JTextField("1.0",5);
  JTextField offsetText = new JTextField("0",5);
  Compass compassOp = new Compass();
  static int number=0;
  String type = new String("Compass");
  int kernelvalue = 0;

  public compass(){
  }

  public compass(JPanel panel, linkData links){
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
    saveData = " " + kernelvalue;
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
    kernelvalue = (int) tokenizer.nval;
    switch( kernelvalue ) {
        case 0: kerneltype.setSelectedIndex(0); break;
        case 1: kerneltype.setSelectedIndex(1); break;
        case 2: kerneltype.setSelectedIndex(2); break;
        case 3: kerneltype.setSelectedIndex(3); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid kernel loaded: "+kernelvalue),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
                
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
   
    JLabel kernelLabel = new JLabel("Kernel type: ");
    JLabel scale = new JLabel("Scale");
    JLabel offset = new JLabel("Offset");
    JButton applyButton = new JButton("Apply");

    panel.add(kernelLabel);
    kerneltype.addItem("Prewitt");
    kerneltype.addItem("Sobel");
    kerneltype.addItem("Kirsch");
    kerneltype.addItem("Robinson");
    panel.add(kerneltype);
    panel.add(scale);
    panel.add(scaleText);
    panel.add(offset);
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
      
      if (kerneltype.getSelectedItem().equals("Prewitt")) {
	kernelvalue = 0;}
      else if (kerneltype.getSelectedItem().equals("Sobel")) {
	kernelvalue = 1;}
      else if (kerneltype.getSelectedItem().equals("Kirsch")) {
	kernelvalue = 2;}
      else if (kerneltype.getSelectedItem().equals("Robinson")) {
	kernelvalue = 3;}

      if(getInput1()!=null){
        System.out.println("Input Width 1 "+getInput1().getWidth());
        output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                input1.getWidth()-2,
                input1.getHeight()-2,
                imageConversions.pix2gs(
                    compassOp.apply_compass(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        kernelvalue,
                        false,
                        (new Float(scaleText.getText()).floatValue()),
                        (new Integer(offsetText.getText()).intValue())
                    ).image1));
        System.out.println("Output Width 1 "+getOutput1().getWidth()); 
        propagate();
      }
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Format Parameters"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
  }
}
  

















