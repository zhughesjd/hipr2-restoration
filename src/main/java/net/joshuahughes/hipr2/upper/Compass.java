package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.Color.*;

/**
 *Compass is an algorithm to apply the compass edge detector to an image
 *@author:Timothy Sharman
 *@see code.iface.compass
 */

public class Compass extends Thread{

  //the Convolution operator used as part of the process.

  Convolution convolution;

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int [] dest_1d;
  private int [] label_1d;

  //Create a new version of twoimages to put the output images in
  
  TwoImages twoimages = new TwoImages();

  //The following are all the kernels that could be applied to the image
  
  //The prewitt kernels
  private double [][] prewitt3 = {{-1,1,1},{-1,-2,1},{-1,1,1}};
  private double [][] prewitt4 = {{1,1,1},{-1,-2,1},{-1,-1,1}};
  private double [][] prewitt5 = {{1,1,1},{1,-2,1},{-1,-1,-1}};
  private double [][] prewitt6 = {{1,1,1},{1,-2,-1},{1,-1,-1}};
  private double [][] prewitt7 = {{1,1,-1},{1,-2,-1},{1,1,-1}};
  private double [][] prewitt8 = {{1,-1,-1},{1,-2,-1},{1,1,1}};
  private double [][] prewitt1 = {{-1,-1,-1},{1,-2,1},{1,1,1}};
  private double [][] prewitt2 = {{-1,-1,1},{-1,-2,1},{1,1,1}};

  //The sobel kernels 
  private double [][] sobel3 = {{-1,0,1},{-2,0,2},{-1,0,1}};
  private double [][] sobel4 = {{0,1,2},{-1,0,1},{-2,-1,0}};
  private double [][] sobel5 = {{1,2,1},{0,0,0},{-1,-2,-1}};
  private double [][] sobel6 = {{2,1,0},{1,0,-1},{0,-1,-2}};
  private double [][] sobel7 = {{1,0,-1},{2,0,-2},{1,0,-1}};
  private double [][] sobel8 = {{0,-1,-2},{1,0,-1},{2,1,0}};
  private double [][] sobel1 = {{-1,-2,-1},{0,0,0},{1,2,1}};
  private double [][] sobel2 = {{-2,-1,0},{-1,0,1},{0,1,2}};

  //The kirsch kernels
  private double [][] kirsch3 = {{-3,-3,5},{-3,0,5},{-3,-3,5}};
  private double [][] kirsch4 = {{-3,5,5},{-3,0,5},{-3,-3,-3}};
  private double [][] kirsch5 = {{5,5,5},{-3,0,-3},{-3,-3,-3}};
  private double [][] kirsch6 = {{5,5,-3},{5,0,-3},{-3,-3,-3}};
  private double [][] kirsch7 = {{5,-3,-3},{5,0,-3},{5,-3,-3}};
  private double [][] kirsch8 = {{-3,-3,-3},{5,0,-3},{5,5,-3}};
  private double [][] kirsch1 = {{-3,-3,-3},{-3,0,-3},{5,5,5}};
  private double [][] kirsch2 = {{-3,-3,-3},{-3,0,5},{-3,5,5}};

  //The robinson kernels 
  private double [][] robinson3 = {{-1,0,1},{-1,0,1},{-1,0,1}};
  private double [][] robinson4 = {{0,1,1},{-1,0,1},{-1,-1,0}};
  private double [][] robinson5 = {{1,1,1},{0,0,0},{-1,-1,-1}};
  private double [][] robinson6 = {{1,1,0},{1,0,-1},{0,-1,-1}};
  private double [][] robinson7 = {{1,0,-1},{1,0,-1},{1,0,-1}};
  private double [][] robinson8 = {{0,-1,-1},{1,0,-1},{1,1,0}};
  private double [][] robinson1 = {{-1,-1,-1},{0,0,0},{1,1,1}};
  private double [][] robinson2 = {{-1,-1,0},{-1,0,1},{0,1,1}};


  /**
   *Applies the compass edge detector to the input image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param kertype The type of kernel to be applied
   *@param coloured Is the labelling image to be in colour or not
   *@param scale A scale factor to be applied to the image
   *@param offset An offset to be added to the image
   *@return An object containing the two output images
   */

  //Tim's Compass Edge Detection Algorithm
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply each kernel to a point in the array
    d) find kernel with maximum response and set as edge magnitude
    e) set pixel label
    */

  public TwoImages apply_compass(int [] src_1d, int width, int height, int kertype, 
				 boolean coloured, float scale, int offset) {

    d_w = width;
    d_h = height; 
    dest_1d = new int[d_w*d_h];
    label_1d = new int[d_w*d_h];
    int [][] tmp_2d = new int[d_w][d_h];
    int result, max, mag;
    max = 0;

    //Clear the destination and label array
    for(int i = 0; i < label_1d.length; i++){
      dest_1d[i] = 0;
      label_1d[i] = 0;
    }

    //Convert 1_d to 2_d for ease of processing in next stages
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	tmp_2d[i][j] = src_1d[i+(j*d_w)] & 0x000000ff;
      }
    }

    //For kernel type prewitt and greyscale labelling
    if(kertype == 0 && coloured == false){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  //Apply a convolution
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt1, 3, 3);
	  //Check if it beats current max
	  if(mag > max){max = mag;
	  //If it does then set as current max, and change the pixel label
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 0 << 16 | 0 << 8 | 0;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 36 << 16 | 36 << 8 | 36;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 72 << 16 | 72 << 8 | 72;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 108 << 16 | 108 << 8 | 108;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 144 << 16 | 144 << 8 | 144;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 180 << 16 | 180 << 8 | 180;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 216 << 16 | 216 << 8 | 216;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 252 << 16 | 252 << 8 | 252;}
	  //Scale and offset
	  max = offset + (int)(max * scale);
	  //Clip values
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	 
	}
      }
    }

    //For kernel type prewitt and colour labelling
    if(kertype == 0 && coloured == true){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.black.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.blue.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.green.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.orange.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.red.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.white.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.yellow.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, prewitt8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.magenta.getRGB();}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }
    

  //For kernel type sobel and greyscale labelling
    if(kertype == 1 && coloured == false){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 0 << 16 | 0 << 8 | 0;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 36 << 16 | 36 << 8 | 36;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 72 << 16 | 72 << 8 | 72;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 108 << 16 | 108 << 8 | 108;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 144 << 16 | 144 << 8 | 144;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 180 << 16 | 180 << 8 | 180;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 216 << 16 | 216 << 8 | 216;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 252 << 16 | 252 << 8 | 252;}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }

    //For kernel type sobel and colour labelling
    if(kertype == 1 && coloured == true){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.black.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.blue.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.green.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.orange.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.red.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.white.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.yellow.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, sobel8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.magenta.getRGB();}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }
 



    //For kernel type kirsch and greyscale labelling
    if(kertype == 2 && coloured == false){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 0 << 16 | 0 << 8 | 0;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 36 << 16 | 36 << 8 | 36;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 72 << 16 | 72 << 8 | 72;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 108 << 16 | 108 << 8 | 108;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 144 << 16 | 144 << 8 | 144;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 180 << 16 | 180 << 8 | 180;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 216 << 16 | 216 << 8 | 216;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 252 << 16 | 252 << 8 | 252;}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }

    //For kernel type kirsch and colour labelling
    if(kertype == 2 && coloured == true){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.black.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.blue.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.green.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.orange.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.red.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.white.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.yellow.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, kirsch8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.magenta.getRGB();}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }


    //For kernel type robinson and greyscale labelling
    if(kertype == 3 && coloured == false){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 0 << 16 | 0 << 8 | 0;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 36 << 16 | 36 << 8 | 36;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 72 << 16 | 72 << 8 | 72;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 108 << 16 | 108 << 8 | 108;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 144 << 16 | 144 << 8 | 144;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 180 << 16 | 180 << 8 | 180;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 216 << 16 | 216 << 8 | 216;}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = 0xff000000 | 252 << 16 | 252 << 8 | 252;}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }

    //For kernel type robinson and colour labelling
    if(kertype == 3 && coloured == true){
      for(int i = 1; i < (d_w-1); i++){
	for(int j = 1; j < (d_h-1); j++){
	  max = 0;
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson1, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.black.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson2, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.blue.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson3, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.green.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson4, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.orange.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson5, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.red.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson6, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.white.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson7, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.yellow.getRGB();}
	  mag = convolution. applyConvolution(tmp_2d, i-1, j-1, robinson8, 3, 3);
	  if(mag > max){max = mag;
	  label_1d[i+(j*(d_w-2))] = Color.magenta.getRGB();}
	  max = offset + (int)(max * scale);
	  if(max > 255){
	    max = 255;
	  }
	  if(max < 0){
	    max = 0;
	  }
	  dest_1d[i+(j*(d_w-2))] = 0xff000000 | max << 16 | max << 8 | max;
	}
      }
    }

    //Set the output images of the return class

    twoimages. image1 = dest_1d;
    twoimages. image2 = label_1d;
     
    return twoimages;
  }
}
