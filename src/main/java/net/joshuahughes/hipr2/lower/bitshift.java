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

import net.joshuahughes.hipr2.upper.ImageBitShift;


public class bitshift extends operator1DInt{

  static int number=0;
  String type = new String("BitShifting");
  JLabel shiftLabel;
  JTextField shiftBox;
  JLabel dirLabel;
  JComboBox dirBox;
  JLabel wrapLabel;
  JComboBox wrapBox;
  JButton applyButton;
  boolean direction;
  boolean wrap;

  public bitshift(){
  }

  public bitshift(JPanel panel, linkData links){
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
    
    if(direction){
      if(wrap){
	saveData = "true true" + " " + shiftBox. getText();
      }
      else{
	saveData = "true false" + " " + shiftBox. getText();
      }
    }
    else {
      if(wrap){
	saveData = "false true" + " " + shiftBox. getText();
      }
      else{
	saveData = "false false" + " " + shiftBox. getText();
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
    String dirValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    String wrapValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    int shiftValue = (int) tokenizer.nval;

    shiftBox. setText(String. valueOf(shiftValue));
    if(dirValue. equals("true")){
      direction = true;
      dirBox. setSelectedIndex(0);
    }
    else {
      direction = false;
      dirBox. setSelectedIndex(1);
    }
    if(wrapValue. equals("true")){
      wrap = true;
      wrapBox. setSelectedIndex(1);
    }
    else {
      wrap = false;
      wrapBox. setSelectedIndex(0);
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
    direction = true;
    wrap = false;
    parameters = new JFrame(name);
    panel = new JPanel();
    shiftLabel = new JLabel("Shift Value");
    shiftBox = new JTextField(5);
    shiftBox. setText("1");
    dirLabel = new JLabel("Shift");
    dirBox = new JComboBox();
    dirBox. addItem("Left");
    dirBox. addItem("Right");
    dirBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("Left") ) {
	  direction = true;
	}
	if(cb. getSelectedItem().equals("Right") ) {
	  direction = false;
	}
      }
    });
    wrapLabel = new JLabel("Wrap/No Wrap");
    wrapBox = new JComboBox();
    wrapBox. addItem("No Wrap");
    wrapBox. addItem("Wrap");
    wrapBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("No Wrap") ) {
	  wrap = false;
	}
	if(cb. getSelectedItem().equals("Wrap") ) {
	  wrap = true;
	}
      }
    });

    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 
    
    panel. add(shiftLabel);
    panel. add(shiftBox);
    panel. add(dirLabel);
    panel. add(dirBox);
    panel. add(wrapLabel);
    panel. add(wrapBox);
    panel. add(applyButton);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){

    System.out.println(name);
    
    if(getInput1()!=null){
      try {
	ImageBitShift bitshiftOp = new ImageBitShift(input1.getWidth());
	System.out.println("Input Width 1 "+getInput1().getWidth());
	output1 = new image1DInt(
                input1.getWidth(),
		input1.getHeight(),
		imageConversions.pix2gs(
                    bitshiftOp.doBitShift(
                        imageConversions.gs2pix(input1.getValues()),
			Integer. valueOf(shiftBox. getText()). intValue(),
			direction,
			wrap,
			input1.getWidth(),
			input1.getHeight(),
			0,
                        1)));
	System.out.println("Output Width 1 "+getOutput1().getWidth());
      }
      catch(NumberFormatException e){
         JOptionPane.showMessageDialog(null,("Shift Value must be an Integer"),("Error!"), JOptionPane.WARNING_MESSAGE);
         return;
      }
      propagate();
    }
  }
}
