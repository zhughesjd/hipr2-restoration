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
 *Hough is an algorithm to apply a hough transform to an image
 *@author:Timothy Sharman
 *@see code.iface.hough
 */

public class Hough extends Thread{

  //the width and height of the output image

  private int d_w;
  private int d_h;
  private int [] dest_1d;
  private int [] hough_1d;

  //Create a new version of twoimages to put the output images in
  
  TwoImages twoimages = new TwoImages();

  /**
   *Applies the hough transform to the input image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param threshold The threshold to be applied to the hough space
   *@param scale A scale value to apply ot the hough space
   *@param offset An offset value to apply to the hough space
   *@return An object containing the hough space image, and the line image
   */

  //Tim's Hough Transform Algorithm
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) find each edge pixel
    d) apply the line equation and update hough array
    e) apply threshold to hough array to find line
    f) draw lines 
    g) Return the hough space and the line image
    */

  public TwoImages apply_hough(int [] src_1d, int width, int height, 
			       float threshold, float scale, float offset){
    
    d_w = width;
    d_h = height; 
    int [][] tmp_2d;
    int [][] dest_2d;
    int [][] src_2d = new int [d_w][d_h];
    int h_w = 500;
    //During processing h_h is doubled so that -ve r values
    //can be easily dealt with
    int h_h;
    int tmp;
    int src_rgb;
    int centre_x = d_w / 2;
    int centre_y = d_h / 2;
    int thresh;

    //Work out how the hough space is quantized

    double theta_step = Math.PI / h_w;

    tmp = Math.max(d_h,d_w);
    h_h = (int) (Math.sqrt(2)*tmp);
    hough_1d = new int[2*h_w*h_h];
//System.out.println("Hough array w: "+h_w+" height: "+2*h_h);

    //Create the hough array and initialize to zero
    tmp_2d = new int [h_w][2*h_h];
    for(int i = 0; i < h_w; i++){
      for(int j = 0; j < 2*h_h; j++){
	tmp_2d[i][j] = 0;
      }
    }

    //Initialize the output arrays to black
    dest_1d = new int[d_w*d_h];
    for(int i = 0; i < dest_1d.length; i++){
      dest_1d[i] = Color.black.getRGB();
    }
    dest_2d = new int [d_w][d_h];
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	dest_2d[i][j] = Color.black.getRGB();
      }
    }

    //Find edge points and vote in array

    //First convert input from 1_d to 2_d for ease of processing
    //Pixels are flipped here
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	src_2d[i][(d_h-j)-1] = src_1d[i+(j*d_w)];
      }
    }

    //Now find edge points and update hough array
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j < d_h; j++){
	src_rgb = src_2d[i][j] & 0x000000ff;
	if(src_rgb == 0){ 
	  //Background found
	}
	else {

	  // Edge pixel found
	  for(int k = 0; k < h_w; k++){

	    //Work out the r values for each theta step
	    tmp = (int) (((i-centre_x)*Math.cos(k*theta_step)) + 
			 ((j-centre_y)*Math.sin(k*theta_step)));

	    //Move all values into positive range for display purposes
//if (tmp== -1 && k ==150 ) System.out.println("LOW  ij: "+i+" "+j);
//if (tmp==  0 && k ==150 ) System.out.println("ON   ij: "+i+" "+j);
//if (tmp==  1 && k ==150 ) System.out.println("HIGH ij: "+i+" "+j);
	    tmp = tmp + h_h;
	    if (tmp < 0 || tmp >= 2*h_h) continue;

	    //Increment hough array
	    tmp_2d[k][tmp]++;
	  }
	}
      }
    }
    
    //Now translate the hough array back into 1_d so that it can be displayed
    int high = 0;
    for(int i = 0; i < h_w; i++){
      for(int j = 0; j < 2*h_h; j++){
	hough_1d[i+(j*h_w)] = tmp_2d[i][j];

	//Find the max hough value for the thresholding operation
	if(tmp_2d[i][j] > high){
	  high = tmp_2d[i][j];
	}
      }
    }

    //Set the threshold limit
    thresh = (int) (threshold * high);
//System.out.println("Threshold: "+threshold+" Max: "+high);

    // Search for local peaks above threshold to draw
    boolean draw = false;
    int k;
    int l;
    int dt;     // test theta
    int dr;     // test offset
    for(int i = 0; i < h_w; i++){
      for(int j = 0; j < 2*h_h; j++){

	// only consider points above threshold
	if(tmp_2d[i][j] >= thresh){

//if (0.2*h_w < i && i < 0.3*h_w)
//System.out.println("Point at: "+i+" "+j+" value: "+tmp_2d[i][j]);

          // see if local maxima
          draw = true;
          int peak = tmp_2d[i][j];
          for(k = -1; k < 2; k++){
            for(l = -1; l < 2; l++){
                if (k==0 && l==0) continue;
                dt = i+k;
                dr = j+l;
                if (dr < 0 || dr >= 2*h_h) continue;
                if (dt < 0) dt = dt + h_w;                
                if (dt >= h_w) dt = dt - h_w;
                if (tmp_2d[dt][dr] > peak)
                {
//if (0.2*h_w < i && i < 0.3*h_w)
//System.out.println("Bigger Point at: "+dt+" "+dr+" value: "+tmp_2d[dt][dr]);
                   draw = false;
                   break;
                }                                
            }
          }
          if (!draw) continue;
//if (0.2*h_w < i && i < 0.3*h_w)
//System.out.println("Drawing: "+i+" "+j+" value: "+tmp_2d[i][j]);

	    //Draw edges in output array
            double tsin = Math.sin(i*theta_step);
            double tcos = Math.cos(i*theta_step);
	    if (i <= h_w/4 || i >= (3*h_w)/4) {
              for(int y = 0; y < d_h; y++){
	        int x = (int) (((j-h_h) - ((y-centre_y)*tsin)) / tcos) + centre_x;
	        if(x < d_w && x >= 0){
		  dest_2d[x][d_h-y-1] = 0xff0000ff;
	        }
	      }
	    }
	    else {
	        for(int x = 0; x < d_w; x++){
	          int y = (int) (((j-h_h) - ((x-centre_x)*tcos)) / tsin) + centre_y;
	          if(y < d_h && y >= 0){
		    dest_2d[x][d_h-y-1] = 0xff0000ff;
	          }
	        }
	    }
	}
      }
    }

    //Convert the output array from 2_d to 1_d
    for(int i = 0; i < d_w; i++){
      for(int j = 0; j <d_h; j++){
	dest_1d[i+(j*d_w)] = dest_2d[i][j];
      }
    }
    
    //Apply scaling and offset to hough space
    for(int i = 0; i < hough_1d.length; i++){

      int answer = (int) ((scale * ((double)hough_1d[i])) + offset);

      //clip to 0 ... 256
      if (answer < 0){
                  answer = 0;
      } else if  (answer > 255){
                  answer = 255;
      }

      hough_1d[i] = 0xff000000 | (answer + (answer << 16) + (answer << 8));
    }

    //Return the two output images
    twoimages. image1 = hough_1d;
    twoimages. image2 = dest_1d;
    return twoimages;
  }
}
