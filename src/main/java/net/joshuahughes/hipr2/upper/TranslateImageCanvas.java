package net.joshuahughes.hipr2.upper;

import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.Color;
import java.net.*;

/* 
 * TranslateImageCanvas is a subclass of NoScaleImageCanvas optimised to allow
 * the user to define the translation using the mouse.
 *@author Timothy Sharman
 */

public class TranslateImageCanvas extends NoScaleImageCanvas {

  public int start_x, start_y, end_x, end_y;  // Start and end of line delimiting translation
  private boolean dragging;
  public float ratio; // ratio between displayed and actual size of image


  public TranslateImageCanvas(Image image) {
    super(image);
    start_x = -1; 
    start_y = -1;
    end_x = -1; 
    end_y = -1;
    dragging = false;
    ratio = (float) 1.0;
  }

  //Called when the line being drwan is first started
  public void setStart(int x, int y) {
    start_x = (int) (x * ratio);
    start_y = (int) (y * ratio);
  }

  //Called to update the current end of the line, and redraw it.
  public void updateLine(int x, int y) {
  
    end_x = (int) (x * ratio);
    end_y = (int) (y * ratio);

    if(x < 0){
      end_x = 1;
    }
    if(y < 0){
      end_y = 1;
    }
    if(x > 256){
      end_x = 255;
    }
    if(y > 256){
      end_y = 255;
    }
  
    paint(getGraphics());
  }

  //Called to update the end points of the line and make them fit
  //into the image canvas space.
  public void endLine(int x, int y) {
    if (x < 0) {
      end_x = 0;
    } else if (x > 256) {
      end_x = 256;
    }
    else {
      end_x = (int) (x * ratio);
    }
    
    if (y < 0) {
      end_y = 0;
    } else if (y > 256) {
      end_y = 256;
      }
    else {
      end_y = (int) (y * ratio);
    }
    paint(getGraphics());
  }
  
  public void paint(Graphics g) {
    // display the image

    super.paint(g);

    if ((image_width >= 256) || (image_height >= 256)){
      
      if (image_width > image_height){
	
	ratio = (float) image_width / 256;

      }  
      else {
	ratio = (float) image_height /  256;
      }
    }


    g.setColor(Color.red);

    if (start_x != -1 && start_y != -1) {

      g.drawLine( (int) (start_x / ratio), (int) (start_y / ratio),
		  (int) (end_x / ratio), (int) (end_y / ratio));
      if (dragging == false) {
	// draw arrowhead
      }
    }

  }  /* paint */
  
} /* TranslateImageCanvas */

