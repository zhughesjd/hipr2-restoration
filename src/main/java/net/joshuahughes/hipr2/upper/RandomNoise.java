package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Random.*;
import java.lang.Math.*;

/**
 *RandomNoise is an algorithm to generate random noise in an image of
 *either type Gaussian or Shot.
 *@Timothy Sharman
 *@see code.iface.rnoise
 */

public class RandomNoise extends Thread{
  
  //The width and height of the output
  private int d_w;
  private int d_h;
  
  private int[] dest_1d;
  
  
  //A random number generator
  private Random randgen = new Random();
  
  
  /**    
   *ShotFull applies full shot noise to the input image. This means that it
   *changes the values in the image with probability par to either 0 or 255.
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param par This is used to represent the probability that a pixel will changed.
   *@return A pixel array containing the noisy input image
   */
  
  
  //Bob's noise algorithm..
  /*a) assume the image is grey level (hence RR=GG=BB)
    b) use value &0x000000ff to get the BB value
    c) apply the operation (eg create noise). This depends on the type of noise which must be created. Different methods create different noise. 
    d) clip to lie from 0 to 255 (if necessary). Call this value 0xCD
    e) create int value 0xffCDCDCD
    */
  
   
  
  public int [] ShotFull(int [] src_1d, int width, int height, float par){
    
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    float ran1;
    float ran2;
    int src_rgb;
    int result = 0;
    
    for (int i = 0; i < dest_1d. length; i++){
      
      //Select required bits from 32 bit integer
      src_rgb = src_1d[i] & 0x000000ff;
      
      ran1 = randgen.nextFloat();
      
      if(ran1 < par){
	
	//Only change the bits which have passed the above test
	
	ran2 = randgen.nextFloat();
	
	//Change them to either 0 or 255
	if(ran2 < 0.5) {result = 0;}
	else if (ran2 >= 0.5) {result = 255;}
      }
      
      
      //Otherwise leave them alone
      else result = src_rgb;
      
      //Convert back to 32 bit integer value
      dest_1d[i] = 0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
    
  }
  
  /**
   *ShotPartial applies partial shot noise to the input image. This means that 
   *it changes the values in the image with probability par to a randomly chosen
   *value between 0 and 255.
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param par This is used to represent the probability that a pixel will change
   *@return A pixel array containing the noisy input image
   */
  
  public int [] ShotPartial(int [] src_1d, int width, int height, float par){
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    float ran1;
    double ran2;
    int src_rgb;
    int result = 0;
    
    for (int i = 0; i < dest_1d. length; i++){
      
      //Select required bits from 32 bit integer
      src_rgb = src_1d[i] & 0x000000ff;
      
      ran1 = randgen.nextFloat();
      
      if(ran1 < par){
	
	//Only change the bits which have passed the above test
	
	ran2 = randgen.nextDouble();
	
	//Change them to a random value between 0 and 255
	
	result = (int)Math.floor(255*ran2);
      }
      
      
      //Otherwise leave them alone
      else result = src_rgb;
      
      //Convert back to 32 bit integer value
      dest_1d[i] = 0xff000000 | (result + (result << 16) + (result << 8));
    }
    return dest_1d;
    
  } 
  
  /**
   *Gaussian adds gaussian noise to the input image. This means that 
   *it adds a gaussian distributed random number, with standard deviation par
   *to the current pixel. This is repeated across the whole image.
   *@param src_1d The source image as a pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@param par This represents the Standard Deviation by which the Gaussian noise is generated
   *@return A pixel array containing the noisy input image
   */

 public int [] Gaussian(int [] src_1d, int width, int height, float par){
    
    d_w = width;
    d_h = height;
    dest_1d = new int[d_w * d_h];
    double ran1;    
    int src_rgb;
    int result = 0;
    
    for (int i = 0; i < dest_1d. length; i++){
      
      //Select required bits from 32 bit integer
      src_rgb = src_1d[i] & 0x000000ff;
      
      //Generate amount to be added
      ran1 = (par*(randgen.nextGaussian()));      
      result = (int)(src_rgb + ran1);
      
      //Clip final result
      if(result < 0) {result = 0;}
      if(result > 255) {result = 255;}
      
      //Convert back to 32 bit integer value
      dest_1d[i] = 0xff000000 | (result + (result << 16) + (result << 8));
    }
    
    return dest_1d;
 }
}
