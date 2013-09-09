package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99


import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The convolutionPadded operator, all inputs/outputs are image2DDouble,
 * 2 inputs - an image and a kernel, 1 output, the padded convolution
 * algorithm is located in code/operator/convolution/Convolution.java.
 */
public class convolutionPadded extends convolution{
  /**
   * Initialise the unique numbering system for all convolutionPadded
   * operators.
   */
  static int number=0;

  /**
   * Set the type of this operator.
   */
  String type = new String("ConvolutionPadded");

  /**
   * Constructor that takes a parent panel for the graphical representation
   * of this operator and a representation of all links in the system.
   * @param panel the panel that will be the parent of the operatorBox
   * @param links all the links in the system
   */
  public convolutionPadded(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setBox(panel,links,2,1);
    box.getIn2().setText("KR");
  }

  /**
   * Runs the operator if both inputs contain data and propagates it's 
   * output to all the operators that it is linked to.
   */
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

    if(getInput1()!=null && getInput2()!=null 
       && getInput1().getWidth()>=getInput2().getWidth()
       && getInput1().getHeight()>=getInput2().getHeight()){
      System.out.println("Width 1 "+getInput1().getWidth());
      System.out.println("Width 2 "+getInput2().getWidth());
      int iterations = (new Integer(iterationsText.getText())).intValue();
      output1 = 
	new image2DDouble(
                input1.getWidth(),
                input1.getHeight(),
                scaleclip(
                        input1.getWidth(),
                        input1.getHeight(),
                        scale,
                        offset,
			convolutionOp.convolutionType2(
                                 input1.getValues2D(),
				 input1.getWidth(),
				 input1.getHeight(),
				 input2.getValues2D(),
				 input2.getWidth(),
				 input2.getHeight(),
				 iterations)));
      System.out.println("Output width "+getOutput1().getWidth());
      propagate();
    }
  }
}
