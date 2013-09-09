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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.FreqFilter;
import net.joshuahughes.hipr2.upper.Gaussian;
import net.joshuahughes.hipr2.upper.ImageMods;
import net.joshuahughes.hipr2.upper.ImageTools;
import net.joshuahughes.hipr2.upper.NoScaleImageCanvas;
import net.joshuahughes.hipr2.upper.NotchFilter;
import net.joshuahughes.hipr2.upper.TwoDArray;


public class mask extends operator1DInt{
  
  /**
   * This operator generates a mask by applying notch filter, frequency filter
   * and gaussian smoothing to be used with the FFT operator
   * this operator needs to be linked the input image of the FFT operator
   * to be initialized because it needs to know the size of the FFT output.
   */
  static int number=0;
  /**
   * The type of operator 
   */
  String type = new String("FFTMask");

  // Output parameters
  int [] input;
  TwoDArray result;             // accumulated mask
  int w;
  int h;

  // Interface parameters
  Image displayImage;
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel();
  JScrollPane scroll;
  maskCanvas dest_canvas;
  JPanel maskpanel = null;
  JPanel imagePanel;
  JPanel filterPanel;
  JButton notch = new JButton("Add Notch Filter");
  JTextField widthText = new JTextField("5",5);
  JTextField radiusText = new JTextField("15",5);
  JLabel widthLabel = new JLabel("Width");
  JLabel radiusLabel = new JLabel("Radius");
  JButton frequency = new JButton("Add Frequency Filter");
  JTextField freqText = new JTextField("50",5);
  JComboBox freqchoice = new JComboBox();
  JLabel freqLabel = new JLabel("Radius");
  JButton gauss = new JButton("Add Gaussian Smooth");
  JTextField gsizeText = new JTextField("30",5);
  JTextField yText = new JTextField("0",5);
  JTextField xText = new JTextField("0",5);
  JLabel gsizeLabel = new JLabel("Size");
  JLabel yLabel = new JLabel("  Y ");
  JLabel xLabel = new JLabel("X ");
  JButton remove = new JButton("Remove Area");
  JButton keep = new JButton("Keep Area"); 
  JLabel y1Label = new JLabel("Y1");
  JLabel x1Label = new JLabel("X1");
  JLabel y2Label = new JLabel("Y2");
  JLabel x2Label = new JLabel("X2");
  JTextField y1Text = new JTextField("10",5);
  JTextField x1Text = new JTextField("10",5);
  JTextField y2Text = new JTextField("20",5);
  JTextField x2Text = new JTextField("20",5);
  JButton apply = new JButton("Apply");
  JButton reset = new JButton("Reset mask");
  MaskHistory maskhistory = new MaskHistory();
  
  /**
   * Constructor taking the parent panel (for the UI) and information
   * about the links in the system
   * @param panel the panel that the UI for the operators is based around
   * @param links the information about the system's links
   */
  public mask(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setType(type);
    setParameters();
    setBox(panel,links,1,1);
    box.getOut1().setText("FFTMask");
    go();
  }
   
  /**
   * Returns the number of this operator.
   * @return the number of the operator
   */
  public int getNumber(){
    return number;
  }
   /**
   * Loads the required parameters (number of iterations)
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{

        maskhistory.clearHistory();
        maskhistory.loadHistory(tokenizer);
  }

  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = maskhistory.saveHistory();
    return saveData;
  }
  /**
   * Sets up the parameters window and the default settings for the operator
   */
  void setParameters(){
    parameters = new JFrame(name);
    filterPanel = new JPanel();
    panel = new JPanel();
    
    panel.setLayout(new BorderLayout());
   
    // The filter panel 

    GridBagLayout filterLayout = new GridBagLayout();
    GridBagConstraints filterc = new GridBagConstraints();
    filterPanel.setLayout(filterLayout);
    
    // Notch filter parameters
    JPanel notchPanel = new JPanel();
    notchPanel.add(notch);
    notchPanel.add(widthLabel);
    notchPanel.add(widthText);
    notchPanel.add(radiusLabel);
    notchPanel.add(radiusText);
    filterc.gridx = 0;
    filterc.gridy = 0;
    filterLayout.setConstraints(notchPanel,filterc);
    filterPanel.add(notchPanel);

    // Frequency filter parameters
    JPanel freqPanel = new JPanel();
    freqPanel.add(frequency);
    freqchoice.addItem("High Pass Filter");
    freqchoice.addItem("Low Pass Filter");
    freqPanel.add(freqchoice);
    freqPanel.add(freqLabel);
    freqPanel.add(freqText);
    filterc.gridx = 0;
    filterc.gridy = 1;
    filterLayout.setConstraints(freqPanel,filterc);
    filterPanel.add(freqPanel);

    // Gaussian filter parameters
    JPanel gaussPanel = new JPanel();
    gaussPanel.add(gauss);
    gaussPanel.add(gsizeLabel);
    gaussPanel.add(gsizeText);
    gaussPanel.add(xLabel);
    gaussPanel.add(xText);
    gaussPanel.add(yLabel);
    gaussPanel.add(yText);
    filterc.gridx = 0;
    filterc.gridy = 2;
    filterLayout.setConstraints(gaussPanel,filterc);
    filterPanel.add(gaussPanel);

    // Remove and keep area parameters
    JPanel areaPanel = new JPanel();
    areaPanel.add(remove);
    areaPanel.add(keep);
    areaPanel.add(x1Label);
    areaPanel.add(x1Text);
    areaPanel.add(y1Label);
    areaPanel.add(y1Text);
    areaPanel.add(x2Label);
    areaPanel.add(x2Text);
    areaPanel.add(y2Label);
    areaPanel.add(y2Text);
    filterc.gridx = 0;
    filterc.gridy = 3;
    filterLayout.setConstraints(areaPanel,filterc);
    filterPanel.add(areaPanel);

    // Display mask 
    
    
    notch.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply_notch();
      }});
    frequency.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply_freq();
      }});
     gauss.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply_gauss();
      }});
     remove.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply_remove();
      }});
     keep.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply_keep();
      }});
    panel.add(filterPanel,"North");

    // mask accumulator pansl
    maskpanel = new JPanel();
    maskpanel.setLayout(new BorderLayout());
    panel.add(maskpanel,"Center");

    // Apply and reset buttons
    JPanel midPanel = new JPanel();
    midPanel.add(apply);
    midPanel.add(reset);
//    filterc.gridx = 0;
//    filterc.gridy = 4;
//    filterLayout.setConstraints(midPanel,filterc);
//    filterPanel.add(midPanel);
     panel.add(midPanel,"South");
     apply.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	apply();
      }});
     reset.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	clear();
	maskhistory.clearHistory();
      }});
    

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }
 /**
   * Runs the operator 
   * and propagates the image to all the operators that it links
   * to.
   */
  public void go(){
    System.out.println(name);
    if (getInput1() != null) {
      h =input1.getHeight();
      w =input1.getWidth();
      input = new int[h*w];
      for (int i = 0; i < input.length; i++) {
        input[i] = 255 ;
      }
      result = new TwoDArray(input,w,h);
      maskhistory.apply_history();
      input = imageConversions.pix2gs(ImageMods.toPixels(result.getReal()));
      updatedisplay();
      output1 = new image1DInt(w,h,input);
      propagate();
      System.out.println("Output width " + output1.getWidth()); 
    }
  }

  /**
   * Resets the mask and creates a full white image
   */
  public void clear() {
    for (int i = 0; i < input.length; i++) {
     input[i] = 255 ;
   }
    result = new TwoDArray(input,w,h);
    updatedisplay();
  }

  /**
   * Sends the mask created to the output.
   */
  public void apply() {
    TwoDArray temp = new TwoDArray(result);
    input = imageConversions.pix2gs(ImageMods.toPixels(temp.DCToCentre(temp.getReal())));
    output1 = new image1DInt(w,h,input);
    System.out.println("Output width " + output1.getWidth()); 
    propagate();
  }

  /**
   * Adds notch filter to the current mask.
   */
  public void apply_notch() {
    TwoDArray temp = new TwoDArray(result);
    int wn,rn;
    try {
            wn = new Integer(widthText.getText()).intValue();
            rn = new Integer(radiusText.getText()).intValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for Width or Radius"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    if(wn > 0 && rn > 0) {
      result = NotchFilter.notch(temp,wn,rn);
      maskhistory.addtoMaskHistory(1, (double) wn, (double) rn, 0.0, 0.0);
    }  
    else {
      JOptionPane.showMessageDialog(null,("Invalid value for Width or Radius"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
     updatedisplay();
  }

  /**
   * Adds gauss filter to the current mask.
   */
  public void apply_gauss() {
    int xg, yg;
    double gsize;
    try {
     xg = new Integer(xText.getText()).intValue();
     yg = new Integer(yText.getText()).intValue();
     gsize = new Double(gsizeText.getText()).doubleValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for Width or Radius"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    TwoDArray temp = new TwoDArray(result);
    result = Gaussian.smooth(temp,xg,yg,gsize);
    maskhistory.addtoMaskHistory(4, (double) xg, (double) yg, (double) gsize, 0.0);
    updatedisplay();
  }

  /**
   * Adds frequency filter to the current mask.
   */
  public void apply_freq () {
    int rf;
    try {
    rf = new Integer(freqText.getText()).intValue();
    }
    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for Width or Radius"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
    }
    TwoDArray temp = new TwoDArray(result);
    if (freqchoice.getSelectedItem().equals("High Pass Filter")) {
      maskhistory.addtoMaskHistory(2, (double) rf, 0.0, 0.0, 0.0);
      result = FreqFilter.filter(temp,false,rf);
    }
    else {
      maskhistory.addtoMaskHistory(3, (double) rf, 0.0, 0.0, 0.0);
      result = FreqFilter.filter(temp,true,rf);
    }
     updatedisplay();
  }

  /**
   * Removes a determined area from the mask.
   */
  public void apply_remove() {
  int x1,x2,y1,y2;
  try {
    x1 = new Integer(x1Text.getText()).intValue();
    x2 = new Integer(x2Text.getText()).intValue();
    y1 = new Integer(y1Text.getText()).intValue();
    y2 = new Integer(y2Text.getText()).intValue();
  }
   catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for coordinates"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
   }
   if (x1 < -w/2 || y1 < -h/2 || x2 > w/2 || y2 > h/2) {
     JOptionPane.showMessageDialog(null,("Coordinates out of bounds"),("Error!"), JOptionPane.WARNING_MESSAGE);
     return;
   }
   else if (x1 > x2 || y1 > y2 ) {
     JOptionPane.showMessageDialog(null,("Coordinates need x1 <= x2, y1 <= y2"),("Error!"), JOptionPane.WARNING_MESSAGE);
     return;
   }
   else {
     actual_remove(x1,x2,y1,y2);
     maskhistory.addtoMaskHistory(5, (double) x1, (double) x2, (double) y1, (double) y2);
     updatedisplay();
   }
  }
  public void actual_remove(int x1,int x2,int y1,int y2) {
     TwoDArray temp = new TwoDArray(result);
     for(int j = 0; j < h ; j++) {
       for(int i = 0; i < w; i++) {

         // decide if inside or outside
         boolean iinside = false;
         boolean jinside = false;
         if (i <= w/2) {
                if (x1 <= i && i <= x2) iinside = true;
         }
         else {
                if (x1 <= (i-w) && (i-w) <= x2) iinside = true;
         }
         if (j <= h/2) {
                if (y1 <= j && j <= y2) jinside = true;
         }
         else {
                if (y1 <= (j-h) && (j-h) <= y2) jinside = true;
         }
                
         if (iinside && jinside) {
           temp.values[i][j].real = 0;
           temp.values[i][j].imaginary = 0;
         }
       }
     }
     result = temp;
  }

  /**
   * Keeps only a determined area from the mask.
   */ 
  public void apply_keep() {
  int x1,x2,y1,y2;
  try {
    x1 = new Integer(x1Text.getText()).intValue();
    x2 = new Integer(x2Text.getText()).intValue();
    y1 = new Integer(y1Text.getText()).intValue();
    y2 = new Integer(y2Text.getText()).intValue();
  }
   catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Wrong Format for coordinates"),("Error!"), JOptionPane.WARNING_MESSAGE);
      return;
   }
   if (x1 < -w/2 || y1 < -h/2 || x2 > w/2 || y2 > h/2) {
     JOptionPane.showMessageDialog(null,("Coordinates out of bounds"),("Error!"), JOptionPane.WARNING_MESSAGE);
     return;
   }
   else if (x1 > x2 || y1 > y2 ) {
     JOptionPane.showMessageDialog(null,("Coordinates need x1 <= x2, y1 <= y2"),("Error!"), JOptionPane.WARNING_MESSAGE);
     return;
   }
   else {
     actual_keep(x1,x2,y1,y2);
     maskhistory.addtoMaskHistory(6, (double) x1, (double) x2, (double) y1, (double) y2);
     updatedisplay();
   }
  }
  public void actual_keep(int x1,int x2,int y1,int y2) {
    TwoDArray temp = new TwoDArray(result);
     for(int j = 0; j < h ; j++) {
       for(int i = 0; i < w; i++) {

         // decide if inside or outside
         boolean iinside = false;
         boolean jinside = false;
         if (i <= w/2) {
                if (x1 <= i && i <= x2) iinside = true;
         }
         else {
                if (x1 <= (i-w) && (i-w) <= x2) iinside = true;
         }
         if (j <= h/2) {
                if (y1 <= j && j <= y2) jinside = true;
         }
         else {
                if (y1 <= (j-h) && (j-h) <= y2) jinside = true;
         }
                
         if (!iinside || !jinside) {
           temp.values[i][j].real = 0;
           temp.values[i][j].imaginary = 0;
         }
       }
     }
     result = temp;
  }  
  
  /**
   * Updates the display of the mask in the parameters popup.
   */
  public void updatedisplay() {
    ImageTools imageTools = new ImageTools();
    TwoDArray temp = new TwoDArray(result);
    input = ImageMods.toPixels(temp.DCToCentre(temp.getReal()));
//for (int i=0; i<h*w; i++) if (input[i] == 0) System.out.println("Image Zero at: "+i);
    
      displayImage = 
	parameters.createImage(
            new MemoryImageSource(
                input1.getWidth(), 
	        input1.getHeight(), 
	        input,
                0, 
	        input1.getWidth()));
      //imageIcon = new ImageIcon(displayImage);
      
      //scroll = new JScrollPane(imageLabel);
      if (dest_canvas!= null) {
	maskpanel.remove(dest_canvas);
      }
      dest_canvas = new maskCanvas(displayImage, input1.getWidth(), input1.getHeight());
      JLabel Wmaskspace = new JLabel("                                            ");
      maskpanel. add(Wmaskspace,"West");
      maskpanel. add(dest_canvas);
      updateParameters();
  }

  /**
   * Canvas used for displaying the mask with axes and scale.
   */
  public class maskCanvas extends NoScaleImageCanvas {
    
    int w;
    int h;
    public maskCanvas(Image i, int width, int height ) {
      super(i);
      w = width;
      h = height;
    }

    public void paint(Graphics graphics) {

      // draw mask image
      super.paint(graphics);

      // add axes on top
      // horizontal line
      graphics.setColor(Color.green);
      graphics.drawLine(0,0,255,0);
      graphics.drawLine(0,65,255,65);
      graphics.drawLine(0,129,256,129);
      graphics.drawLine(0,192,256,192);
      graphics.drawLine(0,256,256,256);
      int a = -h+h/2;
      int b = a+h/4;
      int c = 0;
      int d = h+a+b;
      int e = h+a-1;
      graphics.drawString(Integer.toString(a),5,15);
      graphics.drawString(Integer.toString(b),5,76);
      graphics.drawString(Integer.toString(c),5,140);
      graphics.drawString(Integer.toString(d),5,204);
      graphics.drawString(Integer.toString(e),5,255);

      // vertical line 
      graphics.setColor(Color.red);
      graphics.drawLine(0,0,0,256);
      graphics.drawLine(65,0,65,256);
      graphics.drawLine(129,0,129,256);
      graphics.drawLine(192,0,192,256);
      graphics.drawLine(256,0,256,256);
      int aa = -w+w/2;
      int bb = a+w/4;
      int cc = 0;
      int dd = w+a+b;
      int ee = w+a-1;
      graphics.drawString(Integer.toString(aa),5,25);
      graphics.drawString(Integer.toString(bb),66,25);
      graphics.drawString(Integer.toString(cc),130,25);
      graphics.drawString(Integer.toString(dd),194,25);
      graphics.drawString(Integer.toString(ee),230,25);
    }
  }


  /**
   * Class containing method to save the history of a set of frequency filters
   *
   * @author Bob Fisher
   */
  public class MaskHistoryItem {
          int type;
          double arg1,arg2,arg3,arg4;
  
          public MaskHistoryItem() { }
          public MaskHistoryItem(int t, double a1, double a2, double a3, double a4) {
                  type=t; arg1 = a1; arg2 = a2; arg3 = a3; arg4 = a4; }
          
          public String saveMaskHistoryItem() {
                  String savedata = new String();
                  savedata = " "+type+" "+" "+arg1+" "+arg2+" "+arg3+" "+arg4+" ";
                  return savedata;
          }

          public void loadMaskHistoryItem(StreamTokenizer tokenizer) throws IOException {

            //Grab the parameters
            int tokenType = tokenizer.nextToken();
            type = (int) tokenizer.nval;
            tokenType = tokenizer.nextToken();
            arg1 = (double) tokenizer.nval;
            tokenType = tokenizer.nextToken();
            arg2 = (double) tokenizer.nval;
            tokenType = tokenizer.nextToken();
            arg3 = (double) tokenizer.nval;
            tokenType = tokenizer.nextToken();
            arg4 = (double) tokenizer.nval;
          }

          public void applyHistoryItem() {
                TwoDArray temp;
                switch (type) {
                    case 1:     // add notch
                        temp = new TwoDArray(result);
                        result = NotchFilter.notch(temp,(int)arg1,(int)arg2);
                        break;
                    case 2:     // add high pass
                        temp = new TwoDArray(result);
                        result = FreqFilter.filter(temp,false,(int)arg1);
                        break;
                    case 3:     // add low pass
                        temp = new TwoDArray(result);
                        result = FreqFilter.filter(temp,true,(int)arg1);
                        break;
                    case 4:     // add gaussian
                        temp = new TwoDArray(result);
                        result = Gaussian.smooth(temp,(int)arg1,(int)arg2,arg3);
                        break;
                    case 5:     // remove area
                        actual_remove((int)arg1,(int)arg2,(int)arg3,(int)arg4);
                        break;
                    case 6:     // keep area
                        actual_keep((int)arg1,(int)arg2,(int)arg3,(int)arg4);
                        break;
                    default:
     JOptionPane.showMessageDialog(null,("History item type error: "+type),("Error!"), JOptionPane.WARNING_MESSAGE);
                     return;
                }
          }
  }        
  
  
  public class MaskHistory {
  
    // number of mask components added
    int historylength;
    MaskHistoryItem [] history;
    
    public MaskHistory() {
        historylength = 0;
        history = new MaskHistoryItem [50];
    }
    public void clearHistory() { historylength = 0; }

    public void addtoMaskHistory(int t, double a1, double a2, double a3, double a4) {
        history[historylength] = new MaskHistoryItem(t, a1, a2, a3, a4);
        if (historylength >= 49) {
             JOptionPane.showMessageDialog(null,("Out of history logging space"),("Error!"), JOptionPane.WARNING_MESSAGE);
        }
        else historylength++;
    }
    
    public String saveHistory() {
          String saveData = new String();
          saveData = " "+historylength;
          for (int i=0; i<historylength; i++) {
              saveData = saveData + history[i].saveMaskHistoryItem();
          }
          return saveData;
    }

    public void loadHistory(StreamTokenizer tokenizer) throws IOException {
          int tokenType;
          tokenType = tokenizer.nextToken();
          historylength= (int) tokenizer.nval;
          for (int i=0; i<historylength; i++) {
                  history[i] = new MaskHistoryItem();
                  history[i].loadMaskHistoryItem(tokenizer);
          }
    }

    public void apply_history() {
//System.out.println("HISTORY applied: "+historylength);        
          for (int i=0; i<historylength; i++) {
                  history[i].applyHistoryItem(); // add history into current mask
          }
    }
  }
  
}
