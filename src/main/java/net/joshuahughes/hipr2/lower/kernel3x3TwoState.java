package net.joshuahughes.hipr2.lower;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.color.*;
import javax.swing.*;

public class kernel3x3TwoState extends operator2DInt{
  
  /**
   * The current number of this type of operator, used to generate the
   * unique name for each operator (Kernel3x3TwoState 105 etc).
   */
  static int number=0;
  /**
   * The type of operator (used for identification purposes
   */
  String type = new String("Kernel3x3TwoState");
  /**
   * Array of 9 (3x3) buttons for the kernel UI
   */
  JButton [] buttons;

  /**
   * Constructor taking the parent panel (for the UI) and information
   * about the links in the system
   * @param panel the panel that the UI for the operators is based around
   * @param links the information about the system's links
   */
  public kernel3x3TwoState(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setType(type);
    setParameters();
    setBox(panel,links,0,1);
    box.getOut1().setText("K2");
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
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    for(int i=0;i<9;++i){
      saveData = saveData + buttons[i].getName() + " ";
    }
    return saveData;
  }

  /**
   * Loads the required parameters from the input stream, so the
   * operator can be recreated just as it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    int tokenType;
    int [] values = new int [9];
    for(int i=0;i<9;++i){
      tokenType = tokenizer.nextToken();
      int value = (int) tokenizer.nval;
      if(value==0){
	buttons[i].setText("0");
	buttons[i].setName("0");
      }else{
	buttons[i].setText("1");
	buttons[i].setName("1");
      }
    }

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);

    // propogate mask
    go();
  }
      
  /**
   * Sets up the parameters window and the default settings for the operator,
   * sets up the state of the 9 kernel buttons etc.
   */
  void setParameters(){
    parameters = new JFrame(name);
    panel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(3,3));
    buttons = new JButton [9];
    JButton applyButton = new JButton("Apply");
    buttonListener listener = new buttonListener();
    for(int i=0;i<9;++i){
      buttons[i] = new JButton("1");
      buttons[i].setName("1");
      buttonsPanel.add(buttons[i]);
      buttons[i].addActionListener(listener);
    }
    panel.add(buttonsPanel);
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
  
  /**
   * Runs the kernel by simply generating an image from the current
   * state of the buttons and propagating it to the next operators
   */
  public void go(){
    int [] buttonData = new int [9];
    for(int i=0;i<9;++i){
      buttonData[i] = (new Integer(buttons[i].getName()).intValue());
    }
    setInput1(new image2DInt(3,3,buttonData));
    output1 = new image2DInt(input1);
    propagate();
  }
  
  /**
   * The listener for the kernel buttons, each button is either on or off
   * and clicking on it toggles the state.
   */
  class buttonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      if(((JButton) e.getSource()).getText()=="1"){
	((JButton) e.getSource()).setText("0");
	((JButton) e.getSource()).setName("0");
      }else{
	((JButton) e.getSource()).setText("1");
	((JButton) e.getSource()).setName("1");
      }
    }
  }
}
 
