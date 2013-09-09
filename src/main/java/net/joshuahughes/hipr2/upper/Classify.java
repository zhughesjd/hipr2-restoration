package net.joshuahughes.hipr2.upper;

import java.awt.*;

public class Classify{
  /**
   * Takes a pixel grey-level and an array of classifications
   * returns a value between 1 and the number of classifications
   * if the pixel value falls inside a classification and 0 if
   * the pixel cannot be classified.
   * @param pixel the grey-level value to be classified
   * @classification the array of high and low limits for classifications
   * @return the pixel classification (0 is unclassified)
   */
  int classifyPixel(int pix, Limits [] classification){
    for(int i=0;i<classification.length;++i){
      if((pix >= classification[i].lolim) && 
	 (pix <= classification[i].uplim)){

	return i+1;//classify pixel as array location (start at 1)

      }
    }
    return 0;//if pixel does not fit any classification
  }

  /**
   * Classifies each pixel in the input array according to the
   * array of classifications (high/low limits).
   * @param input the input image
   * @param width of the input image
   * @param classifications 
   * @param height of the input image
   * @return output the output classification image
   */
  public int [] classifyImage(int [] input, int width, int height,
			      Limits [] classification){

    int [] output = new int[width*height];

    for(int i=0;i<input.length;++i){ //for each pixel in the input image
      int pixel = input[i] & 0x000000ff; //get pixel grey level
      output[i] = classifyPixel(pixel, classification);
    }
    return output;
  }
}



