package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * The medial axis transform operator.
 *
 * @author Simon Horne.
 */
public class Mat extends Thread {
  Skeleton skeleton = new Skeleton();
  Distance distance = new Distance();
  int [][] greyLevel;
  /**
   * Default no-args constructor.
   */
  public Mat() {
  }
  /**
   * Takes an image and applies the medial axis transform algorithm by
   * doing a distance transform and then skeletonising the result to remove
   * all unwanted pixels.
   *
   * @param binary the BinaryFast input image
   * @return the new output image
   */
  public BinaryFast mat_image(BinaryFast binary, double scale, double offset,
			      boolean threshold){
    binary = distance.distance_image(binary,scale,offset);
    greyLevel = new int [binary.w][binary.h];
    for(int n=0;n<binary.h;++n){
      for(int m=0;m<binary.w;++m){
	greyLevel[m][n]=binary.pixels[m][n];
	if(binary.pixels[m][n]!=BinaryFast.background) 
	  binary.pixels[m][n]=BinaryFast.foreground;
      }
    }
    binary = skeleton.skeleton_image(binary);
    if(!threshold){
      for(int n=0;n<binary.h;++n){
	for(int m=0;m<binary.w;++m){
	  if(binary.pixels[m][n]==BinaryFast.foreground){
	  //int g = (new Color(greyLevel[m][n])).getRed();
	  //g = (int) Math.round(g * scale + offset);
	  //System.out.println(g);
	  //if(g>255)g=255;
	  //if(g<0)g=0;
  
	    binary.pixels[m][n] = greyLevel[m][n];
	  }
	}
      }
    }
    return binary;
  }
}











