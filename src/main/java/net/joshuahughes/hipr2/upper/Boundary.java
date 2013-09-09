package net.joshuahughes.hipr2.upper;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;


/**
 * Boundary is an algorithm used to track the edge of a binary image.
 * @author Bob Fisher 
 * @author Nathalie Cammas
 */

public class Boundary extends Thread{
  /**
   * The output binary image
   */
  int [] output;
  /**
   * The intermediate image, used to marked pixels
   */
  int [][] intermediate;
  /** 
   * The width of the input image
   */
  int w;
  /**
   * The height of the input image
   */
  int h;
  /**
   * Array for indicating direction when scanning the boundary for the x
   */
  int [] dx;
   /**
   * Array for indicating direction when scanning the boundary for the y
   */
  int [] dy;
  /**
   * int for finding direction
   */
  int vec, oldvec;
  /**
   * Default no argument constructor.
   */
  public Boundary () {
    dx = new int[8];
    dy = new int[8];
    dx[0] = 0;
    dx[1] = -1;
    dx[2] = -1;
    dx[3] = -1;
    dx[4] = 0;
    dx[5] = 1;
    dx[6] = 1;
    dx[7] = 1;
    dy[0] = -1;
    dy[1] = -1;
    dy[2] = 0;
    dy[3] = 1;
    dy[4] = 1;
    dy[5] = 1;
    dy[6] = 0;
    dy[7] = -1;
  }

/**
   * Scans a boundary pixel and the others adjacent to it
   * until it comes back to the starting point
   * @param x,y position of the starting point
   * 
*/
public void single_track(int x,int y) {

  // save starting point
  int xstart = x;
  int ystart = y;

  // find track starting direction
  int ddx,ddy;
  for( int i = 2; i < 8 ; i++) {
    ddx = x + dx[i];
    ddy = y + dy[i];
    if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
    if (intermediate[ddx][ddy] == 255) {
      vec = i;
      break;
    }
  }

  // Loop around the edge until we get back to the starting point
  x = x+dx[vec];
  y = y+dy[vec];
  while (x != xstart || y != ystart) {

    if (x < 0 || y < 0 || x >= w || y >= h){
      System.out.println("Terminating track at image boundary at xy("+x+","+y+")");
      return;
    }
    else
    {
      intermediate[x][y] = 128;
      // System.out.println("boundary at xy("+x+","+y+")");

      // work out starting point for next scan; store end stop
      oldvec = (vec + 4)%8;
      vec = (oldvec + 1)%8;

      // scan round current point for non zero-element
      int loop = 0;
      ddx = x + dx[vec];
      ddy = y + dy[vec];
      if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) {
              System.out.println("Terminating track at image boundary at xy("+x+","+y+")");
              return;
      }
      while (intermediate[x+dx[vec]][y+dy[vec]] != 255) {
        vec = (vec + 1)%8;
        loop = loop+1;
        if (loop > 8) {
                System.out.println("Terminating track due to loop error at xy("+x+","+y+")");
                return;
        }
        ddx = x + dx[vec];
        ddy = y + dy[vec];
        if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) {
              System.out.println("Terminating track at image boundary at xy("+x+","+y+")");
              return;
        }
      }

      // check for valid boundary point
      boolean boundary = false;
      for (int i = 0; i < 8; i++) {
        ddx = x + dx[i]+dx[vec];
        ddy = y + dy[i]+dy[vec];
        if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
        if(intermediate[ddx][ddy] == 0){
          boundary = true;
          break;
        }
      }
      if (!boundary)
      {
        System.out.println("Terminating on internal point at xy("+x+","+y+")");
        return;
      }
    }
    x = x + dx[vec];
    y = y + dy[vec];
  }
  intermediate[x][y] = 128;

  // clear all region points adjacent connected to tracked boundary
  // to prevent internal boundary points from being tracked
  for (int j = 1; j < h-1; j++) {
    for (int i = 1; i < w-1; i++) {
      if (intermediate[i][j] == 128) {

        // have a boundary point - search for neighboring object points
        for( int k = 0; k < 8 ; k++) {
          ddx = i + dx[k];
          ddy = j + dy[k];
          if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
          if (intermediate[ddx][ddy] == 255) {
                clear_interior(ddx,ddy);
          }
        }
      }
    }
  }
  for (int j = 0; j < h; j++) {
    for (int i = 0; i < w; i++) {
      if (intermediate[i][j] == 64) { intermediate[i][j] = 0;}
      if (intermediate[i][j] == 192) { intermediate[i][j] = 255;}
    }
  }
}

/**
   * resets all pixels with value 255 adjacent to current pixel to
   * 64 (interior point) or 192 (interior boundary)
   *
   * @param position of the current pixel
*/
public void clear_interior(int x, int y) {

  int [] stackx = new int[w*h];
  int [] stacky = new int[w*h];
  int ddx,ddy;
  int index = 1;
  stackx[0] = x;
  stacky[0] = y;

  while (index > 0) {

    // pop bottom of stack
    index = index-1;
    x = stackx[index];
    y = stacky[index];

    // set to 64 if still 255 and
    //    1) adjacent to 128 and 0 (diagonal crack case) or
    //    2) adjacent to 64 and not 0 (interior case)
    // else set to 192
    if (intermediate[x][y] == 255) {

      // decide what to set it to
      boolean zero_found = false;
      boolean bnd_found = false;
      for( int k = 0; k < 8 ; k++) {
        ddx = x + dx[k];
        ddy = y + dy[k];
        if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
        if (intermediate[ddx][ddy] == 0) { zero_found = true;}
        if (intermediate[ddx][ddy] == 128) { bnd_found = true;}
      }
      if (!zero_found || (zero_found && bnd_found)) { intermediate[x][y] = 64; }
      else { intermediate[x][y] = 192; }

      // search for neighbors needing resetting
      for( int k = 0; k < 8 ; k++) {
        ddx = x + dx[k];
        ddy = y + dy[k];
        if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
        if (intermediate[ddx][ddy] == 255) {
          stackx[index] = ddx;
          stacky[index] = ddy;
          index = index + 1;
        }
      }
    }
  }
}

/**
   * @param position of the current pixel
   * @return if the pixel is a starting point of a boundary
*/
public boolean starting_point (int x, int y) {

    // a starting point is not a background point (0), not previously
    // tracked (128), has an adjacent background point
    // and is not an isolated point
    if (intermediate[x][y] == 0) return false;  // don't start on a zero
    if (intermediate[x][y] == 128) return false;  // don't start on previous track

    // a non-zero - check for having a zero adjacent but not isolated
    int ddx,ddy;
    boolean isolated = true;
    boolean boundary = false;
    for (int i = 0; i < 8; i++) {
      ddx = x + dx[i];
      ddy = y + dy[i];
      if (ddx < 0 || ddy < 0 || ddx >= w || ddy >= h) continue;
      if(intermediate[ddx][ddy] == 0){
	boundary = true;
      }
      if(intermediate[ddx][ddy] == 255){
	isolated = false;
      }
    }
    if (boundary && !isolated) { return true; }
    return false;
  }

/**
   * Applies the algorithm: scans a pixel if it is a boundary pixel
   * and marks it as scanned with 128.
   * @param input the input binary image
   * @param width of the input image
   * @param height of the input image
   * @return a binary image
*/
public BinaryFast apply_boundary ( BinaryFast input, int width, int height ){
    
    int tmp;
    intermediate = new int[width][height];
    w = width;          // save for global communication
    h = height;

    // Store the Binary image in an array for marking the pixels
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
        tmp = (input.pixels[i][j] & 0x000000ff);
        if (tmp > 0) tmp = 255;        
	intermediate[i][j] = tmp;
      }
    }
    
    // Loop on the image to find a starting point of a boundary
    for (int j = 1; j < h-1; j++) {
      for (int i = 1; i < w-1; i++) {
	if (starting_point(i,j)) {
          // System.out.println("starting_point at xy("+i+","+j+")");
	  single_track(i,j);
	}
      }
    }
    
    // Create the boundary image
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
	if (intermediate[i][j] == 128) {
	  intermediate[i][j] = 0xffffffff;
	}
	else intermediate[i][j] = 0xff000000;
      }
    }

    BinaryFast bin = new BinaryFast(intermediate,w,h);
    return bin;

  }
} 
