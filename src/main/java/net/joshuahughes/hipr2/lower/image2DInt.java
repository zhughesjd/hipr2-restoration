package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last modified 16/9/99

//package code.connections;

import java.awt.color.*;
import java.awt.*;
import java.util.*;

/**
 * Image representation consisting of a 2d int array (where (0,0) is
 * the top-left corner of the image and (1,0) is the adjacent pixel
 * to the right of (0,0) and (0,1) is the adjacent pixel to the bottom of
 * (0,0).
 */
public class image2DInt extends image{
  /**
   * The 2d integer array representing the pixel grey level values.
   */
  private int [][] values;

  /**
   * No argument constructor, sets all variables to 0.
   */
  public image2DInt(){
    width = 0;
    height = 0;
    values = new int [0][0];
  }
   
  /**
   * Constructor that takes all the individual data structures and
   * combines them to form an image representation.
   * @param width of the image
   * @param height of the image
   * @values the 2d int array of grey level values
   */
  public image2DInt(int width, int height, int [][] values){
    this.width = width;
    this.height = height;
    this.values = (int [][]) values.clone();
  }

  /**
   * Constructor that takes a 1d int array and generates an image2DInt
   * from it.
   * @param width of the image
   * @param height of the image
   * @values the 1d int array of grey level values
   */
  public image2DInt(int width, int height, int [] values){
    this.width = width;
    this.height = height;
    this.values = new int [width][height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	this.values[i][j] = values[j*width+i];
      }
    }
  }

  /**
   * Constructor that makes an image2DInt image representation by
   * either making an identical copy of an existing image2DInt or by
   * converting a different image representation.
   * @param oldImage the existing image
   */
  public image2DInt(image oldImage){
    if(oldImage != null){
      width = oldImage.getWidth();
      height = oldImage.getHeight();
      if(oldImage instanceof image2DInt){
	values = (int [][]) ((image2DInt) oldImage).getValues2D().clone();
      }else{
	int [] values1D = (int []) oldImage.getValues().clone();
	values = new int [width][height];
	for(int j=0;j<height;++j){
	  for(int i=0;i<width;++i){
	    values[i][j] = values1D[j*width+i];
	  }
	}
      }
    }
  }
  
  /**
   * Returns an integer array of grey level values by converting the
   * internal 2d array.
   * @return the integer array of grey level values
   */
  public int [] getValues(){
    int [] values1D = new int [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	values1D[j*width+i] = values[i][j];
      }
    }
    return values1D;
  }

  /**
   * Returns the pixel values in 2d int array form by simply returning
   * values (the internal 2d array).
   */
  public int [][] getValues2D(){
    return values;
  }
}
