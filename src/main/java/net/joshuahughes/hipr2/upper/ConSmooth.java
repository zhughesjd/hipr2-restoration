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
 * ConSmooth is an algorithm to conservatively smooth an image 
 * using a 3x3 kernel.
 *
 * @author Simon Horne.
 */
public class ConSmooth extends Thread {

  /**
   * Default no-arg constructor.
   */
  public ConSmooth() {
  }

  /**
   * Finds the maximum value from a 3x3 pixel neighbourhood overlaid with
   * a 3x3 kernel (on/off). The centre pixel of the kernel is ignored.
   *
   * @param input The 2D array of pixel values representing the image.
   * @param kernel The array representing the kernel.
   * @param w The width of the image.
   * @param h The height of the image.
   * @param x The x coordinate of the centre of the 3x3 neighbourhood.
   * @param y The y coordinate of the centre of the 3x3 neighbourhood.
   * @return The maximum value.
   */
  public static int maxNeighbour(int [][] input, int [] kernel,
			  int w, int h, int x, int y) {

    int [] neighbour = new int [9];
    boolean [] neighbourPresent = new boolean [9];
    int max;
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	if((kernel[(3*j)+i]==1)&&(((x-1+i) > 0)&&((x-1+i) < w)&&((y-1+j) > 0)&&((y-1+j) < h)))
	   {
	  neighbour[(3*j)+i] = input[x-1+i][y-1+j];
	  neighbourPresent[(3*j)+i] = true;
	 
	}else{
	  neighbour[(3*j)+i] = 0;
	  neighbourPresent[(3*j)+i] = false;
	  
	}
      }
    }
    max = 0;
    for(int i=0;i<9;++i){
      if((neighbourPresent[i])&&(neighbour[i]>max)&&(i!=4)){
	max = neighbour[i];

      }
    }
   
    return max; 
  }

  /**
   * Finds the minimum value from a 3x3 pixel neighbourhood overlaid with
   * a 3x3 kernel (on/off). The centre pixel of the kernel is ignored.
   *
   * @param input The 2D array of pixel values representing the image.
   * @param kernel The array representing the kernel.
   * @param w The width of the image.
   * @param h The height of the image.
   * @param x The x coordinate of the centre of the 3x3 neighbourhood.
   * @param y The y coordinate of the centre of the 3x3 neighbourhood.
   * @return The minimum value.
   */
  public static int minNeighbour(int [][] input, int [] kernel,
			  int w, int h, int x, int y) {

    int [] neighbour = new int [9];
    boolean [] neighbourPresent = new boolean [9];
    int min;
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	if((kernel[(j*3)+i]==1)&&(((x-1+i) > 0)&&((x-1+i) < w)&&((y-1+j) > 0)&&((y-1+j) < h))){
	  neighbour[(3*j)+i] = input[x-1+i][y-1+j];
	  neighbourPresent[(3*j)+i] = true;
	}else{
	  neighbour[(3*j)+i] = 0;
	  neighbourPresent[(3*j)+i] = false;
	}
      }
    }
    min = 255;
    for(int i=0;i<9;++i){
      if((neighbourPresent[i])&&(neighbour[i]<min)&&(i!=4)){
	min = neighbour[i];
      }
    }
    return min;
      }      

  /**
   * Converts a 1D array to a 2D array.
   *
   * @param input The 1D array.
   * @param width The width of the 2D array.
   * @param height The height of the 2D array.
   * @return The 2D array.
   */
  public static int [][] generateInputArrays(int [] input, 
					     int width, int height){
    int [][] arrays = new int [width][height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	arrays[i][j] = (new Color(input[i+(j*width)])).getRed();
      }
    }
    return arrays;
  }
     
  /**
   * Converts a 2D array into a continuous 1D array.
   *
   * @param outputArrays The 2D array.
   * @param width The width of the 2D array.
   * @param height The height of the 2D array.
   * @return The 1D array.
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
   * True if the kernel consists of 8 0s surrounding the centre pixel.
   *
   * @param kernel The array containing the kernel values.
   * @return True or false (true if all 0s).
   */
  public static boolean emptyKernel(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==1&&(i!=4)) return false;
    }
    return true;
  }

  /**
   * Takes an image and a kernel and applies conservative smoothing to it.
   *
   * @param input The 1D array representing the image.
   * @param kernel The array representing the kernel.
   * @param width The width of the image.
   * @param height The height of the image.
   * @return The array representing the new smoothed image.
   */
  public static int [] smooth_image (int [] input, int [] kernel, int width, 
			     int height) {
    int [][] inputArrays = new int [width][height];
    int [][] outputArrays = new int [width][height];
    inputArrays = generateInputArrays(input,width,height);
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	int max = maxNeighbour(inputArrays,kernel,width,height,i,j);
	int min = minNeighbour(inputArrays,kernel,width,height,i,j);

	// compare to max/min
	if(emptyKernel(kernel)){
	  outputArrays[i][j]=inputArrays[i][j];
	}
	else if(inputArrays[i][j]>max){
	  outputArrays[i][j]=max;
	  
	}else if(inputArrays[i][j]<min){
	  outputArrays[i][j]=min;
	}
	else{
	  outputArrays[i][j]=inputArrays[i][j];
	}
      }
    }
    return generateOutputArray(outputArrays,width,height);
  }

 public int [][] smooth(int [][] inputArrays, int [] kernel,
			 int width, int height){
    int [][] outputArrays = new int [width][height];
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  int max = maxNeighbour(inputArrays,kernel,width,height,i,j);
	  int min = minNeighbour(inputArrays,kernel,width,height,i,j);
	 
	//compare to max/min
	if(emptyKernel(kernel)){
	  outputArrays[i][j]=inputArrays[i][j];
	}
	else if(inputArrays[i][j]>max){
	  outputArrays[i][j]=max;
	 
	}else if(inputArrays[i][j]<min){
	  outputArrays[i][j]=min;
	}
	else{
	  outputArrays[i][j]=inputArrays[i][j];
	}
	}}
    
    return outputArrays;
 }

 public int [] smoothImage (int [] input, int width, int height, 
				    int [] kernel, int kernelWidth,
				    int kernelHeight) {
    int [] outputArray = new int [width*height];
      int [][] inputArrays = new int [width][height];
      int [][] outputArrays = new int [width][height];

      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  inputArrays[i][j] = input[j*width+i];
	}
      }
     
      outputArrays = smooth(inputArrays,kernel,width,height);
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  outputArray[j*width+i] = outputArrays[i][j];
	}
      }
    return outputArray;
  }
}
