package net.joshuahughes.hipr2.upper;
//package code.iface.skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.operator.skeleton.*;

/**
 * The interface for the skeletonise operator.
 *
 * @author Simon Horne.
 */
public class SkeletonInterface extends SingleBinaryImageInterface{

  Skeleton skeleton;
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
	
	binary = 
	  skeleton.skeleton_image(binary);
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
	skeleton = new Skeleton();
	skeleton.start();
      }
    });

  }
  /**
   * Sets up the user interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("rlf1.gif");
    initialiseImages();

    skeleton = new Skeleton();
    timetaken = new TimeTakenInterface();

    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(timetakenPanel);

  }
    /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the skeletonise algorithm to an image.  Author Simon Horne.";
  }
}
