package net.joshuahughes.hipr2.lower;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.RandomNoise;


public class noise extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JLabel noiseLabel = new JLabel("Noise Type: ");
  JComboBox noisecb = new JComboBox();
  JLabel gaussLabel = new JLabel("Gaussian Noise: ");
  JLabel shotLabel = new JLabel("Shot Noise: ");
  JComboBox shotcb = new JComboBox();
  JLabel stdLabel = new JLabel("Standard Deviation ");
  JLabel probaLabel = new JLabel("Probability ");
  JTextField stdText = new JTextField("10",5);
  JTextField probaText = new JTextField("0.5",5);
  JButton applyButton = new JButton("Apply");
  RandomNoise noiseOp = new RandomNoise();
  static int number=0;
  int noisetype = 0; /* Gaussian Noise by default */
  boolean full = true;
  String type = new String("RandomNoise");

  public noise(){
  }

  public noise(JPanel panel, linkData links){
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
    if (full) {
        saveData = "true";
    }
    else {
      saveData = "false";
    }
    saveData = saveData + " " + noisetype + " " + stdText.getText() + " " + probaText.getText();
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
    String fullValue = (String) tokenizer.sval;
    if(fullValue. equals("true")){
      full = true;
      shotcb. setSelectedIndex(0);
    }
    else {
      full = false;
      shotcb. setSelectedIndex(1);
    }
    tokenType = tokenizer.nextToken();
    noisetype = (int) tokenizer.nval;
    if(noisetype == 0) {
      noisecb. setSelectedIndex(0);
    }
    else {
      noisecb. setSelectedIndex(1);
    }
    tokenType = tokenizer.nextToken();
    double stdValue = (double) tokenizer.nval;
    stdText. setText(String. valueOf(stdValue));
    tokenType = tokenizer.nextToken();
    double probValue = (double) tokenizer.nval;
    probaText. setText(String. valueOf(probValue));
                
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
    panel.setLayout(panellayout);

    panelc.gridx = 1;
    panelc.gridy = 0;
    panellayout.setConstraints(noiseLabel, panelc);
    panel.add(noiseLabel);
    noisecb.addItem("Gaussian Noise");
    noisecb.addItem("Shot Noise");
    panelc.gridx = 2;
    panelc.gridy = 0;
    panellayout.setConstraints(noisecb, panelc);
    panel.add(noisecb);
    
    panelc.gridx = 0;
    panelc.gridy = 1;
    panellayout.setConstraints(gaussLabel, panelc);
    panel.add(gaussLabel);
    panelc.gridx = 2;
    panelc.gridy = 1;
    panellayout.setConstraints(shotLabel, panelc);
    panel.add(shotLabel);
    
    panelc.gridx = 0;
    panelc.gridy = 2;
    panellayout.setConstraints(stdLabel, panelc);
    panel.add(stdLabel);
    panelc.gridx = 1;
    panelc.gridy = 2;
    panellayout.setConstraints(stdText, panelc);
    panel.add(stdText);
    stdText.setEditable(true);

    panelc.gridx = 2;
    panelc.gridy = 2;
    shotcb.addItem("Full");
    shotcb.addItem("Partial");
    panellayout.setConstraints(shotcb, panelc);
    panel.add(shotcb);
    panelc.gridx = 1;
    panelc.gridy = 3;
    panellayout.setConstraints(probaLabel, panelc);
    panel.add(probaLabel);
    panelc.gridx = 2;
    panelc.gridy = 3;
    panellayout.setConstraints(probaText, panelc);
    panel.add(probaText);
    probaText.setEditable(false);

    panelc.gridx = 1;
    panelc.gridy = 4;
    panellayout.setConstraints(applyButton, panelc);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       go();
     }});  

    noisecb.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       if (noisecb.getSelectedItem().equals("Gaussian Noise")) {
	 probaText.setEditable(false);
	 stdText.setEditable(true);
	 noisetype = 0;
       }
       else {
	 noisetype = 1; /*Shot noise  */
	 probaText.setEditable(true);
	 stdText.setEditable(false);
       }
     }});  

     shotcb.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       if (shotcb.getSelectedItem().equals("Full")) {
	 full = true;
       }
       else {
	 full = false;
       }
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
    try {
     
      if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      if (noisetype == 0) {
      output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(noiseOp.Gaussian(imageConversions.gs2pix(input1.getValues()),
      input1.getWidth(),
      input1.getHeight(),
      (new Float(stdText.getText()).floatValue()))));
      }
      else if (full == true ) {
	output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(noiseOp.ShotFull(imageConversions.gs2pix(input1.getValues()),
      input1.getWidth(),
      input1.getHeight(),
      (new Float(probaText.getText()).floatValue()))));
      }
      else  output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(noiseOp.ShotPartial(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),(new Float(probaText.getText()).floatValue()))));
      }

      System.out.println("Output width "+getOutput1().getWidth());
     
    propagate();
    }

    catch(NumberFormatException e){
      JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
    }

  }
  
}

















