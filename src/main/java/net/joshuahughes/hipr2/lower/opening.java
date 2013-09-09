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

import net.joshuahughes.hipr2.upper.Open;


public class opening extends operator2DInt{

  JLabel iterLabel = new JLabel("Depth:");
  JTextField iterText = new JTextField("1",5);
  JButton applyButton = new JButton("Apply");
  Open openOp = new Open();
  static int number=0;
  int iteration;
  String type = new String("Opening");

  public opening(){
  }

  public opening(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,2,1);
    box.getIn2().setText("K2");
    box.getOut1().setText("BINOUT1");
    box.getIn1().setText("BININ1");
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = saveData + " " + iteration;
    return saveData;
  }
  
  /**
   * Loads the required parameters (number of iterations)
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    int tokenType;
    tokenType = tokenizer.nextToken();
    iteration = (int) tokenizer.nval;
    iterText. setText(String. valueOf(iteration));

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
    panel.add(iterLabel);
    panel.add(iterText);
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
    
      if(getInput1()!=null && getInput2()!=null) {
          if (getInput1().getWidth()>=getInput2().getWidth()
                && getInput1().getHeight()>=getInput2().getHeight()){
System.out.println("Input Width 1 "+getInput1().getWidth());
System.out.println("Input Width 2 "+getInput2().getWidth());

        // get parameter
        try {
            iteration = (new Integer(iterText.getText())).intValue();
        }
        catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null,("Invalid Number of iterations Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
           iterText.setText("1");
           iteration = 1;
        }

        // execute operator
	try {
	    if(box.links.getLink(this,box.getIn2()).linkFrom.getType().startsWith("Kernel3x3TwoState")) {
		output1 = 
		    new image2DInt(input1.getWidth(),
				   input1.getHeight(),
				   imageConversions.binary2gs(openOp.open_image(imageConversions.gs2binary(input1.getValues(),input1.getWidth(),input1.getHeight()), input2.getValues(),iteration)));
		
		System.out.println("Output Width 1 "+getOutput1().getWidth());
		
		propagate();
	    }
	    else {
		JOptionPane.showMessageDialog(null,("You should use a Kernel3x3TwoState"),("Error!"), JOptionPane.WARNING_MESSAGE);
	    }
	} catch (NullPointerException e) {
	    JOptionPane.showMessageDialog(null,("You should use a Kernel3x3TwoState, not an image or output from an operator in K2"),("Error!"), JOptionPane.WARNING_MESSAGE);
	}}
      }
  }
}


















