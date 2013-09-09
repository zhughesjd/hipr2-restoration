package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.awt.Color.*;

/**
 * The laplacian smoothing operator code.
 * 
 * @author: Simon Horne
 */
public class Laplacian extends Convolution {
  /**
   * Kernel type 1.
   */
  static double [][] kernel1 = new double [][] {{0,1,0},{1,-4,1},{0,1,0}};
  /**
   * Kernel type 2.
   */
  static double [][] kernel2 = new double [][] {{1,1,1},{1,-8,1},{1,1,1}};
  /**
   * Kernel type 3.
   */
  static double [][] kernel3 = new double [][] {{-0.5,2,-0.5},{2,-6,2},{-0.5,2,-0.5}};
  /**
   * The width of the output image after applying the convolution.
   */
  public int outputWidth;
  /**
   * The height of the output image after applying the convolution.
   */
  public int outputHeight;

  /** 
   * Default no-arg constructor.
   */
  public Laplacian() {
  }

  /**
   * Takes an input image and applies the required laplacian smoothing
   * and returns the output image.
   *
   * @param input The input image integer array.
   * @param width The input image width.
   * @param height The input image height.
   * @param kernel The kernel type selected, 1,2 or 3.
   * @param scale the scale factor for the output pixel values
   * @param offset the offset value for the output pixel values
   * @return The output image integer array.
   */
  public int [] laplacian_image (int [] input, int width, int height,
				 int kernel, 
				 double scale, double offset){
    if(kernel==1){
      input = convolution_image(input,width,height,kernel1,3,3,scale,offset);
    }else if(kernel==2){
      input = convolution_image(input,width,height,kernel2,3,3,scale,offset);
    }else{
      input = convolution_image(input,width,height,kernel3,3,3,scale,offset);
    }
    return input;
  }
}
