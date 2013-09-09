package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99

import java.awt.color.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * All operators that work on image1DInt images extend this class,
 * contains functionality specific for working with image1DInt images.
 */
public abstract class operator1DInt extends operator{

  /**
   * Input1 is an image1DInt.
   */
  image1DInt input1;
  /**
   * Input2 is an image1DInt.
   */
  image1DInt input2;
  /**
   * Output1 is an image1DInt.
   */
  image1DInt output1;
  /**
   * Output2 is an image1DInt.
   */
  image1DInt output2;
  /**
   * Returns input1.
   * @return input1 of this operator
   */
  image1DInt getInput1(){
    return input1;
  }
  /**
   * Returns input2.
   * @return input2 of this operator
   */
  image1DInt getInput2(){
    return input2;
  }
  /**
   * Returns output1.
   * @return output1 of this operator
   */
  public image1DInt getOutput1(){
    return output1;
  }
  /**
   * Returns output2.
   * @return output2 of this operator
   */
  public image1DInt getOutput2(){
    return output2;
  }
  /**
   * Sets input1 to an image1DInt, obtained by converting image x.
   * @param x the image x to be converted and used as input1
   */
  public void setInput1(image x){
    if(x!=null){
      input1 = new image1DInt(x);
    }
    else input1 = null; //*** new
  }
  /**
   * Sets input2 to an image1DInt, obtained by converting image x.
   * @param x the image x to be converted and used as input2
   */
  public void setInput2(image x){
    if(x!=null){
      input2 = new image1DInt(x);
    }
    else input2 = null; //*** new
  }
  /**
   * Links this operator and another operator by setting the other
   * operator's input1 to the same image as this operator's output1
   * (converting the image to the correct representation if required).
   * @param linkOp the operator to be linked to
   */
  public void link1To1(operator linkOp){
    linkOp.setInput1(getOutput1());
    linkOp.go();
  }
  /**
   * Links this operator and another operator by setting the other
   * operator's input2 to the same image as this operator's output1
   * (converting the image to the correct representation if required).
   * @param linkOp the operator to be linked to
   */
  public void link1To2(operator linkOp){
    linkOp.setInput2(getOutput1());
    linkOp.go();
  }
  /**
   * Links this operator and another operator by setting the other
   * operator's input1 to the same image as this operator's output2
   * (converting the image to the correct representation if required).
   * @param linkOp the operator to be linked to
   */
  public void link2To1(operator linkOp){
    linkOp.setInput1(getOutput2());
    linkOp.go();
  }
  /**
   * Links this operator and another operator by setting the other
   * operator's input2 to the same image as this operator's output2
   * (converting the image to the correct representation if required).
   * @param linkOp the operator to be linked to
   */
  public void link2To2(operator linkOp){
    linkOp.setInput2(getOutput2());
    linkOp.go();
  }
  
}
