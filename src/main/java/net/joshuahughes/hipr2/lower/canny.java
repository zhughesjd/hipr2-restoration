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

import net.joshuahughes.hipr2.upper.Canny;


public class canny extends operator1DInt{

  Canny cannyOp = new Canny();
  //All the components for the interface
  JComboBox sizebox = new JComboBox();
  JLabel sizelabel = new JLabel( "Select Kernel Size" );
  JLabel thetalabel = new JLabel("Standard Deviation");
  JTextField thetaval = new JTextField(5);
  JLabel highlabel = new JLabel("Upper Threshold");
  JTextField highthresh = new JTextField(5);
  JLabel lowlabel = new JLabel("Lower Threshold");
  JTextField lowthresh = new JTextField(5);
  JButton applyButton = new JButton("Apply");

  static int number=0;
  int kersize = 3;
  String type = new String("CannyEdgeDetector");

  public canny(){
  }

  public canny(JPanel panel, linkData links){
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
    
    saveData = 
      thetaval. getText() + " " +
      highthresh. getText() + " " +
      lowthresh. getText() + " " + kersize;

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
    float thetaValue = (float) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    int highValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    int lowValue = (int) tokenizer.nval;
    tokenType = tokenizer.nextToken();
    int kersize = (int) tokenizer.nval;
    //Reset the components in the interface
    thetaval. setText(String. valueOf(thetaValue));
    highthresh. setText(String. valueOf(highValue));
    lowthresh. setText(String. valueOf(lowValue));
    sizebox. setSelectedIndex((kersize - 3));
    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }

  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    
    sizebox. addItem("3x3 Kernel");
    sizebox. addItem("4x4 Kernel");
    sizebox. addItem("5x5 Kernel");
    sizebox. addItem("6x6 Kernel");
    sizebox. addItem("7x7 Kernel");

    panel. add(sizelabel);
    panel. add(sizebox);
    panel. add(thetalabel);
    panel. add(thetaval);
    thetaval. setText("0.45");
    panel. add(highlabel);
    panel. add(highthresh);
    highthresh. setText("10");
    panel. add(lowlabel);
    panel. add(lowthresh);
    lowthresh. setText("0");
    panel. add(applyButton);


    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
	try {
	float tmp = Float. valueOf(thetaval. getText()). floatValue();
	if((tmp < 0.4) || (tmp > ((float)kersize / 6))){
	  JOptionPane.showMessageDialog(null,("Theta value must be 0.4 < theta < kersize/6"),
					"Error!", JOptionPane.WARNING_MESSAGE);
	  thetaval.setText("0.45");
	}
	else{
	  go();
	}
	}
	 catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
	thetaval.setText("0.45");
	 }
      }
    });

    sizebox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("3x3 Kernel") ) {
	  kersize = 3;
	}
	if(cb. getSelectedItem().equals("4x4 Kernel") ) {
	  kersize = 4;
	}
	if(cb. getSelectedItem().equals("5x5 Kernel") ) {
	  kersize = 5;
	}
	if(cb. getSelectedItem().equals("6x6 Kernel") ) {
	  kersize = 6;
	}
	if(cb. getSelectedItem().equals("7x7 Kernel") ) {
	  kersize = 7;
	}
      }
    });

    
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    System.out.println(name);
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      try {
      output1 = new image1DInt(input1.getWidth(),
			       input1.getHeight(),
			       imageConversions.pix2gs(cannyOp.apply_canny(
			       imageConversions.gs2pix(input1.getValues()),
			       input1.getWidth(),
			       input1.getHeight(),kersize,
			       Float. valueOf (thetaval.getText()). floatValue(),
			       Integer. valueOf(lowthresh. getText()). intValue(),
			       Integer. valueOf(highthresh. getText()). intValue(),
			       1,0)));
      System.out.println("Output width "+getOutput1().getWidth());
      propagate();
      }
       catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
      highthresh.setText("10");
      lowthresh.setText("0");
       }
    }
  }
}

















