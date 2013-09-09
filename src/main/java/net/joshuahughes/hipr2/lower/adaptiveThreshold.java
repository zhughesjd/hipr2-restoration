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

import net.joshuahughes.hipr2.upper.AdapThresh;


public class adaptiveThreshold extends operator1DInt{

  AdapThresh adapthreshOp = new AdapThresh();
  static int number=0;
  String type = new String("AdaptiveThresholding");
  JLabel sizeLabel;
  JTextField sizeBox;
  JLabel functionLabel;
  JComboBox functionBox;
  JLabel conLabel;
  JTextField conBox;
  JButton applyButton;
  int function;
  int constant;
  int size;

  public adaptiveThreshold(){
  }

  public adaptiveThreshold(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
    box.getOut1().setText("BINOUT1");
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    saveData = sizeBox. getText() + " " + 
               function + " " + 
               conBox. getText();
    
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
    int sizeValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    int funValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    int conValue = (int) tokenizer.nval;

    sizeBox. setText(String. valueOf(sizeValue));
    conBox. setText(String. valueOf(conValue));
    functionBox. setSelectedIndex(funValue);

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
    function = 0;
    parameters = new JFrame(name);

    //Change from here
    panel = new JPanel();

    sizeLabel = new JLabel("Neighbourhood Size");
    sizeBox = new JTextField(5);
    sizeBox. setText("7");

    functionLabel = new JLabel("Threshold Function");
    functionBox = new JComboBox();
    functionBox. addItem("Mean");
    functionBox. addItem("Median");
    functionBox. addItem("Mean of Min & Max");
    functionBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("Mean") ) {
	  function = 0;
	}
	if(cb. getSelectedItem().equals("Median") ) {
	  function = 1;
	}
	if(cb. getSelectedItem().equals("Mean of Min & Max") ) {
	  function = 2;
	}
      }
    });

    conLabel = new JLabel("Constant Value");
    conBox = new JTextField(5);
    conBox. setText("7");

    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 

    panel. add(sizeLabel);
    panel. add(sizeBox);
    panel. add(functionLabel);
    panel. add(functionBox);
    panel. add(conLabel);
    panel. add(conBox);
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
      try {
	constant = Integer. valueOf(conBox. getText()). intValue();
	size = Integer. valueOf(sizeBox. getText()). intValue();
      }
      catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
	  sizeBox.setText("7");
	  conBox.setText("7");
	  size = (int) 7;
	  constant = (int) 7;
      }
      System.out.println("Input Width 1 "+getInput1().getWidth());
      if(function == 0){
	output1 = new image1DInt(input1.getWidth(),
				 input1.getHeight(),
				 imageConversions.pix2gs(adapthreshOp.mean_thresh(
                                 imageConversions.gs2pix(input1.getValues()),
				 input1.getWidth(),
				 input1.getHeight(),
				 size,
				 constant)));
      }
      else if(function == 1){
	output1 = new image1DInt(input1.getWidth(),
				 input1.getHeight(),
				 imageConversions.pix2gs(adapthreshOp.median_thresh(
                                 imageConversions.gs2pix(input1.getValues()),
				 input1.getWidth(),
				 input1.getHeight(),
				 size, constant)));
      }
      
      else if(function == 2){
	output1 = new image1DInt(input1.getWidth(),
				 input1.getHeight(),
				 imageConversions.pix2gs(adapthreshOp.meanMaxMin_thresh(
                                 imageConversions.gs2pix(input1.getValues()),
				 input1.getWidth(),
				 input1.getHeight(),
				 size, constant)));
      }
     System.out.println("Output Width 1 "+getOutput1().getWidth()); 
      propagate();
    }
  }
}

















