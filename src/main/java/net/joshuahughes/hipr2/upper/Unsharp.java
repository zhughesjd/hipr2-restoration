package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;
import java.awt.Color.*;

/**
 * The unsharp operator code.
 *
 * @author Simon Horne.
 */
public class Unsharp extends MeanSmooth {

  public Unsharp(){
  }
  /**
   * The all 1s kernel.
   */
  public static int [] kernel = {1,1,1,1,1,1,1,1,1};
  /**
   * Subtracts each pixel in pixels2 from the corresponding pixel in pixels1.
   *
   * @param pixels1 the first array of pixels
   * @param pixels2 the array to subtract from the first
   * @return the resulting array of pixels
   */
  public static int [] pixelSubtraction(int [] pixels1, int [] pixels2){
    int [] result = new int [pixels1.length];
    for(int i=0;i<pixels1.length;++i){
      int grey1 = (new Color(pixels1[i])).getRed();
      int grey2 = (new Color(pixels2[i])).getRed();
      int grey3 = grey1 - grey2;
      if(grey3<0) grey3=0;
      else if(grey3>255) grey3=255;
      Color c = new Color(grey3,grey3,grey3);
      result[i] = c.getRGB();
    }
    return result;
  }
  /**
   * Adds each pixel in pixels2 to the corresponding pixel in pixels1.
   *
   * @param pixels1 the first array of pixels
   * @param pixels2 the pixels to add to pixels1
   * @return the resulting array of pixel values
   */
  public static int [] pixelAddition(int [] pixels1, int [] pixels2){
    int [] result = new int [pixels1.length];
    for(int i=0;i<pixels1.length;++i){
      int grey1 = (new Color(pixels1[i])).getRed();
      int grey2 = (new Color(pixels2[i])).getRed();
      int grey3 = grey1 + grey2;
      if(grey3<0) grey3=0;
      else if(grey3>255) grey3=255;
      Color c = new Color(grey3,grey3,grey3);
      result[i] = c.getRGB();
    }
    return result;
  }
  /**
   * Multiplies each pixel in pixels with the constant.
   *
   * @param pixels the array of pixels
   * @param k the multiplication constant
   * @return the new array
   */
  public static int [] pixelMultiplication(int [] pixels, double k){
    int [] result = new int [pixels.length];
    for(int i=0;i<pixels.length;++i){
      int grey1 = (new Color(pixels[i])).getRed();
      int grey2 = (int) (grey1 * k);
      if(grey2<0) grey2=0;
      else if(grey2>255) grey2=255;
      Color c = new Color(grey2,grey2,grey2);
      result[i] = c.getRGB();
    }
    return result;
  }
  /**
   * Takes an image and applies the unsharp filter to it.
   *
   * @param input the input image array
   * @param width of the image
   * @param height of the image
   * @param k the unsharp constant
   * @return the new image array
   */
  public static int [] unsharp_image(int [] input, int width, int height,
				     double k){
    int [] result = new int [input.length];
    result = smooth_image(input,kernel,width,height,1);
    result = pixelSubtraction(input,result);
    result = pixelMultiplication(result,k);
    result = pixelAddition(result,input);
    return result;
  }
    
}
