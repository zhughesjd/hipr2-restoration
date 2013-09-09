package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * Class containing method to apply a gaussian filter to an image.
 *
 * @author Simon Horne.
 */
public class Gaussian{
   /**
   * Method to apply a gaussian filter to an image.
   *
   * @param input TwoDArray representing the image.
   * @param x,y the position of the centre of the filter
   * @param size the size of the filter.
   * @return TwoDArray representing new image.
   */
  public static TwoDArray smooth(TwoDArray input, int x, int y, double size){
    TwoDArray output = new TwoDArray(input.values,input.size,
				     input.size);
    
    double [] reals = input.getReal();
    double [] imaginaries = input.getImaginary();
    double cmin = imaginaries[0];
    double cmax = imaginaries[0];
    for(int j=0;j<input.size;++j){
      for(int i=0;i<input.size;++i){
	double s = input.size;
	double ir,jr;
	if(i<=s/2 && j<=s/2){
	  ir = i;
	  jr = j;
	}else if(i>s/2 && j<=s/2){
	  ir = i-s;
	  jr = j;
	}else if(i<=s/2 && j>s/2){
	  ir = i;
	  jr = j-s;
	}else{
	  ir = i-s;
	  jr = j-s;
	}
	double ab = ((ir-x)*(ir-x)+(jr-y)*(jr-y))/(2*size*size);
	double g = Math.pow(Math.E,-ab);
	if(imaginaries[(j*input.size)+i]<cmin){
	  cmin = imaginaries[(j*input.size)+i];
	}
	if(imaginaries[(j*input.size)+i]>cmax){
	  cmax = imaginaries[(j*input.size)+i];
	}
	
	double real = reals[(j*input.size)+i] * g;
	double imag = imaginaries[(j*input.size)+i] * g;
	output.values[i][j] = new ComplexNumber(real,imag);
	if((i==0&&j==0)||(i==input.size/2&&j==input.size/2)){
	  double temp1 = reals[(j*input.size)+i];
	  double temp2 = imaginaries[(j*input.size)+i];
	  //System.out.println(temp1+" "+temp2);
	  //System.out.println(real+" "+imag);
	  //System.out.println(g);
	}
      }
    }
    //System.out.println(cmin+" "+cmax);
    return output;
  }
}
