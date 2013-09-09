package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 *Threshold is an algorithm to apply thresholding to an image.
 *@author: Konstantinos Koryllos
 *@author Judy Robertson SELLIC OnLine
 *@see code.iface.threshold
 */

public class Threshold extends Thread{
  
  //the width and height of this image in pixels
  private int i_w, i_h;
  
  //pixel arrays for input and output images
  private int[] src_1d;
  private int[] dest_1d;
  
  /**
   *Constructs a new Threshold
   */
  public Threshold(){
    
  }
  
  /** 
   *Applies the thresholding operator with the specified threshold
   *value to the specified image array
   *
   *@param src pixel array representing image to be thresholded
   *@param width width of the image in pixels
   *@param height height of the image in pixels
   *@param value threshold value
   *@return a thresholded pixel array  of the input image array
   */
  public int [] threshold(int [] src, int width, int height, int value){
    
    i_w = width;
    i_h = height;
//***    src_1d = new int[i_w * i_h];
    dest_1d = new int[i_w*i_h];
    src_1d = src;
 
    apply_threshold(value);
    
    return dest_1d;
  }

   /** 
   *Applies the thresholding operator between the specified threshold
   *values to the specified image array
   *
   *@param src pixel array representing image to be thresholded
   *@param width width of the image in pixels
   *@param height height of the image in pixels
   *@param low lower threshold value
   *@param high higher threshold value
   *@return a thresholded pixel array  of the input image array
   */

  
  public int [] twothreshold(int [] src, int width, int height, int low, int high){
     
    i_w = width;
    i_h = height;
    dest_1d = new int[i_w*i_h];
    src_1d = src;
    
    apply_two_threshold(low, high);
    return dest_1d;
  }
  

  //applies threshold to black-white images
  private void  apply_threshold(int thresh) {

    int blue = 0; 
    for (int i=0; i<src_1d.length; i++) {
      blue = src_1d[i] & 0x000000ff; 
      dest_1d[i] = (blue>=thresh)?0xffffffff:0xff000000;
    }
  }

  //applies two thresholds to black and white image
  //if a pixel is between the threshold values then colour it white
  //other wise it should be black
  private int []  apply_two_threshold(int low, int high) {
    
    int blue = 0; 

    for (int i=0; i<src_1d.length; i++){
      blue = src_1d[i] & 0x000000ff; 
      if ((blue<= high) && (blue >= low)) {
	dest_1d[i] = 0xffffffff;
      } else dest_1d[i] = 0xff000000;
    }
    return dest_1d;
  }
  
  
  /**
   *Turns all the black pixels in the specified image array to white and vice versa
   *@param src The black and white pixel array whose values are to be inverted
   *@return A pixel array which is the  negative of the input image
   */

  public int[]  flip_the_pixels(int [] src ,int w, int h){

    i_w = w;
    i_h = h;
    src_1d = new int[src.length];
    src_1d = src;
    dest_1d = new int[src.length];
    
    //now invert the pixels
    for (int i=0;i<dest_1d.length;i++) {
      dest_1d[i] = (src_1d[i]==0xff000000)?0xffffffff:0xff000000;
    }
    
    return dest_1d;
  }

}








