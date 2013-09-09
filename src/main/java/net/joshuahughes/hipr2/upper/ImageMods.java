package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;
import java.awt.*;

/**
 * Class containing methods to manipulate and modify TwoDArray images.
 *
 * @author Simon Horne.
 */
public class ImageMods{

  /**
   * Default no-args constructor.
   */
    public ImageMods(){
  }
  
  /**
   * Method to find the maximum value from an array of doubles.
   *
   * @param values an array of doubles.
   * @return The maximum value.
   */
  public static double maxValue(double [] values){
    double max = values[0];
    for(int i=1;i<values.length;++i){
      if(values[i]>max) max=values[i];
    }
    return max;
  }

  /**
   * Method to find the minimum value from an array of doubles.
   *
   * @param values an array of doubles.
   * @return The minimum value.
   */
  public static double minValue(double [] values){
    double min = values[0];
    for(int i=1;i<values.length;++i){
      if(values[i]<min) min=values[i];
    }
    return min;
  }


  /**
   * Method to convert an array of doubles to absolute values.
   *
   * @param values an array of doubles.
   * @return An array of absolute value doubles.
   */
  public static double [] abs(double [] values){
    for(int i=0;i<values.length;++i){
      values[i] = Math.abs(values[i]);
    }
    return values;
  }

  /**
   * Method to slide and scale an array of doubles so that the minimum
   * values is 0 (all positive).
   *
   * @param values An array of doubles.
   * @return An array of positive doubles.
   */
  public static double [] allPositive(double [] values){
    double [] output = new double [values.length];
    double m = minValue(values);
    if(m<0){
      for(int i=0;i<values.length;++i){
	output[i] = values[i]-m;
      }
      return output;
    }
    else return values;
  }


  /** 
   * A method to convert an array of grey values to an array of pixel values.
   *
   * @param values An array of grey values (all positive).
   * @return An array of pixel values.
   */
  public static int [] toPixels(double [] values){
    int grey;
//    double [] greys = new double [values.length];
    int [] pixels = new int [values.length];
//    for(int i=0;i<greys.length;++i){
//      greys[i] = values[i];
//    }
//    double max = maxValue(greys);
    double max = maxValue(values);
    double scale;
    if (max == 0) scale = 1.0;
    else scale = 255.0/max;
    
    //System.out.println(max);
//    for(int i=0;i<greys.length;++i){
    for(int i=0;i<values.length;++i){        
//      greys[i] = greys[i] * 255/max;
//      grey = (int) Math.round(greys[i]);
      grey = (int) Math.round(values[i]*scale);
      pixels[i] = new Color(grey,grey,grey).getRGB(); //recombine greys
    }
    return pixels;
  }
  
  /**
   * A method to convert an array of doubles to an array of their log values.
   *
   * @param values An array of doubles (all positive).
   * @return An array of doubles.
   */
  public static double [] logs(double[] values){
    double [] output = new double [values.length];
    for(int i=0;i<values.length;++i){
      values[i] = values[i]*10000;
    }
    double c = 255/Math.log(1+maxValue(values));
    for(int i=0;i<values.length;++i){
      output[i] = c * Math.log(1+values[i]);
    }
    return output;
  }
}
