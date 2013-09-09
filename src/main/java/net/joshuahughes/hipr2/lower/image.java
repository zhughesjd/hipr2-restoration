package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last modified 16/9/99

//package code.connections;

import java.awt.color.*;
import java.awt.*;
import java.util.*;

/**
 * All image representations extend image.  All image representations
 * explicitly store the width and height of the image and the image itself
 * can always be converted to an array of integers.
 */
public abstract class image{
  /**
   * The width of the image.
   */
  protected int width;
  /**
   * The height of the image.
   */
  protected int height;

  /**
   * Returns the width of the image in pixels.
   * @return width in pixels
   */
  public int getWidth(){
    return width;
  }

  /**
   * Returns the height of the image in pixels.
   * @return height in pixels
   */
  public int getHeight(){
    return height;
  }

  /**
   * All image representations define this abstract method to enable
   * any image to be converted to an integer array.
   * @return the image in integer array form
   */
  public abstract int [] getValues();
}
