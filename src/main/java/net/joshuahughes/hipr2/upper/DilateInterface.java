package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;


/**
 * DilateInterface is the user interface to the Dilate algorithm.
 *
 * @author Simon Horne.
 */
public class DilateInterface extends SingleBinaryImageInterface{

  /**
   * Interface for kernel selection.
   */
  TwoState3x3Kernel mask;
  /**
   * Interface for selecting the number of iterations to be performed.
   */
  IterationsInterface iterations;
  /**
   * Interface for displaying the time taken by the operator.
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
	BinaryFast binary2 = new BinaryFast(binary);
	binary2 =
	  Dilate.dilate_image(binary2, kernel, iterations.number());
	outputArray = binary2.convertToArray();
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
   * Sets up the user interface and displays it on the screen.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("art4.gif");
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
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the dilate algorithm to an image.  Author Simon Horne.";
  }
}
