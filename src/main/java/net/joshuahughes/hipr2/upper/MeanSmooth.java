package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;
import java.awt.Color.*;

/**
 * MeanSmooth is an algorithm to meany smooth a binary image 
 * using a 3x3 kernel.
 *
 * @author: Simon Horne.
 */

public class MeanSmooth extends Thread {
  /**
   * Default no-args constructor.
   */
  public MeanSmooth() {
  }
  /**
   * Calculates the mean of a 3x3 pixel neighbourhood (including centre pixel).
   *
   * @param input the input image 2D array
   * @param kernel the kernel 2D array
   * @param w the image width
   * @param h the image height
   * @param x the x coordinate of the centre pixel of the array
   * @param y the y coordinate of the centre pixel of the array
   * @return the mean of the 9 pixels
   */ 
  public static int meanNeighbour(int [][] input, int [][] kernel,
			  int w, int h, int x, int y) {

    int sum = 0;
    int number = 0;
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	if((kernel[i][j]==1) && 
	   ((x-1+i)>=0) && ((y-1+j)>=0) && ((x-1+i)<w) && ((y-1+j)<h) ){
	  sum = sum + input[x-1+i][y-1+j];
	  ++number;
	}
      }
    }
    if(number==0) return 0;
    return (sum/number);
  }

  /**
   * Converts a 1D array into a 2D array.
   *
   * @param input the input image 1D array
   * @param width of the input image
   * @param height of the input image
   * @return the newly created 2D array [width][height]
   */
  public static int [][] generateInputArrays(int [] input, int width, 
					     int height){
    int [][] arrays = new int [width][height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	arrays[i][j] = (new Color(input[i+(j*width)])).getRed();
      }
    }
    return arrays;
  }
      
  /**
   * Converts a 2D array into a 1D array.
   *
   * @param outputArrays the image 2D array
   * @param width of the image
   * @param height of the image
   * @return the new 1D array [width*height]
   */
  public static int [] generateOutputArray(int [][] outputArrays, 
					   int width, int height){
    int [] output = new int [width*height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	int grey = outputArrays[i][j];
	output[i+(j*width)] = (new Color(grey,grey,grey)).getRGB();
      }
    }
    return output;
  }

  /**
   * Takes an image in 2D array form and smoothes it according to the kernel.
   * @param input the input image
   * @kernel the kernel 1D array
   * @param width of the input image
   * @param height of the output image
   * @param iterations to be performed
   * @return the new smoothed image 2D array
   */
  public static int [][] smooth(int [][] input, int [][] kernel,
				int width, int height, int iterations){
    int [][] temporary = new int [width][height];
    int [][] outputArrays = new int [width][height];
    temporary = (int [][]) input.clone();
    for (int its=0;its<iterations;++its){
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  outputArrays[i][j] = meanNeighbour(temporary,kernel,
					     width,height,i,j);
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  temporary[i][j]=outputArrays[i][j];
	}
      }
    }
    return outputArrays;
  }

  /**
   * Takes an image and a kernel and applies the specified number of
   * mean smoothings to it.
   *
   * @param input the input image 1D array
   * @param kernel the kernel 1D array 9 elements (3x3)
   * @param width of the input image
   * @param height of the output image
   * @param iterations to be performed
   * @return the new smoothed image 1D array
   */
  public static int [] smooth_image (int [] input, int [] kernel, int width, 
				     int height, int iterations) {
    int [][] inputArrays = new int [width][height];
    int [][] kernelArrays = new int [3][3];
    int [][] outputArrays = new int [width][height];
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	kernelArrays[i][j] = kernel[j*3 +i];
      }
    }
    inputArrays = generateInputArrays(input,width,height);
    outputArrays = smooth(inputArrays,kernelArrays,width,height,iterations);
    return generateOutputArray(outputArrays,width,height);
  }
 /**
   * Takes an image and a kernel and applies the specified number of
   * mean smoothings to it.
   *
   * @param input the input image 1D array
   * @param kernel the kernel 1D array 9 elements (3x3)
   * @param width of the input image
   * @param height of the output image
   * @param iterations to be performed
   * @return the new smoothed image 1D array
   */
  public static int [] smoothImage (int [] input, int width, int height, 
				    int [] kernel, int kernelWidth,
				    int kernelHeight, int iterations) throws InputException{
    int [] outputArray = new int [width*height];
    if(iterations>0){
      int [][] inputArrays = new int [width][height];
      int [][] kernelArrays = new int [3][3];
      int [][] outputArrays = new int [width][height];
      for(int j=0;j<kernelHeight;++j){
	for(int i=0;i<kernelWidth;++i){
	  int k = kernel[j*kernelWidth +i];
	  //System.out.println(k);
	  if(k>128){
	    k=1;
	  }else{
	    k=0;
	  }
	  kernelArrays[i][j] = k;
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  inputArrays[i][j] = input[j*width+i];
	}
      }
      outputArrays = smooth(inputArrays,kernelArrays,width,height,iterations);
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  outputArray[j*width+i] = outputArrays[i][j];
	}
      }
    }else{
      outputArray = (int []) input.clone();
    }
    return outputArray;
  }
}
