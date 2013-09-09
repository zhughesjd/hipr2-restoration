package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.Math.*;

/**
 *HistEqualize histogram equalizes an image
 *
 *@author Tim Sharman
 *@version July 1999 
 *@see code.iface.equalize
 */

public class HistEqualize extends Thread{
  
  //width and height of the image
  private int i_w=0;
  private int i_h=0;
 
  //pixel arrays for input image and destination image
  private int[] dest_1d;
  private int [] src_1d;

  //Function to return the maximum of two ints
  int max(int a, int b){
    if(a > b){
      return a;
    }
    else {
      return b;
    }
  }
  
  /**
   *Creates an image array for an image which has been histogram equalized
   *@param src Image array to be equalized
   *@param width the width of the image
   *@param height the height of the image
   *@return An image array representing the equalized image
   */

  public int[]  equalize(int [] src, int width, int height){

    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    dest_1d = new int[i_w*i_h];
    int result;
    float tmp, tmp2, tmp3;


    //The array of values corresponding to grey level frequencies
    int [] nArray = new int[256];
  
    int pix;

    int src_rgb;

    pix = i_w*i_h;

    //Initialise the n array

    for(int i = 0; i < 256; i++) {
      nArray[i] = 0;
    }

    //Create the n array

    for(int i = 0; i < src_1d.length; i++) {
      src_rgb = src_1d[i] & 0x000000ff;
      nArray[src_rgb]++;
    }

    //Now calculate the new intensity values
    for(int i = 0; i < src_1d.length; i++){
      src_rgb = src_1d[i] & 0x000000ff;
      tmp3 = 0;
      for (int j = 0; j < (src_rgb+1); j++) {
	tmp = (float)nArray[j];
	tmp2 = (float)pix;
	tmp3 = tmp3 + (tmp/tmp2);
      }
      result = (int) (tmp3*255);
      if (result > 255 ) { result = 255;}
      if (result < 0 ) { result = 0;}
      dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
  }
}







