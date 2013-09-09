package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Translate is an algorithm to translate an image
 * @author:Craig Strachan after Judy Robertson, SELLIC Online
 */

public class Translate extends Thread {

  public Translate() {
  }

  /**
     * returns the value of a int bitmap with width width at point
     * x, y;
     */
  private int get_point(int x, int y, int width, int height, int [] src_bitmap) {
   

    return src_bitmap[(y * width) + x];
  } /* get_point */
  /**
     * Sets point x,y of the bitmap bitmap to have the value of point.
     */
  private void put_point(int point, int x, int y, int width, 
			 int height, int [] bitmap) {
    

    bitmap [(y * width) + x] = point;
  } /* put_point */

       
  /** 
   *Translates an image of width width and height height
   *@param src The input image array
   *@param width The width of the input image
   *@param heigth The height of the input image
   *@param x The x translation value
   *@param y The y translation value
   *@param wrap Boolean which determines whether translated image should be wrapped
   *@return The translated image array
   */

  public int [] translate_image (int [] src, int width, int height, int x, 
				 int y, boolean wrap) {

    int [] dest = new int [width * height];
    int newx = 0;
    int newy = 0;


    // If we're not wrapping, black out the dest image so that parts
    // not wrapped into will appear black
    
    if (wrap == false) {
      
      int black_colour = new Color(0,0,0).getRGB();
      
      for (int i = 0; i < (width * height); i++) {
	dest [i] = black_colour;
	
      }
      
    }
    
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
	if ((i + x >= 0 && i + x < width) && (j + y >= 0 && j + y < height)) {
	  put_point (get_point (i, j, width, height, src), i + x, j + y, width,
		     height, dest);
	}
	else if (wrap == true) {
	  if ((x + i) >= width) {
	    newx = (x + i) - width;
	  }
	  else if ((x + i) < 0) {
	    newx = width + (x + i);
	  }
	  else {
	    newx = x + i;
	  }
	  if ((y + j) >= height) {
	    newy = (y + j) - height;
	  }
	  else if ((y + j) < 0) {
	    newy = height + (y + j);
	  }
	  else {
	    newy = y + j;
	  }
	  
	  put_point (get_point (i, j, width, height, src), newx, newy, width,
		     height, dest);
	}
 
      }
    } 



    return dest;
  }

}

