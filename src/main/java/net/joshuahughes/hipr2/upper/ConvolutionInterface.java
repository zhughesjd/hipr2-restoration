package net.joshuahughes.hipr2.upper;
//package code.iface.convolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.convolution.*;

/**
 * The interface for the convolution operator.
 *
 * @author Simon Horne.
 */
public class ConvolutionInterface extends SingleInputImageInterface{

  /**
   * The interface for the convolution 5x5 selectable value kernel.
   */
  ConvolutionKernel kernel;
  /**
   * The interface for displaying the time taken.
   */
  TimeTakenInterface timetaken;
  ScaleOffsetInterface scaleOffset;
  private long time1, time2;
  private int i;
  /**
   * 2D array representing the kernel.
   */
  private double kernelData [][];
  private int kernelWidth, kernelHeight;
  private int outputWidth, outputHeight;

  /**
   * Sets up what happens when the start and stop buttons are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	//System.out.println("About to start");
	kernelData = kernel.getKernel();
	kernelWidth = kernel.getKernelWidth();
	kernelHeight = kernel.getKernelHeight();
	double scale = scaleOffset.scale();
	double offset = scaleOffset.offset();
	if(scale==1000000 || offset==1000000){
	  JOptionPane.showMessageDialog(null,
					"Invalid parameter entered.",
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE);
	}else{ 
	  outputArray =
	    Convolution.convolution_image(inputArray,
					  inputWidth,inputHeight,
					  kernelData,
					  kernelWidth,kernelHeight,
					  scale,offset);
	  
	  updateOutput(createImage(new MemoryImageSource(
							 inputWidth, 
							 inputHeight, 
							 outputArray,0, 
							 inputWidth)),
		       inputWidth,inputHeight);
	}
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

    initialImage("brg1.gif");
    initialiseImages();

    //convolution = new Convolution();
    kernel = new ConvolutionKernel();
    timetaken = new TimeTakenInterface();
    scaleOffset = new ScaleOffsetInterface();

    JPanel kernelPanel = kernel.createPanel();
    JPanel timetakenPanel = timetaken.createPanel();
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
    return "An applet for applying a convolution to an image.  Author Simon Horne.";
  }
}
