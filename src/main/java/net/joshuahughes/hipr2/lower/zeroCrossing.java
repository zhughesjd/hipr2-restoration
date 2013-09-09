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

import net.joshuahughes.hipr2.upper.ZeroCrossing;


public class zeroCrossing extends operator1DInt{

 
  //All the components for the interface
  JComboBox sizebox = new JComboBox();
  JLabel sizelabel = new JLabel( "Select Kernel Size" );
  JLabel thetalabel = new JLabel("Standard Deviation");
  JTextField thetaval = new JTextField(5);
  JComboBox limiterbox = new JComboBox();
  JLabel limiterLabel = new JLabel("Crossing Limiter");
  JTextField limitTextField = new JTextField("0",3);
  JLabel limitLabel = new JLabel("Gradient Limit Value");
  JButton applyButton = new JButton("Apply");
  ZeroCrossing zeroCrossOp = new ZeroCrossing();
  static int number=0;
   int kersize = 3;
  boolean limiter = true;
  String type = new String("ZeroCrossing");

  public zeroCrossing(){
  }

  public zeroCrossing(JPanel panel, linkData links){
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
    saveData = " " + kersize + " " + thetaval. getText();
    if (limiter) { saveData = saveData + " true "; }
    else { saveData = saveData + " false ";}
    saveData = saveData + limitTextField.getText();
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
    kersize = (int) tokenizer.nval;
    switch( kersize ) {
        case 3: sizebox.setSelectedIndex(0); break;
        case 4: sizebox.setSelectedIndex(1); break;
        case 5: sizebox.setSelectedIndex(2); break;
        case 6: sizebox.setSelectedIndex(3); break;
        case 7: sizebox.setSelectedIndex(4); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid kernel size loaded: "+kersize),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
    tokenType = tokenizer.nextToken();
    float theta = (float) tokenizer.nval;
    thetaval.setText(String. valueOf(theta));

    tokenType = tokenizer.nextToken();
    String limitoption = (String) tokenizer.sval;
    if(limitoption. equals("false")){
      limiterbox. setSelectedIndex(0);
    }
    else {
      limiterbox. setSelectedIndex(1);
    }
    tokenType = tokenizer.nextToken();
    float limit = (float) tokenizer.nval;
    limitTextField.setText(String. valueOf(limit));

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

    sizebox. addItem("3x3 Kernel");
    sizebox. addItem("4x4 Kernel");
    sizebox. addItem("5x5 Kernel");
    sizebox. addItem("6x6 Kernel");
    sizebox. addItem("7x7 Kernel");

    limiterbox.addItem("All Crossings");
    limiterbox.addItem("Gradient Limited Crossings");
    limitTextField.setEnabled(false);
    panel. add(sizelabel);
    panel. add(sizebox);
    panel. add(thetalabel);
    panel. add(thetaval);
    thetaval. setText("0.45");
    panel.add(limiterLabel);
    panel.add(limiterbox);
    panel.add(limitLabel);
    panel.add(limitTextField);
    panel. add(applyButton);

    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
	try {
              float tmp = Float. valueOf(thetaval. getText()). floatValue();
	      if((tmp < 0.4) || (tmp > ((float)kersize / 6))){
        	  JOptionPane.showMessageDialog(null,("Theta value must be 0.4 < theta < kersize/6"), "Error!", JOptionPane.WARNING_MESSAGE);
	          return;
	      }
	      else{
	        go();
	      }
	}
	catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
                return;
        }
    }});

    limiterbox.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e ){
	JComboBox cb = (JComboBox)e.getSource();
	
	if( cb.getSelectedItem().equals("All Crossings") ) {
	  limitTextField.setEnabled(false);
	  limiter = false;}
	else {
          limitTextField.setEnabled(true);
	  limiter = true;
        }
      }});
      

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
        output1 = new image1DInt(
            input1.getWidth(),
            input1.getHeight(),
            input1.getWidth()-3,
            input1.getHeight()-3,
            imageConversions.pix2gs(
                zeroCrossOp.apply_zeroCrossing(
                    imageConversions.gs2pix(input1.getValues()),
                    input1.getWidth(),
                    input1.getHeight(),
                    kersize,
                    Float. valueOf (thetaval.getText()). floatValue(),
                    limiter,
                    (new Float(limitTextField.getText()).intValue()))));

        System.out.println("Output Width 1 "+getOutput1().getWidth());
        propagate();
      }
      catch(NumberFormatException e){
        JOptionPane.showMessageDialog(null,("Invalid Limit or Theta Parameter Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
        return;
       }
    }
  }
}
