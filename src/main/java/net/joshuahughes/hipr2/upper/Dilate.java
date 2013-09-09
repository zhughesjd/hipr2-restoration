package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Dilate is an algorithm to dilate a binary image using a 3x3 kernel.
 * @author Simon Horne
 * @author Craig Strachan
 * @author Judy Robertson, SELLIC Online
 */
public class Dilate extends Thread {
  /**
   * Default no-arg constructor.
   */
  public Dilate() {
  }

  /**
   * Takes a point and a 2D array representing an image and a kernel, if the 
   * area around the point matches the kernel then the method returns true.
   *
   * @param p The point in the centre of the neighbourhood to be checked.
   * @param pixels The 2D array representing the image.
   * @param w The width of the image.
   * @param h The height of the image.
   * @param kernel The kernel used to match with the image.
   * @return True or false (true - the kernel and image match).
   */
  public static boolean kernelMatch(Point p, int [][] pixels, 
				    int w, int h, int [] kernel){
   
    for(int j=-1;j<2;++j){
      for(int i=-1;i<2;++i){
        if (kernel[((j+1)*3)+(i+1)]==1) {
          if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)) {	
//System.out.println("Dilate at ("+(p.x+i)+","+(p.y+j)+"): "+(pixels[(p.x+i)][(p.y+j)])+"="+(BinaryFast.foreground)+" KERNEL("+i+","+j+")");
	    if (pixels[p.x+i][p.y+j]==BinaryFast.foreground) {
	      return true;
	    }
	  }
	}
      }
    }
    return false;
  }
     
  
  /**
   * Returns true if the kernel consists of 9 1s.
   *
   * @param kernel The array representing the kernel.
   * @return True or false (true - kernel all 1s).
   */
  public static boolean kernelAll1s(int [] kernel){
    
    for(int i=0;i<9;++i){
      
	if(kernel[i]==0)  return false;
    }
    return true;
  }

  /**
   * Takes a BinaryFast representation of an image and a kernel
   * and applies a single iteration of the dilate algorithm.
   *
   * @param binary The BinaryFast representation of the input image.
   * @param kernel The array representing the kernel.
   * @return The BinaryFast representation of the new image after dilation.
   */
  public static BinaryFast dilateSingleIteration(BinaryFast binary,
						 int [] kernel){
    Vector result = new Vector();
    Iterator it;
    Point p;
    
    it = binary.backgroundEdgePixels.iterator();
    if(!kernelAll1s(kernel)){
      while(it.hasNext()){
	p = new Point((Point) it.next());
	if(kernelMatch(p, binary.pixels, binary.w, binary.h, kernel)){
	  
	  binary.foregroundEdgePixels.add(p);
	  result.add(p);
	}
      }
    }
    else{
         while(it.hasNext()){
	p = new Point((Point) it.next());
	binary.foregroundEdgePixels.add(p);
	result.add(p);
      }
    }
    it = result.iterator();
    while(it.hasNext()){
      binary.addPixel((Point) it.next());
    }
    binary.generateBackgroundEdgeFromForegroundEdge();
    return binary;
  }

  /**
   * Takes a BinaryFast image, a kernel and the number of iterations
   * and performs the necessary number of dilations on the image.
   *
   * @param binary The BinaryFast representation of the input image.
   * @param kernel The array representing the kernel.
   * @param iterations The requested number of iterations.
   * @return The BinaryFast representation of the new image after dilation.
   */
  public static BinaryFast dilate_image (BinaryFast binary, int [] kernel, 
			      int iterations) {
    int i=0;
    kernel[4]=1;//Ignore centre pixel value in kernel (stops whiteout).
    while(i<iterations){
      binary = dilateSingleIteration(binary,kernel);
      ++i;
    }
    binary.generateForegroundEdgeFromBackgroundEdge();
    return binary;
  }

}
