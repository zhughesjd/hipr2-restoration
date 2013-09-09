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
 * Code for the median smoothing algorithm.
 *
 * @author Simon Horne.
 */

public class MedianSmooth extends Thread {

  /**
   * Default no-args constructor.
   */
  public MedianSmooth() {
  }
  /**
   * Gets the maximum value from a list.
   *
   * @param values the list of values to be checked
   * @return the maximum value
   */
  public int getMax(ArrayList values){
    Iterator it = values.iterator();
    int max = 0;//((Integer) it.next()).intValue();
    while(it.hasNext()){
      int current = ((Integer) it.next()).intValue();
      if(current>max) max=current;
    }
    return max;
  }

  /**
   * Removes a single occurence of the maximum value from a list.
   *
   * @param values the input list
   * @return the input list minus a single maximum element
   */
  public ArrayList removeMax(ArrayList values){
    int max = getMax(values);
    Iterator it = values.iterator();
    while(it.hasNext()){
      if(((Integer) it.next()).intValue() == max){
	it.remove();
	return values;
      }
    }
    return values;//never reaches this point as values contains a max
  }

  //Assume all positive integers
  /**
   * Gets the median value from an input list by removing half of the maximum
   * values and then returning the remaining maximum value or the mean
   * of the two maximum values (if even number of elements).
   *
   * @param values the input list
   * @return the median value
   */
  public int getMedian(ArrayList values){
    int median;
    if(values.size()/2 == (values.size()+1)/2){//if even number of elements
      for(int i=0;i<(values.size()-1)/2;++i){
	removeMax(values);
      }
      median = getMax(values);//median is mean of two central values
      removeMax(values);
      median = median + getMax(values);
      median = median/2;//mean of x,y is (x+y)/2
    }else{//if odd number of elements
      for(int i=0;i<values.size()/2;++i){
	removeMax(values);
      }
      median = getMax(values);//median is central value
    }
    return median;
  }

  /**
   * Takes a 2D input image array and a kernel and a pixel location and 
   * calculates the new pixel value by calculating the median of its 
   * neighbours.
   *
   * @param input the 2D image array
   * @param kernel the kernel array
   * @param w the width of the input image
   * @param h the height of the input image
   * @param x the x coordinate of the pixel at the centre of the neighbourhood
   * @param y the y coordinate of the pixel at the centre of the neighbourhood
   * @return the new pixel value
   */
  public int medianNeighbour(int [][] input, int [][] kernel,
			  int w, int h, int x, int y) {

    ArrayList values = new ArrayList();
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	if((kernel[i][j]==1) && 
	   ((x-1+i)>=0) && ((y-1+j)>=0) && ((x-1+i)<w) && ((y-1+j)<h) ){
	  //System.out.println("Adding a value");
	  values.add(new Integer(input[x-1+i][y-1+j]));
	}
      }
    }
    //System.out.println(values.size());
    int m = getMedian(values);
    //System.out.println(m);
    return m;
  }

  /**
   * Converts a 1D array to a 2D array.
   *
   * @param input the 1D array
   * @param width of the image
   * @param height of the image
   * @return the 2D array
   */
  public int [][] generateInputArrays(int [] input, int width, int height){
    int [][] arrays = new int [width][height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	arrays[i][j] = (new Color(input[i+(j*width)])).getRed();
      }
    }
    return arrays;
  }
     
  /**
   * Converts a 2D array to a 1D array.
   *
   * @param outputArrays the 2D array
   * @param width of the image
   * @param height of the image
   * @return the 1D array
   */ 
  public int [] generateOutputArray(int [][] outputArrays, 
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
   * Returns true if the kernel consists of all 0s.
   *
   * @param kernel the array representing the kernel
   * @return True or false (true is all 0s)
   */
  public boolean emptyKernel(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==1) return false;
    }
    return true;
  }
 /**
   * Takes an image and a kernel and smoothes the image the specified number of
   * iterations.
   *
   * @param input the input image array
   * @param kernel the kernel array
   * @param width of the image
   * @param height of the image
   * @param iterations to be carried out
   * @return the new smoothed image array
   */
  public int [][] smooth(int [][] input, int [][] kernel,
			 int width, int height,
			 int iterations){
    int [][] outputArrays = new int [width][height];
    for (int its=0;its<iterations;++its){
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  outputArrays[i][j] = medianNeighbour(input,kernel,
					     width,height,i,j);
	}
      }
      input = (int [][]) outputArrays.clone(); //copy output to input
    }
    return outputArrays;
  }

  /**
   * Takes an image and a kernel and smoothes the image the specified number of
   * iterations.
   *
   * @param input the input image array
   * @param kernel the kernel array
   * @param width of the image
   * @param height of the image
   * @param iterations to be carried out
   * @return the new smoothed image array
   */
  public int [] smooth_image (int [] input, int [] kernel, int width, 
			     int height, int iterations) {
    int [][] inputArrays = new int [width][height];
    int [][] kernelArrays = new int [3][3];
    int [][] outputArrays = new int [width][height];
    inputArrays = generateInputArrays(input,width,height);
    for(int j=0;j<3;++j){
      for(int i=0;i<3;++i){
	kernelArrays[i][j] = kernel[j*3 +i];
      }
    }
    outputArrays = smooth(inputArrays,kernelArrays,width,height,iterations);
    return generateOutputArray(outputArrays,width,height);
  }

 /**
   * Takes an image and a kernel and smoothes the image the specified number of
   * iterations.
   *
   * @param input the input image array
   * @param kernel the kernel array
   * @param width of the image
   * @param height of the image
   * @param iterations to be carried out
   * @return the new smoothed image array
   */
 public int [] smoothImage (int [] input, int width, int height, 
				    int [] kernel, int kernelWidth,
				    int kernelHeight, int iterations) {
    int [] outputArray = new int [width*height];
    if(iterations>0){
      int [][] inputArrays = new int [width][height];
      int [][] kernelArrays = new int [3][3];
      int [][] outputArrays = new int [width][height];
      for(int j=0;j<kernelHeight;++j){
	for(int i=0;i<kernelWidth;++i){
	  int k = kernel[j*kernelWidth +i];
	  //System.out.println("Input Mask "+k);
/*	  if(k>128){
	    k=1;
	  }else{
	    k=0;
	  }
*/
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









