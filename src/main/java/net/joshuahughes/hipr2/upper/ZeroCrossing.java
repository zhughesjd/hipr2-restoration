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
 *ZeroCrossing is an algorithm to find the zero crossings in an image
 *@author:Timothy Sharman
 */

public class ZeroCrossing extends Thread{

  //The Convolution operator used as part of the process.

  Convolution convolution;

  //The Gaussian Smoothing operator used as part of the process
  
  GaussianSmooth gaussiansmooth;

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int [] dest_1d;

  //The possibls kernels to be used in the Laplacian function

  private double [][] laplacian1 = {{0,1,0},{1,-4,1},{0,1,0}};
  private double [][] laplacian2 = {{1,1,1},{1,-8,1},{1,1,1}};
  private double [][] laplacian3 = {{-1,2,-1},{2,-4,2},{-1,2,-1}};


  /**
   *Applies the zero crossing detector to the input image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param kersize The size of kernel to be applied
   *@param theta The standard deviation of the gaussian smoothing
   *@param limiter specifies whether to limit the zero crossing
   *@param limit The limit applied to the zero crossings
   *@return The output array with the zero crossing points
   */

  //Tim's Zero Crossing Detection Algorithm
  /*
    a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) Apply gaussian smoothng to the image
    d) Convolve the laplacian kernel with the image
    e) Find the points where the answer switches from +ve to -ve
    f) If required limit these points using the supplied thereshold
    g) Return the zero crossing image
    */

  public int [] apply_zeroCrossing(int [] src_1d, int width, int height, 
				   int kersize, float theta, boolean limiter,
				   float limit){
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w*d_h];
    int [] tmp_1d;
    double [] lapResult;
    double [][] tmp_2d;
    double thresh;

    //Set the threshold value
    if(limiter){
      thresh = limit;
    }
    else {
      thresh = 0;
    }

    //Initialise the output array
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = Color.black.getRGB();
    }

    tmp_1d = new int[d_w*d_h];

    //Smooth the initial image
    tmp_1d = gaussiansmooth. smooth_image(src_1d, width, height, 
					  kersize, theta);

    // Create an array to be used for the laplacian convolution result
    lapResult = new double [d_w*d_h];

    // Create a temp 2d int array
    double [][] tmp_2ddouble = new double[d_w][d_h];

    //Convert tmp_1d from 1_d to 2_d 
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	tmp_2ddouble[i][j] = (double)(tmp_1d[i+(j*d_w)] & 0x000000ff);
      }
    }

    //Apply the laplacian operator
    lapResult = convolution. convolutionDoublePadded(tmp_2ddouble, d_w, d_h, 
						     laplacian1, 3, 3);

    //Now need to find points in the array which are zero crossings  

    //First convert from 1_d to 2_d for ease of processing
    tmp_2d = new double [d_w][d_h];

    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	tmp_2d[i][j] = lapResult[i+(j*d_w)];
      }
    }
    
    /**
     *To find the zero crossings in the image you must check each point 
     *in the array to see if it lies on a zero crossing. This is done by
     *checking the neighbours around the pixel.
     */

    int [][] tmp2_2d = new int[d_w][d_h];

    for(int i = 1; i < (d_w-1); i++){
      for(int j = 1; j < (d_h-1); j++){
	tmp2_2d = check_neighbours(tmp_2d, i, j, tmp2_2d, thresh);
      }
    }
  
    //Rescale again.
    d_w = d_w - 3;
    d_h = d_h - 3;

    //Convert the output from 2_d to 1_d 
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	dest_1d[i+(j*d_w)] = tmp2_2d[i][j];
      }
    }

    d_w = d_w + 3;
    d_h = d_h + 3; 

    return dest_1d;

  }

  /**
   *check_neighbours is used to check the neighbourhood of a 
   *pixel to see if there is a zero crossing at the pixel
   *currently being considered. This is done by seeing if there
   *is a change in sign between the two opposite pixels on either
   *side of the middle pixel. This is done in each of the four
   *relevant directions. If there is a change the point is set to
   *white and if there is no change it is set to black
   */ 
  
  private int [][] check_neighbours(double [][] tmp_2d, int i, int j, 
				    int [][] tmp2_2d, double thresh){

    /*If when neighbouring points are multiplied the result is -ve
     *then there must be a change in sign between these two points.
     *If the change is also above the thereshold then set it as a 
     *zero crossing.
     */

    if(tmp_2d[i-1][j]*tmp_2d[i+1][j] < 0){
      if(Math.abs(tmp_2d[i-1][j]) + Math.abs(tmp_2d[i+1][j]) > thresh){
	tmp2_2d[i-1][j-1] = Color.white.getRGB();
      }
      else {
	tmp2_2d[i-1][j-1] = Color.black.getRGB();
      }
    }
    
    else if(tmp_2d[i-1][j-1]*tmp_2d[i+1][j+1] < 0){
      if(Math.abs(tmp_2d[i-1][j-1])+Math.abs(tmp_2d[i+1][j+1]) > thresh){
	tmp2_2d[i-1][j-1] = Color.white.getRGB();
      }
      else {
	tmp2_2d[i-1][j-1] = Color.black.getRGB();
      }
    }

    else if(tmp_2d[i+1][j+1]*tmp_2d[i-1][j-1] < 0){
      if(Math.abs(tmp_2d[i+1][j+1])+Math.abs(tmp_2d[i-1][j-1]) > thresh){
	tmp2_2d[i-1][j-1] = Color.white.getRGB();
      }
      else {
	tmp2_2d[i-1][j-1] = Color.black.getRGB();
      }
    }

    else if(tmp_2d[i][j-1]*tmp_2d[i][j+1] < 0){
      if(Math.abs(tmp_2d[i][j-1])+Math.abs(tmp_2d[i][j+1]) > thresh){
	tmp2_2d[i-1][j-1] = Color.white.getRGB();
      }
      else {
	tmp2_2d[i-1][j-1] = Color.black.getRGB();
      }
    }
    
    else {
      tmp2_2d[i-1][j-1] = Color.black.getRGB();
    }
    return tmp2_2d;
  }
  
}

