package net.joshuahughes.hipr2.upper;

import java.io.*;
import java.awt.*;
import java.awt.image.ImageProducer;
import javax.swing.*;

/**
 * NoScaleImageCanvas simply ties a full size image to a canvas
 * and later updates the onscreen image.
 *
 *@author Timothy Sharman
 */
public class NoScaleImageCanvas extends JLabel{

  /**
   *The image drawn to this canvas
   */
  
  protected int image_width = -1;
  protected int image_height = -1;
  
  ImageIcon picIcon;

  /**
   * Various tools for manipulating images.
   */

  public ImageTools imageTools = new ImageTools();

  /**
   *Creates a new image canvas to draw the specified image
   *@param image The image to be drawn
   */
  public NoScaleImageCanvas(Image image) {
    
    picIcon = new ImageIcon(image);
    image_width = image.getWidth(this);
    image_height = image. getHeight(this);
    //super. setIcon(imageScale(picIcon));
    super. setIcon(picIcon);
  }
 
  /**
   *Allows the updating of the image on the canvas
   *@param image The image to be drawn
   */
  public void updateImage(Image image){
    
    picIcon = new ImageIcon(image);
    image_width = image. getWidth(this);
    image_height = image. getHeight(this);
    this. setIcon(imageScale(picIcon));
  }

  /**
   *Returns the image width
   *@return image_width the image width
   */
  public int getImageWidth(){
    return image_width;
  }

  /**
   *Returns the image height
   *@return image_height the image height
   */
  public int getImageHeight(){
    return image_height;
  }

  /**
   *Redefined paint method
   *@param g The graphics context
   */

  public void paint(Graphics g){
    Image image = picIcon. getImage();
  
    //if ((image_width <= 256) && (image_height <= 256)){
      g.setColor(Color.white);
      //g.drawRect(0, 0, image_width +1 , image_height +1 );
      g.drawImage(image, 1, 1, this);
      //} 
      //else {
      //The image is bigger than it should be and it has to be scaled
      // g.setColor(Color.white);
//       //g.drawRect(0, 0, scalex +2 , scaley + 2);
//       ImageIcon tmp = imageScale(picIcon);
//       image = tmp. getImage();
//       g.drawImage(image, 1, 1, this);
//     }
  }

  /**
   *Scales the image
   *@param unscaledpic The unscaled image
   *@return scaledpic The scaled image
   */

  public ImageIcon imageScale(ImageIcon unscaledpic){
    Image image, image2;
    ImageIcon icon2;
    image = unscaledpic. getImage();
    
    int scalex = image_width;
    int scaley = image_height;
    
    if ((image_width >= 256) || (image_height >= 256)){
      
      if (image_width > image_height){
	
	float ratio = (float) image_width / (float) image_height;
	scalex = 256;
	scaley = (int) ((float) 256/ ratio);
      }  
      else if (image_height > image_width){
	
	float ratio = (float) image_height /  (float) image_width;
	scalex = image_width;
	scaley = 256;
	scalex = (int) ( (float) 256 / ratio);
      } 
      else {
	
	scalex = 256;
	scaley = 256;
      }
      
    }
    
    image2 = image.getScaledInstance(scalex,scaley,1);
    icon2 = new ImageIcon(image2);
    return icon2;
  }

}









