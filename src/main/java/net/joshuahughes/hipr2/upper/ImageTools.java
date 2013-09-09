package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
/**
 * This class implements methods to apply to images.
 */
public class ImageTools{
  /**
   * Scales an image into 256*256.
   * @param icon the ImageIcon representing the image.
   * @return the ImageIcon representing the scaled image.
   */ 
  public ImageIcon scaleImage(ImageIcon icon){
    Image image, image2;
    ImageIcon icon2;
    image = icon.getImage();
    image2 = image.getScaledInstance(256,256,1);
    icon2 = new ImageIcon(image2);
    return icon2;
  }
  /**
   * Returns the width of the image
   */
  public int getWidth(ImageIcon icon){
    int width;
    width = icon.getIconWidth();
    return width;
  }
  /**
   * Returns the height of the image.
   */
  public int getHeight(ImageIcon icon){
    int height;
    height = icon.getIconHeight();
    return height;
  }
  /**
   * Converts an imageIcon into a 1D array.
   * @param the imageIcon representing the image
   * @return the 1D int array representing the image.
   */
  public int [] examineInput(ImageIcon inputIcon){
    int inputArray[];
    int width;
    int height;
    width = getWidth(inputIcon);
    height = getHeight(inputIcon);
    inputArray = new int[height*width];

    PixelGrabber pg = new PixelGrabber(
		      inputIcon.getImage(),
		      0,0,width,height,inputArray,0,width);
    try{
      pg.grabPixels();
    }
    catch(InterruptedException e3){System.out.println("ERROR");};

    return inputArray;
  }


}
