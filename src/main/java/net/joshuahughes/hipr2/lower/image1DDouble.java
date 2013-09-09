package net.joshuahughes.hipr2.lower;
// Author Nathalie Cammas
// Last modified 14/07/00

//package code.connections;

import java.awt.color.*;
import java.awt.*;
import java.util.*;

/**
 * Image representation consisting of a 1d double array, double values are
 * generated from some convolutions, pixel multiplication (x0.5 etc).
 */
public class image1DDouble extends image{
  /**
   * The 1d double array representing pixel grey levels, a value of
   * 100.75 may be used to represent a pixel internally for operators that
   * use double values but when passed to an operator that only uses
   * integer values, the values will be rounded to the nearest integer
   * (101 in this case).
   */
  private double [] values;

  /**
   * No argument constructor, initialises all variables to 0.
   */
  public image1DDouble(){
    width = 0;
    height = 0;
    values = new double [0];
  }
  
 

  /**
   * Constructs a new image1DDouble from a 1d double array.
   * @param width the width of the image
   * @param height the height of the image
   * @param values the 1d double array of pixel grey level values
   */ 
  public image1DDouble(int width, int height, double [] values){
    this.width = width;
    this.height = height;
    this.values = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	this.values[i+(j*width)] = values[j*width+i];
      }
    }
  }

  /**
   * Constructs a new image1DDouble from a 1d int array.
   * @param width the width of the image
   * @param height the height of the image
   * @param values the 1d int array of pixel grey level values
   */ 
  public image1DDouble(int width, int height, int [] values){
    this.width = width;
    this.height = height;
    this.values = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	this.values[i+(j*width)] = values[j*width+i];
      }
    }
  }

  // /**
//    * Converts an existing image representation to make a new image1DDouble
//    * (an exisiting image1DDouble is simply cloned).
//    * @param oldImage the original image to be converted
//    */
//   public image1DDouble(image oldImage){
//     if(oldImage != null){
//       width = oldImage.getWidth();
//       height = oldImage.getHeight();
//       if(oldImage instanceof image1DDouble){
// 	values = new double [width*height];
// 	for(int j=0;j<height;++j){
// 	  for(int i=0;i<width;++i){
// 	    values[i+(j*width)] = ((image1DDouble) oldImage).getValues1D()[i+(j*width)];
// 	  }
// 	}
//       }else if(oldImage instanceof image2DInt){
// 	values = new double [width*height];
// 	for(int j=0;j<height;++j){
// 	  for(int i=0;i<width;++i){
// 	    values[i+(j*width)] = ((imageDInt) oldImage).getValues2D()[i][j];
// 	  }
// 	}
//       }else{
// 	int [] values1D = (int []) oldImage.getValues().clone();
// 	values = new double [width][height];
// 	for(int j=0;j<height;++j){
// 	  for(int i=0;i<width;++i){
// 	    values[i][j] = values1D[j*width+i];
// 	  }
// 	}
//       }
//     }
//   }

  /**
   * Returns the pixel values in 1d int form by converting to a 1d array
   * and rounding the double values.
   * @return the 1d integer array
   */
  public int [] getValues(){
    int [] values1D = new int [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	values1D[j*width+i] = (int) Math.round(values[i+(j*width)]);
      }
    }
    return values1D;
  }

  public double [] getValuesDouble(){
    double [] values1D = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	values1D[j*width+i] = (double) values[i+(j*width)];
      }
    }
    return values1D;
  }

}
