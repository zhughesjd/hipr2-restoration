package net.joshuahughes.hipr2.lower;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.joshuahughes.hipr2.upper.Histogram;
import net.joshuahughes.hipr2.upper.NoScaleImageCanvas;
import net.joshuahughes.hipr2.upper.Threshold;


public class threshold extends operator1DInt{

  image1DInt temp;
  Threshold thresholdOp = new Threshold();
  static int number=0;
  String type = new String("Thresholding");
  thresholdImageCanvas histImage;
  JLabel lowLabel;
  JTextField lowBox;
  JSlider lowSlider;
  JLabel highLabel;
  JTextField highBox;
  JSlider highSlider;
  JPanel histPanel, topPanel, midPanel, bottomPanel;
  JButton applyButton;
  JLabel maxLabel, peakLabel;
  GridBagLayout panelLayout;
  GridBagConstraints panelCon;
  int highT, lowT;
  int histpeak;
  double histmaximum;
  int val;
  JLabel rescaleLabel;
  JTextField rescaleText;
  int scaleMax;
  JPanel rescalePanel;
  //histogram label. A bit artificial for 256x256 images...
  JLabel hist_range = new JLabel("0           64"+
			       "             128"+
			       "           192"+
			       "          256");
  Histogram histogramAlgorithm = new Histogram();;
  int [] hist_1d;
  Image hist;

  public threshold(){
  }

  public threshold(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
    box.getOut1().setText("BINOUT1");
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    saveData = highT + " " + lowT + " " + rescaleText.getText();
    return saveData;
  }

  /**
   *Used to load all the parameters for this particular operator and reset the 
   *interface so that it contains these loaded parameters
   */

  public void loadParameters(StreamTokenizer tokenizer) throws IOException{
    
    //Grab the parameters
    int tokenType;
    tokenType = tokenizer.nextToken();
    int highValue = (int) tokenizer.nval;
    highSlider. setValue(highValue);
    highBox. setText(String. valueOf(highValue));
    highT = highValue;

    tokenType = tokenizer.nextToken();
    int lowValue = (int) tokenizer.nval;
    lowSlider. setValue(lowValue);
    lowBox. setText(String. valueOf(lowValue));
    lowT = lowValue;

    tokenType = tokenizer.nextToken();
    scaleMax = (int) tokenizer.nval;
    rescaleText.setText(String. valueOf(scaleMax));

    updateThreshold(lowSlider. getValue(), highSlider. getValue());

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }

  
  void setParameters(){
    /**
     *This function is used to set up the parameters window. This window
     *should contain enough parameters to be able to run the operator
     *although parameters like scaling and offset are not required as there
     *is an operator already defined to do this. The interface components
     *should be added to the parameters frame. In this example a single panel
     *is created which is used to hold a label saying there are no paramters.
     *In general this will not be true of most operators.
     */
    highT = 192;
    lowT = 64;
    histImage = null;
    parameters = new JFrame(name);
    panel = new JPanel();
    panelLayout = new GridBagLayout();
    panelCon = new GridBagConstraints();
    histPanel = new JPanel();
    topPanel = new JPanel();
    midPanel = new JPanel();
    bottomPanel = new JPanel();

    //Create and add the components to the panels
    lowLabel = new JLabel("Low Threshold");
    lowBox = new JTextField(5);
    lowBox. setText("64");
    lowBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
	try{
	  int val = Integer. valueOf (lowBox. getText()). intValue();
	  if(val >= 0 && val <= 255){
	     lowSlider. setValue(val);
	  }
	  else{
	    JOptionPane.showMessageDialog(null,("Threshold must lie between 0 and 255"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);     
	     lowSlider. setValue(64);
	     lowBox. setText("64");
	  }
	  updateThreshold(lowSlider. getValue(), highSlider. getValue()); 
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid threshold specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);  
	}    
      }
    });
    lowSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 64);
    lowSlider. setMajorTickSpacing(50);
    lowSlider. setMinorTickSpacing(5);
    lowSlider. setPaintTicks(true);
    lowSlider. setPaintLabels(true);
    lowSlider. addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent evt) {
      //Do the slider change operations
      lowBox. setText(String. valueOf(lowSlider. getValue()));
      updateThreshold(lowSlider. getValue(), highSlider. getValue()); 
      }
    });
    topPanel. add(lowLabel);
    topPanel. add(lowBox);
    topPanel. add(lowSlider);

    highLabel = new JLabel("High Threshold");
    highBox = new JTextField(5);
    highBox. setText("192");
    highBox. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt){
	try{
	  try {
	  val = Integer. valueOf (highBox. getText()). intValue();
	  }
	  	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid threshold specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
		} 
	  if(val >= 0 && val <= 255){
	     highSlider. setValue(val);
	  }
	  else{
	    JOptionPane.showMessageDialog(null,("Threshold must lie between 0 and 255"),
					  ("Error!"), JOptionPane.WARNING_MESSAGE);     
	     highSlider. setValue(192);
	     highBox. setText("192");
	  }
	  updateThreshold(lowSlider. getValue(), highSlider. getValue()); 
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid threshold specified"),
					("Error!"), JOptionPane.WARNING_MESSAGE);  
	}    
      }
    });
    highSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 192);
    highSlider. setMajorTickSpacing(50);
    highSlider. setMinorTickSpacing(5);
    highSlider. setPaintTicks(true);
    highSlider. setPaintLabels(true);
    highSlider. addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent evt) {
      //Do the slider change operations
      highBox. setText(String. valueOf(highSlider. getValue()));
      updateThreshold(lowSlider. getValue(), highSlider. getValue());
      }
    });
    midPanel. add(highLabel);
    midPanel. add(highBox);
    midPanel. add(highSlider);

    applyButton = new JButton("Apply");
    applyButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	dothresh();
      }
    });
    bottomPanel. add(applyButton);

    //Add the panels to the main panel
    panel. setLayout(panelLayout);

    panelCon. gridx = 0;
    panelCon. gridy = 0;
    panelLayout. setConstraints(topPanel, panelCon);
    panel. add(topPanel);
    
    panelCon. gridx = 0;
    panelCon. gridy = 1;
    panelLayout. setConstraints(midPanel, panelCon);
    panel. add(midPanel);
    
    panelCon. gridx = 0;
    panelCon. gridy = 2;
    panelLayout. setConstraints(bottomPanel, panelCon);
    panel. add(bottomPanel);
    
    rescaleLabel = new JLabel("New maximum for histogram scaling:");
    rescaleText = new JTextField(6);
    rescaleText. setText("-1");
    rescalePanel = new JPanel();
    //rescalePanel.setLayout(new BorderLayout());
    rescalePanel.add(rescaleLabel);
    rescalePanel.add(rescaleText);
    JButton rescaleButton = new JButton("Scale");
    rescalePanel.add(rescaleButton);
    rescaleButton. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	scale();
      }
    });

    //Add the main panel
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void updateThreshold(int low, int high){
    if(histImage!=null){
      if(high != highT){
	histImage. highthresh = high;
	highT = high;
      }
      if(low != lowT){
	histImage. lowthresh = low;
	lowT = low;
      }
      histImage. invalidate();
      histImage. repaint();
    }
  }

  // executed if new image coming downstream
  public void go(){

    System.out.println(name);

    if(getInput1()!=null){

      // reset image scaling if nothing previously set
      if (new Double(rescaleText.getText()).doubleValue() < 0)
      {
        int [] rawpixels;
        rawpixels = imageConversions.gs2pix(input1.getValues());
        scaleMax = (int) histogramAlgorithm.maximum(rawpixels, input1.getWidth(),input1.getHeight());
        rescaleText. setText(String. valueOf(scaleMax));
      }
      dothresh();
    }
  }
  
  public void dothresh(){
    
    System.out.println(name);
    
    if(getInput1()!=null){


      // analyse image
      int [] rawpixels;
      rawpixels = imageConversions.gs2pix(input1.getValues());
      histpeak = histogramAlgorithm.peak(rawpixels, input1.getWidth(),input1.getHeight());
      histmaximum = histogramAlgorithm.maximum(rawpixels, input1.getWidth(),input1.getHeight());

      // make histogram
      int [] histpixels;
      histpixels = histogramAlgorithm.histogramScale(rawpixels, input1.getWidth(),input1.getHeight(),scaleMax);
      hist = parameters.createImage(new MemoryImageSource(256,256,histpixels,0,256));
      updateDisplay();

      //Now apply the thresholding
      if(highSlider. getValue() < lowSlider. getValue()){
	  JOptionPane.showMessageDialog(null,("High threshold must be bigger than low threshold"),
				      ("Error!"), JOptionPane.WARNING_MESSAGE);  
      }
      else {
	output1 = new image1DInt(
                   input1.getWidth(),
  		   input1.getHeight(),
      		   imageConversions.pix2gs(thresholdOp.twothreshold(
		       imageConversions.gs2pix(input1.getValues()),
      		       input1.getWidth(),
      		       input1.getHeight(),
      		       lowT, highT)));

	propagate();
      }
    }
  }

void updateDisplay(){

     histImage = new thresholdImageCanvas(hist, 256, scaleMax);
     histImage. lowthresh = lowT;
     histImage. highthresh = highT;
     panel. remove(histPanel);
     histPanel = new JPanel();
     histPanel.setLayout(new BorderLayout());
     histPanel. setBackground(Color.white);
     histPanel. add(histImage,"Center");
     panelCon. gridx = 0;
     panelCon. gridy = 3;
     panelLayout. setConstraints(histPanel, panelCon);
     panel. add(histPanel);

     if (peakLabel != null) {
        panel.remove(peakLabel);
     }
     peakLabel = new JLabel("Peak Position: "+histpeak);
     panelCon.gridx = 0;
     panelCon.gridy = 4;
     panelLayout. setConstraints(peakLabel, panelCon);
     panel.add(peakLabel);
     
     if (maxLabel != null) {
        panel.remove(maxLabel);
     }
     maxLabel =  new JLabel("Maximum value: "+(int)histmaximum);
     panelCon.gridx = 0;
     panelCon.gridy = 5;
     panelLayout. setConstraints(maxLabel, panelCon);
     panel.add(maxLabel);

     panelCon.gridx = 0;
     panelCon.gridy = 6;
     panelLayout. setConstraints(rescalePanel, panelCon);
     panel.add(rescalePanel);


     panel.repaint();
     parameters.pack();
     updateParameters();
}

 void scale() {

    int [] pixels;

    // get rescale parameter
    try {
      scaleMax = (int) new Double(rescaleText.getText()).doubleValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for scale"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (scaleMax <= 0) {
      JOptionPane.showMessageDialog(null,("Scale value must be positive"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
   
    pixels = histogramAlgorithm.histogramScale(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),scaleMax);
   
    hist = 
	parameters.createImage(new MemoryImageSource(256,256,pixels,0,256));
    
    if (histImage != null) {
	histPanel.remove(histImage);
    }
    histImage = new thresholdImageCanvas(hist,256,scaleMax);
    histImage.lowthresh = lowT;
    histImage.highthresh = highT;
    histPanel. add(histImage,"Center");
    parameters.pack();
    updateParameters();
 }

 class thresholdImageCanvas extends NoScaleImageCanvas{
 
  public Image image;
  public int lowthresh = 64;
  public int highthresh = 192;

  //The old threshold values - needed for updating the image
  private int oldlowthresh = 64;
  private int oldhighthresh = 192;

  int wvalue;
  int hvalue;
  
  public thresholdImageCanvas(Image i, int widthvalue, int heightvalue) {
    
    super(i);
    hvalue = heightvalue;
    wvalue = widthvalue;
    //resize(256, 256);
  }

  
  public void paint(Graphics graphics) {
    
    //histogram always gets painted at 256 x 256
    setBackground(Color. gray);
    super. paint(graphics);
    graphics. setColor(Color.red);
    graphics. drawLine(highthresh, 1, highthresh, 256);
    graphics. setColor(Color. green);
    graphics. drawLine(lowthresh, 1, lowthresh  , 256);
    graphics.setColor(Color.white);

    // Vertical line
      graphics.setColor(Color.red);
      graphics.drawLine(0,0,0,255);
      graphics.drawLine(0,0,10,0);
      graphics.drawLine(0,64,5,64);
      graphics.drawLine(0,128,10,128);
      graphics.drawLine(0,192,5,192);
      graphics.drawLine(0,256,10,256);
      int a = 0;
      int b = hvalue/4;
      int c = b+b;
      int d = b+c;
      int e = hvalue;
      graphics.drawString(Integer.toString(e),0,15);
      //graphics.drawString(Integer.toString(d),0,74);
      graphics.drawString(Integer.toString(c),10,140);
      //graphics.drawString(Integer.toString(b),0,202);
      graphics.drawString(Integer.toString(a),5,250);

      // Horizontal line 
      graphics.setColor(Color.red);
      graphics.drawLine(0,255,255,255);
      graphics.drawLine(0,255,0,245);
      graphics.drawLine(64,255,64,250);
      graphics.drawLine(128,255,128,245);
      graphics.drawLine(192,255,192,250);
      graphics.drawLine(256,255,256,245);
      int aa = 0;
      int bb = wvalue/4;
      int cc = bb+bb;
      int dd = bb+cc;
      int ee = wvalue;
      graphics.drawString(Integer.toString(aa),5,240);
      //graphics.drawString(Integer.toString(bb),58,240);
      graphics.drawString(Integer.toString(cc),123,240);
      //graphics.drawString(Integer.toString(dd),180,240);
      graphics.drawString(Integer.toString(ee),230,240);
  }
 
    
 
   public void update(Graphics g){
    
     g. setColor(getBackground());
     g. drawLine(oldhighthresh, 1, oldhighthresh, 256);
     g. drawLine(oldlowthresh, 1, oldlowthresh, 256);
     g. drawImage(image, 1, 1,256, 256 ,this);
     g. setColor(Color.red);
     g. drawLine(highthresh, 1, highthresh, 256);
     g. setColor(Color.green);
     g. drawLine(lowthresh , 1, lowthresh , 256);
     g.setColor(Color.white);
     //g.drawRect(0, 0, 258, 258 );
     oldlowthresh = lowthresh;
     oldhighthresh = highthresh;
   }
 }
}
