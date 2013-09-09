package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Erode is an algorithm to erode a binary image using a 3x3 kernel.
 *
 * @author Simon Horne
 * @author Craig Strachan
 * @author Judy Robertson, SELLIC Online
 */
public class Erode extends Thread {

  /**
   * Default no-args constructor.
   */
  public Erode() {
  }

  /**
   * Returns true if the kernel matches the area of image centred on
   * the given point.
   *
   * @param p The centre point identifying the pixel neighbourhood.
   * @param pixels The 2D array representing the image.
   * @param w The width of the image.
   * @param h The height of the image.
   * @param kernel The array representing the kernel.
   * @return True or false (true - the kernel and image match).
   */
  public static boolean kernelMatch(Point p, int [][] pixels, 
				    int w, int h, int [] kernel){
    for(int j=-1;j<2;++j){
      for(int i=-1;i<2;++i){
        if (kernel[((j+1)*3)+(i+1)]==1) {
          if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)) {
            if (pixels[p.x+i][p.y+j]==BinaryFast.background) {
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
   * @return True or false (true - kernel is all 1s).
   */

  public static boolean kernelAll1s(int [] kernel){
    for(int i=0;i<9;++i){
      if(kernel[i]==0) return false;
    }
    return true;
  }

  /**
   * Applies a single iteration of the erode algorithm to the image.
   *
   * @param binary The BinaryFast representation of the input image.
   * @param kernel The array representing the kernel.
   * @return The BinaryFast representation of the new eroded image.
   */
  public static BinaryFast erodeSingleIteration(BinaryFast binary,
						 int [] kernel){
    Vector result = new Vector();
    Iterator it;
    Point p;
    
    it = binary.foregroundEdgePixels.iterator();
    if(!kernelAll1s(kernel)){
      while(it.hasNext()){
	p = new Point((Point) it.next());
	if(kernelMatch(p, binary.pixels, binary.w, binary.h, kernel)){
	  binary.backgroundEdgePixels.add(p);
	  result.add(p);
	}
      }
    }
    else{


      while(it.hasNext()){
	p = new Point((Point) it.next());
	binary.backgroundEdgePixels.add(p);
	result.add(p);
      }
    }
    it = result.iterator();
    while(it.hasNext()){
      binary.removePixel((Point) it.next());
    }
    binary.generateForegroundEdgeFromBackgroundEdge();
    return binary;
  }

  /**
   * Applies several iterations of the erode algorithm to an image.
   *
   * @param binary The BinaryFast representation of the input image.
   * @param kernel The array representing the kernel.
   * @param iterations The number of iterations to be applied.
   * @return The BinaryFast representation of the new eroded image.
   */
  public static BinaryFast erode_image (BinaryFast binary, int [] kernel, 
			      int iterations) {
    int i=0;
    kernel[4]=1;//Ignore centre pixel value in kernel (stops whiteout)
    while(i<iterations){
      binary = erodeSingleIteration(binary,kernel);
      ++i;
    }
    binary.generateBackgroundEdgeFromForegroundEdge();
    return binary;
  }


}












