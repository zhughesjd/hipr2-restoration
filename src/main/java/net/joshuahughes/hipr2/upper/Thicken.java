package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Thicken is an algorithm to thicken a binary image using a 3x3 kernel.
 *
 * @author Simon Horne.
 */
public class Thicken extends Thread {

  //Black background
  /**
   * Background is black.
   */
  int background = (new Color(0,0,0)).getRGB();
  //White foreground
  /**
   * Foreground is white.
   */
  int foreground = (new Color(255,255,255)).getRGB();
  
  HitMiss hitmiss;

  public Thicken() {
  }
  /**
   * Takes a BinaryFast Image and a kernel and applies a single iteration
   * of the thicken algorithm to the image.
   *
   * @param b the BinaryFast image
   * @param kernel the thickening kernel
   * @return the new thickened BinaryFast image
   */
  public static BinaryFast ThickenBinaryRep(BinaryFast b, int [] kernel){
    Point p;
    HashSet result = new HashSet();
    HashSet inputHashSet = new HashSet();
    if(HitMiss.kernelNo1s(kernel)){
      for(int j=0;j<b.h;++j){
	for(int i=0;i<b.w;++i){
	  if(b.pixels[i][j]==BinaryFast.background){
	    inputHashSet.add(new Point(i,j));
	  }
	}
      }
    }else{
      Iterator it = b.backgroundEdgePixels.iterator();
      while(it.hasNext()){
	inputHashSet.add(it.next());
      }
    }
    //result is the pixels to be thickened/added to the image
    result = HitMiss.HitMissHashSet(b,inputHashSet,kernel);
    Iterator it = result.iterator();
    while(it.hasNext()){
      p = new Point((Point) it.next());
      //make p a background pixel and update the edge sets
      b.addPixel(p);
      b.backgroundEdgePixels.remove(p);
      b.foregroundEdgePixels.add(p);
      //check if new background pixels are exposed as edges
      for(int j=-1;j<2;++j){
	for(int k=-1;k<2;++k){
	  if(p.x+j>=0 && p.y+k>0 && 
	     p.x+j<b.w && p.y+k<b.h &&
	     b.pixels[p.x+j][p.y+k]==BinaryFast.background){
	    Point p2 = new Point(p.x+j,p.y+k);
	    b.backgroundEdgePixels.add(p2);
	  }
	}
      }
    }
    return b;
  }

  /**
   * Takes an image and a kernel and applies thicken the specified number
   * of times.
   *
   * @param binary the input image
   * @param kernel the kernel
   * @param iteraitons the specified number of iterations
   * @return the thickened image
   */
  public static BinaryFast thicken_image (BinaryFast binary, int [] kernel, 
					  int iterations) {
    for(int i=0;i<iterations;++i){
      binary = ThickenBinaryRep(binary,kernel);
    }
    binary.generateForegroundEdgeFromBackgroundEdge();
    return binary;
  }
}

    







