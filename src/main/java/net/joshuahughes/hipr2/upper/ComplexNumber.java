package net.joshuahughes.hipr2.upper;

import java.lang.Math.*;

/**
 * Class representing a complex number.
 *
 * @author Simon Horne.
 */
public class ComplexNumber{
  /**
   * The real part of the complex number.
   */
  public double real;
  /** 
   * The imaginary part of the complex number.
   */
  public double imaginary;

  /**
   * Default no-arg constructor.
   */
  public ComplexNumber(){
  }

  /**
   * Constrctor taking two doubles, the real and the imaginary values.
   *
   * @param r A double representing the real value.
   * @param i A double representing the imaginary value.
   */
  public ComplexNumber(double r, double i){
    real = r;
    imaginary = i;
  }

  /**
   * Constructor taking a ComplexNumber to make an identical copy.
   *
   * @param c ComplexNumber to be copied.
   */
  public ComplexNumber(ComplexNumber c){
    real = c.real;
    imaginary = c.imaginary;
  }

  /**
   * Method to obtain the magnitude of the complex number.
   *
   * @return double representing the magnitude.
   */ 
  public double magnitude(){
    return Math.sqrt(this.cNorm());
  }

  /**
   * Method to obtain the phase angle (in radians) of the complex number.
   *
   * @return double representing the phase angle in radians.
   */ 
  public double phaseAngle(){
    if(real==0 && imaginary == 0) return 0;
    else return Math.atan(imaginary/real);
  }

  double cNorm (){
    return real*real + imaginary*imaginary;
  }

  /**
   * Method to obtain the exp of a complex number.
   *
   * @param ComplexNumber input.
   * @return ComplexNumber output.
   */
  public static ComplexNumber cExp (ComplexNumber z){
    ComplexNumber x,y;
    x = new ComplexNumber(Math.exp(z.real),0.0);
    y = new ComplexNumber(Math.cos(z.imaginary),Math.sin(z.imaginary));
    return cMult (x,y);
  }

  /**
   * Method to multiply two complex numbers together.
   *
   * @param z1 ComplexNumber.
   * @param z2 ComlexNumber.
   * @return z3 ComplexNumber z3 = z1 * z2.
   */
  public static ComplexNumber cMult (ComplexNumber z1, ComplexNumber z2){
    ComplexNumber z3 = new ComplexNumber();
    z3.real = (z1.real)*(z2.real) - (z1.imaginary)*(z2.imaginary);
    z3.imaginary = (z1.real)*(z2.imaginary) + (z1.imaginary)*(z2.real);
    return z3;
  }

  /** 
   * Method to add two complex numbers together.
   *
   * @param z1 ComplexNumber.
   * @param z2 ComlexNumber.
   * @return z3 ComplexNumber z3 = z1 + z2.
   */
  public static ComplexNumber cSum (ComplexNumber z1, ComplexNumber z2){
    ComplexNumber z3 = new ComplexNumber();
    z3.real = z1.real + z2.real;
    z3.imaginary = z1.imaginary + z2.imaginary;
    return z3;
  }

  /** 
   * Method to divide a complex number by another.
   *
   * @param z1 ComplexNumber.
   * @param z2 ComlexNumber.
   * @return z3 ComplexNumber z3 = z1 / z2.
   */
  public static ComplexNumber cDiv (ComplexNumber z1, ComplexNumber z2){
    ComplexNumber z3 = new ComplexNumber();
    double n = z2.cNorm();
    
    z3.real = ((z1.real*z2.real) + (z1.imaginary*z2.imaginary))/n;
    z3.imaginary = ((z2.real*z1.imaginary) - (z1.real*z2.imaginary))/n;
    return z3;
  }

  /** 
   * Method to subtract a complex number from another.
   *
   * @param z1 ComplexNumber.
   * @param z2 ComlexNumber.
   * @return z3 ComplexNumber z3 = z1 - z2.
   */
  public static ComplexNumber cDif (ComplexNumber z1, ComplexNumber z2){
    ComplexNumber z3 = new ComplexNumber();

    z3.real = z1.real - z2.real;
    z3.imaginary = z1.imaginary - z2.imaginary;
    return z3;
  }

}
