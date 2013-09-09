package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * The FFT class contains methods to apply the 2D FFT to a
 * TwoDArray.
 *
 * @author Simon Horne
 */
public class FFT{
  
  /**
   * Data structure to hold the input to the algorithm.
   */
  public TwoDArray input;
  /**
   * Data structure to hold the intermediate results of the algorithm.
   * After applying the 1D FFT to the columns but before the rows.
   */
  public TwoDArray intermediate;
  /**
   * Data structure to hold the ouput of the algorithm.
   */
  public TwoDArray output;

  /**
   * Default no argument constructor.
   */
  public FFT(){
  }

  /** 
   * Constructor to set up an FFT object and then automatically 
   * apply the FFT algorithm.
   *
   * @param pixels  int array containing the image data.
   * @param w  The width of the image in pixels.
   * @param h  The height of the image in pixels.
   */
  public FFT(int [] pixels, int w, int h){
    input = new TwoDArray(pixels,w,h);
    intermediate = new TwoDArray(pixels,w,h);
    output = new TwoDArray(pixels,w,h);
    transform();
  }
  
  /**
   * Method to recursively apply the 1D FFT to a ComplexNumber array.
   *
   * @param  x  A ComplexNumber array containing a row or a column of
   * image data.
   * @return A ComplexNumber array containing the result of the 1D FFT.
   */
  static ComplexNumber [] recursiveFFT (ComplexNumber [] x){
    ComplexNumber z1,z2,z3,z4,tmp,cTwo;
    int n = x.length;
    int m = n/2;
    ComplexNumber [] result = new ComplexNumber [n];
    ComplexNumber [] even = new ComplexNumber [m];
    ComplexNumber [] odd = new ComplexNumber [m];
    ComplexNumber [] sum = new ComplexNumber [m];
    ComplexNumber [] diff = new ComplexNumber [m];
    cTwo = new ComplexNumber(2,0);
    if(n==1){
      result[0] = x[0];
    }else{
      z1 = new ComplexNumber(0.0, -2*(Math.PI)/n);
      tmp = ComplexNumber.cExp(z1);
      z1 = new ComplexNumber(1.0, 0.0);
      for(int i=0;i<m;++i){
	z3 = ComplexNumber.cSum(x[i],x[i+m]);
	sum[i] = ComplexNumber.cDiv(z3,cTwo);
	
	z3 = ComplexNumber.cDif(x[i],x[i+m]);
	z4 = ComplexNumber.cMult(z3,z1);
	diff[i] = ComplexNumber.cDiv(z4,cTwo);
	
	z2 = ComplexNumber.cMult(z1,tmp);
	z1 = new ComplexNumber(z2);
      }
      even = recursiveFFT(sum);
      odd = recursiveFFT(diff);
      
      for(int i=0;i<m;++i){
	result[i*2] = new ComplexNumber(even[i]);
	result[i*2 + 1] = new ComplexNumber(odd[i]);
      }
    }
    return result;
  }
   
  /**
   * Method to apply the 2D FFT by applying the recursive 1D FFT to the
   * columns and then the rows of image data.
   */ 
  void transform(){
      
      for(int i=0;i<input.size;++i){
	  intermediate.putColumn(i, recursiveFFT(input.getColumn(i)));
      }
      for(int i=0;i<intermediate.size;++i){
	  output.putRow(i, recursiveFFT(intermediate.getRow(i))); 
      }
      for(int j=0;j<output.values.length;++j){
	  for(int i=0;i<output.values[0].length;++i){
	      intermediate.values[i][j] = output.values[i][j];
	      input.values[i][j] = output.values[i][j];
	  }
      }
  }
}

