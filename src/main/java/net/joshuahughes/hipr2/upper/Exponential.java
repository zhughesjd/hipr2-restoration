package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *Exponential is an algorithm to exponentiate an image
 *@author:Timothy Sharman
 *@author Konstantinos Koryllos
 *@see code.iface.exponential
 */

public class Exponential extends Thread{


  //The width and height of the output
  private int d_w;
  private int d_h;

  private int[] dest_1d;


/**    
   *apply_expo applies an exponential operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param expo The exponential scaling factor
   *@param scaleval The scaling factor to be applied
   *@param offsetval The offset value to be added
   *
   *@return A pixel array containing the exponential image
   */


  //Tim's exponential algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg exponentiate). 
    d) return the result
    */

   public int [] apply_expo(int [] src_1d, int width, int height, double expo,
			    float scaleval, float offsetval) {
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    int src_rgb;
    int result = 0; 

    for(int i = 0; i < dest_1d. length; i++) {
      
      //Select required bits from image
      src_rgb = src_1d[i] & 0x000000ff;
 
      //Perform operation
      result = (int) (Math.pow(expo,src_rgb));
      result = (int) ((result*scaleval) + offsetval);
      if (result > 255) result = 255;
      if (result < 0) result = 0;
      dest_1d[i] = 0xff000000 | result << 16 | result << 8 | result;
    }
    return dest_1d;
  }
}

  
