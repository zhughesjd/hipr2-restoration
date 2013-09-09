package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *ImageXor is an algorithm to apply logical XOR/XNOR between two images
 *@author:Judy Robertson, SELLIC OnLine
 *@author:Neil Brown, DAI
 *@author:Timothy Sharman
 *@see code.iface.imagexor
 */

public class ImageXor extends Thread{

  //the width of the input images in pixels
  private int i1_w;
  private int i2_w;

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int[] dest_1d;
  private boolean is_colored;
 

  /**
   *Constructs a new Image Xor
   *@param firstwidth The width of the iorst input image
   *@param secondwidth The width of the second input image
   */

  public ImageXor(int firstwidth, int secondwidth){
     i1_w = firstwidth;
     i2_w = secondwidth;
  }

  /**
   *Applies the image XOR operator on the specified image arrays, with the specified offset and scale value
   *@param src1_1d The first source image as a pixel array
   *@param src2_1d The second source image as a pixel array
   *@param XNOR Boolean to indicate wether we should XNOR rather than XOR
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param oset The offset value
   *@param scale The scale value
   *@return A pixel array containing the X(N)OR of the two input images
   */

  //Bob's image xor algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
        b) use value &0x000000ff to get the BB value
        c) do this for both input images
        d) apply the operation (eg XOR)
        e) add oset and then scale
        f) clip to lie from 0 to 255. Call this value 0xCD
        g) create int value 0xffCDCDCD
	*/

  public int [] doXor(int [] src1_1d, int [] src2_1d, boolean XNOR, 
		      int width, int height, float oset, float scale){
    
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
	
	/* Due to Java not having unsigned ints, the ~ will always give a
	   negative result. As we a dealing with only the bottom 8 bits of
	   the int, I will just mask out the bottom bits. Neil.
	   */
	if (XNOR) {
	  result = (int) ((scale * (float)(~(src1rgb ^ src2rgb) & 0xff))+oset);
	} else {
	  result = (int) ((scale * (float) ( src1rgb ^ src2rgb )) + oset);
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
    else {
      
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
	
	/* Due to Java not having unsigned ints, the ~ will always give a
	   negative result. As we a dealing with only the bottom 8 bits of
	   the int, I will just mask out the bottom bits. Neil.
	   */
	if (XNOR) {
	  result = (int) ((scale * (float) (~(src1rgb ^ src2rgb) & 0xff))+ oset);
	} else {
	  result = (int) ((scale * (float) ( src1rgb ^ src2rgb )) + oset);
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
   *XORs the specified input image by the constant, also adds offset
   *
   *@param src1_1d The input pixel array
   *@param constant The constant value to AND every pixel in the input array by
   *@param XNOR Boolean to indicate wether we should XNOR rather than XOR
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param oset The offset value
   *@param scale The scale value
   *@return A pixel array with the constant value X(N)ORed with every pixel in the input array
   */

  public int [] doXor(int [] src1_1d, int constant, boolean XNOR,
		      int width, int height, float oset, float scale ){
    
    //Get size of image and make 1d_arrays
    d_w = width;
    d_h = height;
    
    dest_1d = new int[d_w*d_h];
    
    int src1rgb;
    int result = 0;
    
    /* Due to Java not having unsigned ints, the ~ will always give a
       negative result. As we a dealing with only the bottom 8 bits of
       the int, I will just mask out the bottom bits. Neil.
       */
    //now do the XOR on the input image 
    
    for (int i=0; i< src1_1d. length; i++){
      src1rgb = src1_1d[i] & 0x000000ff;
      if(XNOR) {
	result = (int) (( scale * ((float) (~(src1rgb ^ constant) &0xff))) + oset);
      } else {
	result = (int) (( scale * ((float) ( src1rgb ^ constant ))) + oset);
      }
      if (result > 255){
	result = 255;
      }
      if (result < 0){
	result = 0;
      }
      dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
    
  }
  
  
}
