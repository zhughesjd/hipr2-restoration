package net.joshuahughes.hipr2.lower;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.joshuahughes.hipr2.upper.InputException;
import net.joshuahughes.hipr2.upper.MeanSmooth;


public class meanSmooth extends convolution{

  /**
   * The mean smoothing operator code.
   */
  MeanSmooth meanOp = new MeanSmooth();
  static int number=0;
  int iterations;
  String type = new String("MeanSmooth");

  public meanSmooth(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,2,1);
    box.getIn2().setText("K2");
  }

  public void go(){
    System.out.println(name);

      // get and check iteration count
      try {
              iterations = (new Integer(iterationsText.getText())).intValue();
              scale = Double. valueOf (scaleText. getText()). doubleValue();
              offset = Double. valueOf (offsetText. getText()). doubleValue();
      }
      catch(NumberFormatException e){
              JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
      }
      if (iterations <= 0) {
              JOptionPane.showMessageDialog(null,("Number of iterations must be > 0"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
      }
                        
    try {

//System.out.println("*** MeanSmooth getInput1: "+getInput1());   
//System.out.println("*** MeanSmooth getInput2: "+getInput2());   
  
      if(getInput1()!=null && getInput2()!=null) {
          if (getInput1().getWidth()>=getInput2().getWidth()
                && getInput1().getHeight()>=getInput2().getHeight()){
  
System.out.println("Input Width 1 "+getInput1().getWidth());
System.out.println("Input Width 2 "+getInput2().getWidth());
  
        // get and check iteration count
        try {
                iterations = (new Integer(iterationsText.getText())).intValue();
        }
        catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,("Invalid Number of iterations Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
                iterationsText.setText("1");
                iterations = 1;
        }
        if (iterations <= 0) {
                JOptionPane.showMessageDialog(null,("Invalid Number of iterations Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
                iterationsText.setText("1");
                iterations = 1;
        }
  
	// check 2nd input type
	try {
	    if(box.links.getLink(this,box.getIn2()).linkFrom.getType().startsWith("Kernel3x3TwoState")) {
		
		output1 = 
		    new image2DDouble(
				      input1.getWidth(),
				      input1.getHeight(),
				      intscaleclip(
						   input1.getWidth(),
						   input1.getHeight(),
						   scale,
						   offset,
						   meanOp.smoothImage(
								      input1.getValues(),
								      input1.getWidth(),
								      input1.getHeight(),
								      input2.getValues(),
								      input2.getWidth(),
								      input2.getHeight(),
								      iterations)));
		
		System.out.println("Output width "+getOutput1().getWidth());
		propagate();
	    }
	    else {
		JOptionPane.showMessageDialog(null,("meanSmooth1: You should use a Kernel3x3TwoState"),("Error!"), JOptionPane.WARNING_MESSAGE);
	    } // link type
	} catch (NullPointerException e) {
	    JOptionPane.showMessageDialog(null,("You should use a Kernel3x3TwoState, not an image or output from an operator in K2"),("Error!"), JOptionPane.WARNING_MESSAGE);
	}}
      } //inputs ready
    } //try
    catch(InputException e){
	JOptionPane.showMessageDialog(null,("meanSmooth2: InputException"+e),("Error!"), JOptionPane.WARNING_MESSAGE);
	return;
    } 
  } //go
} //meanSmooth  
