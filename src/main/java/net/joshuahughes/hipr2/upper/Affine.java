package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 *Affine is an algorithm to apply an Affine Trasformation on an image
 *It's not very efficient, and assumes that the output array has the
 *same size as the input array. Efficiency could be much improved by iterating
 *over all output pixels, back projecting to the corresponding input pixel
 *and interpolating to overcome aliasing.
 *@author:Timothy Sharman
 *@see code.iface.affine
 */

public class Affine extends Thread{

  //the width of the input images in pixels
  private int i1_w;   //The width of the input image - currently unused
  private int i2_w;   //The height of the input image - currently unused

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int[] dest_1d;
 

  /**
   *Constructs a new Affine operator
   *@param firstwidth The width of the input image - currently unused
   *@param secondwidth The height of the input image - currently unused
   */

  public Affine(int width, int height){
     i1_w = width;
     i2_w = height;
  }
  
  /**
   *Applies the Affine operator on the specified image array
   *@param src_1d The first source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param a_array A 2x2 array used to find the transformation
   *@param b_array A 1x1 array used to find the transformation
   *@return A pixel array containing the transformed image
   */

  //Bob's affine transformation algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) split each pixel into 100 subpixels
    d) apply the operation (eg Affine)
    e) create int value 0xffCDCDCD
    */
  
  public int [] affine_transform(int [] src_1d, int width, int height, 
				 float [] a_array, float [] b_array) {
    
    //The output array
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];

    //Used to divide each pixel into 100
    int j = 0;
    int k = 0;

    float tmpj = 0;
    float tmpk = 0;

    //Local variables

    int [] tmp_1d = new int[d_w * d_h];
    int outpos = 0;
    int x_pos = 0;
    int y_pos = 0;
    int src_rgb;
    float x_aff;
    float y_aff;
    int x_out;
    int y_out;
    float x_tmp;
    float y_tmp;
    int result;

    //Used to count how many subpixels hit each output pixel
    int [] count = new int[d_w * d_h];

    //initialise the two arrays used for the computation

    for (int y = 0; y < dest_1d.length; y++) {
      tmp_1d[y] = 0x00000000;
      dest_1d[y] = 0x00000000;
      count[y] = 0;
    }
     
    //Now update the two arrays
    for(int i = 0; i < dest_1d. length; i++) {
      y_pos = find_y(i, d_w); 
      x_pos = (i - (y_pos * d_w));
      src_rgb = src_1d[i] & 0x000000ff;
      for (j = 0; j < 10; j++) {
	for (k = 0; k < 10; k++) {
	  //Work out the sub pixel postion
	  tmpj = j;
	  tmpk = k;
	  x_tmp = x_pos + (tmpj / 10);
	  y_tmp = y_pos + (tmpk / 10);

	  //Multiply by the transformation matrices
	  x_aff = (((a_array[0]*x_tmp) + (a_array[1]*y_tmp)) + (b_array[0]));
	  y_aff = (((a_array[2]*x_tmp) + (a_array[3]*y_tmp)) + (b_array[1]));
	  
	  //Convert back to integer
	  x_out = (int)Math.floor(x_aff);
	  y_out = (int)Math.floor(y_aff);

	  //Check if the answer is outside image range
	  if (x_out > (d_w-1) || x_out < 0 || y_out > (d_h-1) || y_out <
0){}
	  
	  else{

	  //Find position in output array
	  outpos = (x_out + (d_w*y_out));

	  //Increment arrays
	  tmp_1d[outpos] = (tmp_1d[outpos] + src_rgb);
	  count[outpos]++;
	  }
	}
      }
    }

    //Now scale down the destination arrays by the number of hits

    for (int m = 0; m < dest_1d.length; m++) {
      if(count[m] == 0){
	//set to black
	result = 0;
      }
      else {
	result = (tmp_1d[m] / count[m]);

	//clip values
	if(result > 255) {
	result = 255;
	}
	else if(result < 0) {
	result = 0;
	}
	
      }
      dest_1d[m] =  0xff000000 | (result + (result << 16) + (result << 8));
    }
	
    return dest_1d;
	    
  }


  /**
   *Used to find the y position of the current point
   */

  private int find_y(int offset, int width) {
    int ans = 0;
    while(offset >= width){
      ans ++;
      offset = offset - width;
      }
    return ans;
  }
  
}
