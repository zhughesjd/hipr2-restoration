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

import net.joshuahughes.hipr2.upper.ImageOr;


public class or extends operator1DInt{

  static int number=0;
  String type = new String("OR");
  JLabel sourceLabel;
  JComboBox sourceBox;
  JLabel conLabel;
  JTextField conBox;
  JLabel operatorLabel;
  JComboBox operatorBox;
  JButton applyButton;
  boolean picture;
  boolean op;

  public or(){
  }

  public or(JPanel panel, linkData links){
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
      if(op){
	saveData = "true true" + " " + conBox. getText();
      }
      else{
	saveData = "true false" + " " + conBox. getText();
      }
    }
    else {
      if(op){
	saveData = "false true" + " " + conBox. getText();
      }
      else{
	saveData = "false false" + " " + conBox. getText();
      }
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
    String opValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    int conValue = (int) tokenizer.nval;

    conBox. setText(String. valueOf(conValue));
    if(picValue. equals("true")){
      picture = true;
      sourceBox. setSelectedIndex(0);
    }
    else {
      picture = false;
      sourceBox. setSelectedIndex(1);
    }
    if(opValue. equals("true")){
      op = true;
      operatorBox. setSelectedIndex(1);
    }
    else {
      op = false;
      operatorBox. setSelectedIndex(0);
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
    op = false;
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
    operatorLabel = new JLabel("Operator");
    operatorBox = new JComboBox();
    operatorBox. addItem("OR");
    operatorBox. addItem("NOR");
    operatorBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("OR") ) {
	  op = false;
	}
	if(cb. getSelectedItem().equals("NOR") ) {
	  op = true;
	}
      }
    });

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
    panel. add(operatorLabel);
    panel. add(operatorBox);
    panel. add(applyButton);

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
      //And the other input should be a picture
      if(picture){
	//And there is a picture there
	if(getInput2()!=null){
	  ImageOr orOp = new ImageOr(input1.getWidth(),input2.getWidth());
	  System.out.println("Input Width 2 "+getInput2().getWidth());
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
	  //Apply the operator
	  output1 = new image1DInt(width,height,
				     imageConversions.pix2gs(orOp.doOr(
				     imageConversions.gs2pix(input1.getValues()),
				     imageConversions.gs2pix(input2.getValues()),
				     op,
				     width,
				     height,
				     0,1)));
	  System.out.println("Output Width 1 "+getOutput1().getWidth());
	  }
	}
	//Otherwise other input should be a constant
	else if(!picture){
	  try {
	  ImageOr orOp = new ImageOr(input1.getWidth(),input1.getWidth());
	  output1 = new image1DInt(input1. getWidth(), input1.getHeight(),
				   imageConversions.pix2gs(orOp.doOr(
				   imageConversions.gs2pix(input1.getValues()),
				   Integer. parseInt(conBox.getText()),
				   op,
				   input1.getWidth(),
				   input1.getHeight(),
				   0,1)));
	  System.out.println("Output Width 1 "+getOutput1().getWidth());
	  }
	   catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Constant Value must be an Integer"),("Error!"), JOptionPane.WARNING_MESSAGE);
      conBox.setText("0");
	   }
	}
    }
    
    propagate();
  }
}


















