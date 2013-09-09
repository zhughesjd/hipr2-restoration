package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Open is an algorithm to open a binary image using a 3x3 kernel using
 * erosion followed by dilation.
 *
 * @author Simon Horne.
 */
public class Open extends Thread {

  /**
   * Default no-arg constructor.
   */
  public Open() {
  }

  /**
   * Method to open a binary image by eroding and then dilating the image
   * using the specified kernel.
   */
  public static BinaryFast open_image(BinaryFast b, int [] kernel, 
				      int iterations){
    int i=0;
      b = Erode.erode_image(b, kernel, iterations);
      b = Dilate.dilate_image(b, kernel, iterations);
    return b;
  }

}












