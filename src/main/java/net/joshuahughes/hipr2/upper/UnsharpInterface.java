package net.joshuahughes.hipr2.upper;
//package code.iface.unsharp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.operator.unsharp.*;

/**
 * Interface for the unsharp filter operator (code.operator.unsharp.Unsharp.java).
 *
 * @author Simon Horne.
 */
public class UnsharpInterface extends SingleInputImageInterface{

  KValue k;
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;
  /**
   * Sets the actions to be performed when the start and stop buttons are pushed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	//System.out.println("About to start");
	double kValue = k.value();
	if(kValue>=0){
	  outputArray =
	    Unsharp.unsharp_image(inputArray,inputWidth,inputHeight,k.value());
	  
	  inputArray = outputArray;
	  
	  updateOutput(createImage(new MemoryImageSource(
							 inputWidth, 
							 inputHeight, 
							 outputArray,0, 
							 inputWidth)),
		       inputWidth,inputHeight);
	  //System.out.println(outputWidth+" "+outputHeight);
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
   * Sets up the interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("brg1.gif");
    initialiseImages();

    timetaken = new TimeTakenInterface();
    k = new KValue();

    JPanel timetakenPanel = timetaken.createPanel();
    JPanel kPanel = k.createPanel();

    setOperation();
    
    operatorPanel.add(kPanel);
    operatorPanel.add(timetakenPanel);

  }
    /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the unsharp operator to an image.  Author Simon Horne.";
  }
}
