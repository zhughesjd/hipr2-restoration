package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;

import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *ImageNot is an algorithm to apply logical NOT to an image
 *@author:Timothy Sharman
 *@see code.iface.imagenot
 */

public class ImageNot extends Thread{


  //The width and height of the output
  private int d_w;
  private int d_h;

  private int[] dest_1d;


  /**    
   *apply_invert applies an inversion operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *
   *@return A pixel array containing the inverted input image
   */


  //Bob's noise algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg invert). 
    d) return the result
    */
  
  
  public int [] apply_invert(int [] src_1d, int width, int height) {
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    int src_rgb;
    int result = 0; 
    
    
    for(int i=0;i<src_1d.length;i++){
  	
      //Select required bits from 32 bit integer
      src_rgb = src_1d[i] & 0x000000ff;
      
      //Calculate the result
      result = 255 - src_rgb;
      
      //Convert back to 32 bit integer value
      dest_1d[i] = 0xff000000 | result<<16 | result<<8 | result;
      
    }
    
   return dest_1d; 
   
  }
}


