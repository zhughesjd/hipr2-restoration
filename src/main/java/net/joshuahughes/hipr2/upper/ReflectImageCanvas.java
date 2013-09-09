package net.joshuahughes.hipr2.upper;

import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;

/** 
 * RelfectImageCanvas is a subclass of NoScaleImageCanvas optimised to allow
 * easy selection of the axis of reflection.
 */

public class ReflectImageCanvas extends NoScaleImageCanvas {

  public int x, y;  // point axis passes through
  public double angle; // Angle of axis WRT X axis
  public float ratio; // ratio between displayed and actual size of image


  public ReflectImageCanvas(Image image) {
    super(image);
    x = -1; 
    y = -1;
    angle = 0;
    ratio = (float) 1.0;
  }

  public void setLineCenter (int x, int y) {
    this.x = (int) (x * ratio);
    this.y = (int) (y * ratio);

    paint(getGraphics());
  }


  public void paint(Graphics g) {
    // display the image

    int xzero = -1;
    int yzero = -1;
    int xwidth = -1;
    int yheight = -1;
    Point [] points = new Point[2];
    int which_point = 0;

    super.paint(g);

    if ((image_width >= 256) || (image_height >= 256)){
      
      if (image_width > image_height){
	
	ratio = (float) image_width / 256;

      }  
      else {
	ratio = (float) image_height /  256;
      }
    }

    if (x == -1) {
      x = image_width / 2;
    }

    if (y == -1) {
      y = image_height / 2;
    }

    // Now draw the axis of reflection
    g.setColor(Color.red);

    // find the point where the line crosses the X and Y axes
    double rad_angle = (180 - angle)* Math.PI / 180;
    if (angle == 90 || angle == 270) {
	
      yzero = this.x;
      g.drawLine( (int) (yzero / ratio), 0, (int) (yzero / ratio), 
		    (int) image_height);
//***		    (int) (image_height / ratio));
      return;
    } 
    if (angle == 0 || angle == 180) {

      xzero = this.y;
      g.drawLine( 0, (int) (xzero / ratio), (int) (image_width / ratio), 
		      (int) (xzero / ratio));
      return;
    }
      
    
    xzero = (int) (this.y + (((-1 * this.x) / Math.cos(rad_angle)) 
			     * Math.sin(rad_angle))) ;

    if (xzero >= 0 && xzero <= image_height) {
      points [which_point++] = new Point(0, (int) (xzero / ratio));
    }

    xwidth = (int) (this.y + ((image_width - this.x) / Math.cos(rad_angle))
		    * Math.sin(rad_angle));

    if (xwidth >=0 && xwidth <= image_height) {
      points[which_point++] = new Point((int)(image_width / ratio),
					(int) (xwidth / ratio));
    }

    if (which_point  < 2) { // Haven't found the two ends of the axis yet

      yheight = (int) (this.x + ((image_height - this.y) / Math.sin(rad_angle))
		       * Math.cos (rad_angle));
      if (yheight >=0 && yheight <= image_width) {
      points[which_point++] = new Point((int)(yheight / ratio),
					(int) (image_height / ratio));
      }
    }

    if (which_point  < 2) { // Haven't found the two ends of the axis yet

      yzero = (int) (this.x + (((-1 * this.y) / Math.sin(rad_angle)) 
			       * Math.cos(rad_angle))) ;

      points[which_point] = new Point((int)(yzero/ratio), 0);
    }

    g.drawLine( points[0].x, points[0].y, points[1].x, points[1].y);


  }  /* paint */
  
} /* ReflectImageCanvas */



