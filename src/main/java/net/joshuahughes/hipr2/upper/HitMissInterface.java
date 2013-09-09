package net.joshuahughes.hipr2.upper;
//package code.iface.hitmiss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.hitmiss.*;

/**
 * The interfacefor the hitmiss operator.
 *
 * @author Simon Horne
 */

public class HitMissInterface extends SingleBinaryImageInterface{

  ThreeState3x3Kernel kernel;
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;

  /**
   * Sets up the actions to be performed when the start and
   * stop buttons are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	
	int kernelArray [] = new int[9];
	kernelArray = kernel.array();

	binary =
	  HitMiss.hitmiss_image(binary,kernelArray);
	//System.out.println((new Color(binary.pixels[0][0])).getRed());
	outputArray = binary.convertToArray();
	updateOutput(createImage(
	      new MemoryImageSource(inputWidth, 
				    inputHeight, 
				    binary.convertToArray(), 
				    0, inputWidth)),
		     inputWidth, inputHeight);
	
	
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
   * Sets up the user interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("rlf1.gif");
    initialiseImages();

    kernel = new ThreeState3x3Kernel();
    timetaken = new TimeTakenInterface();

    JPanel kernelPanel = kernel.createPanel(getDocumentBase());
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(kernelPanel);
    operatorPanel.add(timetakenPanel);

  }
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "Applet for applying the hit/miss operator to an image.  Author Simon Horne.";
  }
}
