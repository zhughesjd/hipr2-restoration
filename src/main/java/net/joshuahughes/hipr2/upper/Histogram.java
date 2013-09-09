package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *Histogram creates a histogram of a specified image array and returns an
 *grey_scales*hist_height image array to represent the histogram
 *
 *@author Bob Fisher
 *@author Tim Sharman
 *@author Nathalie Cammas
 *@version August 1999
 *@version August 2000
 */

public class Histogram extends Thread{
  
  //no of grey-scale values 
  private final int grey_scales = 256;
  private final int hist_height = 256;

  // histogram colors
  private final int fgcolor = 0xff000000;
  private final int bgcolor = 0xffffffff;
  
  //width and height of the image
  private int i_w=0;
  private int i_h=0;
 
  //pixel arrays for input image and histogram
  private int [] hist_1d;
  private int [] src_1d;
  
  //width of histogram.  
  public int hist_w = 256; 
  
  //array for histogram bins
  private double [] hist_values;


  /**
   *Creates a 256*256 image array for the histogram of the specified image array
   *@param src Image array to be turned into a histogram
   *@param width The width of the input image
   *@param height The height of the input image
   *@return An image array representing the histogram of the input image
   */

  public int[]  histogram(int [] src, int width, int height){
    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    
    hist_1d = new int[hist_w*i_h];
    int gs = grey_scales; 
    hist_values = new double[gs];
    
    //histogram "normalisation"
    double increment = 1.0/256.0; 
    
    //the grayscale value appearing more often.
    double max_value = 0.0;
    
    //to spread over 256 vertical values of histogram
    int scale_factor = 0; 
    int blue;
    int l = hist_1d.length;
    int n = 1; 
    
    for(int i=0; i<src_1d.length; i++){
      blue = src_1d[i] & 0x000000ff;
      hist_values[blue] += increment;
      max_value = (hist_values[blue]>max_value)?hist_values[blue]:max_value;
    }
    
    scale_factor = (int) Math.floor(i_h / max_value);
    
    for(int i=0;i<hist_values.length;i++){
      hist_values[i] *= scale_factor;
    }
    
    //this is the bit which represents the hist bins as black
    //lines.
    
    for(int i = 0; i < hist_values.length; i++){
      while (hist_values[i] > 0){
	  hist_1d[(l - gs*n) + i] = fgcolor;
	hist_values[i] -= 1;
	n++;
      }
      n=1;
    }
    
    return hist_1d ;  
  }

  /**
   *Calculates the peak value of the histogram
   *@param the image
   *@param the width of the image
   *@param the height of the image
   *@return the peak value in the histogram 
   */

  public int  peak (int [] src, int width, int height){
  
    int result = 0;    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    
    hist_1d = new int[hist_w*i_h];
    int gs = grey_scales; 
    hist_values = new double[gs];
    
    //histogram "normalisation"
    double increment = 1.0/256.0; 
    
    //the grayscale value appearing more often.
    double max_value = 0.0;
    
    //to spread over 256 vertical values of histogram
    int scale_factor = 0; 
    int blue;
    int l = hist_1d.length;
    int n = 1; 
    
    for(int i=0; i<src_1d.length; i++){
      blue = src_1d[i] & 0x000000ff;
      hist_values[blue] += increment;
      max_value = (hist_values[blue]>max_value)?hist_values[blue]:max_value;
      
    }
    

    for (int i = 0; i<hist_values.length;i++){
      if (hist_values[i] == max_value) {
	result = i;
      }
    }
     
  return result;
}
  /**
   *Calculates the maximum value in the histogram
   *@param the image
   *@param the width of the image
   *@param the height of the image
   *@return the maximum value in the histogram
   */

  public double  maximum (int [] src, int width, int height){
  
    int result = 0;    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    
    hist_1d = new int[hist_w*i_h];
    int gs = grey_scales; 
    hist_values = new double[gs];
    
    //histogram "normalisation"
    double increment = 1.0/256.0; 
    
    //the grayscale value appearing more often.
    double max_value = 0.0;
    
    //to spread over 256 vertical values of histogram
    int scale_factor = 0; 
    int blue;
    int l = hist_1d.length;
    int n = 1; 
    
    for(int i=0; i<src_1d.length; i++){
      blue = src_1d[i] & 0x000000ff;
      hist_values[blue] += increment;
      max_value = (hist_values[blue]>max_value)?hist_values[blue]:max_value;
      
    }
    

   return max_value * 256;
  }

  /**
   *Calculates the histogram and scales it.
   *@param the image
   *@param the width of the image
   *@param the height of the image
   *@param the value to set the histogram maximum
   *@return the int array containing the histogram scaled
   */
  public int[]  histogramScale(int [] src, int width, int height, double max){
    
    i_w = width;
    src_1d = src;
    
    hist_1d = new int[grey_scales*hist_height];
    hist_values = new double[grey_scales];
    
    //histogram "normalisation"
    //to spread over hist_height vertical values of histogram
    double increment = ((double)hist_height)/max; 
    int blue;
    
    for(int i=0; i<src_1d.length; i++){
      blue = src_1d[i] & 0x000000ff;
      hist_values[blue] += increment;
    }
    
    //draw the hist bins as lines
    int n; 
    for(int i = 0; i < hist_1d.length; i++){ hist_1d[i] = bgcolor; }
    for(int i = 0; i < hist_values.length; i++){
      if (hist_values[i] > hist_height) { hist_values[i] = hist_height; }
      n=1;
      while (hist_values[i] > 0){
	hist_1d[(hist_1d.length - grey_scales*n) + i] = fgcolor;
	hist_values[i] -= 1;
	n++;
      }
    }
    return hist_1d ;  
  }

}




