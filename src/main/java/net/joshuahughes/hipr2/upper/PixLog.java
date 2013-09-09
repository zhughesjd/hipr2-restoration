package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;

/**
 *PixLog is an algorithm to apply a log function to an image intensity values
 *@author:Timothy Sharman
 *@author:Bob Fisher
 *@see code.iface.pixlog
 */

public class PixLog extends Thread{


  //The width and height of the output
  private int d_w;
  private int d_h;

  private int[] dest_1d;


  /**    
   *apply_log applies a logarithm operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param logscale The value to scale the image by
   *@param offsetval The value to be added to the output
   *
   *@return A pixel array containing the logarithm image
   */
  
  
  //Tim's log algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg log).
    d) scale the result
    e) return the result
    */
  
  
  public int [] apply_log(int [] src_1d, int width, int height, double logscale,
			  float offsetval) {
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    int src_rgb;
    int result = 0; 
    
    
    for(int i = 0; i < dest_1d. length; i++) {
      
      //Select required bits from image
      src_rgb = src_1d[i] & 0x000000ff;
      
      //Perform operation
      result = (int) (logscale * Math.log(src_rgb + 1.0));
      result = (int) (result + offsetval);
      if (result > 255) result = 255;
      if (result < 0) result = 0;
      dest_1d[i] = 0xff000000 | result << 16 | result << 8 | result;
    }
    return dest_1d;
  }
}

