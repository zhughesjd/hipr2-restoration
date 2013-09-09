package net.joshuahughes.hipr2.upper;

import java.io.*;
import java.awt.*;
import java.awt.image.ImageProducer;

/**
 *
 * HistogramImageCanvas draws a histogram diagram to a canvas, with red
 * lines indicating the current threshold values. Histogram images are
 * aways drawn at 256X256 pixels
 *
 *@author Judy Robertson SELLIC OnIine
 */
public class HistogramImageCanvas extends NoScaleImageCanvas{

  /**
   *The histogram diagram which will be drawn on the canvas
   */
  public Image image;

  /**
   *The low threshold value which must be indicated on the diagram
   */
    public int lowthresh =64;
  /**
   *The high threshold value which must be indicated on the diagram
   */
  public int highthresh = 192;

  //The old threshold values - needed for updating the image
  private  int oldlowthresh = 64;
  private int oldhighthresh = 192;


  /**
   *Creates a canvas for the specified histogram diagram to appear on
   *
   *@param image The histogram diagram
   */
    
  public HistogramImageCanvas(Image i) {
    
    super(i);
    
    setSize(256, 256);
  }

   /**
   *Redraws the diagram and its border onto this canvas, with red lines at
   *the threshold points
   *
   *@param g The graphics context of this canvas
   */
  public void paint(Graphics g) {
    
    //histogram always gets painted at 256 x 256
    setBackground(Color. gray);
    super. paint(g);
    //    g.drawImage(image, 1, 1,256, 256, this);
    g. setColor(Color.red);
    g. drawLine(highthresh, 1, highthresh, 256);
    g. setColor(Color. green);
    g. drawLine(lowthresh, 1, lowthresh  , 256);
    g.setColor(Color.white);
    // g.drawRect(0, 0, 258,258 );
  }
 
    
  /** 
   *Overrides update in the canvas super class so it redraws the image
   *without blanking out the background first. This should stop
   *flickering.
   *
   *@param g The graphics context of this canvas
   */
  public void update(Graphics g){
    
    g. setColor(getBackground());
    g. drawLine(oldhighthresh, 1, oldhighthresh, 256);
    g. drawLine(oldlowthresh, 1, oldlowthresh, 256);
    g. drawImage(image, 1, 1,256, 256 ,this);
    g. setColor(Color.red);
    g. drawLine(highthresh, 1, highthresh, 256);
    g. setColor(Color.green);
    g. drawLine(lowthresh , 1, lowthresh , 256);
    g.setColor(Color.white);
    //g.drawRect(0, 0, 258, 258 );
    oldlowthresh = lowthresh;
    oldhighthresh = highthresh;
  }

}  
 





