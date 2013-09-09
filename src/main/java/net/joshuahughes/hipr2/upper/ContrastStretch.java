package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *Contrast Stretch stretches the contrast range of an image
 *
 *@author Tim Sharman
 *@version July 1999 
 *@see code.iface.contrast
 */

public class ContrastStretch extends Thread{
  
  //width and height of the image
  private int i_w=0;
  private int i_h=0;
 
  //pixel arrays for input image and destination image
  private int[] dest_1d;
  private int [] src_1d;
  
  /**
   *Creates an image array for an image which has been contrast stretched
   *@param src Image array to be stretched
   *@param width the width of the image
   *@param height the height of the image
   *@param uplim The upper limit for the values to be stretched between
   *@param lolim The lower limit for the values to be stretched between
   *@return An image array representing the stretched image
   */

  public int[]  normal_stretch(int [] src, int width, int height, 
			       int uplim, int lolim){
    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    dest_1d = new int[i_w*i_h];
    int min = src_1d[0] & 0x000000ff;
    int max = src_1d[0] & 0x000000ff;
    int src_rgb;
    int result;
    float quotient = 0;
 
    //Find the max and min values   
    for(int i = 0; i < src_1d.length; i++){
      src_rgb = src_1d[i] & 0x000000ff;
      if(src_rgb < min){
	min = src_rgb;
      }
      else if(src_rgb > max){
	max = src_rgb;
      }
      
    }
    //Work out (b-a)/(d-c)
    quotient = (((float)(uplim - lolim))/((float)(max - min)));
    //Calculate the output values
    for(int i = 0; i < src_1d.length; i++){
      src_rgb = src_1d[i] & 0x000000ff;
      result = (int)( (src_rgb - min) * quotient )+lolim;
      dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
  }

  
  /**
   *Creates an image array for an image which has been contrast stretched
   *In this case it is done using the cutoff method
   *@param src Image array to be stretched
   *@param width The width of the image
   *@param height The height of the image
   *@param cutoff The scale value used in cutting off low values
   *@param uplim The upper limit for the values to be stretched between
   *@param lolim The lower limit for the values to be stretched between
   *@return An image array representing the stretched image
   */
  
  public int[]  cutoff_stretch(int [] src, int width, int height, float cutoff, 
			       int uplim, int lolim){
    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    int [] inter = new int[i_w*i_h];
    dest_1d = new int[i_w*i_h];
    int max = 0;
    int min;
    int src_rgb;
    int result;
    int scale;
    float quotient = 0;
    int [] hist = new int[256];

    //Initialise the distribution array
    for(int i = 0; i < 256; i++){
      hist[i] = 0;
    }
    
    //Find the distribution of intensity values
    for(int i = 0; i < src_1d.length; i++) {
      src_rgb = src_1d[i] & 0x000000ff;
      hist[src_rgb]++;
    }

    //Find max value of the intensity values
    for(int i = 0; i < hist.length; i++) {
      if(hist[i] > max){
	max = hist[i];
      }
    }
    //Calculate the minimum amount of intensity required
    //in order to be stretched
    scale = (int) ((float) max * cutoff);
    
    //Find the first and last intensity values which are above the cutoff
    int first = 0;
    int last = 0;
    //Flag indicating if either value has been found yet
    boolean flag = false;
    for(int i = 0; i < hist.length; i++){
      if(!flag){
	if(hist[i] >= scale){
	  first = i;
	  flag = true;
	}
      }
    }
    flag = false;
    for(int i = 255; i >= 0; i--){
      if(!flag){
	if(hist[i] >= scale){
	  last = i;
	  flag = true;
	}
      }
    }
    //Find the max and min values   
    max = last;
    min = first;
    
    //Work out (b-a)/(d-c)
    quotient = (((float)(uplim - lolim))/((float)(max - min)));
    
    //Calculate the output values
    for(int i = 0; i < src_1d.length; i++){
      result = (int)( ((src_1d[i]&0x000000ff) - min) * quotient )+lolim;
      if(result < 0){result = 0;}
      if(result > 255){result = 255;}
      dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
  }
  

  /**
   *Creates an image array for an image which has been contrast stretched
   *In this case it is done using the percentile method
   *@param src Image array to be stretched
   *@param width The width of the image
   *@param height The height of the image
   *@param high The upper percentage value for the histogram cutoff
   *@param low The lower percentage value for the histogram cutoff
   *@param uplim The upper limit for the values to be stretched between
   *@param lolim The lower limit for the values to be stretched between
   *@return An image array representing the stretched image
   */
  
  public int[]  percentile_stretch(int [] src, int width, int height, float high, 
				   float low, int uplim, int lolim){
    
    i_w = width;
    i_h = height;
    src_1d = new int[i_w * i_h];
    src_1d = src;
    int [] hist = new int[256];
    dest_1d = new int[i_w*i_h];
    int max;
    int min;
    int src_rgb;
    float lowpercent = low;
    float highpercent = high;
    int result;
    float quotient = 0;
    int tmp, tmp2;
    boolean flag = false;

    //Initialise the distribution array
    for(int i = 0; i < 256; i++){
      hist[i] = 0;
    }
    
    //Find the distribution of intensity values
    for(int i = 0; i < src_1d.length; i++) {
      src_rgb = src_1d[i] & 0x000000ff;
      hist[src_rgb]++;
    }
    
    //Edit the input array so that anything outside the range 
    //is set to zero
    tmp = 0;
    while(!flag){
      //Check if setting the next section to zero will take you past limit
      if((low - (((float)hist[tmp])/((float)(i_w*i_h)))) >= 0){
	//If not set to zero and reduce the limit
	low = low - (((float)hist[tmp])/((float)(i_w*i_h)));
	hist[tmp] = 0;
	tmp++;
      }
      else{
	/**Otherwise reduce by the required fraction to bring to zero
	 *Note: Casting to int makes this inexact, so it is unlikely
	 *that zero will be exactly reached. However the process is
	 *close enough to make little difference
	 */
	hist[tmp] = hist[tmp] - (int)(low * (float)(i_w*i_h));
	flag = true;
      }
    }

    tmp = 255;
    flag = false;
    while(!flag){
      //Check if setting the next section to zero will take you past limit
      if((high + (((float)hist[tmp])/((float)(i_w*i_h)))) <= 1){
	high = high + (((float)hist[tmp])/((float)(i_w*i_h)));
	hist[tmp] = 0;
	tmp--;
      }
      else{
	//Otherwise increase by the required fraction to bring to one
	hist[tmp] = hist[tmp] + (int)(high * (float)(i_w*i_h));
	flag = true;
      }
    }

    //Process image now that low values have been eliminated
    
    //Find the max and min values that are within the range
    
    //Find the first value pixel within the range
    flag = false;
    tmp = 0;
    while(!flag){
      src_rgb = src_1d[tmp] & 0x000000ff;
      if(hist[src_rgb] != 0){
	flag = true;
      }
      else {
	tmp++;
      }
    }
    
    //Set the max and min according to this value
    max = src_1d[tmp] & 0x000000ff;
    min = src_1d[tmp] & 0x000000ff;
    
    //Now check the rest of the image
    for(int i = 0; i < src_1d.length; i++){
      src_rgb = src_1d[i] & 0x000000ff;
      if((src_rgb < min) && (hist[src_rgb] != 0)){
	min = src_rgb;
      }
      else if((src_rgb > max) && (hist[src_rgb] != 0)){
	max = src_rgb;
      }
      
    }

    //Work out (b-a)/(d-c)
    quotient = (((float)(uplim - lolim))/((float)(max - min)));
    
    //Calculate the output values
    for(int i = 0; i < src_1d.length; i++){
      src_rgb = src_1d[i] & 0x000000ff;
      result = (int)( (src_rgb - min) * quotient )+lolim;
      if(result < 0){result = 0;}
      if(result > 255){result = 255;}
      dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
  }
}





