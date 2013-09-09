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

import net.joshuahughes.hipr2.upper.ImageDifference;


public class subtraction extends operator1DInt{

  static int number=0;
  String type = new String("Subtraction");
  JLabel sourceLabel;
  JComboBox sourceBox;
  JLabel conLabel;
  JTextField conBox;
  JLabel directionLabel;
  JComboBox directionBox;
  JLabel scaleLabel;
  JTextField scaleText;
  JLabel offsetLabel;
  JTextField offsetText;
  JButton applyButton;
  boolean picture;
  boolean direction;
  float scale;
  float offset;

  public subtraction(){
  }

  public subtraction(JPanel panel, linkData links){
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
      if(direction){
	saveData = "true true";
      }
      else{
	saveData = "true false";
      }
      saveData = saveData + " " + conBox.getText() + " " + scaleText.getText() + " " + offsetText.getText();
    }
    else {
      if(direction){
	saveData = "false true";
      }
      else{
	saveData = "false false";
      }
      saveData = saveData + " " + conBox.getText() + " " + scaleText.getText() + " " + offsetText.getText();
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
    String dirValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    int conValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    scale = (float) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    offset = (float) tokenizer.nval;

    // set up interface text
    conBox. setText(String. valueOf(conValue));
    scaleText.setText(String. valueOf(scale));
    offsetText.setText(String. valueOf(offset));
    if(picValue.equals("true")){
      picture = true;
      sourceBox. setSelectedIndex(0);
    }
    else {
      picture = false;
      sourceBox. setSelectedIndex(1);
    }
    if(dirValue.equals("true")){
      direction = true;
      directionBox. setSelectedIndex(0);
    }
    else {
      direction = false;
      directionBox. setSelectedIndex(1);
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
    direction = true;
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
    
    directionLabel = new JLabel("Direction");
    directionBox = new JComboBox();
    directionBox. addItem("Source 1 - Source 2");
    directionBox. addItem("Source 2 - Source 1");
    directionBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("Source 1 - Source 2") ) {
	  direction = true;
	}
	if(cb. getSelectedItem().equals("Source 2 - Source 1") ) {
	  direction = false;
	}
      }
    });

    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 

    scaleLabel = new JLabel("Scale");
    scaleText = new JTextField("1.0",5);
    offsetLabel = new JLabel("Offset");
    offsetText = new JTextField("0.0",5);

    panel. add(sourceLabel);
    panel. add(sourceBox);
    panel. add(directionLabel);
    panel. add(directionBox);
    panel. add(conLabel);
    panel. add(conBox);
    panel. add(scaleLabel);
    panel. add(scaleText);
    panel. add(offsetLabel);
    panel. add(offsetText);
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
    try {
       scale = new Float(scaleText.getText()).floatValue();
      offset = new Float(offsetText.getText()).floatValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for Scale or Offset"),("Error!"), JOptionPane.WARNING_MESSAGE);
       scaleText.setText("1.0");
      offsetText.setText("0");
      scale = 1;
      offset = 0;
    }
    //If there is an input
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
	//And the other input should be a picture
	if(picture){
	  //And there is a picture there
	  if(getInput2()!=null){
	    ImageDifference subtractionOp = new ImageDifference(input1. getWidth(),input2.getWidth());
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
				     imageConversions.pix2gs(subtractionOp.imagedifference(
				     imageConversions.gs2pix(input1.getValues()),
				     imageConversions.gs2pix(input2.getValues()), 
				     width,
				     height,
				     offset,scale,direction)));
	     System.out.println("Output Width 1 "+getOutput1().getWidth());
	  }
	}
      //Otherwise other input should be a constant
	else if(!picture){
	  try {

	    ImageDifference subtractionOp = new ImageDifference(input1. getWidth(),input1.getWidth());
	    if (direction) {
	        output1 = new image1DInt(
                        input1. getWidth(),
                        input1.getHeight(),
	                imageConversions.pix2gs(
                            subtractionOp.imagedifference(
				     imageConversions.gs2pix(input1.getValues()),
				     input1.getWidth(),
				     input1.getHeight(),
				     offset,scale,
				     Integer. parseInt(conBox.getText()))));
	    } else {
	        output1 = new image1DInt(
                        input1. getWidth(),
                        input1.getHeight(),
	                imageConversions.pix2gs(
                            subtractionOp.imagedifference(
				     imageConversions.gs2pix(input1.getValues()),
				     input1.getWidth(),
				     input1.getHeight(),
				     offset,scale,
				     Integer. parseInt(conBox.getText()))));
	    }
	    System.out.println("Output Width 1 "+getOutput1().getWidth());
	  }
	  catch(NumberFormatException e){
              JOptionPane.showMessageDialog(null,("Constant Value must be an Integer"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
	 }
	}
	propagate();
  }
}
}
