package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * Class containing method to apply a notch filter to an image.
 *
 * @author Simon Horne.
 */
public class NotchFilter{

  /**
   * Method to apply notch filter to image by setting pixels within
   * width of the x and y axes to (0,0) but leaving all pixels within
   * radius of (0,0) untouched.
   *
   * @param input TwoDArray representing the input image.
   * @param w Width of the notch in pixels.
   * @param r Radius of the area centred on (0,0) to be left intact.
   * @return TwoDArray of output image.
   */
  public static TwoDArray notch(TwoDArray input, int w, int r){
    TwoDArray input2 = new TwoDArray(input.values,input.size,input.size);
    for(int i=r;i<input.size-r+1;++i){

      // positive side of axis
      for(int j=0;j<w+1;++j){
	input2.values[i][j]=new ComplexNumber(0,0);
	input2.values[j][i]=new ComplexNumber(0,0);
      }

      // negative side of axis
      for(int j=input.size-w;j<input.size;++j){
	input2.values[i][j]=new ComplexNumber(0,0);
	input2.values[j][i]=new ComplexNumber(0,0);
      }
    }

    return input2;
  }
}
