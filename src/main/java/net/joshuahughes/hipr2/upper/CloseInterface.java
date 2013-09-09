package net.joshuahughes.hipr2.upper;
//package code.iface.close;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.close.*;

/**
 * The interface for the close algorithm.
 *
 * @author Simon Horne.
 */
public class CloseInterface extends SingleBinaryImageInterface{

  /**
   * The interface for the two state on/off 3x3 kernel.
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
   * Sets up what happens when the start and stop buttons are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	
	int kernel [] = new int[9];
	kernel = mask.array();

	binary = 
	    Close.close_image(binary,kernel,iterations.number());
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
   * Sets up the interface and draws everything onscreen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("art4.gif");
    initialiseImages();

    iterations = new IterationsInterface();
    mask = new TwoState3x3Kernel();
    timetaken = new TimeTakenInterface();

    JPanel maskPanel = mask.createPanel(getDocumentBase());
    JPanel timetakenPanel = timetaken.createPanel();
    JPanel iterationsPanel = iterations.createPanel();
    iterations.l.setText("Depth");

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
    return "An applet for applying the close algorithm to a binary image.  Author Simon Horne.";
  }
}
