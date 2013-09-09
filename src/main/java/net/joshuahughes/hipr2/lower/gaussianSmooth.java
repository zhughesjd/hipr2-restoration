package net.joshuahughes.hipr2.lower;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.GaussianSmooth;


public class gaussianSmooth extends operator1DInt{

  JLabel thetaLabel = new JLabel("Theta Value:");
  JTextField thetaText = new JTextField("0.4",5);
  JLabel kernelLabel = new JLabel("Kernel Size");
  JPanel  p = new JPanel();
  ButtonGroup group = new ButtonGroup();
  JRadioButton size3 = new JRadioButton("3x3");
  JRadioButton size4 = new JRadioButton("4x4");
  JRadioButton size5 = new JRadioButton("5x5");
  JRadioButton size6 = new JRadioButton("6x6");
  JRadioButton size7 = new JRadioButton("7x7");
 
  GaussianSmooth gaussianSmoothOp = new GaussianSmooth();
  static int number=0;
  int s = 3;
  double theta;
  String type = new String("GaussianSmoothing");

  public gaussianSmooth(){
  }

  public gaussianSmooth(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
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
    s = (int) tokenizer.nval;
    switch( s ) {
        case 3: size3.setSelected(true); break;
        case 4: size4.setSelected(true); break;
        case 5: size5.setSelected(true); break;
        case 6: size6.setSelected(true); break;
        case 7: size7.setSelected(true); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid kernel size loaded: "+s),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
    tokenType = tokenizer.nextToken();
    theta = (double) tokenizer.nval;
    thetaText.setText(String. valueOf(theta));

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = " " + s + " " + theta;
    return saveData;
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
    p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
    JButton applyButton = new JButton("Apply");
    panel.add(thetaLabel);
    panel.add(thetaText);
    size3.setSelected(true);
    group.add(size3);
    group.add(size4);
    group.add(size5);
    group.add(size6);
    group.add(size7);

    size3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 3;
      }
    });
    size4.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 4;
      }
    });
    size5.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 5;
      }
    });
    size6.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 6;
      }
    });
    size7.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        s = 7;
      }
    });

    p.add(kernelLabel);
    p.add(size3);
    p.add(size4);
    p.add(size5);
    p.add(size6);
    p.add(size7);
    panel.add(p);

    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }
    }); 
    panel.add(applyButton);

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);

    // get and check parameter    
    try {
              theta = Double.valueOf(thetaText.getText()).doubleValue();
    }
    catch(NumberFormatException e){
              JOptionPane.showMessageDialog(null,("Invalid Theta Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
              return;
    }
    if( theta < 0.4 || theta > ((float)s/6)){

	  JOptionPane.showMessageDialog(null,
		      "Invalid theta value. Must have 0.4 < theta < size/6",
		      "Invalid Value",
		      JOptionPane.WARNING_MESSAGE); 
	  return;
    }

    // execute
    if(getInput1()!=null){
	System.out.println("Input Width 1 "+getInput1().getWidth());
	
	output1 = new image1DInt(input1.getWidth(),
				 input1.getHeight(),
				 imageConversions.pix2gs(
							 gaussianSmoothOp.smooth_image(
										       imageConversions.gs2pix(input1.getValues()),
										       input1.getWidth(),
										       input1.getHeight(),
										       s,
										       theta )));
	
	System.out.println("Output width "+getOutput1().getWidth());
	propagate();
    } //if
  } //go
}
