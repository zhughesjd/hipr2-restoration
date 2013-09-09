package net.joshuahughes.hipr2.lower;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.LineDetector;


public class lineDetector extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JLabel cblabel = new JLabel("Select which lines to view ");
  JCheckBox cb1 = new JCheckBox("Horizontal Lines", true);
  JCheckBox cb2 = new JCheckBox("45 Degree Lines", true);
  JCheckBox cb3 = new JCheckBox("135 Degree Lines", true);
  JCheckBox cb4 = new JCheckBox("Vertical Lines", true);
//  JLabel applythresh = new JLabel("Apply Thresholding ");
//  JComboBox applythreshcb = new JComboBox();
//  JLabel threshvalue = new JLabel("Threshold Value ");
//  JTextField threshText = new JTextField("0",4);
  JLabel scale = new JLabel("Scale ");
  JTextField scaleText = new JTextField("1.0",5);
  JLabel offset = new JLabel("Offset ");
  JTextField offsetText = new JTextField("0.0",5);
  JButton applyButton = new JButton("Apply ");
  
  LineDetector lineDetOp = new LineDetector();
  static int number=0;
//  boolean choice;
  int lines = 15;
  String type = new String("LineDetector");

  public lineDetector(){
  }

  public lineDetector(JPanel panel, linkData links){
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
    saveData = " " + lines + " " + scaleText.getText() + " " + offsetText.getText();
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
    lines = (int) tokenizer.nval;
    if ((lines & 1) != 0) {cb1.setSelected(true); }
    else {cb1.setSelected(false); }
    if ((lines & 2) != 0) {cb2.setSelected(true); }
    else {cb2.setSelected(false); }
    if ((lines & 4) != 0) {cb3.setSelected(true); }
    else {cb3.setSelected(false); }
    if ((lines & 8) != 0) {cb4.setSelected(true); }
    else {cb4.setSelected(false); }

    tokenType = tokenizer.nextToken();
    double scale = (double) tokenizer.nval;
    scaleText. setText(String. valueOf(scale));

    tokenType = tokenizer.nextToken();
    double offset = (double) tokenizer.nval;
    offsetText. setText(String. valueOf(offset));

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

    panelc.gridx = 0;
    panelc.gridy = 0;
    panellayout.setConstraints(cblabel, panelc);
    panel.add(cblabel);    
//    applythreshcb.addItem("No");
//    applythreshcb.addItem("Yes");
    panelc.gridx = 0;
    panelc.gridy = 1;
    panellayout.setConstraints(cb1, panelc);
    panel.add(cb1);
    panelc.gridx = 0;
    panelc.gridy = 2;
    panellayout.setConstraints(cb2, panelc);
    panel.add(cb2);
    panelc.gridx = 0;
    panelc.gridy = 3;
    panellayout.setConstraints(cb3, panelc);
    panel.add(cb3);
    panelc.gridx = 0;
    panelc.gridy = 4;
    panellayout.setConstraints(cb4, panelc);
    panel.add(cb4);

/*
    panelc.gridx = 1;
    panelc.gridy = 0;
    panellayout.setConstraints(applythresh, panelc);
    panel.add(applythresh);   
    panelc.gridx = 1;
    panelc.gridy = 1;
    panellayout.setConstraints(applythreshcb, panelc);
    panel.add(applythreshcb);   
    panelc.gridx = 1;
    panelc.gridy = 2;
    panellayout.setConstraints(threshvalue, panelc);
    panel.add(threshvalue);
    panelc.gridx = 1;
    panelc.gridy = 3;
    panellayout.setConstraints(threshText, panelc);
    panel.add(threshText);
    threshText.setEditable(false);
*/

    panelc.gridx = 2;
    panelc.gridy = 0;
    panellayout.setConstraints(scale, panelc);
    panel.add(scale);   
    panelc.gridx = 3;
    panelc.gridy = 0;
    panellayout.setConstraints(scaleText, panelc);
    panel.add(scaleText);   
    panelc.gridx = 2;
    panelc.gridy = 1;
    panellayout.setConstraints(offset, panelc);
    panel.add(offset);   
    panelc.gridx = 3;
    panelc.gridy = 1;
    panellayout.setConstraints(offsetText, panelc);
    panel.add(offsetText);   
    panelc.gridx = 2;
    panelc.gridy = 2;
    panellayout.setConstraints(applyButton, panelc);
    panel.add(applyButton);   

    applyButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       go();
     }});  
/*
    applythreshcb.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       if (applythreshcb.getSelectedItem().equals("No")){
	 threshText.setEditable(false);
	 choice = false;}
       else {threshText.setEditable(true);
       choice = true;}
     }});    
*/

    cb1.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       	if(cb1. isSelected()){
	  lines = lines + 1;
	}
	else {
	  lines = lines - 1;
	}
     }});
     cb2.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       	if(cb2. isSelected()){
	  lines = lines + 2;
	}
	else {
	  lines = lines - 2;
	}
     }});
      cb3.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       	if(cb3. isSelected()){
	  lines = lines + 4;
	}
	else {
	  lines = lines - 4;
	}
     }});
       cb4.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       	if(cb4. isSelected()){
	  lines = lines + 8;
	}
	else {
	  lines = lines - 8;
	}
     }});

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);

    if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      try {
        output1 = new image1DInt(
            input1.getWidth(),
            input1.getHeight(),
            imageConversions.pix2gs(
                lineDetOp.apply_lineDetect(
                    imageConversions.gs2pix(input1.getValues()),
                    input1.getWidth(),
                    input1.getHeight(),
                    lines,
//                    (new Integer(threshText.getText()).intValue()),
//                    choice,
                    0,
                    false,
                    (new Float(scaleText.getText()).floatValue()),
                    (new Float(offsetText.getText()).floatValue())).image1));
        System.out.println("Output width "+getOutput1().getWidth());
        propagate();
      }
      catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Format Parameters"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
      }
    }
  }
}
