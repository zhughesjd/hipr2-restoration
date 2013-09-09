package net.joshuahughes.hipr2.lower;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
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
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.Histogram;
import net.joshuahughes.hipr2.upper.NoScaleImageCanvas;


public class histogram extends operator1DInt{

  /**
   * This class is used to display the histogram of an image with 
   * horizontal and vertical axis
   */
   public class HistCanvas extends NoScaleImageCanvas {

    int w;
    int h;

    public HistCanvas (Image i, int width, int height) {
      super(i);
      w = width;
      h = height;
    }
     public void paint(Graphics graphics) {
      
      super.paint(graphics);
      // Vertical line
      graphics.setColor(Color.red);
      graphics.drawLine(0,0,0,255);
      graphics.drawLine(0,0,10,0);
      graphics.drawLine(0,64,5,64);
      graphics.drawLine(0,128,10,128);
      graphics.drawLine(0,192,5,192);
      graphics.drawLine(0,256,10,256);
      int a = 0;
      int b = h/4;
      int c = b+b;
      int d = b+c;
      int e = h;
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
      int bb = w/4;
      int cc = bb+bb;
      int dd = bb+cc;
      int ee = w;
      graphics.drawString(Integer.toString(aa),5,240);
      //graphics.drawString(Integer.toString(bb),58,240);
      graphics.drawString(Integer.toString(cc),123,240);
      //graphics.drawString(Integer.toString(dd),180,240);
      graphics.drawString(Integer.toString(ee),230,240);
    }
    
  }


  
  image1DInt temp;
  static int number=0;
  String type = new String("IntensityHistogram");
  HistCanvas dest_canvas;
  JButton applyButton;
  JLabel maxLabel;
  JLabel peakLabel;
  JPanel bottompanel;
  JLabel rescaleLabel;
  JTextField rescaleText;
  JLabel spaceText;
  int histpeak;
  double histmaximum;
  int scaleMax;
  
  int val;
  //histogram label. A bit artificial for 256x256 images...
  JLabel hist_range = new JLabel("0           64"+
			       "             128"+
			       "           192"+
			       "          256");
  Histogram histogramAlgorithm = new Histogram();;
  int [] hist_1d;
  Image hist;

  public histogram(){
  }

  public histogram(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = rescaleText.getText();
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
    scaleMax = (int) tokenizer.nval;
    rescaleText.setText(String. valueOf(scaleMax));

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
     
    parameters = new JFrame(name);
    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    bottompanel = new JPanel();
    bottompanel.setLayout(new BorderLayout());
    panel.add(bottompanel,"South");
    rescaleLabel = new JLabel("New maximum for histogram scaling:");
    rescaleText = new JTextField(5);
    rescaleText. setText("-1");
    spaceText = new JLabel("                ");
    panel.add(spaceText, "West");
    JPanel topPanel = new JPanel();
    JButton applyButton = new JButton("Apply scaling");
    topPanel.add(rescaleLabel);
    topPanel.add(rescaleText);
    topPanel.add(applyButton);
    applyButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	scale();
      }
    });

    panel.add(topPanel,"North");
    //Add the main panel
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){

    System.out.println(name);
   
    if(getInput1()!=null){

      // analyse image
      int [] rawpixels;
      rawpixels = imageConversions.gs2pix(input1.getValues());
      histpeak = histogramAlgorithm.peak(rawpixels, input1.getWidth(),input1.getHeight());
      histmaximum = histogramAlgorithm.maximum(rawpixels, input1.getWidth(),input1.getHeight());
//System.out.println("peak+max: "+histpeak+" "+histmaximum);
      
      // reset image scaling if nothing previously set
      if (new Double(rescaleText.getText()).doubleValue() < 0)
      {
              // need to find max for first scaling
              scaleMax = (int) histmaximum;
              rescaleText. setText(String. valueOf(scaleMax));
      }              

      // make histogram
      int [] histpixels;
      histpixels = histogramAlgorithm.histogramScale(rawpixels, input1.getWidth(),input1.getHeight(),scaleMax);
      hist = parameters.createImage(new MemoryImageSource(256,256,histpixels,0,256));
    
      updateDisplay();

      output1 = new image1DInt(input1.getWidth(),input1.getHeight(), input1.getValues());
System.out.println("Output Width 1 "+getOutput1().getWidth());
	
      propagate();
    }
  }

void updateDisplay(){

     if (dest_canvas != null) {
            panel.remove(dest_canvas);
     }
     dest_canvas = new HistCanvas(hist,256,scaleMax);
     panel.add(dest_canvas, "Center");

     if (peakLabel != null) {
            bottompanel.remove(peakLabel);
     }
     peakLabel = new JLabel("Peak position: "+histpeak);
     bottompanel.add(peakLabel,"North");

     if (maxLabel != null) {
            bottompanel.remove(maxLabel);
     }
     maxLabel = new JLabel("Maximum value: "+(int)histmaximum);
     bottompanel.add(maxLabel,"South");

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
    
    if (dest_canvas != null) {
	panel.remove(dest_canvas);
    }
    dest_canvas = new HistCanvas(hist,256,scaleMax);
    panel. add(dest_canvas,"Center");
    parameters.pack();
    updateParameters();
 }

}
