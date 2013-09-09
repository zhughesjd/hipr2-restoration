package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;
import java.awt.Color.*;

/**
 * The laplacian of Gaussian operator code.
 *
 * @author Simon Horne.
 */
public class Log{

  /**
   * A convolution operator.
   */
  Convolution convolution;
  /**
   * An array representing the input image.
   */
  int [] input;
  /**
   * An array representing the output image.
   */
  int [] output;
  /**
   * The input image width.
   */
  public int width;
  /**
   * The input image height.
   */
  public int height;
  /**
   * The output image width.
   */
  public int width2;
  /**
   * The output image height.
   */
  public int height2;
  /**
   * The 2D array representing the laplacian of gaussian kernel.
   */
  double [][] logKernel;
  /**
   * The 2D array representing the input image.
   */
  double [][] input2D;
  /**
   * The 2D array representing the input image.
   */
  double [][] output2D;
  /**
   * The user selected theta value.
   */
  double theta;
  /**
   * The user selected kernel size.
   */
  int kernelSize;

  /**
   * Constructor that generates the kernel from the kernel size and theta.
   *
   * @param k The kernel size.
   * @param t the theta value.
   */
  public Log(int k, double t){
    convolution = new Convolution();
    kernelSize = k;
    theta = t;
    logKernel = new double [k][k];
    generateKernel();
  }

  /**
   * Boolean tests for valid kernel construction from the kernel size and theta.
   *
   * @param t the theta value.
   */
  public boolean kernelValid(double t){
    double sum = 0;
    double max = logKernel[0][0];
    for(int j=0;j<kernelSize;++j){
      for(int i=0;i<kernelSize;++i){
        //if (j == i)
	//System.out.println(logKernel[i][j]);
	sum = sum+logKernel[i][j];
      }
    }
    //System.out.println("Max "+max);
    //System.out.println("Sum "+sum);
    if(sum>0.1 || sum<-0.1 || t<=0.5 || (t/kernelSize)>0.09){
        // too far from a good value
        return false;
    }else{
      // adjust values slightly to get zero sum
      double delta = sum / (kernelSize * kernelSize);
      for(int j=0;j<kernelSize;++j){
        for(int i=0;i<kernelSize;++i){
           logKernel[i][j] = logKernel[i][j] - delta;
        }}
      return true;
    }
  }
	

  /**
   * Takes x and y coordinates and calculates the kernel element at that point.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @return The kernel element.
   */
  double logElement(int x, int y){
    double g = 0;
    for(double ySubPixel = y - 0.5; ySubPixel < y + 0.55; ySubPixel += 0.1){
      for(double xSubPixel = x - 0.5; xSubPixel < x + 0.55; xSubPixel += 0.1){
	double s = -((xSubPixel*xSubPixel)+(ySubPixel*ySubPixel))/
	  (2*theta*theta);
	g = g + (1/(Math.PI*Math.pow(theta,4)))*
	  (1+s)*
	  Math.pow(Math.E,s);
      }
    }
    g = -g/121;
    //System.out.println(g);
    return g;
  }
  /**
   * Generates the kernel from the current theta and kernel size.
   */
  void generateKernel(){
    for(int j=0;j<kernelSize;++j){
      for(int i=0;i<kernelSize;++i){
	int x = (-kernelSize/2)+i;
	int y = (-kernelSize/2)+j;
	logKernel[i][j] = logElement(x,y);
      }
    }
  }

  /**
   * Takes an input image and returns the output image after applying the
   * laplacian of gaussian operator.
   *
   * @param input The input image integer array.
   * @param width The image width.
   * @param height The image height.
   * @return The output image integer array.
   */
  public int [] log_image(int [] input, int width, int height, 
			  double scale, double offset){

    input = convolution.convolution_image(input,width,height,
					  logKernel,kernelSize,kernelSize,
					  scale,offset);
    //System.out.println(scale+" "+offset);
    return input;
  }
}

