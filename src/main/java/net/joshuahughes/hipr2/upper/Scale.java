package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Scale is an algorithm to alter the size of an image
 * @author:Craig Strachan after Judy Robertson, SELLIC Online
 * @see code.iface.scale
 */

public class Scale extends Thread {
  
  public Scale() {
  }
  
  private int [] gradeline(int end1, int end2, int scalef) {
    
    /* Given two points 2 * scalef apart in an image, interpolates
     * between those  points and returns the result in an int array
     */
    
    
    int [] result = new int [2 * scalef];
    Color cend1, cend2;
    double red_grad, green_grad, blue_grad;
    int red, green, blue;
    
    
    result[0] = end1;
    result[(2 * scalef) - 1] = end2;
    cend1 = new Color(end1);
    cend2 = new Color(end2);
    if (cend1.getRed() == cend2.getRed()) {
      red_grad = 0.0;
    }
    else if (cend1.getRed() > cend2.getRed()) {
      red_grad = ((cend2.getRed() - cend1.getRed()) + 1) / (2 * (scalef -1));
    }
    else {
      red_grad = ((cend2.getRed() - cend1.getRed()) - 1) / (2 * (scalef - 1));
    }
    
    if (cend1.getGreen() == cend2.getGreen()) {
      green_grad = 0.0;
    }
    else if (cend1.getGreen() > cend2.getGreen()) {
      green_grad = ((cend2.getGreen() - cend1.getGreen()) + 1) / (2 * (scalef -1));
    }
    else {
      green_grad = ((cend2.getGreen() - cend1.getGreen()) - 1) / (2 * (scalef - 1));
    }
    
    if (cend1.getBlue() == cend2.getBlue()) {
      blue_grad = 0.0;
    }
    else if (cend1.getBlue() > cend2.getBlue()) {
      blue_grad = ((cend2.getBlue() - cend1.getBlue()) + 1) / (2 * (scalef -1));
    }
    else {
      blue_grad = ((cend2.getBlue() - cend1.getBlue()) - 1) / (2 * (scalef - 1));
    }
    
    
    for (int i = 1; i < ((2 * scalef) - 2); i++) {
      red = (int) Math.rint ((double) i * red_grad) + cend1.getRed();
      green = (int) Math.rint ((double) i * green_grad) + cend1.getGreen();
      blue = (int) Math.rint ((double) i * blue_grad) + cend1.getBlue();
      Color tmp = new Color(red, green, blue);
      result[i] = tmp.getRGB();
      
    }
    
    return result;
  } /* gradline */
  
  /** 
   *Give an image of width src_width contained in int array src
   *returns the rectangle with top left corner at x,y and with width 
   *width and height height contained in that image as an int array
   *@param src The input image array
   *@param src_width The width of the input image array
   *@param x The x co-ordinate of the top left corner of the rectangle
   *@param y The y co-ordinate of the top left corner of the rectangle
   *@param width The width of the rectangle
   *@param height The height of the rectangle
   *@return The rectangle taken from the image 
   */
  
  public int [] getrect (int [] src, int src_width, int x, int y, int width,
			 int height) {
    
    int [] result = new int [width * height];
    int k = 0;
    for (int j = y; j < y + height; j++) {
      for (int i = x; i < x + width; i++) {
	result [k++] = src[(j * src_width) + i];
      }
    }
    return result;
  } /* getrect */
  
  private int get_point(int x, int y, int width, int [] src_bitmap) {
    /* 
     * returns the value of a int bitmap with width width at point
     * x, y;
     */
    
    return src_bitmap[(y * width) + x];
  } /* get_point */
  
  private void put_point(int point, int x, int y, int width, int [] bitmap) {
    /*
     * Sets point x,y of the bitmap bitmap to have the value of point.
     */
    
    bitmap [(y * width) + x] = point;
  } /* put_point */
  
  
  /**
   *Reduces an image by a factor of scalefactor using the sample method 
   *@param src The input image array
   *@param orig_w The width of the input image
   *@param orig_h The height of the input image
   *@param scalefactor The amount the image is to be shrunk
   *@return The shrunken image array
   */
  
  public int [] shrink_sample (int [] src, int orig_w, int orig_h, int
			       scalefactor) {
    
    /* Reduces an image by a factor of scalefactor using the sample
     * method 
     */
    
    
    int new_w = orig_w / scalefactor;
    int new_h = orig_h / scalefactor;
    int [] result = new int [new_w * new_h];
    int [] sample;
    int sample_point;
    
    for (int i = 0; i < (orig_h - scalefactor); i = i + scalefactor) {
      for (int j = 0; j < (orig_w - scalefactor); j = j + scalefactor) {
	
	sample = getrect (src, orig_w, j, i, scalefactor, scalefactor);
	
	/* sample is simply the top left pixel */
	sample_point = sample[0];
	result [((i / scalefactor) * new_w) + (j / scalefactor)] =
	  sample_point; 
      }
    }
    return result;
  } /* shrink_sample */


  /** 
   *Reduces an image by a factor of scalefactor using the average method 
   *@param src The input image array
   *@param orig_w The width of the input image
   *@param orig_h The height of the input image
   *@param scalefactor The amount the image is to be shrunk
   *@return The shrunken image array
   */

  public int [] shrink_average (int [] src, int orig_w, int orig_h, int
				scalefactor) {
    
    int new_w = orig_w / scalefactor;
    int new_h = orig_h / scalefactor;
    int [] result = new int [new_w * new_h];
    int [] sample;
    int sample_point;
    int red_sum, green_sum, blue_sum;
    Color colorpoint;
    
    if (scalefactor == 1)
    {
        for (int i = 0; i < orig_h*orig_w; i++) {
	    result[i] = src[i];
        }
        return result;
    }
    
    for (int i = 0; i < (orig_h - scalefactor); i = i + scalefactor) {
      for (int j = 0; j < (orig_w - scalefactor); j = j + scalefactor) {
	
	sample = getrect (src, orig_w, j, i, scalefactor, scalefactor);
	
	/* sample_point  is the average of the pixels in the sample */
	red_sum = 0;
	green_sum = 0;
	blue_sum = 0;
	
	for (int k = 0; k < (scalefactor * scalefactor) -1; k ++) {
	  colorpoint = new Color(sample[k]);
	  red_sum += colorpoint.getRed();
	  green_sum += colorpoint .getGreen();
	  blue_sum += colorpoint.getBlue();
	}
	
	Color tmp = new Color ((red_sum / (scalefactor * scalefactor)), 
			       (green_sum / (scalefactor * scalefactor)),
			       (blue_sum / (scalefactor*
					    scalefactor)));
	sample_point = tmp.getRGB();
	result [((i / scalefactor) * new_w) + (j / scalefactor)] =
	  sample_point; 
      }
    }
    
    
    return result;
  } /* shrink_sample */
  
  /** 
   *Grows an image by a factor of scalefactor using the replicate method 
   *@param src The input image array
   *@param orig_w The width of the input image
   *@param orig_h The height of the input image
   *@param scalefactor The amount the image is to be grown
   *@return The grown image array
   */


  public int [] grow_replicate(int [] src, int orig_w, int orig_h,
			       int scalefactor) {
    
    int new_w = orig_w * scalefactor;
    int new_h = orig_h * scalefactor;
    int [] result = new int[new_w * new_h];
    int sample;

    for (int i = 0; i < new_h; i++) {
      for (int j = 0; j < new_w; j++) {
	sample = src[(i / scalefactor) * orig_w + (j / scalefactor)];
	result[i * new_w + j] = sample;
      }
    }
    return result;
  } /* grow_replicate */
	
  /** 
   *Grows an image by a factor of scalefactor using the interpolate method 
   *@param src The input image array
   *@param orig_w The width of the input image
   *@param orig_h The height of the input image
   *@param scalefactor The amount the image is to be grown
   *@return The grown image array
   */

  public int [] grow_interpolate (int [] src, int orig_w, int orig_h,
				  int scalefactor) {
   
    int new_w = orig_w * scalefactor;
    int new_h = orig_h * scalefactor;
    int [] result = new int [new_w * new_h];

    if (scalefactor == 1)
    {
        for (int i = 0; i < orig_h*orig_w; i++) {
	    result[i] = src[i];
        }
        return result;
    }
    
    for (int i = 0; i < (orig_h - 1); i++) {
      for (int j = 0; j < (orig_w -1); j++) {
	int [] line = gradeline(get_point(j, i, orig_w, src),
				get_point(j + 1, i, orig_w, src),
				scalefactor);
	for (int k = 0; k <= scalefactor; k++) {
	  put_point(line[k], (j * scalefactor) + k, i * scalefactor,
		    new_w, result);
	}
	line = gradeline(get_point(j, i, orig_w, src), 
			 get_point(j, i + 1, orig_w, src),
			 scalefactor);
	for (int k = 0; k <= scalefactor; k++) {
	  put_point(line[k], j * scalefactor, (i * scalefactor) + k,
		    new_w, result);
	}
	line = gradeline(get_point(j + 1, i, orig_w, src),
			 get_point(j + 1, i + 1, orig_w, src),
			 scalefactor);
	for (int k = 0; k <= scalefactor; k++) {
	  put_point(line[k], (j + 1) * scalefactor, (i * scalefactor) + k,
		    new_w, result);
	}
	
	for (int k = 1; k <= scalefactor; k++) {
	  line = gradeline(get_point((j * scalefactor), 
				     (i * scalefactor) + k, new_w, result),
			   get_point((j + 1) * scalefactor, 
				     (i * scalefactor) + k, new_w,
				     result), 
			   scalefactor);
	  for (int l = 0; l <= scalefactor; l++) {
	    put_point(line[l], (j * scalefactor) + l, 
		      (i * scalefactor) + k, new_w, result);
	  }
	}
      }
    }
    return result;
  } /* grow_interpolate */
  
  
  
}
