package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;


/**
 *ImageBitShift is an algorithm to apply arithmetic BitShift on a image
 *@author:Judy Robertson, SELLIC OnLine
 *@author:Neil Brown, DAI
 *@see code.iface.imagebitshift
 */

public class ImageBitShift extends Thread{

  //the width of the input images in pixels
  private int i1_w;
  private int i2_w;

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int[] dest_1d;
  private boolean is_colored;
 

  /**
   *Constructs a new Image Operator
   *@param fistwidth The width of the input image
   */

  public ImageBitShift( int firstwidth ){
     i1_w = firstwidth;
  }

  /**
   *BitShifts the specified input image by the constant, also adds offset
   *and scales
   *@param src1_1d The input pixel array
   *@param constant The constant value to shift every pixel value by
   *@param shiftleft Boolean to specify a left or right bit shift
   *@param wrap Boolean to specify if the shifted values should be wrapped
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param oset The offset value
   *@param scale The scale value
   *@return A pixel array containing the shifted image
   */

  public int [] doBitShift(int [] src1_1d, int constant, boolean shiftleft,
			   boolean wrap, int width, int height, float oset, 
			   float scale ){

    //Get size of image and make 1d_arrays
    d_w = width;
    d_h = height;
    
    dest_1d = new int[d_w*d_h];
    
    int src1rgb;
    int result = 0;
    
    int [] wrapArray = new int[9];

    constant = constant % 8;

    wrapArray[0] = 0;
    wrapArray[1] = 128;
    wrapArray[2] = 192;
    wrapArray[3] = 224;
    wrapArray[4] = 240;
    wrapArray[5] = 248;
    wrapArray[6] = 252;
    wrapArray[7] = 254;
    wrapArray[8] = 255;

    //now do the BitShift on the input image 
    //This works for unwrapped shifting
    if(wrap == false){
      for (int i=0; i< src1_1d. length; i++){
	src1rgb = src1_1d[i] & 0x000000ff;
	if( shiftleft ) {
	  result = ( src1rgb << constant );
	} else {
	  result = ( src1rgb >> constant );
	}

        // mask to get result bits
        result = result & 0x000000ff;

        // rescale for display
	result = (int) ( scale * (float) result + oset );
	if (result > 255) result = 255;
	if (result < 0)   result = 0;

	dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
      }
      
    }
    else if(wrap == true){
      int tmp;
      for (int i=0; i< src1_1d. length; i++){
	src1rgb = src1_1d[i] & 0x000000ff;
	if( shiftleft ) {
	  result = 0;
	  tmp = wrapArray[constant] & src1rgb;
	  tmp = tmp >> (8-constant);
	  result = ( src1rgb << constant );
	  result = result | tmp;
	} 
	else {
	  //Shifting left 3 = shift right by 8-3, so just subtract shift
	  //value from 8.
	  result = 0;
	  constant = 8 - constant;
	  tmp = wrapArray[constant] & src1rgb;
	  tmp = tmp >> (8-constant);
	  result = ( src1rgb << constant );
	  result = result | tmp;
	}
    
        // mask to get result bits
        result = result & 0x000000ff;

        // rescale for display
	result = (int) ( scale * (float) result + oset );
	if (result > 255) result = 255;
	if (result < 0)   result = 0;
	dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
      }
    }
    return dest_1d;
  }
}
