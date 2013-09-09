package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * The InverseFFT class contains a method to apply the 2D inverse FFT to a
 * TwoDArray.
 *
 * @author Simon Horne
 */
public class InverseFFT{

  /**
   * Default no argument constructor.
   */
  public InverseFFT(){
  }
  
  /**
   * Recursively applies the 1D inverse FFT algorithm.
   *
   * @param x ComplexNumber array containing the input to the 1D inverse FFT.
   * @return ComplexNumber array containing the result of the 1D inverse FFT.
   */
  public ComplexNumber [] recursiveInverseFFT (ComplexNumber [] x){
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
      z1 = new ComplexNumber(0.0, 2*(Math.PI)/n);
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
      even = recursiveInverseFFT(sum);
      odd = recursiveInverseFFT(diff);
      
      for(int i=0;i<m;++i){
	result[i*2] = new ComplexNumber(even[i]);
	result[i*2 + 1] = new ComplexNumber(odd[i]);
      }
    }
    return result;
  }
    
  /**
   * Takes a TwoDArray, applies the 2D inverse FFT to the input by applying
   * the 1D inverse FFT to each column and then each row in turn.
   *
   * @param input TwoDArray containing the input image data.
   * @return TwoDArray containing the new image data.
   */
  public TwoDArray transform(TwoDArray input){
    TwoDArray intermediate = new TwoDArray(input.width, input.height);
    TwoDArray output = new TwoDArray(input.width, input.height);
     
    for(int i=0;i<input.size;++i){
	intermediate.putColumn(i, recursiveInverseFFT(input.getColumn(i)));
    }

    for(int i=0;i<intermediate.size;++i){
	output.putRow(i, recursiveInverseFFT(intermediate.getRow(i)));
    }
    return output;
  }
  
}



