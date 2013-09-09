package net.joshuahughes.hipr2.upper;

import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;

import net.joshuahughes.hipr2.lower.image;
import net.joshuahughes.hipr2.lower.image1DInt;
import net.joshuahughes.hipr2.lower.image2DDouble;
import net.joshuahughes.hipr2.lower.image2DInt;


/**
 * Class BinaryFast is a representation of a binary image storing
 * the foreground and background edge pixels in hash tables for efficiency.
 *
 * @author Simon Horne.
 */
public class BinaryFast extends image{
  
  /**
   * Background is black.
   */
  public static final int background = (new Color(0,0,0)).getRGB();
  /**
   * Foreground is white.
   */
  public static final int foreground = (new Color(255,255,255)).getRGB();
  /**
   * Width of the image.
   */
  public int w;
  /**
   * Height of the image.
   */
  public int h;
  /**
   * Size of the image (w*h), number of pixels.
   */
  public int s;
  /**
   * The 2D array of all pixels.
   */
  public int [][] pixels;
  /**
   * The hash set storing positions of foreground edge pixels as Points.
   */
  public HashSet foregroundEdgePixels = new HashSet();
  /**
   * The hash set storing positions of background edge pixels as Points.
   */
  public HashSet backgroundEdgePixels = new HashSet();


  /** Constructor taking an image and converting it to a BinaryFast image **/
  
  public BinaryFast(image oldimage) {
    w = oldimage.getWidth();
    h = oldimage.getHeight();
    s = w*h;
    pixels = new int[w][h];
    if (oldimage != null) {
    if (oldimage instanceof image1DInt) {
      /* To convert the values to a 2D array */
      int [][]array2D = new int[w][h];
      int []array1D = new int[s];
      
      array1D = (int []) oldimage.getValues().clone();
      
      for (int i = 0; i < w; i++) {
	for (int j = 0; j < h; j++) {
	  array2D[i][j] = array1D[(j*w)+i];
	}
      }
      
      pixels = array2D;
      
      /* To convert the values to RGB reprensentation and binarise it*/
      for (int i = 0; i < w; i++) {
	for (int j = 0; j < h; j++) {
	  int grey = pixels[i][j];
	  if (grey >= 128) {
	    grey = 255; 
	  pixels[i][j] = 0xffffffff;}
	  else {grey = 0;
	  pixels[i][j] = 0xff000000;}
	}
      }
      
    }
    else if (oldimage instanceof image2DDouble) {
      int []values1D = new int[s];
      values1D = oldimage.getValues();
      
      for (int i = 0; i < w; i++) {
	for (int j = 0; j < h; j++) {
	  int grey = values1D[(j*w)+i];
	   if (grey >= 128) {
	    grey = 255; }
	  else grey = 0;
	  pixels[i][j] = (new Color(grey,grey,grey)).getRGB();
	}
      }
    }
    else if (oldimage instanceof image2DInt) {
      int []values1D = new int[s];
      values1D = oldimage.getValues();
      for (int i = 0; i < w; i++) {
	for (int j = 0; j < h; j++) {
	  int grey = values1D[(j*w)+i];
	   if (grey >= 128) {
	    grey = 255; }
	  else grey = 0;
	  pixels[i][j] = (new Color(grey,grey,grey)).getRGB();
	}
      }
    }
    }
    generateForegroundEdge();
    generateBackgroundEdgeFromForegroundEdge();
  }



  /** 
   * Constructor taking a 2D array of pixel values.
   *
   * @param p The 2D array of pixel values.
   * @param width The width of the image.
   * @param height The height of the image.
   */
  public BinaryFast(int [][] p, int width, int height){ //constructor
    pixels = p;
    w = width;
    h = height;
    s = w*h;
    
    generateForegroundEdge();
    //System.out.println(
    //"Foreground Edge Pixels "+foregroundEdgePixels.size());
    generateBackgroundEdgeFromForegroundEdge();
    //System.out.println(
    //"Background Edge Pixels "+backgroundEdgePixels.size());
  }

  public BinaryFast(){
   
    foregroundEdgePixels = new HashSet();
    backgroundEdgePixels = new HashSet();
  }

  public BinaryFast(BinaryFast oldBinary){
    w = oldBinary.w;
    h = oldBinary.h;
    s = oldBinary.s;
    
    backgroundEdgePixels = new HashSet();
    Iterator it1 = oldBinary.backgroundEdgePixels.iterator();
    while(it1.hasNext()){
      backgroundEdgePixels.add(it1.next());
    }
    foregroundEdgePixels = new HashSet();
    Iterator it2 = oldBinary.foregroundEdgePixels.iterator();
    while(it2.hasNext()){
      foregroundEdgePixels.add(it2.next());
    }
    pixels = (int [][]) oldBinary.pixels.clone();
  }

  /**
   * Removes a foreground pixel from the 2D array by setting its value
   * to background.
   *
   * @param p The point to be removed.
   */
  public void removePixel(Point p){
    pixels[p.x][p.y] = background;
  }

  /**
   * Adds a foreground pixel to the 2D array by setting its value
   * to foreground.
   *
   * @param p The point to be added.
   */
  public void addPixel(Point p){
    pixels[p.x][p.y] = foreground;
  }

  /**
   * Converts the 2D array into a 1D array of pixel values.
   *
   * @return The 1D array of pixel values.
   */
  public int [] convertToArray(){
    int [] p = new int [s];
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	p[(j*w)+i]=pixels[i][j];
      }
    }
    return p;
  }

  /**
   * Generates a new 2D array of pixels from a hash set of 
   * foreground pixels.
   *
   * @param pix The hash set of foreground pixels.
   */
   public void generatePixels(HashSet pix){
    //Reset all pixels to background
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	pixels[i][j]=background;
      }
    }
    convertToPixels(pix);
   }

  /**
   * Adds the pixels from a hash set to the 2D array of pixels.
   *
   * @param pix The hash set of foreground pixels to be added.
   */
   public void convertToPixels(HashSet pix){
    int i=0;
    Point p = new Point();
    Iterator it = pix.iterator();
    while(it.hasNext()){
      p = (Point) it.next();
      pixels[p.x][p.y]=foreground;
    }
   }

  /**
   * Generates the foreground edge hash set from the 2D array of pixels.
   */
  public void generateForegroundEdge(){
    foregroundEdgePixels.clear();
    Point p;
    for(int n=0;n<h;++n){
      for(int m=0;m<w;++m){
	if(pixels[m][n]==foreground){
	  p = new Point(m,n);
	  for(int j=-1;j<2;++j){
	    for(int i=-1;i<2;++i){
	      if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)){
		if((pixels[p.x+i][p.y+j]==background)&&
		   (!foregroundEdgePixels.contains(p))){
		  foregroundEdgePixels.add(p);
		}
	      }
	    }
	  }
	}
      }
    }
  }

  /**
   * Generates the background edge hash set from the foreground edge
   * hash set and the 2D array of pixels.
   */
  public void generateBackgroundEdgeFromForegroundEdge(){
    backgroundEdgePixels.clear();
    Point p,p2;
    Iterator it = foregroundEdgePixels.iterator();
    while(it.hasNext()){
      p = new Point((Point) it.next());
      for(int j=-1;j<2;++j){
	for(int i=-1;i<2;++i){
	  if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)){
	    p2 = new Point(p.x+i,p.y+j);
	    if(pixels[p2.x][p2.y]==background){
	      backgroundEdgePixels.add(p2);
	    }
	  }
	}
      }
    }
  }

  /**
   * Generates the foreground edge hash set from the background edge hash
   * set and the 2D array of pixels.
   */
  public void generateForegroundEdgeFromBackgroundEdge(){
    foregroundEdgePixels.clear();
    Point p,p2;
    Iterator it = backgroundEdgePixels.iterator();
    while(it.hasNext()){
      p = new Point((Point) it.next());
      for(int j=-1;j<2;++j){
	for(int i=-1;i<2;++i){
	  if((p.x+i>=0)&&(p.x+i<w)&&(p.y+j>=0)&&(p.y+j<h)){
	    p2 = new Point(p.x+i,p.y+j);
	    if(pixels[p2.x][p2.y]==foreground){
	      foregroundEdgePixels.add(p2);
	    }
	  }
	}
      }
    }
  }

  /** 
   * Returns the int [] values of the Binary Fast image
   * @return int[] the greylevel array of the image
   */
  public int [] getValues() {
    int []values1D = new int[s];
    int []graylevel = new int[s];
    values1D = convertToArray();
    for(int i = 0; i < s; i++) {
      graylevel[i] = values1D[i] & 0x000000ff;
    }
    return graylevel;
  }


}

