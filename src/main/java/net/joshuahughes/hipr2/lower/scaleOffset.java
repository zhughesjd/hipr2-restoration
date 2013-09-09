package net.joshuahughes.hipr2.lower;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class scaleOffset extends operator2DDouble{

  JTextField scaleText;
  JTextField offsetText;
  static int number=0;
  String type = new String("ScaleOffset");

  public scaleOffset(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
  }

  /**
   * Returns the number of this operator.
   * @return the number of the operator
   */
  public int getNumber(){
    return number;
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
    double scale = (double) tokenizer.nval;
    scaleText. setText(String. valueOf(scale));

    tokenType = tokenizer.nextToken();
    double offset = (double) tokenizer.nval;
    offsetText. setText(String. valueOf(offset));

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date), there are no parameters with imageDisplay
   * that require saving.
   */
  public String saveParameters(){
    String saveData = new String();
    saveData = scaleText.getText() + " " + offsetText.getText();
    return saveData;
  }

  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    JLabel scaleLabel = new JLabel("Scale");
    scaleText = new JTextField("1.0",5);
    JLabel offsetLabel = new JLabel("Offset");
    offsetText = new JTextField("0",5);
    JButton applyButton = new JButton("Apply");
    panel.add(scaleLabel);
    panel.add(scaleText);
    panel.add(offsetLabel);
    panel.add(offsetText);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    scaleText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    offsetText.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	go();
      }});
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  double [][] apply(double [][] values,
		    int width, int height,
		    double scale, double offset){
   
    double [][] result = new double [width][height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	result[i][j] = values[i][j] * scale + offset;
      }
    }
    return result;
  }

  public void go(){
    System.out.println(name);
    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      try{
      output1 = 
	new image2DDouble(input1.getWidth(),
			  input1.getHeight(),
			  apply(input1.getValues2D(),
				input1.getWidth(),
				input1.getHeight(),
				(new Double(scaleText.getText()).
				doubleValue()),
				(new Double(offsetText.getText()).
				doubleValue())));
      }
      
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Scale or Offset Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
      scaleText.setText("1.0");
      offsetText.setText("0");
    }
      System.out.println("Output width "+getOutput1().getWidth());
    }
      propagate();
    
  }
}















