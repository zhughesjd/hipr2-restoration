package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;

/* 
 * ScaleImageCanvas is a subclass of NoScaleImageCanvas optimised to allow
 * easy selection of the area to expand.
 */

public class ScaleImageCanvas extends NoScaleImageCanvas {

  public int x, y;  // top left corner of selected area
  public int width, height; // Size of the selected area
  public float ratio; // ratio between displayed and actual size of image
  public Point point = null;

  public ScaleImageCanvas(Image image) {
    super(image);
    x = 0; 
    y = 0;
    width = 0;
    height = 0;
    ratio = (float) 1.0;
  }

  public void x_Y(int x ,int y) {
    int true_x, true_y; // actual positions on original image
    true_x = (int) (x * ratio);
    true_y = (int) (y * ratio);
    if (true_x + this.width <= getImageWidth() )
      this.x = true_x;
    else
      this.x = getImageWidth() - width;
    if (true_y + this.height <= getImageHeight() )
      this.y = true_y;
    else
      this.y = getImageHeight() - height;
    
    paint(getGraphics());
  }



  public void paint(Graphics g) {
    // display the image
    super.paint(g);
    int scale_width, scale_height;

    if (width > 0 && height > 0) {
      if ((image_width >= 256) || (image_height >= 256)){
      
	if (image_width > image_height){
	
	  ratio = (float) image_width / 256;

	}  
	else if (image_height > image_width){
	  ratio = (float) image_height /  256;
	}
	scale_width = (int) (width / ratio);
	scale_height = (int) (height / ratio);
      }
      else {
	scale_width = width;
	scale_height = height;
      }

    // Now draw the rectangle



      g.setColor(Color.red);
      g.drawRect((int) (x / ratio), (int) (y / ratio) , scale_width, scale_height);
    }
  }
} /* ScaleImageCanvas */



