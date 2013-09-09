package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last modified 16/9/99

//package code.connections;

import java.awt.color.*;
import java.awt.*;
import java.util.*;

/**
 * Image representation consisting of an array of integers, each integer
 * representing a grey-scale value between 0 and 255 (values outside this 
 * range are allowed but it should be noted that the imageDisplay and
 * possibly other operators will simply set all values above 255 to 255
 * and all negative values to 0), starting at the top-left corner of the
 * image (0,0) and continuing row by row to the bottom-right corner.
 */
public class image1DInt extends image{
  /**
   * The array of integer values representing the pixel grey levels.
   */
  private int [] values;

  /**
   * No argument constructor, sets all variables to 0.
   */
  public image1DInt(){
    width = 0;
    height = 0;
    values = new int [0];
  }
   
  /**
   * Constructor taking a width, a height and an int array (all
   * the individual components of this image representation).
   * @param width of the image
   * @param height of the image
   * @param values representing the pixel gray levels
   */
  public image1DInt(int width ,int height ,int [] values){
    this.width = width;
    this.height = height;
    this.values = (int []) values.clone();
  }

  /**
   * Constructor that makes an image1DInt image representation by
   * either making an identical copy of an existing image1DInt or by
   * converting a different image representation.
   * @param oldImage the existing image
   */
  public image1DInt(image oldImage){
    if(oldImage != null){
      width = oldImage.getWidth();
      height = oldImage.getHeight();
      if(oldImage instanceof image1DInt){
	values = (int []) oldImage.getValues().clone();
      }else{
	values = (int []) oldImage.getValues().clone();
      }
    }
  }
  /** Constructor for an image which size is smaller than the image src
   *the real size is the same, but some part of the new image are black
   */

  public image1DInt(int src_w, int src_h,int dest_w, int dest_h, int [] src_1d) {
     this.width = src_w;
     this.height = src_h;
     this.values = new int [src_1d.length];
     
     for(int i = 0; i < src_1d.length; i++) {
       this.values[i] = 0;}

     for(int i = 0; i < dest_w; i++) {
       for(int j = 0; j < dest_h; j++) {
	 this.values[(j*src_w)+i] = src_1d[(j*dest_w)+i];
       }
     }
  }
  
  /**
   * Returns the image as an int array by simply returning the values array.
   * @return the int array representing the pixel values
   */
  public int [] getValues(){
    return values;
  }

}
