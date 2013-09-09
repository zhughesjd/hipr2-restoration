package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99


import java.awt.color.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * All operators that deal with image2DDouble images extend this class.
 */
public abstract class operator2DDouble extends operator{
  /**
   * Input1 is an image2DDouble.
   */
  image2DDouble input1;
  /**
   * Input2 is an image2DDouble.
   */
  image2DDouble input2;
  /**
   * Output1 is an image2DDouble.
   */
  image2DDouble output1;
  /**
   * Output2 is an image2DDouble.
   */
  image2DDouble output2;
  /**
   * Returns input1.
   * @return the input1 image
   */
  public image2DDouble getInput1(){
    return input1;
  }
  /**
   * Returns input2.
   * @return the input2 image
   */
  public image2DDouble getInput2(){
    return input2;
  }
  /**
   * Returns output1.
   * @return the output1 image (null if operator has not been run)
   */
  public image2DDouble getOutput1(){
    return output1;
  }
  /**
   * Returns output2.
   * @return the output2 image (null if operator has not been run
   * or the operator has only one output
   */
  public image2DDouble getOutput2(){
    return output2;
  }
  /**
   * Sets input1 to the specified image, converting to image2DDouble
   * if necessary.
   * @param x the image to be used
   */
  public void setInput1(image x){
    if(x!=null){
      input1 = new image2DDouble(x);
    }
    else input1 = null; //*** new
  }
  /**
   * Sets input2 to the specified image, converting to image2DDouble
   * if necessary.
   * @param x the image to be used
   */
  public void setInput2(image x){
    if(x!=null){
      input2 = new image2DDouble(x);
    }
    else input2 = null; //*** new
  }
  /**
   * Links output1 of this operator to input1 of another operator.
   * @param linkOp the operator to be linked to
   */
  public void link1To1(operator linkOp){
    linkOp.setInput1(getOutput1());
    linkOp.go();
  }
  /**
   * Links output1 of this operator to input2 of another operator.
   * @param linkOp the operator to be linked to
   */
  public void link1To2(operator linkOp){
    linkOp.setInput2(getOutput1());
    linkOp.go();
  }
  /**
   * Links output2 of this operator to input1 of another operator.
   * @param linkOp the operator to be linked to
   */
  public void link2To1(operator linkOp){
    linkOp.setInput1(getOutput2());
    linkOp.go();
  }
  /**
   * Links output2 of this operator to input2 of another operator.
   * @param linkOp the operator to be linked to
   */
  public void link2To2(operator linkOp){
    linkOp.setInput2(getOutput2());
    linkOp.go();
  }
}
