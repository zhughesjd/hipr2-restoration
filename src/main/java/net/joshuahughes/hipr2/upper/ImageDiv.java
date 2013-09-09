package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;


/**
 *ImageDiv  is an algorithm to divide two images
 *@author:Judy Robertson, SELLIC OnLine 
 *@author Neil Brown, DAI 
 *@author Timothy Sharman
 *@see code.iface.imagediv
 */

public class ImageDiv extends Thread{
  
  //the width of the input images in pixels
  private int i1_w;
  private int i2_w;
  
  //the width and height of the output image
  
  private int d_w;
  private int d_h;
  private int[] dest_1d;
  private boolean is_colored;
  
  
  /**
   *Constructs a new ImageDiv
   *@param firstwidth The width of the first image
   *@param secondwidth The width of the second image
   */
  
  public ImageDiv(int firstwidth, int secondwidth){
    i1_w = firstwidth;
    i2_w = secondwidth;
  }
  
  /**
   *Applies the image Div operator on the specified image arrays, with the
   *specified offset and scale value
   *@param src1_1d The first source image as a pixel array
   *@param src2_1d The second source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param oset The offset value
   *@param scale The scale value
   *@param reverse Reverse the order of the division
   *@return A pixel array containing one image divided by the other
   */
  //Bob's image product algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) do this for both input images
    d) apply the operation (eg Div)
    e) add 128 and then scale
    f) clip to lie from 0 to 255. Call this value 0xCD
    g) create int value 0xffCDCDCD
    */
  
  public int [] doDiv(int [] src1_1d, int [] src2_1d, int width, int height, 
		      float oset, float scale, boolean reverse) {
    
    int place1 = -1;
    int place2 = -1;
    int src1rgb = 0;
    int src2rgb = 0;
    int result = 0;
    //Get size of image and make 1d_arrays
    d_w = width;
    d_h = height;
    
    
    dest_1d = new int[d_w*d_h];
    
    boolean firstwider = false;
    boolean secondwider = false;
    int wrap;
    
    if (i1_w > d_w){
      wrap =   ((i1_w + 1) - d_w);
      firstwider = true;
    } else if (i2_w > d_w){
      wrap =    ((i2_w + 1) - d_w);
      secondwider = true;
      
    } else {
      wrap = 0;
    }
    
    //if you know there is no wrap around, you can save yourself some time
    
    if (wrap == 0) {
      for (int i=0; i< dest_1d. length ; i++){
	src2rgb = src2_1d[i] & 0x000000ff;
	src1rgb = src1_1d[i] & 0x000000ff;
	if( !reverse ) {
	  result = (int) ((scale * ((float) src2rgb / (float) src1rgb ))+oset);
	} else {
	  result = (int) ((scale * ((float) src1rgb / (float) src2rgb ))+oset);
	}
	//clip to 0 ... 256
	if (result < 0){
	  result = 0;
	} else if  (result > 255){
	  result = 255;
	}
	
	//create an int value for dest_1d
	dest_1d[i ] =  0xff000000 | (result + (result << 16) + (result << 8));
	
      }
      
      return dest_1d;
      
    } else { // wrap != 0
      
      for (int i=0; i< dest_1d. length ; i++){
	
	//we might need to skip out some pixels which aren't in the overlap area
	
	if ((i %d_w  ) == 0 ) {
	  if ( i == 0 ){
	    place1 = 0;
	    place2 = 0;
	  } else if (secondwider) {
	    place2 = place2 + wrap;
	    place1 ++;
	  } else {
	    place1 = place1 + wrap;
	    place2 ++;
	  }
	} else{
	  place2 ++;
	  place1 ++;
	}
	
	src2rgb = src2_1d[place2] & 0x000000ff;
	src1rgb = src1_1d[place1] & 0x000000ff;
	
	if( !reverse ) {
	  if (src1rgb == 0) src1rgb = 1;
	  result = (int) ((scale * ((float) src2rgb / (float) src1rgb ))+oset);
	} else {
	  if (src2rgb == 0) src2rgb = 1;
	  result = (int) ((scale * ((float) src1rgb / (float) src2rgb ))+oset);
	}
	
	//clip to 0 ... 256
	if (result < 0){
	  result = 0;
	} else if  (result > 255){
	  result = 255;
	}
	
	//create an int value for dest_1d
	dest_1d[i ] =  0xff000000 | (result + (result << 16) + (result << 8));
      }
      return dest_1d;
    }
  }
  
  /**
   *Divides the specified input image by the constant, also adds offset
   *no need for scaling in this example.
   *
   *@param src1_1d The input pixel array
   *@param constant The constant value to divide every pixel in the input array by
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param oset The offset value
   *@param scale The scale value
   *@return A pixel array of the input array divided by the constant  
   */
  public int [] doDiv(int [] src1_1d, float constant, int width, int height, 
		      float oset, float scale, boolean reverse ) {
    
    //Get size of image and make 1d_arrays
    d_w = width;
    d_h = height;
    
    dest_1d = new int[d_w*d_h];
    
    int src1rgb;
    int result = 0;
    //now do the division on the input image 
    if (reverse) {
      if (constant == 0 ) {
        JOptionPane.showMessageDialog(null,("Constant can't be 0"),("Error!"), JOptionPane.WARNING_MESSAGE);
        constant=1;
      }
      else {
        for (int i=0; i< src1_1d. length; i++){
          src1rgb = src1_1d[i] & 0x000000ff;
          result = (int) ( scale * ((float) src1rgb / constant ) + oset);
          if (result > 255){
    	        result = 255;
          }
          if (result < 0){
    	        result = 0;
          }
          dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
        }
      }
    }
    else {
      for (int i=0; i< src1_1d. length; i++){
        src1rgb = src1_1d[i] & 0x000000ff;
        result = (int) ( scale * (constant / (float) src1rgb ) + oset);
        if (result > 255){
	        result = 255;
        }
        if (result < 0){
	        result = 0;
        }
        dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
      }
    }
    return dest_1d;
  }
}
