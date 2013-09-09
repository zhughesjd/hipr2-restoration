package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math.*;

/**
 *ImageLabel is an algorithm that applies Connected Component Labeling
 *alogrithm to an input image. Only mono images are catered for.
 *@author:Neil Brown, DAI
 *@author:Judy Robertson, SELLIC OnLine
 *@see code.iface.imagelabel
 */

public class ImageLabel extends Thread{
  
  //the width of the input image in pixels
  private int i1_w;
  
  //the width and height of the output image
  
  private int d_w;
  private int d_h;
  private int[] dest_1d;
  private int labels[];
  private int numberOfLabels;
  private boolean labelsValid = false;
  
  /**
   *Constructs a new Image Operator
   *@param firstwidth The width of the input image
   */

  public ImageLabel( int firstwidth ){
    i1_w = firstwidth;
  }
  
  /**
   * getNeighbours will get the pixel value of i's neighbour that's ox and oy
   * away from i, if the point is outside the image, then 0 is returned.
   * This version gets from source image.
   */
  
  private int getNeighbours( int [] src1d, int i, int ox, int oy ){
    int x, y, result;
    
    x = ( i % d_w ) + ox; // d_w and d_h are assumed to be set to the
    y = ( i / d_w ) + oy; // width and height of scr1d
    
    if( ( x < 0 ) || ( x >= d_w ) || ( y < 0 ) || ( y >= d_h ) ) {
      result = 0;
    } else {
      result = src1d[ y * d_w + x ] & 0x000000ff;
    }
    return result;
  }
  
  /**
   * getNeighbourd will get the pixel value of i's neighbour that's ox and oy
   * away from i, if the point is outside the image, then 0 is returned.
   * This version gets from destination image.
   */
  
  private int getNeighbourd( int [] src1d, int i, int ox, int oy ){
    int x, y, result;
    
    x = ( i % d_w ) + ox; // d_w and d_h are assumed to be set to the
    y = ( i / d_w ) + oy; // width and height of scr1d
    
    if( ( x < 0 ) || ( x >= d_w ) || ( y < 0 ) || ( y >= d_h ) ) {
      result = 0;
    } else {
      result = src1d[ y * d_w + x ];
    }
    return result;
  }
  
  /**
   * Associate(equivalence) a with b.
   *  a should be less than b to give some ordering (sorting)
   * if b is already associated with some other value, then propagate
   * down the list.
    */
  private void associate( int a, int b ) {
    
    if( a > b ) {
      associate( b, a );
      return;
    }
    if( ( a == b ) || ( labels[ b ] == a ) ) return;
    if( labels[ b ] == b ) {
      labels[ b ] = a;
    } else {
      associate( labels[ b ], a );
      if (labels[ b ] > a) {             //***rbf new
        labels[ b ] = a;
      }
    }
  }
  /**
   * Reduces the number of labels.
   */
  private int reduce( int a ){
    
    if( labels[ a ] == a ){
      return a;
    } else {
      return reduce( labels[ a ] );
    }
  }
  
  /**
   *doLabel applies the Labeling alogrithm plus offset and scaling
   *The input image is expected to be 8-bit mono 0=black everything else=white
   *@param src1_1d The input pixel array
   *@param width width of the destination image in pixels
   *@param height height of the destination image in pixels
   *@return A pixel array containing the labelled image
   */
  
  //NB For images  0,0 is the top left corner.
  
  public int [] doLabel(int [] src1_1d, int width, int height){
    
    int nextlabel = 1;
    int nbs[] = new int[ 4 ];
    int nbls[] = new int[ 4 ];

    //Get size of image and make 1d_arrays
    d_w = width;
    d_h = height;
    
    dest_1d = new int[d_w*d_h];
    labels  = new int[d_w*d_h / 2]; // the most labels there can be is 1/2 of the points in checkerboard
    
    int src1rgb;
    int result = 0;
    int px, py, count, found;
    
    labelsValid = false; // only set to true once we've complete the task
    //initialise labels
    for (int i=0; i<labels.length; i++) labels[ i ] = i;
    
    //now Label the image
    for (int i=0; i< src1_1d. length; i++){

      src1rgb = src1_1d[ i ] & 0x000000ff;
      
      if( src1rgb == 0 ){
	result = 0;  //nothing here
      } else {

        //The 4 visited neighbours
        nbs[ 0 ] = getNeighbours( src1_1d, i, -1, 0 );
        nbs[ 1 ] = getNeighbours( src1_1d, i, 0, -1 );
        nbs[ 2 ] = getNeighbours( src1_1d, i, -1, -1 );
        nbs[ 3 ] = getNeighbours( src1_1d, i, 1, -1 );
      
        //Their corresponding labels
        nbls[ 0 ] = getNeighbourd( dest_1d, i, -1, 0 );
        nbls[ 1 ] = getNeighbourd( dest_1d, i, 0, -1 );
        nbls[ 2 ] = getNeighbourd( dest_1d, i, -1, -1 );
        nbls[ 3 ] = getNeighbourd( dest_1d, i, 1, -1 );
      
	//label the point
	if( (nbs[0] == nbs[1]) && (nbs[1] == nbs[2]) && (nbs[2] == nbs[3])
	    && (nbs[0] == 0 )) { 
	  // all neighbours are 0 so gives this point a new label
	  result = nextlabel;
	  nextlabel++;
	} else { //one or more neighbours have already got labels
	  count = 0;
	  found = -1;
	  for( int j=0; j<4; j++){
	    if( nbs[ j ] != 0 ){
	      count +=1;
	      found = j;
	    }
	  }
	  if( count == 1 ) {
	    // only one neighbour has a label, so assign the same label to this.
	    result = nbls[ found ];
	  } else {
	    // more than 1 neighbour has a label
	    result = nbls[ found ];
	    // Equivalence the connected points
	    for( int j=0; j<4; j++){
	      if( ( nbls[ j ] != 0 ) && (nbls[ j ] != result ) ){
		associate( nbls[ j ], result );
	      }
	    }
	  }
	}
      }
      dest_1d[i] = result;
    }
    
    //reduce labels ie 76=23=22=3 -> 76=3
    //done in reverse order to preserve sorting
    for( int i= labels.length -1; i > 0; i-- ){
      labels[ i ] = reduce( i );
    }
    
    /*now labels will look something like 1=1 2=2 3=2 4=2 5=5.. 76=5 77=5
      this needs to be condensed down again, so that there is no wasted
      space eg in the above, the labels 3 and 4 are not used instead it jumps
      to 5.
      */
    int condensed[] = new int[ nextlabel ]; // cant be more than nextlabel labels
    
    count = 0;
    for (int i=0; i< nextlabel; i++){
      if( i == labels[ i ] ) condensed[ i ] = count++;
    }
    // Record the number of labels
    numberOfLabels = count - 1;

    // now run back through our preliminary results, replacing the raw label
    // with the reduced and condensed one, and do the scaling and offsets too
    
    //Now generate an array of colours which will be used to label the image
    int [] labelColors = new int[numberOfLabels+1];
    
    //Variable used to check if the color generated is acceptable
    boolean acceptColor = false;

    for(int i = 0; i < labelColors.length; i++){
      acceptColor = false;
      while(!acceptColor){
	double tmp = Math. random();
	labelColors[i] = (int)(tmp * 16777215);
	if(((labelColors[i] & 0x000000ff) < 200) &&
	   (((labelColors[i] & 0x0000ff00) >> 8) < 64) &&
	   (((labelColors[i] & 0x00ff0000) >> 16) < 64)){
	  //Color to be rejected so don't set acceptColor
	}
	else{
	  acceptColor = true;
	}
      }
      if (i == 0) labelColors[i] = 0;
    }

    for (int i=0; i< src1_1d. length; i++){
      result = condensed[ labels[ dest_1d[ i ] ] ];
      //result = (int) ( scale * (float) result + oset );
      //truncate if necessary
      //if( result > 255 ) result = 255;
      //if( result <  0  ) result = 0;
      //produce grayscale
      //dest_1d[i] =  0xff000000 | (result + (result << 16) + (result << 8));
      dest_1d[i] = labelColors[result] + 0xff000000;
    }
    
    labelsValid = true; // only set to true now we've complete the task
    return dest_1d;
  }
  
  /**  
   *getColours 
   *@return the number of unique, non zero colours. -1 if not valid
   */
  
  public int getColours() {
    
    if( labelsValid ) {
     
      return numberOfLabels;
    } else {
      return -1;
    }
  }
 
  /**
   * Returns the number of labels.
   */
  public int getNumberOfLabels() {
    return numberOfLabels;
  }

 
}






