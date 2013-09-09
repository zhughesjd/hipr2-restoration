package net.joshuahughes.hipr2.upper;
//package code.iface.mediansmooth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.mediansmooth.*;

/**
 * Interface for the median smoothing operator 
 * (code.operator.mediansmooth.MedianSmooth.java).
 *
 * @author Simon Horne.
 */
public class MedianSmoothInterface extends SingleInputImageInterface{

  MedianSmooth smooth;
  TwoState3x3Kernel mask;
  IterationsInterface iterations;
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;
  /**
   * Sets the actions to be performed when the start and stop buttons are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	
	int kernel [] = new int[9];
	kernel = mask.array();
	int its = iterations.number();
	if(its>0){
	  outputArray =
	    smooth.smooth_image(inputArray,kernel,
				inputWidth,inputHeight,
				iterations.number());
	  //inputArray = outputArray;
	  
	  updateOutput(createImage(new MemoryImageSource(inputWidth, 
							 inputHeight, 
							 outputArray, 
							 0, inputWidth)),
		       inputWidth,inputHeight);
	  
	  
	  //Stop the timer					      
	  time2 = System.currentTimeMillis();
	  timetaken.updateTime(time1,time2);
	  updateInput(inputImage);
      }}});
    
    startstop.stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });

  }
  /**
   * Sets up the user interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("fce5noi3.gif");
    initialiseImages();

    smooth = new MedianSmooth();
    mask = new TwoState3x3Kernel();
    timetaken = new TimeTakenInterface();
    iterations = new IterationsInterface();

    JPanel maskPanel = mask.createPanel(getDocumentBase());
    JPanel timetakenPanel = timetaken.createPanel();
    JPanel iterationsPanel = iterations.createPanel();

    setOperation();

    operatorPanel.add(maskPanel);
    operatorPanel.add(iterationsPanel);
    operatorPanel.add(timetakenPanel);

  }
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying median smoothing to an image.  Author Simon Horne.";
  }
}
