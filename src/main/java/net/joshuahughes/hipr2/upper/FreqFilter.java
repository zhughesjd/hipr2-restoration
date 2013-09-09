package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * Class containing method to apply a frequency filter to an image.
 *
 * @author Simon Horne.
 */
public class FreqFilter{

  /**
   * Method to apply a high or low pass filter to an image.
   *
   * @param input TwoDArray representing the image.
   * @param h boolean true - highpass, false - lowpass.
   * @return TwoDArray representing new image.
   */
  public static TwoDArray filter(TwoDArray input, boolean flag, int r){
    TwoDArray output = new TwoDArray(input.values,input.size,input.size);
				     
    int i2,j2;
    double r2 = r*r;
    if(flag){//if low pass filter
      for(int j=0;j<input.size;++j){
	for(int i=0;i<input.size;++i){
	  if(i>=input.size/2) i2=i-input.size;
	  else i2=i;
	  if(j>=input.size/2) j2=j-input.size;
	  else j2=j;
	  double d2 = i2*i2+j2*j2;
	  if(d2>r2) output.values[i][j] = new ComplexNumber(0,0);
	  else output.values[i][j] = input.values[i][j];
	}
      }
    }else {//else high pass filter
      for(int j=0;j<input.size;++j){
	for(int i=0;i<input.size;++i){
	  if(i>=input.size/2) i2=i-input.size;
	  else i2=i;
	  if(j>=input.size/2) j2=j-input.size;
	  else j2=j;
	  double d2 = i2*i2+j2*j2;
	  if(d2>r2) output.values[i][j] = input.values[i][j];
	  else output.values[i][j] = new ComplexNumber(0,0);
	}
      }
    }
    return output;
  }
}
