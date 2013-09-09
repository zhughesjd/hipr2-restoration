package net.joshuahughes.hipr2.upper;
//package code.iface.fft;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import java.awt.color.*;
//import code.iface.common.*;
//import code.iface.highlevel.*;
//import code.operator.fft.*;
//import code.operator.gaussiansmooth.*;

/**
 * Interface for the FFT/Inverse FFT/Various Intermediate Operators.
 *
 * @author Simon Horne.
 */
public class FFTInterface extends SingleInputImageInterface{

public class verticalAxis extends JPanel{
    int size;
    public verticalAxis(int size){
      this.size = size;
    }
  public void paint(Graphics graphics){
    graphics.drawLine(39,0,39,255);
    graphics.drawLine(29,0,39,0);
    graphics.drawLine(34,64,39,64);
    graphics.drawLine(29,128,39,128);
    graphics.drawLine(34,192,39,192);
    graphics.drawLine(29,256,39,256);
    int a = -size+size/2;
    int b = a+size/4;
    int c = 0;
    int d = size+a+b;
    int e = size+a-1;
    graphics.drawString(Integer.toString(e),0,15);
    graphics.drawString(Integer.toString(d),0,74);
    graphics.drawString(Integer.toString(c),10,138);
    graphics.drawString(Integer.toString(b),0,202);
    graphics.drawString(Integer.toString(a),0,256);
  }
}

  public class horizontalAxis extends JPanel{
    int size;
    public horizontalAxis(int size){
      this.size = size;
    }
    public void paint(Graphics graphics){
      graphics.drawLine(39,0,295,0);
      graphics.drawLine(39,0,39,10);
      graphics.drawLine(104,0,104,5);
      graphics.drawLine(168,0,168,10);
      graphics.drawLine(232,0,232,5);
      graphics.drawLine(295,0,295,10);
      int a = -size+size/2;
      int b = a+size/4;
      int c = 0;
      int d = size+a+b;
      int e = size+a-1;
      graphics.drawString(Integer.toString(a),20,25);
      graphics.drawString(Integer.toString(b),92,25);
      graphics.drawString(Integer.toString(c),162,25);
      graphics.drawString(Integer.toString(d),220,25);
      graphics.drawString(Integer.toString(e),280,25);
    }
  }
  /** The notch filter interface.
   */
  DefinableNotchInterface coordNotch;
  /**
   * The vertical and horizontal line removal interface.
   */
  NotchFilterInterface notch;
  /**
   * The high pass/low pass filter operator interface.
   */
  FreqFilterInterface freq;
  /**
   * The Gaussian smoothing operator interface.
   */
  GaussianInterface gaussian;

  TimeTakenInterface timetaken;
  private long time1, time2;
  /**
   * The FFT operator class.
   */
  FFT fft = new FFT();
  /**
   * The inverse FFT operator class.
   */
  InverseFFT inverse = new InverseFFT();

  /**
   * The panel containing the various operator controls.
   */
  public JPanel leftPanel;
  /**
   * The fourier image for displaying the effects of filters etc.
   */
  public Image fourierImageCurrent;

  /**
   * For displaying the 7 fourier transform images.
   */
  public JLabel [] fourierLabel = new JLabel [7];
  /**
   * For displaying the fourier image used in the filters.
   */
  public JLabel fourierCurrentLabel = new JLabel();
  /**
   * For displaying the 7 fourier transform images.
   */
  public ImageIcon [] fourierIcon = new ImageIcon [7];
  /**
   * For displaying the filtered fourier image.
   */
  public ImageIcon fourierCurrentIcon;
  /**
   * An array of doubles representing the current FFT image.
   */
  double [] fourierCurrent;
  /**
   * For displaying the 7 fourier images in a small space.
   */
  public JTabbedPane fourierPane;
  public JPanel fourierPanel;
  /** 
   * For displaying the fourier image dimensions.
   */
  public JLabel fourierCurrentDims = new JLabel();

  /**
   * Updates the fourier images on the screen.
   */
  public void updateFourierImages(){
    int w = fft.output.width;
    int h = fft.output.height;
    double [][] fouriers = new double [7][w*h];
    int [] fourierPixels = new int [w*h];

    fouriers[0] = fft.intermediate.DCToCentre(fft.intermediate.getMagnitude());
    fouriers[1] = ImageMods.logs(fouriers[0]);
    fouriers[2] = ImageMods.abs(fft.intermediate.DCToCentre(fft.intermediate.getPhase()));
    fouriers[3] = ImageMods.abs(fft.intermediate.DCToCentre(fft.intermediate.getReal()));
    fouriers[4] = ImageMods.logs(fouriers[3]);
    fouriers[5] = 
      ImageMods.abs(fft.intermediate.DCToCentre(fft.intermediate.getImaginary()));
    fouriers[6] = ImageMods.logs(fouriers[5]);
    for(int i=0;i<7;++i){
      fourierPixels = ImageMods.toPixels(fouriers[i]);
      fourierLabel[i] = new JLabel();
      fourierLabel[i].setIcon(imageTools.scaleImage(new ImageIcon(
				createImage(new MemoryImageSource(
				w,h,fourierPixels,0,w)))));
    }
    updateTabs();
  }

  /**
   * Converts an int array representing an image into a 2D int array.
   *
   * @param input The int array representing the image.
   * @param w The width of the image.
   * @param h The height of the image.
   * @return A 2D array [w][h] representing the image.
   */
  public int [][] convertTo2D(int [] input, int w, int h){
    int [][] output = new int [w][h];
    for(int j=0;j<h;++j){
      for(int i=0;i<w;++i){
	output[i][j] = input[j*w +i];
      }
    }
    return output;
  }


  void initialiseFourier(){
    System.out.println("Doing the FFT now");
    fft = new FFT(inputArray,inputWidth,inputHeight);
    updateFourierImages();
  }

  /**
   * Updates the tabbed pane containing the various FFT images.
   */
  void updateTabs(){
    int x = fourierPane.getSelectedIndex();
    fourierPane.removeAll();
    String [] title = new String [] {"Magnitude","Magnitude Log",
					"Phase Angle","Real",
					"Real Log","Imaginary",
					"Imaginary Log"};
    for(int i=0;i<7;++i){
      JPanel imageAndAxes = new JPanel();
      imageAndAxes.setLayout(null);
      verticalAxis vertical = new verticalAxis(inputHeight);
      horizontalAxis horizontal = new horizontalAxis(inputWidth);
      imageAndAxes.add(vertical);
      imageAndAxes.add(horizontal);
      imageAndAxes.add(fourierLabel[i]);
      vertical.setBounds(0,0,40,260);
      horizontal.setBounds(0,256,350,40);
      fourierLabel[i].setBounds(40,0,256,256);
      fourierPane.addTab(title[i],null,imageAndAxes,title[i]);
    }
    fourierPane.setSelectedIndex(x);
  }

  /**
   * Loads the current image and displays it on the screen.
   */
  public void loadImage(){
    if(initialiseImages()==true){
      initialiseFourier();
      updateTabs();
    }
  }

  /** 
   * Creates the start panel for inclusion in the GUI.
   */
  public JPanel createStartPanel(){
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setAlignmentX(Component.LEFT_ALIGNMENT);
    //p.setMaximumSize(new Dimension(500,10));

    p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(10,10,10,0)),"Apply"));
    //    p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Apply"));
    p.setOpaque(false);
    
    timetaken = new TimeTakenInterface();
    JPanel timeTakenPanel = timetaken.createPanel();

    JButton goFFT = new JButton("Fourier Transform");
    goFFT.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	//Start the timer
	time1 = System.currentTimeMillis();
	initialiseFourier();
	//Stop the timer					      
	time2 = System.currentTimeMillis();
	timetaken.updateTime(time1,time2);
      }
    });
    JButton goInverseFFT = new JButton("Inverse Fourier Transform");
    goInverseFFT.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	//Start the timer
	time1 = System.currentTimeMillis();
	if(fourierPane.getTabCount()==1){//no fourier to inverse
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  fft.output = inverse.transform(fft.intermediate);
	  double [] outputArrayDoubles = 
	    new double [fft.output.width*fft.output.height];
	  outputArrayDoubles = fft.output.getReal();
	  //outputArrayDoubles = fft.output.getMagnitude();
	  outputArray = new int [outputArrayDoubles.length];
	  //outputArray = ImageMods.toPixels(outputArrayDoubles);
	  outputArray = 
	    ImageMods.toPixels(ImageMods.allPositive(outputArrayDoubles));
	  int max=0;
	  int min =255;
	  updateOutput(createImage(new MemoryImageSource(fft.output.width, 
							 fft.output.height, 
							 outputArray, 
							 0, fft.output.width)),
		       fft.output.width,fft.output.height);
	  //Stop the timer					      
	  time2 = System.currentTimeMillis();
	  timetaken.updateTime(time1,time2);
	}
      }});
    p.add(goFFT);
    p.add(Box.createRigidArea(new Dimension(5,5)));
    p.add(goInverseFFT);
    p.add(timeTakenPanel);
    return p;
  }

  /**
   * Creates the images panel for inclusion in the GUI.
   */
  public void createFourierPanel(){
    JPanel imageAndAxes = new JPanel();
    imageAndAxes.setLayout(null);
    imageAndAxes.setBackground(Color.white);
    verticalAxis vertical = new verticalAxis(256);
    horizontalAxis horizontal = new horizontalAxis(256);
    imageAndAxes.add(vertical);
    imageAndAxes.add(horizontal);
    vertical.setBounds(0,0,40,260);
    horizontal.setBounds(0,256,350,40);
    fourierPane = new JTabbedPane(SwingConstants.LEFT);
    fourierPane.setPreferredSize(new Dimension(435,350));
    fourierPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    fourierPane.addTab("Fourier Images",null,imageAndAxes,"Fourier not applied");
    fourierPanel = new JPanel();
    fourierPanel.setLayout(new BoxLayout(fourierPanel,BoxLayout.Y_AXIS));
    fourierPanel.add(fourierPane);
    JButton reset = new JButton("Reset Images");
    reset.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  fft.intermediate = new TwoDArray(fft.input);
	  updateFourierImages();
	  updateTabs();
	}
      }});
    fourierPanel.add(Box.createRigidArea(new Dimension(0,5)));
    fourierPanel.add(reset);
    fourierPanel.add(Box.createRigidArea(new Dimension(0,200)));
  }

  /**
   * Sets the look and feel of the GUI.
   *
   * @exception Exception When things don't go right.
   */
  public void setLook(){
    try {
      UIManager.setLookAndFeel(
		UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) { }
  }

  /**
   * Sets up the top-level container for the GUI.
   */
  public void setContainer(){
    container = this.getContentPane();
    container.setLayout(new BorderLayout());
    container.setBackground(Color.white);
    notch = new NotchFilterInterface();
    freq = new FreqFilterInterface();
    coordNotch = new DefinableNotchInterface();
    gaussian = new GaussianInterface();
    JPanel coordNotchPanel = coordNotch.createPanel();
    coordNotchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //    coordNotchPanel.setMaximumSize(new Dimension(500,10));
    JPanel notchPanel = notch.createPanel();
    notchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //    notchPanel.setMaximumSize(new Dimension(500,10));
    JPanel freqPanel = freq.createPanel();
    freqPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //    freqPanel.setMaximumSize(new Dimension(500,10));
    JPanel gaussianPanel = gaussian.createPanel();
    gaussianPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //    gaussianPanel.setMaximumSize(new Dimension(500,10));
    JPanel goPanel = createStartPanel();
    loadPanel = createLoadPanel();
    
    imagesPanel = createImagesPanel();
    imagesPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

    leftPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout());
    leftPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    leftPanel.setBackground(Color.white);
    operatorPanel = new JPanel();
    //    buttonPanel = new JPanel();
    operatorPanel.setLayout(new BoxLayout(operatorPanel,BoxLayout.X_AXIS));
    buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
    buttonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    buttonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(10,10,10,0)),"Frequency Domain Operators"));
    operatorPanel.setOpaque(false);
    buttonPanel.setOpaque(false);
    buttonPanel.add(coordNotchPanel);
    buttonPanel.add(Box.createRigidArea(new Dimension(10,10)));
    buttonPanel.add(notchPanel);
    buttonPanel.add(Box.createRigidArea(new Dimension(10,10)));
    buttonPanel.add(freqPanel);
    buttonPanel.add(Box.createRigidArea(new Dimension(10,10)));
    buttonPanel.add(gaussianPanel);
    operatorPanel.add(leftPanel);
    createFourierPanel();
    //operatorPanel.add(fourierPanel);
    leftPanel.add(buttonPanel,"North");
    leftPanel.add(goPanel,"Center");
    leftPanel.add(imagesPanel,"South");
    
    loadPanel.setBackground(Color.white);
    operatorPanel.setBackground(Color.white);
    fourierPanel.setBackground(Color.white);

    container.add(loadPanel,"North");
    container.add(operatorPanel,"Center");
    container.add(fourierPanel,"East");
  }

  /**
   * Sets the operations linked to the various GUI operator buttons.
   */
  public void setOperation(){
    
    notch.notchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else if(notch.getWidth()/2==0 || notch.getRadius()==0){
	}else{
	  fft.intermediate = NotchFilter.notch(fft.intermediate,
					 notch.getWidth()/2,
					 notch.getRadius());
	  updateFourierImages();
	  updateTabs();
	}
      }});
    freq.filterButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else if(freq.getRadius()==0){
	}else{
	  fft.intermediate = FreqFilter.filter(fft.intermediate,
					 freq.getHighLow(),
					 freq.getRadius());
	  updateFourierImages();
	  updateTabs();
	}
      }});
    coordNotch.notchButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  int [] coords = new int [4];
	  coords = coordNotch.getCoords(fft.input.size);
	  if(coords[0]<fft.input.size/2-fft.input.size||
	     coords[1]<fft.input.size/2-fft.input.size||
	     coords[2]<fft.input.size/2-fft.input.size||
	     coords[3]<fft.input.size/2-fft.input.size||
	     coords[0]>fft.input.size/2||
	     coords[1]>fft.input.size/2||
	     coords[2]>fft.input.size/2||
	     coords[3]>fft.input.size/2){
	    JOptionPane.showMessageDialog(null,
					  ("Coordinates out of bounds."),
					  "Invalid Value",
					  JOptionPane.WARNING_MESSAGE); 
	  }else{
	    fft.intermediate = 
	      DefinableNotch.filter(fft.intermediate,coords,0);
	    updateFourierImages();
	    updateTabs();
	  }
	}
      }});
    coordNotch.notchKeepButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  int [] coords = new int [4];
	  coords = coordNotch.getCoords(fft.input.size);
	  if(coords[0]<0||
	     coords[1]<0||
	     coords[2]<0||
	     coords[3]<0||
	     coords[0]>fft.input.size ||
	     coords[1]>fft.input.size ||
	     coords[2]>fft.input.size ||
	     coords[3]>fft.input.size){
	    JOptionPane.showMessageDialog(null,
					  ("Coordinates out of bounds."),
					  "Invalid Value",
					  JOptionPane.WARNING_MESSAGE); 
	  }else{
	    fft.intermediate = 
	      DefinableNotch.filter(fft.intermediate,coords,1);
	    updateFourierImages();
	    updateTabs();
	  }
	}
      }});
    gaussian.smooth.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	if(fourierPane.getTabCount()==1){
	  //pop up warning box
	  JOptionPane.showMessageDialog(null,
		("Must Apply Fourier Transform First"),
					"No Fourier Transform Result",
					JOptionPane.WARNING_MESSAGE); 
	}else{
	  double size = gaussian.getSize();
	  int x = gaussian.getX();
	  int y = gaussian.getY();
	  if(size<0 || x>=1000000 || y >= 1000000){
	    //pop up warning
	  }else{
	    fft.intermediate =
	      Gaussian.smooth(fft.intermediate,x,y,size);
	    updateFourierImages();
	    updateTabs();
	  }
	}
      }});
    }
  
  /**
   * JApplet init method to start the applet and initialise it.
   */
  public void init() {

    setLook();
    setContainer();

    initialImage("cln1.gif");
    initialiseImages();
    //timetaken = new TimeTakenInterface();
    //JPanel timetakenPanel = timetaken.createPanel();

    setOperation();

    //operatorPanel.add(timetakenPanel);
  }

  /**
   * Used by the browser to obtain details of the applet.
   *
   * @return Brief description of the applet.
   */
  public String getAppletInfo() {
    return "An applet for applying the FFT, various intermediate operations and the inverse FFT to an image.  Author Simon Horne.";
  }

}

