package net.joshuahughes.hipr2.upper;
//package code.iface.erode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.common.*;
//import code.iface.highlevel.*;
//import code.iface.kernels.*;
//import code.operator.erode.*;

/**
 * DilateInterface is the user interface to the Dilate algorithm.
 *
 * @author Simon Horne.
 */

public class ErodeInterface extends SingleBinaryImageInterface{

  /**
   * The interface for the kernel.
   */
  TwoState3x3Kernel mask;
  /**
   * The interface for selecting the number of iterations.
   */
  IterationsInterface iterations;
  /**
   * The interface for displaying the time taken by the operator.
   */
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;
  /**
   * Sets up the actions to be performed when the start and stop buttons
   * are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	
	int kernel [] = new int[9];
	kernel = mask.array();
     
	binary = 
	  Erode.erode_image(binary,kernel,iterations.number());
	outputArray = binary.convertToArray();
	updateOutput(createImage(new MemoryImageSource(
				       inputWidth, 
				       inputHeight, 
				       outputArray, 
				       0, inputWidth)),
		     inputWidth,inputHeight);
	
	
	//Stop the timer					      
	time2 = System.currentTimeMillis();
	timetaken.updateTime(time1,time2);
	updateInput(inputImage);
      }});
    
    startstop.stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    
  }
  /**
   * Sets up the interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("art3.gif");
    initialiseImages();

    mask = new TwoState3x3Kernel();
    iterations= new IterationsInterface();
    timetaken = new TimeTakenInterface();

    JPanel maskPanel = mask.createPanel(getDocumentBase());
    JPanel iterationsPanel = iterations.createPanel();
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(maskPanel);
    operatorPanel.add(iterationsPanel);
    operatorPanel.add(timetakenPanel);

  }
  public String getAppletInfo() {
    return "An applet for applying the erode algorithm to an image.  Author Simon Horne.";
  }
}
