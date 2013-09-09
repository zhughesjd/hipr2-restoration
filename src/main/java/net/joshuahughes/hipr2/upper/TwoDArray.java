package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;

/**
 * TwoDArray is a data structure to represent a two-dimensional array
 * of complex numbers.  ie The result of applying the 2D FFT to an image.
 *
 * @author Simon Horne.
 */
public class TwoDArray{
  /**
   * The actual width of the image represented by the TwoDArray.
   */
  public int width;
  /**
   * The actual height of the image represented by the TwoDArray.
   */
  public int height;
  /**
   * Smallest value of 2^n such that the 2^n > width and 2^n > height.
   * The dimensions of the square 2D array storing the image.
   */
  public int size;
  /**
   * The 2D array of complex numbers padded out with (0,0) 
   * to 2^n width and height.
   */
  public ComplexNumber [][] values;
  
  /**
   * Default no-arg constructor.
   */
  public TwoDArray(){
  }
  
  /**
   * Constructor that takes a TwoDArray and duplicates it exactly.
   *
   * @param a TwoDArray to be duplicated.
   */
  public TwoDArray(TwoDArray a){

    width = a.width;
    height = a.height;
//System.out.println("NEW 2D 1 w: "+width+" height: "+height);    
    size = a.size;
    values = new ComplexNumber[size][size];
    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	ComplexNumber c = new ComplexNumber(a.values[i][j]);
	values[i][j] = c;
      }
    }
  }

  /**
   * Constructor that takes a width and a height, generates the appropriate
   * size values and then sets up an array of (0,0) complex numbers.
   *
   * @param w Width of the new TwoDArray.
   * @param h Height of the new TwoDArray.
   */
  public TwoDArray(int w, int h){
    width = w;
    height = h;
//System.out.println("NEW 2D 2 w: "+width+" height: "+height);    
    int n=0;
    while(Math.pow(2,n)<Math.max(w,h)){
      ++n;
    }
    size = (int) Math.pow(2,n);
    values = new ComplexNumber [size][size];
    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	values[i][j] = new ComplexNumber(0,0);
      }
    }
  }

  /** 
   * Constructor that takes a single dimension, generates an appropriate
   * size and sets up a size x size array of (0,0) complex numbers.
   *
   * @param s Width or height of new TwoDArray.
   */
  public TwoDArray(int s){
    width = s;
    height = s;
//System.out.println("NEW 2D 3 w: "+width+" height: "+height);    
    int n=0;
    while(Math.pow(2,n)<s){
      ++n;
    }
    size = (int) Math.pow(2,n);
    values = new ComplexNumber [size][size];
    
    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	values[i][j] = new ComplexNumber(0,0);
      }
    }
  }

  /** 
   * Constructor taking int array of pixel values and width and height
   * of the image represented by the array of pixels, sets values to
   * (x,0) for each pixel x.
   *
   * @param p int array of pixel values.
   * @param w Width of image.
   * @param h Height of image.
   */
  public TwoDArray(int [] p, int w, int h){
    width = w;
    height = h;
    //System.out.println("NEW 2D 4 w: "+width+" height: "+height);    
    int n=0;

    while(Math.pow(2,n)<Math.max(w,h)){
	++n;
    }
    //System.out.println("n is "+n+" w is "+w+" h is "+h);

    size = (int) Math.pow(2,n);
    values = new ComplexNumber [size][size];
    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	values[i][j] = new ComplexNumber(0,0);
      }
    }
    /*System.err.println("Just about to add image to array");*/
    /* DONALD NAIRN 2003 */
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	  
	values[i][j] = new ComplexNumber(p[i+(j*w)], 0.0);
      }
    }
  }
  
  /** 
   * Constructor taking 2D int array of pixels values, width and height,
   * sets values to (x,0) for each pixel x.
   *
   * @param v 2D array of pixel values.
   * @param w Width of image.
   * @param h Height of image.
   */
  public TwoDArray(int [][] v, int w, int h){
    width = w;
    height = h;
//System.out.println("NEW 2D 5 w: "+width+" height: "+height);    
    int n=0;
    while(Math.pow(2,n)<Math.max(w,h)){
      ++n;
    }
    size = (int) Math.pow(2,n);
    values = new ComplexNumber [size][size];
    
    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	values[i][j] = new ComplexNumber(0,0);
      }
    }
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	values[i][j] = new ComplexNumber(v[i][j], 0.0);
      }
    }
  }

  /**
   * Constructor taking 2D array of complex numbers, width and height.
   *
   * @param v 2D array of complex numbers.
   * @param w Width of image.
   * @param h Height of image.
   */
  public TwoDArray(ComplexNumber [][] v, int w, int h){
    width = w;
    height = h;
    //System.out.println("NEW 2D 6 w: "+width+" height: "+height);    
    int n=0;
    while(Math.pow(2,n)<Math.max(w,h)){
      ++n;
    }
    size = (int) Math.pow(2,n);
    values = new ComplexNumber [size][size];

    for(int j=0;j<size;++j){
      for(int i=0;i<size;++i){
	values[i][j] = new ComplexNumber(0,0);
      }
    }
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	values[i][j] = new ComplexNumber(v[i][j]);
      }
    }
  }
  
  /** 
   * Takes a column number and returns an array containing the 
   * complex numbers in that column.
   *
   * @param n int column number (0 is first column).
   * @return ComplexNumber array containing column.
   */
  public ComplexNumber [] getColumn(int n){
    ComplexNumber [] c = new ComplexNumber [size];
    for(int i=0;i<size;++i){
	c[i] = new ComplexNumber(values[n][i]);
    }
    return c;
  }
  
  /**
   * Takes a column number and an array of complex numbers and replaces
   * that column with the new data.
   *
   * @param n int column number (0 is first column).
   * @param Array of complex numbers representing the new data.
   */
  public void putColumn(int n, ComplexNumber [] c){
    for(int i=0;i<size;++i){
	values[n][i] = new ComplexNumber(c[i]);
    }
  }
 
  /**
   * Takes a row number and an array of complex numbers and replaces
   * that row with the new data.
   *
   * @param n int row number (0 is first row).
   * @param c Array of complex numbers representing the new data.
   */
  public void putRow(int n, ComplexNumber [] c){
    for(int i=0;i<size;++i){
	values[i][n] = new ComplexNumber(c[i]);
    }
  }
  
  /** 
   * Takes a row number and returns an array containing the 
   * complex numbers in that row.
   *
   * @param n int row number (0 is first row).
   * @return ComplexNumber array containing row.
   */
  public ComplexNumber [] getRow(int n){
    ComplexNumber [] r = new ComplexNumber [size];
    for(int i=0;i<size;++i){
	r[i] = new ComplexNumber(values[i][n]);
    }
    return r;
  }
  
  /** 
   * Takes a 2D array of doubles representing an image and translates
   * and wraps the image to put (0,0) the DC value in the centre of the image.
   * at (width/2,height/2)  [because image runs -128..+127]
   *
   * @param input 2D array of doubles.
   * @return 2D array of doubles representing the new image.
   */
  public double [][] DCToCentre(double [][] input){
    double [][] output = new double [width][height];
    int x = width/2;
    int y = height/2;
    int i2,j2;
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	i2=i+x;
	j2=j+y;
	if(i2>=width)i2=i2%width;
	if(j2>=height)j2=j2%height;
	output[i][j] = input[i2][j2];
      }
    }
    return output;
  }

  /** 
   * Takes a 2D array of doubles representing an image and translates
   * and wraps the image to put the centre pixel at (0,0).
   *
   * @param input 2D array of doubles.
   * @return 2D array of doubles representing the new image.
   */
  public double [][] DCToTopLeft(double [][] input){
    double [][] output = new double [width][height];
    int i2,j2;
    int x = width/2;
    int y = height/2;
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	i2=i+x;
	j2=j+x;
	if(i2>=width)i2=i2%width;
	if(j2>=height)j2=j2%height;
	output[i][j] = input[i2][j2];
      }
    }
    return output;
  }




  public ComplexNumber [][] DCToTopLeft(ComplexNumber [][] input){
    ComplexNumber [][] output = new ComplexNumber [width][height];
    int i2,j2;
    int x = width/2;
    int y = height/2;
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	i2=i+x;
	j2=j+x;
	if(i2>=width)i2=i2%width;
	if(j2>=height)j2=j2%height;
	output[i][j] = input[i2][j2];
      }
    }
    return output;
  }

  /** 
   * Takes an array of doubles representing an image and translates
   * and wraps the image to put (0,0) the DC value in the centre of the image.
   * at (width/2,height/2)  [because image runs -128..+127]
   *
   * @param input array of doubles.
   * @return array of doubles representing the new image.
   */
  public double [] DCToCentre(double [] input){
    double [][] input2 = new double [width][height];
    double [][] output2 = new double [width][height];
    double [] output = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	input2[i][j] = input[j*width +i];
      }
    }
    int x = width/2;
    int y = height/2;
    int i2,j2;
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	i2=i+x;
	j2=j+y;
//if (input2[i][j] == 0){System.out.println("ZEROa at ("+i+","+j+") moved to ("+i2+","+j2+")");}
	if(i2>=width)i2=i2%width;
	if(j2>=height)j2=j2%height;
	output2[i][j] = input2[i2][j2];
      }
    }
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	output[j*width +i] = output2[i][j];
      }
    }
    return output;
  }
    
  /**
   * Method to extract the real parts from a TwoDArray.
   *
   * @return An array of doubles representing the real parts of
   * each element of the TwoDArray.
   */
  public double [] getReal(){
    double [] output = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	output[(j*width)+i] = values[i][j].real;
//if (values[i][j].real == 0){System.out.println("found ZERO at ("+i+","+j+")");}
      }
    }
    return output;
  }

  /**
   * Method to extract the imaginary parts from a TwoDArray.
   *
   * @return An array of doubles representing the imaginary parts of
   * each element of the TwoDArray.
   */
  public double [] getImaginary(){
    double [] output = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	output[(j*width)+i] = values[i][j].imaginary;
      }
    }
    return output;
  }

  /**
   * Method to extract the magnitude of each element from a TwoDArray.
   *
   * @return An array of doubles representing the magnitude of
   * each element of the TwoDArray.
   */
  public double [] getMagnitude(){
    double [] output = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	output[(j*width)+i] = values[i][j].magnitude();
      }
    }
    return output;
  }

  /**
   * Method to extract the phase angle of each element from a TwoDArray.
   *
   * @return An array of doubles representing the phase angle of
   * each element of the TwoDArray.
   */
  public double [] getPhase(){
    double [] output = new double [width*height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	output[(j*width)+i] = values[i][j].phaseAngle();
      }
    }
    return output;
  }

}
