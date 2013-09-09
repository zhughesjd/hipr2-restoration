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

import net.joshuahughes.hipr2.upper.Scale;


public class scale extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JComboBox method;
  JComboBox direction;
  JComboBox scale;
  Scale scaleOp = new Scale();
  static int number=0;
  String type = new String("Scaling");
  
  public scale(){
  }

  public scale(JPanel panel, linkData links){
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
    if(method.getSelectedItem().equals("Selection/Replication")){
        saveData = "true";
    }
    else{
        saveData = "false";
    }
    if(direction.getSelectedItem().equals("Shrink")){
        saveData = saveData + " true";
    }
    else{
        saveData = saveData + " false";
    }
    saveData = saveData + " " + scale.getSelectedItem();
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
    String repValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    String shrinkValue = (String) tokenizer.sval;
    tokenType = tokenizer.nextToken();
    int scalevalue = (int) tokenizer.nval;

    if(repValue. equals("true")){
      method. setSelectedIndex(0);
    }
    else {
      method. setSelectedIndex(1);
    }
    if(shrinkValue. equals("true")){
      direction. setSelectedIndex(0);
    }
    else {
      direction. setSelectedIndex(1);
    }
    switch( scalevalue ) {
        case 1: scale.setSelectedIndex(0); break;
        case 2: scale.setSelectedIndex(1); break;
        case 3: scale.setSelectedIndex(2); break;
        case 4: scale.setSelectedIndex(3); break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid scale loaded: "+scalevalue),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
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

    /* Parameters */
    parameters = new JFrame(name);
    panel = new JPanel();
    JLabel methodLabel = new JLabel("Method");
    method = new JComboBox();
    JLabel directionLabel = new JLabel("Direction");
    direction = new JComboBox();
    JLabel scaleLabel = new JLabel("Scale");
    scale = new JComboBox();
    JButton applyButton = new JButton("Apply");
    
    
    /* Placement of the parameters in the window */

   panel.setLayout(panellayout);
  
   panelc. gridx = 0;
   panelc. gridy = 0;
   panellayout. setConstraints(methodLabel, panelc);
   panel.add(methodLabel);
   panelc. gridx = 0;
   panelc. gridy = 1;
   method.addItem("Selection/Replication");
   method.addItem("Interpolation");
   panellayout. setConstraints(method, panelc);
   panel.add(method);
  

   panelc.gridx = 1;
   panelc.gridy = 0;
   panellayout.setConstraints(directionLabel, panelc);
   panel.add(directionLabel);
   panelc.gridx = 1;
   panelc.gridy = 1;
   direction.addItem("Shrink");
   direction.addItem("Expand");
   panellayout.setConstraints(direction, panelc);
   panel.add(direction);

   panelc.gridx = 2;
   panelc.gridy = 0;
   panellayout.setConstraints(scaleLabel, panelc);
   panel.add(scaleLabel);
   panelc.gridx = 2;
   panelc.gridy = 1;
   scale.addItem("1");
   scale.addItem("2");
   scale.addItem("3");
   scale.addItem("4");
   panellayout.setConstraints(scale,panelc);
   panel.add(scale);

   panelc.gridx = 0;
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

    int scalevalue;
    
    /* To get the scalefactor */
    if (scale.getSelectedItem().equals("2")) {
      scalevalue = 2;}
      else if (scale.getSelectedItem().equals("3")) {
	scalevalue = 3;}
	else if (scale.getSelectedItem().equals("4")) {
	  scalevalue = 4;
	}
    else scalevalue = 1;

    System.out.println(name);
   
      if(getInput1()!=null){
      System.out.println("Input Width 1 "+getInput1().getWidth());
      if (method.getSelectedItem().equals("Selection/Replication")) {
        if (direction.getSelectedItem().equals("Shrink")){
	  output1 = new image1DInt(
                (input1.getWidth()/scalevalue),
		(input1.getHeight()/scalevalue),
		imageConversions.pix2gs(
                    scaleOp.shrink_sample(
                        imageConversions.gs2pix(input1.getValues()),
		        input1.getWidth(),input1.getHeight(),scalevalue)));}
        else 
	  output1 = new image1DInt(
                (input1.getWidth()*scalevalue),
		(input1.getHeight()*scalevalue),
		imageConversions.pix2gs(
                    scaleOp.grow_replicate(
                        imageConversions.gs2pix(input1.getValues()),
	        	input1.getWidth(),input1.getHeight(),scalevalue)));
      }
      else  // interpolation case 
	if (direction.getSelectedItem().equals("Shrink")){
	  output1 = new image1DInt(
                (input1.getWidth()/scalevalue),
		(input1.getHeight()/scalevalue),
		imageConversions.pix2gs(
                    scaleOp.shrink_average(
                        imageConversions.gs2pix(input1.getValues()),
        		input1.getWidth(),input1.getHeight(),scalevalue)));}
	else 
	  output1 = new image1DInt(
                (input1.getWidth()*scalevalue),
		(input1.getHeight()*scalevalue),
		imageConversions.pix2gs(
                    scaleOp.grow_interpolate(
                        imageConversions.gs2pix(input1.getValues()),
		        input1.getWidth(),input1.getHeight(),scalevalue)));
       System.out.println("Output Width 1 "+getOutput1().getWidth());
      }
    propagate();
  }
}
