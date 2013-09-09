package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * Class containing method to apply a rectangular notch filter to an image.
 *
 * @author Simon Horne.
 */
public class DefinableNotch{

  /**
   * Method to apply a rectangular notch filter to an image.
   *
   * @param input TwoDArray representing image.
   * @param coords int array representing 
   * topleft and bottom right coordinates.
   * @param type 0 or 1, 0 remove area, 1 remove everything but area.
   * @return TwoDArray representing new image.
   */
  public static TwoDArray filter(TwoDArray input, int [] coords, int type){
    //System.out.println(input.size);
    TwoDArray output = new TwoDArray(input.values,input.size,
				     input.size);
    //System.out.println(output.size);
    int x1=Math.min(coords[0],coords[2]);
    int x2=Math.max(coords[0],coords[2]);
    int y1=Math.min(coords[1],coords[3]);
    int y2=Math.max(coords[1],coords[3]);
    int i2,j2;
    if(type == 0){
      for(int j=0;j<input.size;++j){
	for(int i=0;i<input.size;++i){
	  output.values[i][j]=input.values[i][j];
	}
      }
    }else{
      for(int j=0;j<input.size;++j){
	for(int i=0;i<input.size;++i){
	  output.values[i][j]=new ComplexNumber(0,0);
	}
      }
    }
    for(int j=y1;j<y2;++j){
      for(int i=x1;i<x2;++i){
	if(i<0){
	  i2 = i+input.size;
	}else{
	  i2 = i;
	}
	if(j<0){
	  j2 = j+input.size;
	}else{
	  j2 = j;
	}
	if(type==0){
	  output.values[i2][j2]=new ComplexNumber(0,0);
	}else{
	  output.values[i2][j2]=input.values[i2][j2];
	}
      }
    }
    return output;
  }
}
   
