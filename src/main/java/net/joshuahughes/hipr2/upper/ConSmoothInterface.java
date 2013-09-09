package net.joshuahughes.hipr2.upper;
//package code.iface.consmooth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.consmooth.*;

/**
 *ConSmooth Interface is the user interface to the conservative
 *smoothing algorithm. 
 * @author Simon Horne
 */
public class ConSmoothInterface extends SingleInputImageInterface{

  /**
   * The interface for the on/off kernel.
   */
  TwoState3x3Kernel mask;
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

	outputArray =
	  ConSmooth.smooth_image(inputArray,kernel,
			      inputWidth,inputHeight);
	inputArray = outputArray;
	
	updateOutput(createImage(new MemoryImageSource(inputWidth, 
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

    initialImage("fce5noi3.gif");
    initialiseImages();

    mask = new TwoState3x3Kernel();
    timetaken = new TimeTakenInterface();

    JPanel maskPanel = mask.createPanel(getDocumentBase());
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(maskPanel);
    operatorPanel.add(timetakenPanel);

  }

  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying conservative smoothing to an image.  Author Simon Horne.";
  }
}
