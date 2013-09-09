package net.joshuahughes.hipr2.upper;
//package code.iface.meansmooth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.meansmooth.*;

/**
 * The interface for the mean smooth operator (code.operator.meansmooth.MeanSmooth.java.
 *
 * @author Simon Horne.
 */
public class MeanSmoothInterface extends SingleInputImageInterface{

  TwoState3x3Kernel mask;
  IterationsInterface iterations;
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;
  /**
   * Sets up the actions to be performed when the start and stop button
   * are pressed.
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
	    MeanSmooth.smooth_image(inputArray,kernel,
				    inputWidth,inputHeight,
				    its);
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
    return "An applet for applying the mean smoothing algorithm to an image.  Author Simon Horne.";
  }
}
