package net.joshuahughes.hipr2.upper;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.Math;
import java.awt.Color.*;

/**
 * Crimmins is an operator that applies the Crimmins Speckle Reduction 
 * Algorithm.
 *
 * @author Simon Horne.
 */
public class Crimmins{

  /**
   * Default no-arg constructor.
   */
  public Crimmins() {
  }

  /**
   * Converts a 1D array of pixels to a 2D array of grey level values.
   *
   * @param input The 1D array.
   * @param width The width of the 2D array.
   * @param height The height of the 2D array.
   * @return The 2D array.
   */
  public static int [][] pixelsToGreys(int [] input, 
					     int width, int height){
    int [][] arrays = new int [width][height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	arrays[i][j] = (new Color(input[i+(j*width)])).getRed();
      }
    }
    return arrays;
  }
     
  /**
   * Converts a 2D array of grey level values into a continuous 1D array
   * of pixels.
   *
   * @param outputArrays The 2D array.
   * @param width The width of the 2D array.
   * @param height The height of the 2D array.
   * @return The 1D array.
   */ 
  public static int [] greysToPixels(int [][] outputArrays, 
				     int width, int height){
    int [] output = new int [width*height];
    for(int i=0;i<width;++i){
      for(int j=0;j<height;++j){
	int grey = outputArrays[i][j];
	//System.out.println(grey);
	output[i+(j*width)] = (new Color(grey,grey,grey)).getRGB();
      }
    }
    return output;
  }


  /**
   * Takes a 2d array of grey level values and applies the
   * Crimmins Speckle Reduction algorithm to it for a 
   * specified number of iterations.
   *
   * @param image the input image
   * @param width of the input image
   * @param height of the input image
   * @param iterations to be applied
   * @return the new Crimminsed image
   */
  public int [][] crimminsImage(int [][] image, int width,
			   int height, int iterations){
    int [][] image2 = (int [][]) image.clone();
    for(int its=0;its<iterations;++its){
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && image[i-1][j]>image[i][j]+1){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width
	     && image2[i-1][j]>image2[i][j] && image2[i+1][j]>=image2[i][j]){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && i-1>=0
	     && image[i-1][j]>=image[i][j] && image[i+1][j]>image[i][j]){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && image2[i+1][j]>image2[i][j]+1){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && image[i-1][j]<image[i][j]-1){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width
	     && image2[i-1][j]<image2[i][j] && image2[i+1][j]<=image2[i][j]){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && i-1>=0
	     && image[i-1][j]<=image[i][j] && image[i+1][j]<image[i][j]){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && image2[i+1][j]<image2[i][j]-1){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      
      
      
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j-1>=0 && image[i][j-1]>image[i][j]+1){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j-1>=0 && j+1<height
	     && image2[i][j-1]>image2[i][j] && image2[i][j+1]>=image2[i][j]){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j+1<height && j-1>=0
	     && image[i][j-1]>=image[i][j] && image[i][j+1]>image[i][j]){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j+1<height && image2[i][j+1]>image2[i][j]+1){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j-1>=0 && image[i][j-1]<image[i][j]-1){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j-1>=0 && j+1<height
	     && image2[i][j-1]<image2[i][j] && image2[i][j+1]<=image2[i][j]){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j+1<height && j-1>=0
	     && image[i][j-1]<=image[i][j] && image[i][j+1]<image[i][j]){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(j+1<height && image2[i][j+1]<image2[i][j]-1){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      
      
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && j-1>=0
	     && image[i+1][j-1]>image[i][j]+1){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image2[i+1][j-1]>image2[i][j]
	     && image2[i-1][j+1]>=image2[i][j]){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image[i+1][j-1]>=image[i][j]
	     && image[i-1][j+1]>image[i][j]){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && j+1<height
	     && image2[i-1][j+1]>image2[i][j]+1){
	    image[i][j] = image2[i][j] +1;
	  }
	else image[i][j] = image2[i][j];
	}
      }


      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && j-1>=0
	     && image[i+1][j-1]<image[i][j]-1){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image2[i+1][j-1]<image2[i][j]
	     && image2[i-1][j+1]<=image2[i][j]){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image[i+1][j-1]<=image[i][j]
	     && image[i-1][j+1]<image[i][j]){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && j+1<height
	     && image2[i-1][j+1]<image2[i][j]-1){
	    image[i][j] = image2[i][j] -1;
	  }
	else image[i][j] = image2[i][j];
	}
      }

      



      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && j+1<height
	     && image[i+1][j+1]>image[i][j]+1){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image2[i+1][j+1]>image2[i][j]
	     && image2[i-1][j-1]>=image2[i][j]){
	    image[i][j] = image2[i][j] +1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image[i+1][j+1]>=image[i][j]
	     && image[i-1][j-1]>image[i][j]){
	    image2[i][j] = image[i][j] +1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && j-1>=0
	     && image2[i-1][j-1]>image2[i][j]+1){
	    image[i][j] = image2[i][j] +1;
	  }
	else image[i][j] = image2[i][j];
	}
      }

      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i+1<width && j+1<height
	     && image[i+1][j+1]<image[i][j]-1){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image2[i+1][j+1]<image2[i][j]
	     && image2[i-1][j-1]<=image2[i][j]){
	    image[i][j] = image2[i][j] -1;
	  }
	  else image[i][j] = image2[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && i+1<width && j-1>=0 && j+1<height
	     && image[i+1][j+1]<=image[i][j]
	     && image[i-1][j-1]<image[i][j]){
	    image2[i][j] = image[i][j] -1;
	  }
	  else image2[i][j] = image[i][j];
	}
      }
      for(int j=0;j<height;++j){
	for(int i=0;i<width;++i){
	  if(i-1>=0 && j-1>=0
	     && image2[i-1][j-1]<image2[i][j]-1){
	    image[i][j] = image2[i][j] -1;
	  }
	else image[i][j] = image2[i][j];
	}
      }
    }
    return image;
  }


  /**
   * Takes a 1d image and applies Crimmins to it.
   * 
   * @param input the input image
   * @param width of the input image
   * @param height of the input image
   * @param iterations to be applied
   * @return the new image
   */
  public int [] crimmins_image(int [] input, int width, 
				      int height, int iterations) {
    int [][] inputArrays = new int [width][height];
    int [][] outputArrays = new int [width][height];
    inputArrays = pixelsToGreys(input,width,height);
    outputArrays = crimminsImage(inputArrays,width,height,iterations);
    return greysToPixels(outputArrays,width,height);
  }

  /**
   * Takes a 2d image and applies Crimmins to it.
   *
   * @param input the input image
   * @param width of the input image
   * @param height of the input image
   * @param iterations to be applied
   * @return the new image
   */
  public int [][] crimmins(int [][] input, int width, int height,
			   int iterations){
    int [][] output = new int [width][height];
    for(int j=0;j<height;++j){
      for(int i=0;i<width;++i){
	input[i][j] = (new Color(input[i][j])).getRed();
      }
    }
    output = crimminsImage(input,width,height,iterations);
    
    return output;
  }

  /**
   * Applies crimmins algorithm.
   * @param input the 1D int array representing the image.
   * @param width the width of the image.
   * @param height the height of the image.
   * @param iterations the number of iterations to apply the algorithm
   * @return the 1D int array representing the new image.
   */

  public int [] crimmins_trans(int []input, int width, int height, int iterations) {
    int [] output_1d = new int [width*height];
    int [][] output_2d = new int [width][height];
    int [][] input_2d = new int [width][height];

    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
	input_2d [i][j] = input[(j*width)+i];
      }
    }

    output_2d = crimminsImage(input_2d,width,height,iterations);

    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
	output_1d [(j*width)+i] = output_2d[i][j];
      }
    }
  return output_1d;
  }

}










