package net.joshuahughes.hipr2.lower;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.joshuahughes.hipr2.upper.FFT;
import net.joshuahughes.hipr2.upper.ImageMods;
import net.joshuahughes.hipr2.upper.InverseFFT;
import net.joshuahughes.hipr2.upper.TwoDArray;


public class fourier extends operator1DInt{


  JPanel fftPanel = new JPanel();
  JPanel ifftPanel = new JPanel();
  JComboBox fftchoice = new JComboBox();
  JComboBox ifftchoice = new JComboBox();
  
  
  int fftmod;
  int ifftmod;
  JPanel filterPanel = new JPanel();
  int h;
  int w;
  TwoDArray fres;
  TwoDArray ires;
  double [][]fouriers;
  double [][]ifouriers;
  double [][]fmask;
  double [][]imask;
  FFT fourierOp;
  InverseFFT inverse;
  static int number=0;
  String type = new String("Fourier");

  public fourier(){
  }

  public fourier(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,2,2);
    box.getIn2().setText("FFTMask");
    box.getOut1().setText("FFT");
    box.getOut2().setText("IFFT");
  }
  /**
   * Loads the required parameters (number of iterations)
   * from the input stream, so the operator can be recreated in an 
   * identical state to when it was saved.
   * @param tokenizer the input stream split into tokens
   * @throws IOException if error occurs during token retrieval
   */
  public void loadParameters(StreamTokenizer tokenizer) throws IOException{

    int tokenType;
    tokenType = tokenizer.nextToken();
    fftmod = (int) tokenizer.nval;
    switch( fftmod ) {
        case 0: fftchoice.setSelectedIndex(0); break;
        case 1: fftchoice.setSelectedIndex(1); break;
        case 2: fftchoice.setSelectedIndex(2); break;
        case 3: fftchoice.setSelectedIndex(3); break;
        case 4: fftchoice.setSelectedIndex(4); break;
        case 5: fftchoice.setSelectedIndex(5); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid FFT display option"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
    tokenType = tokenizer.nextToken();
    ifftmod = (int) tokenizer.nval;
    switch( ifftmod ) {
        case 0: ifftchoice.setSelectedIndex(0); break;
        case 1: ifftchoice.setSelectedIndex(1); break;
        case 2: ifftchoice.setSelectedIndex(2); break;
        case 3: ifftchoice.setSelectedIndex(3); break;
        case 4: ifftchoice.setSelectedIndex(4); break;
        case 5: ifftchoice.setSelectedIndex(5); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid FFT display option"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }

    //Repack the components in the interface
    parameters.pack();
    parameters.setVisible(false);
  }
  /**
   * Returns a String representing all the parameters for this operator
   * for the purpose of saving the system setup (and being able to load
   * it again at a future date).
   */
  public String saveParameters(){
    String saveData = new String();
    
    //Add any parameters to the string
    saveData = " " + fftmod + " " + ifftmod;
    return saveData;
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
    GridBagLayout panelLayout = new GridBagLayout();
    GridBagConstraints panelc = new GridBagConstraints();
    panel.setLayout(panelLayout);

    /* Buttons for the FFT */

    fftchoice.addItem("Magnitude");
    fftchoice.addItem("Magnitude log");
    fftchoice.addItem("Phase angle");
    fftchoice.addItem("Real");
    fftchoice.addItem("Real log");
    fftchoice.addItem("Imaginary");
    fftchoice.addItem("imaginary log");
    
    JLabel fftLabel = new JLabel("FFT Display");

    fftPanel.setLayout(new BorderLayout());
    fftPanel.add(fftLabel, "North");
    fftPanel.add(fftchoice,"South");
  
    
    /* Buttons for the inverse FFT */
 
    ifftchoice.addItem("Magnitude");
    ifftchoice.addItem("Magnitude log");
    ifftchoice.addItem("Phase angle");
    ifftchoice.addItem("Real");
    ifftchoice.addItem("Real log");
    ifftchoice.addItem("Imaginary");
    ifftchoice.addItem("imaginary log");

    JLabel ifftLabel = new JLabel(" Inverse FFT Display");
    
    ifftPanel.setLayout(new BorderLayout());
    ifftPanel.add(ifftLabel, "North");
    ifftPanel.add(ifftchoice,"South");
   
    panelc.gridx = 0;
    panelc.gridy = 0;
    panelLayout.setConstraints(fftPanel,panelc);
    panel.add(fftPanel);
    panelc.gridx = 1;
    panelc.gridy = 0;
    panelLayout.setConstraints(ifftPanel,panelc);
    panel.add(ifftPanel);


    fftchoice.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	if(cb.getSelectedItem().equals("Magnitude") ){
	  fftmod = 0;
	  display();
	}
	  else if (cb.getSelectedItem().equals("Magnitude log")){
	    fftmod = 1;
	     display();
	  }
	    else if (cb.getSelectedItem().equals("Phase angle")){
	      fftmod = 2;
	      display();
	    }
	      else if (cb.getSelectedItem().equals("Real")){
		fftmod = 3;
		 display();
	      }
		else if(cb.getSelectedItem().equals("Real log")){
		  fftmod = 4;
		   display();
		}
		  else if(cb.getSelectedItem().equals("Imaginary")){
		    fftmod = 5;
		     display();
		  }
		    else {
		      fftmod = 6;
		       display();
		    }
      }});

    ifftchoice.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	JComboBox cb = (JComboBox)e.getSource();
	if(cb.getSelectedItem().equals("Magnitude") ){
	  ifftmod = 0;
	   display();
	}
	  else if (cb.getSelectedItem().equals("Magnitude log")){
	    ifftmod = 1;
	     display();
	  }
	    else if (cb.getSelectedItem().equals("Phase angle")){
	      ifftmod = 2;
	       display();
	    }
	      else if (cb.getSelectedItem().equals("Real")){
		ifftmod = 3;
		 display();
	      }
		else if(cb.getSelectedItem().equals("Real log")){
		  ifftmod = 4;
		   display();
		}
		  else if(cb.getSelectedItem().equals("Imaginary")){
		    ifftmod = 5;
		     display();
		  }
		    else {ifftmod = 6;
		     display();}
      }});

   


   
    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    /**
     *Most of this function is left blank at the moment. When the operator
     *is ready to be added to the tableau, this function will contain the code
     *to run the operator that was written in the hipr package. One of the already
     *implemented operators is left here as a guide, however some operators could 
     *look quite different.
     */
    
    System.out.println(name);
    if(getInput1()!=null && getInput2()!=null){
       
      System.out.println("Input Width 1 "+getInput1().getWidth());
      fourierOp = new FFT(input1.getValues(),input1.getWidth(),input1.getHeight());
      inverse = new InverseFFT();
      w = fourierOp.output.width;
      h = fourierOp.output.height;
      fres = new TwoDArray(w,h);
      ires = new TwoDArray(w,h);
      fouriers = new double[7][w*h];
      ifouriers = new double[7][w*h];
      fres = fourierOp.intermediate;
      ires = inverse.transform(fourierOp.intermediate);
      try {
	  if(box.links.getLink(this,box.getIn2()).linkFrom.getType().equals("FFTMask")) {
	      multiply();
	  }
	  else {
	      JOptionPane.showMessageDialog(null,("You should use a mask"),("Error!"), JOptionPane.WARNING_MESSAGE);
	      return;
	  }
      } catch (NullPointerException e) {
	  JOptionPane.showMessageDialog(null,("You should use a FFT Mask, not an image or output from an operator in FFTMask"),("Error!"), JOptionPane.WARNING_MESSAGE);
      }

      fouriers[0] = fres.DCToCentre(fres.getMagnitude());
      fouriers[1] = ImageMods.logs(fouriers[0]);
      fouriers[2] = ImageMods.abs(fres.DCToCentre(fres.getPhase()));
      fouriers[3] = ImageMods.abs(fres.DCToCentre(fres.getReal()));
      fouriers[4] = ImageMods.logs(fouriers[3]);
      fouriers[5] = ImageMods.abs(fres.DCToCentre(fres.getImaginary()));
      fouriers[6] = ImageMods.logs(fouriers[5]);
      ifouriers[0] = ires.getMagnitude();
      ifouriers[1] = ImageMods.logs(ifouriers[0]);  
      ifouriers[2] = ImageMods.abs(ires.getPhase());
      ifouriers[3] = ImageMods.abs(ires.getReal());
      ifouriers[4] = ImageMods.logs(ifouriers[3]);
      ifouriers[5] = ImageMods.abs(ires.getImaginary());
      ifouriers[6] = ImageMods.logs(ifouriers[5]);
      output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(ImageMods.toPixels(fouriers[fftmod])));
      output2 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(ImageMods.toPixels(ImageMods.allPositive(ifouriers[ifftmod]))));
      System.out.println("Output width "+getOutput1().getWidth());
      propagate();
     }
  }

  /**
   * Updates the outputs when the user chooses a different output from
   * the parameters popup
   */
  public void display (){
    if(getInput1()!=null){ 
       output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(ImageMods.toPixels(fouriers[fftmod])));
        output2 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(ImageMods.toPixels(ImageMods.allPositive(ifouriers[ifftmod]))));
	propagate();
    }
  }

 
  /**
   * Applies the mask from the second input to the FFT and calculates
   * the new inverse transform.
   */
  public void multiply() {
   
      System.out.println("width "+w+" height "+h+" length "+input2.getValues().length);

    TwoDArray in2 = new TwoDArray(input2.getValues(),w,h);
    TwoDArray temp = new TwoDArray(fres);
    
    // translate mask before applying it
 
    double [] tr = new double[w*h];
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
	tr[j*w+i] = (double)input2.getValues()[j*w+i];
      }
    }
    tr = in2.DCToCentre(tr);
    int [] ti = new int[w*h];
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
	ti[j*w+i] = (int) tr[j*w+i];
      }
    }
    in2 = new TwoDArray(ti,w,h);

    // apply mask on the FFT transform
    
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
	// if mask [i] is 255 value doesn't change, if 0 value is 0, else
	// value is mask[i]*value
	if (in2.values[i][j].real != (double)255 ) {
	  temp.values[i][j] = temp.values[i][j].cMult(temp.values[i][j],in2.values[i][j]);
	}
      }
    }
    fres = temp;
    // calculate the inverse FFT 
    ires = inverse.transform(fres);
  }
  

}



