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

import net.joshuahughes.hipr2.upper.Translate;


public class translate extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JComboBox wrap = new JComboBox();
  JTextField xText = new JTextField("0",5);
  JTextField yText = new JTextField("0",5);
  Translate translateOp = new Translate();
  static int number=0;
  String type = new String("Translation");

  public translate(){
  }

  public translate(JPanel panel, linkData links){
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
    saveData = xText.getText() + " " + yText.getText();
    if(wrap.getSelectedItem().equals("Wrap")){
        saveData = saveData + " true";
    }
    else{
        saveData = saveData + " false";
    }
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
    int tx = (int) tokenizer.nval;
    xText. setText(String. valueOf(tx));
    tokenType = tokenizer.nextToken();
    int ty = (int) tokenizer.nval;
    yText. setText(String. valueOf(ty));
    tokenType = tokenizer.nextToken();
    String wrapValue = (String) tokenizer.sval;
    if(wrapValue. equals("true")){
      wrap. setSelectedIndex(0);
    }
    else {
      wrap. setSelectedIndex(1);
    }
        
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
    JLabel wrapLabel = new JLabel("Wrap/No Wrap");
    JLabel xLabel = new JLabel("x");
    JLabel yLabel = new JLabel("y");
    JButton applyButton = new JButton("Apply");

    panel.setLayout(panellayout);

    panelc.gridx = 0;
    panelc.gridy = 0;
    panellayout.setConstraints(wrapLabel, panelc);
    panel.add(wrapLabel);
    panelc.gridx = 0;
    panelc.gridy = 1;
    wrap.addItem("Wrap");
    wrap.addItem("No Wrap");
    panellayout.setConstraints(wrap, panelc);
    panel.add(wrap);

    panelc.gridx = 1;
    panelc.gridy = 0;
    panellayout.setConstraints(xLabel, panelc);
    panel.add(xLabel);
    panelc.gridx = 2;
    panelc.gridy = 0;
    panellayout.setConstraints(xText, panelc);
    panel.add(xText);

    panelc.gridx = 1;
    panelc.gridy = 1;
    panellayout.setConstraints(yLabel, panelc);
    panel.add(yLabel);
    panelc.gridx = 2;
    panelc.gridy = 1;
    panellayout.setConstraints(yText, panelc);
    panel.add(yText);

    panelc.gridx = 1;
    panelc.gridy = 2;
    panellayout.setConstraints(applyButton, panelc);
    panel.add(applyButton);

    applyButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       go();
     }});

    parameters.getContentPane().add(panel);
    parameters.pack();
    parameters.setVisible(false);
  }

  public void go(){
    
    System.out.println(name);
    
      if(getInput1()!=null){
	try {
          System.out.println("Input Width 1 "+getInput1().getWidth());
          if (wrap.getSelectedItem().equals("Wrap")) {
	    output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(translateOp.translate_image(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),(new Integer(xText.getText()).intValue()),(new Integer(yText.getText()).intValue()),true)));}
          else 
            output1 = new image1DInt(input1.getWidth(),input1.getHeight(),imageConversions.pix2gs(translateOp.translate_image(imageConversions.gs2pix(input1.getValues()),input1.getWidth(),input1.getHeight(),(new Integer(xText.getText()).intValue()),(new Integer(yText.getText()).intValue()),false)));
	}
	catch(NumberFormatException e){
          JOptionPane.showMessageDialog(null,("Invalid Value for X or Y  Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
	}
	
        System.out.println("Output Width 1 "+getOutput1().getWidth());
        propagate();
      }
  }
}
