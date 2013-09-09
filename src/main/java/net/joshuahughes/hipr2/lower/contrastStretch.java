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

import net.joshuahughes.hipr2.upper.ContrastStretch;


public class contrastStretch extends operator1DInt{

  GridBagConstraints panelc = new GridBagConstraints();
  GridBagLayout panellayout = new GridBagLayout();
  JTextField cutoff;
  JTextField lowbox;
  JTextField highbox;
  JTextField hipercent;
  JTextField lopercent;
  JComboBox choice;
  int low;
  int high;
  float cut;
  float hiper;
  float loper;
  int choicetype;
  
  ContrastStretch contrastOp = new ContrastStretch();
  static int number=0;
  String type = new String("ContrastStretching");

  public contrastStretch(){
  }

  public contrastStretch(JPanel panel, linkData links){
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
    saveData = " " + choicetype + " " + lowbox.getText() + " " + highbox.getText()
        + " " + lopercent.getText() + " " + hipercent.getText() + " " + cutoff.getText();
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
    choicetype = (int) tokenizer.nval;
    switch( choicetype ) {
        case 0: 
                choice.setSelectedIndex(0);
        	hipercent. setEditable(false);
	        lopercent. setEditable(false);
        	cutoff. setEditable(false);
        case 1:
                choice.setSelectedIndex(1);
        	cutoff. setEditable(false);
	        hipercent. setEditable(true);
        	lopercent. setEditable(true);
                break;
        case 2: 
                choice.setSelectedIndex(2);
        	cutoff. setEditable(true);
	        hipercent. setEditable(false);
        	lopercent. setEditable(false);
                break;
        default:
          JOptionPane.showMessageDialog(null,("Invalid equalization option loaded: "+choicetype),("Error!"), JOptionPane.WARNING_MESSAGE);
          return;
    }
    tokenType = tokenizer.nextToken();
    low = (int) tokenizer.nval;
    lowbox.setText(String. valueOf(low));
    tokenType = tokenizer.nextToken();
    high = (int) tokenizer.nval;
    highbox.setText(String. valueOf(high));

    tokenType = tokenizer.nextToken();
    loper = (float) tokenizer.nval;
    lopercent.setText(String. valueOf(loper));
    tokenType = tokenizer.nextToken();
    hiper = (float) tokenizer.nval;
    hipercent.setText(String. valueOf(hiper));
    tokenType = tokenizer.nextToken();
    cut = (float) tokenizer.nval;
    cutoff.setText(String. valueOf(cut));
    
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

   /* Components of the parameters'window */
   parameters = new JFrame(name);
   panel = new JPanel();
   JLabel stretch_type = new JLabel("Stretching Method");
   choice = new JComboBox();
   JLabel cutofflabel = new JLabel("Cutoff Value");
   cutoff= new JTextField(10);
   JLabel lowlabel = new JLabel("Lower Limit - A");
   lowbox = new JTextField(5);
   JLabel highlabel = new JLabel("Upper Limit - B");
   highbox = new JTextField(5);
   JLabel hiperlabel = new JLabel("Upper Percentage Boundary");
   hipercent = new JTextField(5);
   JLabel loperlabel = new JLabel("Lower Percentage Boundary");
   lopercent = new JTextField(5);
   JButton applybutton = new JButton("Apply");

   /* Placement of these components in the window */

   panel.setLayout(panellayout);
  
   panelc. gridx = 0;
   panelc. gridy = 0;
   panellayout. setConstraints(stretch_type, panelc);
   panel.add(stretch_type);
   panelc. gridx = 1;
   panelc. gridy = 0;
   choice. addItem("Normal");
   choice. addItem("Percentile");
   choice. addItem("Cutoff");
   panellayout. setConstraints(choice, panelc);
   panel.add(choice);
  
   panelc. gridx = 0;
   panelc. gridy = 1;
   panellayout. setConstraints(lowlabel, panelc);
   panel.add(lowlabel);
   panelc. gridx = 1;
   panelc. gridy = 1;
   panellayout. setConstraints(lowbox, panelc);   
   panel.add(lowbox);
   lowbox. setText("0");

   panelc. gridx = 0;
   panelc. gridy = 2;
   panellayout. setConstraints(highlabel, panelc);   
   panel.add(highlabel);
   panelc. gridx = 1;
   panelc. gridy = 2;
   panellayout. setConstraints(highbox, panelc);   
   panel.add(highbox);
   highbox. setText("255");

   panelc. gridx = 0;
   panelc. gridy = 3;
   panellayout. setConstraints(hiperlabel, panelc);
   panel.add(hiperlabel);
   panelc. gridx = 1;
   panelc. gridy = 3;
   panellayout. setConstraints(hipercent, panelc);
   panel.add(hipercent);
   hipercent. setText("0.95");
   hipercent. setEditable(false);

   panelc. gridx = 0;
   panelc. gridy = 4;
   panellayout. setConstraints(loperlabel, panelc);
   panel.add(loperlabel);
   panelc. gridx = 1;
   panelc. gridy = 4;
   panellayout. setConstraints(lopercent, panelc);   
   panel.add(lopercent);
   lopercent. setText("0.05");
   lopercent. setEditable(false);

   panelc. gridx = 0;
   panelc. gridy = 5;
   panellayout. setConstraints(cutofflabel, panelc);
   panel.add(cutofflabel);
   panelc. gridx = 1;
   panelc. gridy = 5;
   panellayout. setConstraints(cutoff, panelc);
   panel.add(cutoff);
   cutoff. setText("0.02");
   cutoff. setEditable(false);

   panelc. gridx = 0;
   panelc. gridy = 6;
   panellayout. setConstraints(applybutton, panelc);   
   panel.add(applybutton);

   applybutton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       go();
     }});
   
   choice.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e){
       choicecl(e);
     }});

   parameters.getContentPane().add(panel);
   parameters.pack();
   parameters.setVisible(false);
  }


  public void choicecl(ActionEvent e) {

      JComboBox cb = (JComboBox)e.getSource();
      
      if( cb.getSelectedItem().equals("Normal")){
	hipercent. setEditable(false);
	lopercent. setEditable(false);
	cutoff. setEditable(false);
	choicetype = 0;
      }
      else if(cb.getSelectedItem().equals("Percentile")){
	cutoff. setEditable(false);
	hipercent. setEditable(true);
	lopercent. setEditable(true);
	choicetype = 1;
      }
      else if(cb.getSelectedItem().equals("Cutoff")){
	cutoff. setEditable(true);
	hipercent. setEditable(false);
	lopercent. setEditable(false);
	choicetype = 2;
      }
  }

  public void go(){
    
    System.out.println(name);
    try {
      low = new Integer(lowbox.getText()).intValue();
      high = new Integer(highbox.getText()).intValue();
      if (low < 0 || low > 255 || high < 0 || high > 255 ) {
	JOptionPane.showMessageDialog(null,("Lower limit and Higher limit must lie between 0 and 255"),("Error!"), JOptionPane.WARNING_MESSAGE);
	return;
      }
      if (low > high ) {
	JOptionPane.showMessageDialog(null,("Lower limit must be < Higher limit"),("Error!"), JOptionPane.WARNING_MESSAGE); 
	return;
      }
    }
    catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid Limits specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
  	  return;
    }
    
    if(getInput1()!=null){

      System.out.println("Input Width 1 "+getInput1().getWidth());
     
      
      if( choice.getSelectedItem().equals("Normal")){
          output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    contrastOp.normal_stretch(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        high, 
                        low)));
      }
      else if(choice.getSelectedItem().equals("Percentile")){
	try {
	  hiper = new Float(hipercent.getText()).floatValue();
	  loper = new Float(lopercent.getText()).floatValue();
	  if ( hiper > 1 || hiper <0 || loper > 1 || loper < 0 ) {
	    JOptionPane.showMessageDialog(null,("Percentage Boundaries must lie between 0 and 1"),("Error!"), JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	  if ( hiper < loper ) {
	    JOptionPane.showMessageDialog(null,("Lower percentage boundary must be < to Higher percentage boundary"),("Error!"), JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	}
	catch(NumberFormatException e){
  	    JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
	    return;
	}
      
        output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    contrastOp.percentile_stretch(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        hiper,
                        loper,
                        high,
                        low)));
      }
      else if(choice.getSelectedItem().equals("Cutoff")){
	try {
	  cut = new Float(cutoff.getText()).floatValue();
	}
	catch(NumberFormatException e){
	  JOptionPane.showMessageDialog(null,("Invalid Parameters Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
	  return;
	}

        output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    contrastOp.cutoff_stretch(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        cut,
                        high,
                        low)));
      }
      System.out.println("Output Width 1 "+getOutput1().getWidth());
    }
    propagate();
  }
}
