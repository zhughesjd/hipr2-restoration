package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
*Rotate is an algorithm to apply rotation to an image
*@author Timothy Sharman
*@author Konstantinos Koryllos
*@see code.iface.rotate
*/

public class Rotate extends Thread{

  //The width and height of the output
  private int d_w;
  private int d_h;
  
  private int[] dest_1d;

  /**    
   *rotate applies a rotation operator to an image
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param angle The angle to rotate the image by
   *
   *@return A pixel array containing the rotated image
   */


  //Bob's rotation algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg rotate). 
    d) return the result
    */


  public int [] rotate(int [] src_1d, int width, int height, double angle) {
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    
    double dx_x = rot_x (-angle, 1.0, 0.0);
    double dx_y = rot_y (-angle, 1.0, 0.0);
    double dy_x = rot_x (-angle, 0.0, 1.0);
    double dy_y = rot_y (-angle, 0.0, 1.0);
    
    double x0 = rot_x (-angle, -d_w/2.0, -d_h/2.0) + d_w/2.0;
    double y0 = rot_y (-angle, -d_w/2.0, -d_h/2.0) + d_h/2.0;
    
    double x1 = x0;
    double y1 = y0;
    for (int y = 0; y<d_h; y++) {
      double x2 = x1;
      double y2 = y1;
      for (int x = 0; x<d_w; x++) {
	int pixel = src_color(src_1d,x2,y2);
	dest_1d [y*d_w + x] = pixel;
	x2 += dx_x;
	y2 += dx_y;
      }
      x1 += dy_x;
      y1 += dy_y;
    }
    return dest_1d;
  }
  /**
   * Rotates the x position of a point.
   */
  private double rot_x (double angle, double x, double y) {
    double cos = Math.cos(angle/180.0*Math.PI);
    double sin = Math.sin(angle/180.0*Math.PI);
    return (x * cos + y * (-sin));
  }
  /**
   * Rotates the y position of a point.
   */
  private double rot_y (double angle, double x, double y) {
    double cos = Math.cos(angle/180.0*Math.PI);
    double sin = Math.sin(angle/180.0*Math.PI);
    return (x * sin + y * cos);
  }
  /**
   * Return the value of the x,y point from the image 
   * represented by the input array.
   */
  private int src_color (int [] src_1d, double x, double y) {
    int ix = (int) Math.floor (x);
    int iy = (int) Math.floor (y);
    if ((ix >= d_w) || (ix < 0) ||
	(iy >= d_h) || (iy < 0)) {
      return 0xff000000;
    } else {
      return src_1d[iy*d_w + ix];
    }
  } 
}
