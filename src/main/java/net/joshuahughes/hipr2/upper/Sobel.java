package net.joshuahughes.hipr2.upper;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *Sobel is an algorithm to apply the sobel edge detector operator
 *@author:Timothy Sharman
 *@see code.iface.sobel
 */

public class Sobel extends Thread{


  //The width and height of the output
  private int d_w;
  private int d_h;

  private int[] dest_1d;


  /**    
   *apply_sobel applies a sobel operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param sobscale A scale factor for the image
   *@param offsetval The offset to be added to the output
   *
   *@return A pixel array containing the output image
   */
  
  
  //Bob's sobel algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg sobel). 
    d) return the result
    */ 
  
  
  
  public int [] apply_sobel(int [] src_1d, int width, int height, double sobscale,
			    float offsetval) {
    
    int i_w = width;
    int i_h = height;
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    
    for(int i=0;i<src_1d.length;i++){
      try {
	
	int a = src_1d[i] & 0x000000ff;
	int b = src_1d[i+ 1] & 0x000000ff;
	int c = src_1d[i+ 2] & 0x000000ff;
	int d = src_1d[i + i_w] & 0x000000ff;
	int e = src_1d[i + i_w + 2] & 0x000000ff;
	int f = src_1d[i + 2*i_w ] & 0x000000ff;
	int g = src_1d[i + 2*i_w + 1] & 0x000000ff;
	int h = src_1d[i + 2*i_w + 2] & 0x000000ff;
	int hor = (a+d+f) - (c+e+h);
	if (hor < 0) hor = -hor;
	int vert = (a+b+c) - (f+g+h);
	if (vert < 0) vert = -vert;
	short gc = (short) (sobscale * (hor + vert));
	gc = (short) (gc + offsetval);
	if (gc > 255) gc = 255;
	dest_1d[i] = 0xff000000 | gc<<16 | gc<<8 | gc;
	
        //reached borders of image so goto next row
        //(see Convolution.java)
        if (((i+3)%i_w)==0)  {
	  dest_1d[i] = 0;
	  dest_1d[i+1] = 0;
	  dest_1d[i+2] = 0;
	  i+=3;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
	//if reached row boudary of image return.
	i = src_1d.length;
      }
    }
    return dest_1d;
  }

}
