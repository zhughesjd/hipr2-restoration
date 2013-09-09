package net.joshuahughes.hipr2.upper;
//package code.iface.crimmins;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.operator.crimmins.*;

/**
 * CrimminsInterface is the user interface to the 
 * Crimmins Speckle Reduction algorithm. 
 * @author Simon Horne
 */
public class CrimminsInterface extends SingleInputImageInterface{

  Crimmins crimmins;
  /**
   * The interface for entering the number of iterations.
   */
  IterationsInterface iterations;
  /**
   * The interface for displaying the time taken by the operator.
   */
  TimeTakenInterface timetaken;
  private long time1, time2;

  /**
   * Sets up the actions to be performed when the start and stop buttons
   * are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	
	outputArray =
	  crimmins.crimmins_image(inputArray,
				  inputWidth,inputHeight,
				  iterations.number());
	
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

    initialImage("fce5noi4.gif");
    initialiseImages();

    crimmins = new Crimmins();
    iterations = new IterationsInterface();
    timetaken = new TimeTakenInterface();

    JPanel iterationsPanel = iterations.createPanel();
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(iterationsPanel);
    operatorPanel.add(timetakenPanel);

  }

  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the Crimmins Speckle Reduction Algorithm to an image.  Author Simon Horne.";
  }
}
