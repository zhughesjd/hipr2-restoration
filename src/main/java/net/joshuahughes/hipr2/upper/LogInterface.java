package net.joshuahughes.hipr2.upper;
//package code.iface.laplacian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.iface.kernels.*;
//import code.operator.laplacian.*;

/*class LogThread extends Thread{
  public boolean stopped;
  public boolean finished;
  int [] inputArray;
  int width;
  int height;
  int kernelSize;
  double theta;
  Log log;
  public int [] outputArray;
  
  public LogThread(int [] input, 
		   int w, int h, int k, double t){
    stopped = false;
    inputArray = new int [input.length];
    System.arraycopy(input,0,inputArray,0,input.length);
    outputArray = new int [(w-k+1)*(w-k+1)];
    width = w;
    height = h;
    kernelSize = k;
    theta = t;
    log = new Log(kernelSize,theta);
  }

  public void run(){
    LogInterface.outputArray = log.logImage(inputArray,
			       width,
			       height);
    //need to update the screen
  }
}*/

/**
 * LogInterface is the GUI interface for the laplacian of gaussian smoothing
 * operator.
 *
 * @author Simon Horne.
 */
public class LogInterface extends SingleInputImageInterface{

  /**
   * The kernel interface part of the GUI.
   */
  KernelInterface kernel;
  /**
   * The laplacian of gaussian operator code.
   */
  ScaleOffsetInterface scaleOffset;

  Log log;
  //LogThread logThread;
  TimeTakenInterface timetaken;
  private long time1, time2;

  /**
   * Sets up the operations fo rthe start and stop buttons.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	//System.out.println("About to start");
	
	if(kernel.getKernelSize()<1||kernel.getTheta()<0||scaleOffset.scale()==1000000||scaleOffset.offset()==1000000){
	  JOptionPane.showMessageDialog(null,
					"Invalid parameter entered.",
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  log = new Log(kernel.getKernelSize(),kernel.getTheta());

	  if(!log.kernelValid(kernel.getTheta())){
	    JOptionPane.showMessageDialog(null,
			  "Theta and size cannot generate a valid kernel. 0.5 < Theta < 0.09*size must hold.","Invalid kernel",JOptionPane.WARNING_MESSAGE); 
	  }else{
	    
	    
	    //logThread = new LogThread(inputArray,inputWidth,inputHeight,
	    //			  kernel.getKernelSize(),
	    //			  kernel.getTheta());
	    //logThread.start();
	    outputArray = log.log_image(inputArray,
					inputWidth,
					inputHeight,
					scaleOffset.scale(),
					scaleOffset.offset());
	    updateOutput(createImage(new MemoryImageSource(
							   inputWidth, 
							   inputHeight,
							   outputArray,
							   0,
							   inputWidth)),
			 inputWidth,inputHeight);
	    //Stop the timer					      
	    time2 = System.currentTimeMillis();
	    timetaken.updateTime(time1,time2);
	    updateInput(inputImage);
	    inputPanel.requestFocus();
	  }
	}
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

    kernel = new KernelInterface();
    timetaken = new TimeTakenInterface();
    scaleOffset = new ScaleOffsetInterface();

    JPanel timetakenPanel = timetaken.createPanel();
    JPanel kernelPanel = kernel.createPanel();
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
    return "An applet for applying the LoG to an image.  Author Simon Horne.";
  }


}
