package net.joshuahughes.hipr2.lower;
// Author Simon Horne


import java.awt.color.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * All operators extend this class (in reality all operators extend
 * one of several classes for example operator1DInt that extend this class).
 */
public abstract class operator{
        
  /**
   * The panel that the operatorBox (representing the operator graphically
   * on the UI) is placed on (this is the panel that is on the big 
   * scrollpane taking up most of the UI).
   */
  protected JPanel panel;
  /**
   * The frame that contains the parameter window for this operator (made
   * visible by clicking on the operator name in the operatorBox on th UI).
   */
  protected JFrame parameters;
  /**
   * The type of operator (for example convolution).
   */
  protected String type;
  /**
   * The unique name of the operator (for example convolution) 
   */
  protected String name;
  /**
   * The graphical representation of this operator on the UI.
   */
  protected operatorBox box;
  
  /**
   * Returns a String representing the location of the operatorBox
   * (top-left corner location), for the purpose of saving the system
   * and reloading at a future date.
   */
  String saveLocation(){
    String x = String.valueOf(box.getLocation().getX());
    String y = String.valueOf(box.getLocation().getY());
    return(x+" "+y);
  }
   
  /**
   * Returns a String representing the name of the operator,
   * for the purpose of saving the system and reloading at a future date.
   */
  String saveName(){
    String t = String.valueOf(getName());
    return(t);
  }

  /**
   * Returns a String representing the operator, containing all the
   * information required to reconstruct this operator at a later date.
   * @return the String containing all necessary information
   */
  public String saveOperator(){
    String data = new String(saveName()+" "+
			     saveLocation()+" "+
			     saveParameters()+" ENDZZZ1");
    return data;
  }

  /**
   * Creates the box for the operator which is the graphical representation
   * of the operator on the UI.
   * @param panel the panel that the box is to added to
   * @param links the representation of the links between the operators
   * @param inputs the number of inputs to the operator (1 or 2)
   * @param outputs the number of outputs from the operator (1 or 2)
   */
  protected void setBox(JPanel panel, linkData links, int inputs, int outputs){
    box = new operatorBox(this,panel,links,inputs,outputs);
  }
  /**
   * Returns the graphical representation of the operator.
   * @return the operatorBox representing the operator
   */
  protected operatorBox getBox(){
    return box;
  }

  /**
   * Returns the parameters frame for this operator.
   * @param the parameters frame for this operator
   */
  public JFrame getParameters(){
    return parameters;
  }

  /**
   * Lays out the parameters frame correctly.
   */
  protected void updateParameters(){
    parameters.invalidate();
    parameters.validate();
    parameters.pack();
  }

  /**
   * Sets the name of this operator.
   * @param name of the operator to be used
   */
  public void setName(String name){
    this.name = new String(name);
  }
  /**
   * Returns the name of the operator.
   * @return the operator name
   */
  public String getName(){
    return name;
  }

  /**
   * Makes the parameters window visible and deiconifies it if necessary.
   */
  public void showParameters(){
    parameters.setLocation(parameters.getParent().getLocation());
    parameters.setVisible(true);
    parameters.setState(Frame.NORMAL);
  }


  public void setType(String type) {
    this.type = new String(type);
  }

  /**
   * Returns the type of this operator (for example convolution).
   * @return the type of the operator
   */
  public String getType(){
    return type;
  }

  /**
   * Runs all the operators that this operator passes stuff to (all
   * operators that are input linked to one of this operator's outputs).
   */
  public void propagate(){
    Iterator it = getBox().getLinkData().getLinks().iterator();
    while(it.hasNext()){
      singleLink next = (singleLink) it.next();
      if(next.linkFrom == this){
	propagateSingleLink(next);
      }
    }
  }

  /**
   * Passes data from one of this operator's output to one of 
   * another operator's input (converting the image representation
   * if necessary) and runs the next operator.
   * @param link the link that is to be used
   */
  public void propagateSingleLink(singleLink link){
    if(link.connectionFrom.getName() == "OUT1"){
      if(link.connectionTo.getName() == "IN1"){
	link1To1(link.linkTo);
      }else{
	link1To2(link.linkTo);
      }
    }else if(link.connectionTo.getName() == "IN1"){
      link2To1(link.linkTo);
    }else{
      link2To2(link.linkTo);
    }
  }

  /**
   * Sets input1 to the specified image.
   * @param x the image to be used
   */
  public abstract void setInput1(image x);
  /**
   * Sets input2 to the specified image.
   * @param x the image to be used
   */
  public abstract void setInput2(image x);
  /**
   * Adds a link between this operator and another operator
   * (output1 to input1).
   * @param linkOp the operator to be linked to
   */
  public abstract void link1To1(operator linkOp);
  /**
   * Adds a link between this operator and another operator
   * (output1 to input2).
   * @param linkOp the operator to be linked to
   */
  public abstract void link1To2(operator linkOp);
  /**
   * Adds a link between this operator and another operator
   * (output2 to input1).
   * @param linkOp the operator to be linked to
   */
  public abstract void link2To1(operator linkOp);
  /**
   * Adds a link between this operator and another operator
   * (output2 to input2).
   * @param linkOp the operator to be linked to
   */
  public abstract void link2To2(operator linkOp);

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   * @return String representing the operator
   */
  public abstract String saveParameters();

  public abstract void loadParameters(StreamTokenizer tokenizer) 
    throws IOException;

  /**
   * Runs the operator and then propagates the outputs to all
   * operators that this operator links to.
   */
  public abstract void go();
  
  }

  
