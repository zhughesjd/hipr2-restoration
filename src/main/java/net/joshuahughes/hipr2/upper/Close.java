package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * The close algorithm simply dilates and then erodes the image 
 * using the same kernel.
 *
 * @author Simon Horne.
 */
public class Close extends Thread {

  /**
   * Default no-arg constructor.
   */
  public Close() {
  }

  /**
   * Method that takes a binary representation of an image, dilates
   * using the specified kernel and then erodes using the same kernel.
   *
   * @param b The binary representation of the image.
   * @param kernel The array representing the kernel values.
   * @param iterations The number of iterations to be carried out.
   * @return The binary representation of the new image.
   */
  public static BinaryFast close_image(BinaryFast b, int [] kernel,
				      int iterations){
    b = Dilate.dilate_image(b, kernel, iterations);
    b = Erode.erode_image(b, kernel, iterations);
    return b;
  }

}












