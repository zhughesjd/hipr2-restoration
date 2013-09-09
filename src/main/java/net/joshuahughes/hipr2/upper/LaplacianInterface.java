package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * LaplacianInterface is the user interface to the 
 * Laplacian smoothing algorithm.
 * @author Simon Horne.
 */
public class LaplacianInterface extends SingleInputImageInterface{
  /**
   * The laplacian smoothing operator code.
   */
  Laplacian laplacian;
  /**
   * The kernel type interface code.
   */
  KernelType kernel;
  /**
   * The time taken interface.
   */
  ScaleOffsetInterface scaleOffset; 
  TimeTakenInterface timetaken;
  private long time1, time2;
  private int i;
  int kernelNumber = 1;
  /**
   * Sets up the operations for the start and stop buttons.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();

	outputArray =
	  laplacian.laplacian_image(inputArray,inputWidth,inputHeight,
				    kernel.type(),
				    scaleOffset.scale(),
				    scaleOffset.offset());

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
      }});
    
    startstop.stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });
    
  }
  /**
   * Initialises the interface and starts it.
   */
  public void init(){

    setLook();
    setContainer();

    initialImage("brg1.gif");
    initialiseImages();

    laplacian = new Laplacian();
    kernel = new KernelType();
    timetaken = new TimeTakenInterface();
    scaleOffset = new ScaleOffsetInterface();

    JPanel timetakenPanel = timetaken.createPanel();
    JPanel kernelPanel = kernel.createPanel(getDocumentBase());
    JPanel scaleOffsetPanel = scaleOffset.createPanel(); 

    setOperation();

    operatorPanel.add(kernelPanel);
    operatorPanel.add(scaleOffsetPanel);
    operatorPanel.add(timetakenPanel);

  }

  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying Laplacian smoothing to an image.  Author Simon Horne.";
  }
}
