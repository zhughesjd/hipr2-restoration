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

import net.joshuahughes.hipr2.upper.ImageBlend;


public class blending extends operator1DInt{

  static int number=0;
  String type = new String("Blending");
  JLabel sourceLabel;
  JComboBox sourceBox;
  JLabel conLabel;
  JTextField conBox;
  JLabel blendLabel;
  JTextField blendBox;
  JButton applyButton;
  boolean picture;
  int constant;
  float blend;
  

  public blending(){
  }

  public blending(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,2,1);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    if(picture){
      saveData = "true" + " " + conBox. getText() + " " + blendBox. getText();
    }
    else {
      saveData = "false" + " " + conBox. getText() + " " + blendBox. getText();
    }
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
    String picValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    int conValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    double blendValue = (double) tokenizer.nval;

    conBox. setText(String. valueOf(conValue));
    blendBox. setText(String. valueOf(blendValue));
    if(picValue.equals("true")){
      picture = true;
      sourceBox. setSelectedIndex(0);
    }
    else {
      picture = false;
      sourceBox. setSelectedIndex(1);
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
    picture = true;
    parameters = new JFrame(name);
    panel = new JPanel();

    //Create the interface components
    sourceLabel = new JLabel("Source 2");
    sourceBox = new JComboBox();
    sourceBox. addItem("Input 2");
    sourceBox. addItem("Constant");
    sourceBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("Input 2") ) {
	  picture = true;
	}
	if(cb. getSelectedItem().equals("Constant") ) {
	  picture = false;
	}
      }
    });

    conLabel = new JLabel("Constant Value");
    conBox = new JTextField(5);
    conBox. setText("0");
    blendLabel = new JLabel("Blending Factor");
    blendBox = new JTextField(5);
    blendBox. setText("0.5");
    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 

    panel. add(sourceLabel);
    panel. add(sourceBox);
    panel. add(conLabel);
    panel. add(conBox);
    panel. add(blendLabel);
    panel. add(blendBox);
    panel. add(applyButton);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
    try {
      blend = Float.valueOf( blendBox.getText()).floatValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for Blending Factor"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }

    if(getInput1()!=null){
    
      System.out.println("Input Width 1 "+getInput1().getWidth());
      if(picture){
	if(getInput2()!=null){

	  System.out.println("Input Width 2 "+getInput2().getWidth());
	  ImageBlend blendingOp = new ImageBlend(input1.getWidth(),input2.getWidth());

	  //Determine the size of the output image
	  int width, height;
	  if(input1.getWidth() > input2. getWidth()){
	      width = input2.getWidth();
	  }
	  else {
	      width = input1.getWidth();
	  }
	  if(input1.getHeight() > input2. getHeight()){
	      height = input2. getHeight();
	  }
	  else {
	      height = input1. getHeight();
	  }
	    
	  output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(blendingOp.DoBlend(imageConversions.gs2pix(input1.getValues()),imageConversions.gs2pix(input2.getValues()),blend,width,height,0,1)));
	  System.out.println("Output Width 1 "+getOutput1().getWidth());
	}
      }
      else{
	try {
	  constant = Integer. parseInt(conBox.getText());
	  
	}
	catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Constant Value must be an Integer"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
	}
	    
	ImageBlend blendingOp = new ImageBlend(input1.getWidth(),input1.getWidth());
	  
	output1 = new image1DInt(
                input1.getWidth(),
		input1.getHeight(),
		imageConversions.pix2gs(
                    blendingOp.DoBlend(
			imageConversions.gs2pix(input1.getValues()),
			constant,
			blend,
			input1.getWidth(),
			input1.getHeight(),
			0,1)));
	System.out.println("Output Width 1 "+getOutput1().getWidth());
      }
      propagate();
    }
  }
}
