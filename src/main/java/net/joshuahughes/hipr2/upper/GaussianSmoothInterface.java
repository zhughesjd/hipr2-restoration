package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * GaussianSmoothInterface is the user interface to the Gaussian
 * smoothing algorithm. 
 *
 * @author Simon Horne
 */
public class GaussianSmoothInterface extends SingleInputImageInterface{
  /**
   * The interface for selecting the kernel size.
   */
  KernelSize kernel;
  /**
   * The interface for selecting the theta value.
   */
  ThetaInterface theta;
  /**
   * The interface for displaying the time taken.
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
	
	//System.out.println(theta.number());
	//System.out.println(((double)kernel.size())/6);
	if(theta.number()<0.4 || theta.number() > (((double)kernel.size())/6)){
	  JOptionPane.showMessageDialog(null,
			"Invalid theta value. Must have 0.4 < theta < size/6",
			"Invalid Value",
			JOptionPane.WARNING_MESSAGE); 
	}else{
	  outputArray =
	    GaussianSmooth.smooth_image(inputArray,inputWidth,inputHeight,
					kernel.size(),theta.number());
	  //inputArray = outputArray;
	  
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
	}
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
    boolean b = initialiseImages();

    kernel = new KernelSize();
    timetaken = new TimeTakenInterface();
    theta = new ThetaInterface();

    JPanel kernelPanel = kernel.createPanel();
    kernelPanel.setAlignmentX(BOTTOM_ALIGNMENT);
    JPanel thetaPanel = theta.createPanel();
    thetaPanel.setAlignmentX(BOTTOM_ALIGNMENT);
    theta.t.setText("0.45");

    JPanel timetakenPanel = timetaken.createPanel();
    timetakenPanel.setAlignmentX(BOTTOM_ALIGNMENT);

    setOperation();

    operatorPanel.add(kernelPanel);
    operatorPanel.add(thetaPanel);
    operatorPanel.add(timetakenPanel);
    

  }
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying gaussian smoothing to an image.  Author Simon Horne.";
  }
}
