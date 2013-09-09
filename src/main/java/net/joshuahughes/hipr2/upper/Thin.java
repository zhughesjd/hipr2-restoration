package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Thin is an algorithm to thin a binary image using a 3x3 kernel.
 * @author Simon Horne.
 */

public class Thin extends Thread {

  /**
   * The default constructor with no parameters. 
   */
  public Thin() {
  }
  /**
   * Takes an image and a kernel and thins it once.
   *
   * @param b the BinaryFast input image
   * @param kernel the thinning kernel
   * @return the thinned BinaryFast image
   */
  public static BinaryFast ThinBinaryRep(BinaryFast b, int [] kernel){
    Point p;
    HashSet result = new HashSet();
    HashSet inputHashSet = new HashSet();
    if(HitMiss.kernelNo0s(kernel)){
      for(int j=0;j<b.h;++j){
	for(int i=0;i<b.w;++i){
	  if(b.pixels[i][j]==BinaryFast.foreground){
	    inputHashSet.add(new Point(i,j));
	  }
	}
      }
    }else{
      Iterator it = b.foregroundEdgePixels.iterator();
      while(it.hasNext()){
	inputHashSet.add(it.next());
      }
    }
    result = HitMiss.HitMissHashSet(b, inputHashSet, kernel);
    Iterator it = result.iterator();
    while(it.hasNext()){
      p = new Point((Point) it.next());
      //make p a background pixel and update the edge sets
      b.removePixel(p);
      b.foregroundEdgePixels.remove(p);
      b.backgroundEdgePixels.add(p);
      //check if new foreground pixels are exposed as edges
      for(int j=-1;j<2;++j){
	for(int k=-1;k<2;++k){
	  if(p.x+j>=0 && p.y+k>0 && 
	     p.x+j<b.w && p.y+k<b.h &&
	     b.pixels[p.x+j][p.y+k]==BinaryFast.foreground){
	    Point p2 = new Point(p.x+j,p.y+k);
	    b.foregroundEdgePixels.add(p2);
	  }
	}
      }
    }
    return b;
  }

  /**
   * Takes an image and a kernel and thins it the specified number of times.
   *
   * @param b the BinaryFast input image
   * @param kernel the thinning kernel
   * @param iterations required
   * @return the thinned BinaryFast image
   */
  public static BinaryFast thin_image (BinaryFast binary, int [] kernel, 
				       int iterations) {
    for(int i=0;i<iterations;++i){
      binary = ThinBinaryRep(binary,kernel);
    }
    binary.generateBackgroundEdgeFromForegroundEdge();
    return binary;
  }
}

    







