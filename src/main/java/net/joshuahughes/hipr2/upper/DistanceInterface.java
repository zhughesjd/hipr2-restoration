package net.joshuahughes.hipr2.upper;
//package code.iface.distanceTransform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//import code.iface.highlevel.*;
//import code.iface.common.*;
//import code.operator.distanceTransform.*;

/**
 * Interface for the distance transform operator.
 *
 * @author Simon Horne.
 */
public class DistanceInterface extends SingleBinaryImageInterface{
  /**
   * Distance transform operator.
   */
  Distance distance;
  ScaleOffsetInterface scaleOffset;
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
	double scale = scaleOffset.scale();
	double offset = scaleOffset.offset();
	if(scale==1000000 || offset==1000000){
	  JOptionPane.showMessageDialog(null,
					"Invalid parameter entered.",
					"Invalid Value",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  binary = 
	    distance.distance_image(binary,scale,offset);
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
	}
      }
    });
    
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

    initialImage("wdg2thr3.gif");
    initialiseImages();

    distance = new Distance();
    scaleOffset = new ScaleOffsetInterface();
    timetaken = new TimeTakenInterface();

    JPanel scaleOffsetPanel = scaleOffset.createPanel();
    JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    operatorPanel.add(scaleOffsetPanel);
    operatorPanel.add(timetakenPanel);

  }
  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the distance transform to an image.  Author Simon Horne.";
  }

}
