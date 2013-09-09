package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.Color.*;
import java.lang.Math.*;

/**
 *LineDetector is an algorithm to find the lines in an image
 *@author:Timothy Sharman
 *@see code.iface.linedet
 */

public class LineDetector extends Thread{

  //The Convolution operator used as part of the process.
  Convolution convolution;

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int [] dest_1d;

  //Create a new version of twoimages to put the output images in
  TwoImages twoimages = new TwoImages();

  //The kernels to be used in the detection function
  private double [][] ker4 = {{-0.1667,-0.1667,-0.1667},{0.3333,0.3333,0.3333},{-0.1667,-0.1667,-0.1667}};
  private double [][] ker2 = {{-0.1667,-0.1667,0.3333},{-0.1667,0.3333,-0.1667},{0.3333,-0.1667,-0.1667}};
  private double [][] ker3 = {{0.3333,-0.1667,-0.1667},{-0.1667,0.3333,-0.1667},{-0.1667,-0.1667,0.3333}};
  private double [][] ker1 = {{-0.1667,0.3333,-0.1667},{-0.1667,0.3333,-0.1667},{-0.1667,0.3333,-0.1667}};

  /**
   *Applies the line detector to the input image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param kernels Integer which determines which kernels are applied :
   * 1 = Kernel 1, 2 = Kernel 2, 4 = Kernel 3, 8 = Kernel 4. 
   * These values are summed and the result is in kernels
   *@param thresh The threshold limit for kernel detection
   *@param choice Boolean which specifies whether to apply thresholding
   *@param scale The scale value to apply to the output
   *@param offset The offset to add to the output
   *@return The output array with the line edges and the colored edge image
   */

  //Tim's Line Detection Algorithm
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the selected kernels to the image
    d) return the output image
    */

  public TwoImages apply_lineDetect(int [] src_1d, int width, int height,
				    int kernels, int thresh, boolean choice,
				    float scale, float offset){

    d_w = width;
    d_h = height;
    dest_1d = new int[d_w*d_h];
    int [] label_1d = new int[d_w*d_h];
    double [] tmp_1d = new double[d_w*d_h];
    double [] tmp2_1d = new double[d_w*d_h];
    double [][] src_2d = new double[d_w][d_h];

    //Initialise the destination and label array
    for(int i = 0; i < dest_1d.length; i++){
      tmp2_1d[i] = -1000.0;
      label_1d[i] = 0xff000000;
    }

    //Change the source array into 2d double array for use with 
    //convolution operator.
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
//if (i==32 && j==100) System.out.println("Convolve bug: "+i+"+"+(j*d_w)); /// 26432 264 w 268 h

	src_2d[i][j] = (double)(src_1d[i+(j*d_w)] & 0x000000ff);
//if (i==32 && j==100) System.out.println("Convolve data 2d: "+src_2d[i][j]+" "+src_1d[i+(j*d_w)]);
      }
    }

    //Find if the first kernel needs to be applied
    if((kernels & 1) == 1){
      tmp_1d = convolution. convolutionDoublePadded(src_2d, d_w, d_h, ker1, 3, 3);
      for(int i = 0; i < tmp_1d.length; i++){
	if(tmp_1d[i] > tmp2_1d[i]){
	  tmp2_1d[i] = tmp_1d[i];
	  label_1d[i] = 0xffffff00;
	}
      }
    }

    if((kernels & 2) == 2){
      tmp_1d = convolution. convolutionDoublePadded(src_2d, d_w, d_h, ker2, 3, 3);
      for(int i = 0; i < tmp_1d.length; i++){
	if(tmp_1d[i] > tmp2_1d[i]){
	  tmp2_1d[i] = tmp_1d[i];
	  label_1d[i] = 0xffff0000;
	}
      }
    }
    
    if((kernels & 4) == 4){
      tmp_1d = convolution. convolutionDoublePadded(src_2d, d_w, d_h, ker3, 3, 3);
      for(int i = 0; i < tmp_1d.length; i++){
	if(tmp_1d[i] > tmp2_1d[i]){
	  tmp2_1d[i] = tmp_1d[i];
	  label_1d[i] = 0xff00ff00;
	}
      }
    } 

    if((kernels & 8) == 8){
      tmp_1d = convolution. convolutionDoublePadded(src_2d, d_w, d_h, ker4, 3, 3);
      for(int i = 0; i < tmp_1d.length; i++){
//if (i==26432) System.out.println("Convolve bug tmp: "+tmp_1d[i]+" tmp2: "+tmp2_1d[i]);
	if(tmp_1d[i] > tmp2_1d[i]){
	  tmp2_1d[i] = tmp_1d[i];
	  label_1d[i] = 0xff0000ff;
	}
      }
    } 

    // convert to integer
    double tmp_rgb;
    int dest_rgb;
    for(int i = 0; i < tmp2_1d.length; i++){

      //Find the absolute edge strength
      tmp_rgb = tmp2_1d[i];

      //Apply the threshold if necessary
      if(choice){
	if(tmp_rgb > thresh){
	  //No change in value
	}
	else{
	  tmp_rgb = 0;
	  label_1d[i] = 0xff000000;
	}
      }

      //Apply scaling and offset
      dest_rgb = (int)((scale*tmp_rgb)+offset);

      //Clip values
      if(dest_rgb < 0){dest_rgb = 0;}
      if(dest_rgb > 255){dest_rgb = 255;}
      
      //Output them
      dest_1d[i] = 0xff000000 | (dest_rgb + (dest_rgb << 16) 
				 + (dest_rgb << 8));
    }
    
    //Set the output images of the return class
    twoimages. image1 = dest_1d;
    twoimages. image2 = label_1d;
    return twoimages;
  }
}
