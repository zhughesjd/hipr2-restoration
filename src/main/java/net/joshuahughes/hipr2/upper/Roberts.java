package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *RobertsCross is an algorithm to apply the roberts cross operator
 *@author:Timothy Sharman
 *@see code.iface.roberts
 */

public class Roberts extends Thread{


  //The width and height of the output
  private int d_w;
  private int d_h;

  private int[] dest_1d;


  /**    
   *apply_roberts applies a roberts cross operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param robscale A scale factor for the image
   *@param offsetval The offset to be added to the output
   *
   *@return A pixel array containing the output image
   */


  //Bob's roberts cross algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg roberts cross). 
    d) return the result
    */
  
  
  public int [] apply_roberts(int [] src_1d, int width, int height, double robscale,
			      float offsetval) {
    
    int i_w = width;
    int i_h = height;
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    
    
    int blue,a,b,c,d;
    for(int i=0;i<src_1d.length;i++){
      try {
	
	a = src_1d[i] & 0x000000ff;
	b = src_1d[i+ 1] & 0x000000ff;
	c = src_1d[i + i_w] & 0x000000ff;
	d = src_1d[i + i_w + 1] & 0x000000ff;
	int r1 = a-d;
	if (r1 < 0) r1 = -r1;
	int r2 = b-c;
	if (r2 < 0) r2 = -r2;
	short gc = (short) (robscale * (r1 + r2));
	gc = (short) (gc + offsetval);
	if (gc > 255) gc = 255;
	dest_1d[i] = 0xff000000 | gc<<16 | gc<<8 | gc;
	
	//reached borders of image so goto next row
	//(see Convolution.java)
	if (((i+2)%i_w)==0)  {
	  dest_1d[i] = 0;
	  dest_1d[i+1] = 0;
	  i+=2;
	}
      } catch (ArrayIndexOutOfBoundsException e) {
	//if reached row boudary of image return.
	i = src_1d.length;
      }
    }
    return dest_1d;
  }
}
