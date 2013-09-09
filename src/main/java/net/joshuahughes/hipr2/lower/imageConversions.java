package net.joshuahughes.hipr2.lower;

import net.joshuahughes.hipr2.upper.BinaryFast;

/**
 * This class is used to convert pixels
 */
public class imageConversions {
  /**
   * Converts an array of greylevel pixels into an RGB representation 
   */
  public static int[] gs2pix(int[] gs){
    
    int[] pix = new int[gs.length];
    
    for(int i = 0; i < gs.length; i++){
      pix[i] = 0xff000000 | gs[i]<<16 | gs[i]<<8 | gs[i];
    }
    return pix;
  }

  /**
   * Converts an array of RGb pixels into a greylevel array
   */
  public static int[] pix2gs(int[] pix){
    
    int[] gs = new int[pix.length];

    for(int i = 0; i < pix.length; i++){
      gs[i] = pix[i] & 0x000000ff;
    }
    return gs;
  }

  /**
   * Converts an array of greylevel pixels into a Binary Fast image
   */

  public static BinaryFast gs2binary(int[] gs, int width, int height) {
  
  int[][] bin = new int[width][height];
  int tmp;

  for(int i = 0; i < width; i++) {
    for(int j = 0; j < height; j++) {
    tmp = gs[i+(j*width)];
    if (tmp >= 128) { bin[i][j] = 0xffffffff;}
    else bin[i][j] = 0xff000000;
    }}
  
  BinaryFast image = new BinaryFast(bin, width, height);

  return image;
}

  /**
   * Converts a Binary Fast image into a greylevel representation
   */
  public static int[] binary2gs(BinaryFast image) {

    int [] gs = new int [image.w*image.h];
    for(int i = 0; i < image.w; i++) {
      for(int j = 0; j < image.h; j++) {
	gs[j*image.w+i] = image.pixels[i][j] & 0x000000ff;}}
    
    return gs; 
  }

}
