package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Reflect is an algorithm to reflect an image about an axis
 * @author Craig Strachan after Judy Robertson, SELLIC Online
 * @see code.iface.reflect
 */

public class Reflect extends Thread {

  public Reflect() {
  }


  private int get_point(int x, int y, int width, int height, int [] src_bitmap) {
    /* 
     * returns the value of a int bitmap with width width at point
     * x, y;
     */
    
    return src_bitmap[(((height - 1) - y) * width) + x];
  } /* get_point */
  
  private void put_point(int point, int x, int y, int width, 
			 int height, int [] bitmap) {
    /*
     * Sets point x,y of the bitmap bitmap to have the value of point.
     */
    
    bitmap [(((height - 1) - y) * width) + x] = point;
  } /* put_point */
  
  /**
   *Reflects the input image by the specified angle. This reflection 
   *happens about a line defined through the specified point.
   *@param src The input pixel array
   *@param width The width of the input image
   *@param height The height of the input image
   *@param x The x co-ordiante used to find the reflection axis
   *@param y The y co-ordinate used to find the reflection axis
   *@param angle The angle of the reflection axis
   *@param wrap Boolean which determines if the reflected image is wrapped around
   *@return The reflected image array
   */
  
  public int [] reflect_image (int [] src, int width, int height, int x, int y, 
			       double angle, boolean wrap) {
    
    /* 
     * Reflects an image of width width and height height about an
     * axis which passes through point x,y at angle angle to the X axis.
     */
    
    int [] dest = new int [width * height];
    int real_angle;
    y = height - y;
    
    // If we're not wrapping, black out the dest image so that parts
    // not wrapped into will appear black
    
    if (wrap == false) {
      
      int black_colour = new Color(0,0,0).getRGB();
      
      for (int i = 0; i < (width * height); i++) {
	dest [i] = black_colour;
      }
      
    }
    
    double rad_angle = angle * Math.PI / 180;
    double delta;
    int new_x, new_y;
    
    for (int j = 0; j < height; j++) {  // y coord
      for (int i = 0; i < width; i++) { // x coord
	delta = ((i - x) * Math.sin(rad_angle)) - 
	  ((j - y) * Math.cos(rad_angle));
	new_x = (int) Math.round((i + (2 * delta * (Math.sin(rad_angle)) * -1)));
	new_y = (int) Math.round((j + (2 * delta * (Math.cos(rad_angle)) )));
	
	if (new_x >= 0 && new_x < width && new_y >= 0 && new_y <
	    height) {
	  put_point(get_point(i, j, width, height, src), new_x, new_y, width,
		    height, dest);
	}
	else {
	  if (wrap ==true) {
	    //do something clever
	  }
	}
      }
    }
    
    int [][] tmp_2d = new int[width][height];

    //Convert array from 1_d to 2_d for ease of processing
    for(int i = 0; i < width; i++){
      for(int j = 0; j < height; j++){
	tmp_2d[i][j] = dest[i+(j*width)];
      }
    }

    //Now go through image and fill in points which had no point reflected
    //to them. This should eliminate the black dots in the output image.

    for(int i = 1; i < (width-1); i++){
      for(int j = 1; j < (height-1); j++){
	if((tmp_2d[i][j] & 0x000000ff) == 0){
	  //If black point
	  dest[i+(j*width)] = validatePoint(tmp_2d, i, j);
	}
	else {
	  dest[i+(j*width)] = tmp_2d[i][j];
	}
      }
    }
    return dest;
  }
 
  int validatePoint(int [][] array, int xPoint, int yPoint){

    int count = 0;

    //If enough neighbours are non-black then set point to mean of them
    if(array[xPoint-1][yPoint-1] != 0xff000000){
      count ++;
    }
    if(array[xPoint][yPoint-1] != 0xff000000){
      count ++;
    }
    if(array[xPoint+1][yPoint-1] != 0xff000000){
      count++;
    }
    if(array[xPoint-1][yPoint] != 0xff000000){
      count++;
    }
    if(array[xPoint+1][yPoint] != 0xff000000){
      count++;
    }
    if(array[xPoint-1][yPoint+1] != 0xff000000){
      count++;
    }
    if(array[xPoint][yPoint+1] != 0xff000000){
      count++;
    }
    if(array[xPoint+1][yPoint+1] != 0xff000000){
      count++;
    }
    if(count >= 5){
      int sum = (array[xPoint-1][yPoint-1] & 0x000000ff) +
	(array[xPoint][yPoint-1] & 0x000000ff) +
	(array[xPoint+1][yPoint-1] & 0x000000ff) +
	(array[xPoint-1][yPoint] & 0x000000ff) +
	(array[xPoint+1][yPoint] & 0x000000ff) +
	(array[xPoint-1][yPoint+1] & 0x000000ff) +
	(array[xPoint][yPoint+1] & 0x000000ff) +
	(array[xPoint+1][yPoint+1] & 0x000000ff);
      int answer = (int)(sum/count);
      return 0xff000000 | answer<<16 | answer<<8 | answer;
    }
    else {
      return array[xPoint][yPoint];
    }
  }
  
}


