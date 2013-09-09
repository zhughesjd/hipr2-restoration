package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Distance is an algorithm to create a distance transform grey level image
 * from a binary image.
 *
 * @author Simon Horne.
 */
public class Distance extends Thread {
  /**
   * 2D array representing the new grey level image produced.
   */
  int [][] greyLevel;
  /**
   * Hash table representing original foreground edge pixels.
   */
  HashSet oldForegroundEdge;
  /**
   * Hash table representing original background edge pixels.
   */
  HashSet oldBackgroundEdge;

  /**
   * Default no-args constructor.
   */
  public Distance() {
  }

  /**
   * Performs a single thinning and colours the thinned pixels accordingly.
   *
   * @param binary The BinaryFast representation of the input image.
   * @param i The grey-level with which to colour the thinned pixels.
   * @return The BinaryFast representation of the thinned image.
   */
  public BinaryFast distanceSingleIteration(BinaryFast binary, int i){
    Iterator it;
    Point p;
    it = binary.foregroundEdgePixels.iterator();
    while(it.hasNext()){
      p = new Point((Point) it.next());
      binary.backgroundEdgePixels.add(p);
      binary.removePixel(p);
      greyLevel[p.x][p.y]=i;  
    }
    binary.generateForegroundEdgeFromBackgroundEdge();
    return binary;
  }

  /**
   * Takes a BinaryFast representation and produces a distance transform
   * with the grey-level image stored in the BinaryFast pixels 2D array.
   *
   * @param binary The input image.
   * @return The output grey-level image.
   */
  public BinaryFast distance_image (BinaryFast binary, 
				    double scale, double offset) {
    greyLevel = new int [binary.w][binary.h];
    int i=1;
    int grey, colour;
    oldForegroundEdge = new HashSet(binary.foregroundEdgePixels);
    oldBackgroundEdge = new HashSet(binary.backgroundEdgePixels);

    for(int n=0;n<binary.h;++n){
      for(int m=0;m<binary.w;++m){
	greyLevel[m][n]=0;
      }
    }

    while(binary.foregroundEdgePixels.size()>0){
      binary = distanceSingleIteration(binary,i);
      ++i;
    }
    if(i<2)i=2;//Stops division by 0 in bad cases due to non-binary images
    for(int n=0;n<binary.h;++n){
      for(int m=0;m<binary.w;++m){
	//grey = greyLevel[m][n] * (255/(i-1));
	grey = (int) Math.round(greyLevel[m][n] * scale + offset);
	//System.out.println(grey);
	if(grey>255){
	  grey = 255;
	}else if(grey<0){
	  grey = 0;
	}
	colour = new Color(grey,grey,grey).getRGB();
	binary.pixels[m][n] = colour;
      }
    }
    binary.foregroundEdgePixels = new HashSet(oldForegroundEdge);
    binary.backgroundEdgePixels = new HashSet(oldBackgroundEdge);

    return binary;
  }

}












