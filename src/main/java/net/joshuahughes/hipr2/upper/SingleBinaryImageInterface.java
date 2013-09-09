package net.joshuahughes.hipr2.upper;
//package code.iface.highlevel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import java.awt.color.*;
//import code.iface.common.*;
//import code.operator.binaryTools.*;

/**
 * Superclass of the efficient binary image operators.
 *
 * @author Simon Horne.
 */
public class SingleBinaryImageInterface extends SingleInputImageInterface{

  /**
   * The efficient representation of a binary image.
   */
  public BinaryFast binary;

  /**
   * Initialises the interface by updating the input image
   * and generating the BinaryFast representation.
   *
   * @param image the new input image
   */
  public void initialiseInput(Image image){
    //System.out.println("Test");
    inputIcon = new ImageIcon(image);
    inputLabel.setIcon(imageTools.scaleImage(inputIcon));
    inputWidth = imageTools.getWidth(inputIcon);
    inputHeight = imageTools.getHeight(inputIcon);
    inputArray = imageTools.examineInput(inputIcon);
    //System.out.println(inputWidth +" "+inputHeight);
    if(inputWidth == -1){
      inputDims.setText("No image");
    }else{
      inputDims.setText(inputWidth + " x " + inputHeight);
      int [][] pixels = new int [inputWidth][inputHeight];
      for(int j=0;j<inputHeight;++j){
	for(int i=0;i<inputWidth;++i){
	  pixels[i][j] = inputArray[(j*inputWidth)+i];
	}
      }
      binary = new BinaryFast(pixels,inputWidth,inputHeight);
      outputArray = (int []) inputArray.clone();
    }
  }

    /**
   * Updates the input image onscreen.
   *
   * @param image the new output image
   */
  public void updateInput(Image image){
    inputIcon = new ImageIcon(image);
    inputLabel.setIcon(imageTools.scaleImage(inputIcon));
    inputWidth = imageTools.getWidth(inputIcon);
    inputHeight = imageTools.getHeight(inputIcon);
    inputArray = imageTools.examineInput(inputIcon);
    if(inputWidth == -1){
      inputDims.setText("No image");
    }else{
      inputDims.setText(inputWidth + " x " + inputHeight);
    }
    int [][] pixels = new int [inputWidth][inputHeight];
    for(int j=0;j<inputHeight;++j){
	for(int i=0;i<inputWidth;++i){
	  pixels[i][j] = inputArray[(j*inputWidth)+i];
	}
      }
    binary = new BinaryFast(pixels,inputWidth,inputHeight);
  }
}
