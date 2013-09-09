package net.joshuahughes.hipr2.lower;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.color.*;
import javax.swing.*;

public class kernel3x3Double extends operator2DDouble{
  
  static int number=0;
  String type = new String("Kernel3x3double");
  JTextField [] values;

  public kernel3x3Double(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setType(type);
    setParameters();
    setBox(panel,links,0,1);// 0 inputs, 1 output
    box.getOut1().setText("KR");
    go();
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
    for(int i=0;i<9;++i){
      tokenType = tokenizer.nextToken();
      float value = (float) tokenizer.nval;
      values[i].setText(String. valueOf(value));
    }

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);

    // propogate mask
    go();
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    for(int i=0;i<9;++i){
      saveData = saveData + values[i].getText() + " ";
    }
    return saveData;
  }

  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    JPanel valuesPanel = new JPanel();
    valuesPanel.setLayout(new GridLayout(3,3));
    values = new JTextField [9];
    JButton applyButton = new JButton("Apply");
    for(int i=0;i<9;++i){
      values[i] = new JTextField("1.0",5);
      valuesPanel.add(values[i]);
    }
    panel.add(valuesPanel);
    applyButton.addActionListener(new ActionListener(){
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
    double [][] valuesData = new double [3][3];
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	valuesData[i][j] = 
	  Double.valueOf(values[j*3+i].getText()).doubleValue();
      }
    }
    setInput1(new image2DDouble(3,3,valuesData));
    output1 = new image2DDouble(input1);
    propagate();
  }
}
 
