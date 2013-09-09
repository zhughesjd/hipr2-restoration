package net.joshuahughes.hipr2.upper;
//package code.iface.mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.operator.mat.*;

/**
 * Interface for the medial axis transform operator 
 * (code.operator.mat.Mat.java).
 *
 * @author Simon Horne.
 */
public class MatInterface extends SingleBinaryImageInterface{

  Mat mat;
  TimeTakenInterface timetaken;
  ScaleOffsetInterface scaleOffset;
  ThresholdInterface threshold;
  private long time1, time2;
  private int i;
  /**
   * Sets the actions to be performed when the start and stop 
   * button are pressed.
   */
  public void setOperation(){

    startstop.start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	//Start the timer
	time1 = System.currentTimeMillis();
	double scale = scaleOffset.scale();
	double offset = scaleOffset.offset();
	if(scale==1000000 || offset==1000000){
	  JOptionPane.showMessageDialog(null,
					"Invalid parameter entered.",
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  binary = 
	    mat.mat_image(binary,scale,offset,
			  threshold.getBoolean());
	  
	  updateOutput(createImage(new MemoryImageSource(
					  inputWidth, 
					  inputHeight, 
					  binary.convertToArray(), 
					  0, inputWidth)),
		       inputWidth,inputHeight);
	  outputArray = binary.convertToArray();
	  //Stop the timer					      
	  time2 = System.currentTimeMillis();
	  timetaken.updateTime(time1,time2);
	  updateInput(inputImage);
	}
      }
    });
    
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

    mat = new Mat();
    scaleOffset = new ScaleOffsetInterface();
    threshold = new ThresholdInterface();
    timetaken = new TimeTakenInterface();

    JPanel scaleOffsetPanel = scaleOffset.createPanel();
    JPanel thresholdPanel = threshold.createPanel();
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(scaleOffsetPanel);
    operatorPanel.add(thresholdPanel);
    operatorPanel.add(timetakenPanel);

  }
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the medial axis transform to an image.  Author Simon Horne.";
  }
}
