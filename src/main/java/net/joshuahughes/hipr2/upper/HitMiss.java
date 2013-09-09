package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * HitMiss is an algorithm to 'hit and miss' a 
 * binary image using a 3x3 kernel.
 *
 * @author: Simon Horne
 */
public class HitMiss extends Thread {
  
  /**
   * Default no-args constructor.
   */
  public HitMiss() {
  }

  /**
   *Returns true if the 8 neighbours of p match the kernel
   *0 is background
   *1 is foreground
   *2 is don't care.
   *
   * @param p the point at the centre of the 
   * 9 pixel neighbourhood
   * @param pixels the 2D array of the image
   * @param w the width of the image
   * @param h the height of the image
   * @param kernel the array of the kernel values
   * @return True if the kernel and image match.
   */ 
  public static boolean kernelMatch(Point p, int [][] pixels, 
			     int w, int h, int [] kernel){
    int matched = 0;
    for(int j=-1;j<2;++j){
      for(int i=-1;i<2;++i){
	if(kernel[((j+1)*3)+(i+1)]==2){
	  ++matched;
	}else if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)&&
	   (((pixels[p.x+i][p.y+j]==BinaryFast.foreground)&&
	     (kernel[((j+1)*3)+(i+1)]==1))||
	    ((pixels[p.x+i][p.y+j]==BinaryFast.background)&&
	     (kernel[((j+1)*3)+(i+1)]==0)))){
	  ++matched;
	}
      }
    }
    if(matched==9){
      return true;
    }
    else return false;
  }

  /**
   * Applies the hitmiss operation to a set of pixels
   * stored in a hash table.
   *
   * @param b the BinaryFast input image
   * @param input the set of pixels requiring matching
   * @param kernel the kernel to match them with
   * @return A hash table containing all the successful matches.
   */ 
  public static HashSet HitMissHashSet(BinaryFast b, 
				       HashSet input,
				       int [] kernel){
    HashSet output = new HashSet();
    Iterator it = input.iterator();
    while(it.hasNext()){
      Point p = (Point) it.next();
      if(kernelMatch((Point) p, b.pixels, b.w, b.h, kernel)){
	//System.out.println("Match "+p.x+" "+p.y);
	output.add(p);
      }
    }
    //System.out.println(output.size());
    return output;
  }

  /**
   * Returns true if the 3x3 kernel consists of 9 1s.
   *
   * @param kernel the array storing the 9 values
   * @return True if all 1s (false otherwise)
   */
  public static boolean kernelAll1s(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==0) return false;
    }
    return true;
  }

  /**
   * Returns true if the 3x3 kernel consists of 9 0s.
   *
   * @param kernel the array storing the 9 values
   * @return True if all 0s (false otherwise)
   */
  public static boolean kernelAll0s(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==1) return false;
    }
    return true;
  }
  /**
   * Returns true if the 3x3 kernel has no 0s.
   *
   * @param kernel the array storing the 9 values
   * @return True if no 0s (false otherwise)
   */
  public static boolean kernelNo0s(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==0) return false;
    }
    return true;
  }
  /**
   * Returns true if the 3x3 kernel has no 1s.
   *
   * @param kernel the array storing the 9 values
   * @return True if no 1s (false otherwise)
   */
  public static boolean kernelNo1s(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==1) return false;
    }
    return true;
  }

  /**
   * Takes a BinaryFast image representation and a kernel and
   * applies the hitmiss algorithm to the image.
   *
   * @param b the image in BinaryFast representation
   * @param kernel the kernel in 1D array form
   * @return The new BinaryFast image after hitmissing
   */
  public static BinaryFast hitmiss_image(BinaryFast b, 
					 int [] kernel){
    HashSet input = new HashSet();
    HashSet result = new HashSet();
    Iterator it;
    Point p;

    //if kernel is all1s then simply remove foreEdge from fore
    //if kernel is all0s then flip image
    //if kernel is all1sAndAnys [4] 1 then add all fore
    //if kernel is all0sAndAnys [4] 0 then make input all back pixels
    //if kernel is mixed 1s0s (and/or anys) and [4] is 1 then use fore edge
    //if kernel is mixed 1s0s and [4] is 0 then use back edge
    //if kernel [4] is any then use all pixels
    if(kernelNo1s(kernel) && kernelNo0s(kernel)){
      for(int j=0;j<b.h;++j){
	for(int i=0;i<b.w;++i){
	  b.pixels[i][j] = BinaryFast.foreground;
	}
      }
    }else{
      if(kernel[4]==1){
	for(int j=0;j<b.h;++j){
	  for(int i=0;i<b.w;++i){
	    if(b.pixels[i][j]==BinaryFast.foreground){
	      p = new Point(i,j);
	      input.add(p);
	    }
	  }
	}
      }else if(kernel[4]==0){
	for(int j=0;j<b.h;++j){
	  for(int i=0;i<b.w;++i){
	    if(b.pixels[i][j]==BinaryFast.background){
	      p = new Point(i,j);
	      input.add(p);
	    }
	  }
	}
      }else{
	for(int j=0;j<b.h;++j){
	  for(int i=0;i<b.w;++i){
	    p = new Point(i,j);
	    input.add(p);
	  }
	}
      }
      result = new HashSet(HitMissHashSet(b, input, kernel));
      //System.out.println("Test");
      //System.out.println(result.size());
      b.generatePixels(result);
    }
    return b;
  }
}
