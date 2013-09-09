package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Code for the skeletonise algorithm.
 *
 * @author Simon Horne.
 */
public class Skeleton extends Thread {
  /**
   * 2D array of kernel values, 8 3x3 kernels.
   */
  int kernel [] [] = new int [] [] {
    {0,0,0,2,1,2,1,1,1},
      {2,0,0,1,1,0,2,1,2},
	{1,2,0,1,1,0,1,2,0},
	  {2,1,2,1,1,0,2,0,0},
	    {1,1,1,2,1,2,0,0,0},
	      {2,1,2,0,1,1,0,0,2},
		{0,2,1,0,1,1,0,2,1},
		  {0,0,2,0,1,1,2,1,2}
		    };
  
  //int test_kernel [] = new int [] {0,0,0,2,1,2,1,1,1};
  /**
   * Default no-args constructor.
   */
  public Skeleton() {
  }
  /**
   * Takes an image and skeletonises it.
   *
   * @param binary the BinaryFast input image
   * @return the new skeletonised image
   */
  public BinaryFast skeleton_image(BinaryFast binary) {
    Point p;
    int oldForeEdge = 0;
    
    while(binary.foregroundEdgePixels.size() != oldForeEdge){
      oldForeEdge = binary.foregroundEdgePixels.size();
      //binary.backgroundEdgePixels.clear();
      //System.out.println(binary.foregroundEdgePixels.size());
      for(int i=0;i<8;++i){
	//System.out.println(binary.backgroundEdgePixels.size());
	binary = Thin.ThinBinaryRep(binary,kernel[i]);
	binary.generateBackgroundEdgeFromForegroundEdge();
      }
    }
    return binary;
  }
}



