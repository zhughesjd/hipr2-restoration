package net.joshuahughes.hipr2.lower;
// Author Nathalie Cammas
// Last Modified 19/07/00


import net.joshuahughes.hipr2.upper.BinaryFast;

/**
 * All operators that work on Binary Fast images extend this class,
 * contains functionality specific for working with Binary Fast images.
 */
public abstract class operatorBinary extends operator{

  /**
   * Input1 is a BinaryFast image.
   */
   BinaryFast input1;
  /**
   * Input2 is a BinaryFast image.
   */
   BinaryFast input2;
  /**
   * Output1 is a BinaryFast image.
   */
   BinaryFast output1;
  /**
   * Output2 is a BinaryFast image.
   */
   BinaryFast output2;

  /**
   * Returns input1.
   * @return input1 of this operator
   */
  BinaryFast getInput1(){
    return input1;
  }
  /**
   * Returns input2.
   * @return input2 of this operator
   */
  BinaryFast getInput2(){
    return input2;
  }
  /**
   * Returns output1.
   * @return output1 of this operator
   */
  public BinaryFast getOutput1(){
    return output1;
  }
  /**
   * Returns output2.
   * @return output2 of this operator
   */
  public BinaryFast getOutput2(){
    return output2;
  }
  /**
   * Sets input1 to a BinaryFast image, obtained by converting image x.
   * @param x the image x to be converted and used as input1
   */
  public void setInput1(image x){
    if(x!=null){
      input1 = new BinaryFast(x);
    }
    else input1 = null; //*** new
  }
  /**
   * Sets input2 to a BinaryFast image, obtained by converting image x.
   * @param x the image x to be converted and used as input2
   */
  public void setInput2(image x){
    if(x!=null){
      input2 = new BinaryFast(x);
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

