package net.joshuahughes.hipr2.lower;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.joshuahughes.hipr2.upper.Laplacian;


public class laplacian extends operator1DInt{

  JLabel l;
  JPanel  p;
  JLabel scaleLabel = new JLabel("Output Display Scale Value");
  JTextField scaleText = new JTextField("1.0",5);
  JLabel offsetLabel = new JLabel("Output Display Offset Value");
  JTextField offsetText = new JTextField("0.0",5);
  JButton applyButton = new JButton("Apply");
    ButtonGroup group = new ButtonGroup();
    JRadioButton type1 = new JRadioButton("1");
    JRadioButton type2 = new JRadioButton("2");
    JRadioButton type3 = new JRadioButton("3");

  Laplacian laplacianOp = new Laplacian();
  static int number=0;
  int t = 1;    // kernel type (1,2,3)
  String type = new String("Laplacian");

  public laplacian(){
  }

  public laplacian(JPanel panel, linkData links){
    ++number;
    setName(type+"_"+number);
    setParameters();
    setType(type);
    setBox(panel,links,1,1);
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
    t = (int) tokenizer.nval;
    switch (t) {
        case 1:     type1.setSelected(true); break;
        case 2:     type2.setSelected(true); break;
        case 3:     type3.setSelected(true); break;
        default: 
                JOptionPane.showMessageDialog(null,("Laplacian: invalid kernel type loaded"),("Error!"), JOptionPane.WARNING_MESSAGE);
                return;
    }
    tokenType = tokenizer.nextToken();
    float scale = (float) tokenizer.nval;
    scaleText.setText(String. valueOf(scale));
    tokenType = tokenizer.nextToken();
    float offset = (float) tokenizer.nval;
    offsetText.setText(String. valueOf(offset));

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
    saveData = t + " " + scaleText.getText() + " " + offsetText.getText();
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
    p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.setOpaque(false);
    type1.setOpaque(false);
    type2.setOpaque(false);
    type3.setOpaque(false);
    group.add(type1);
    group.add(type2);
    group.add(type3);
    type1.setSelected(true);

    /* Kernel 1 */
    JPanel k1 = new JPanel();
    GridBagConstraints k1c = new GridBagConstraints();
    GridBagLayout k1layout = new GridBagLayout();
    k1.setLayout(k1layout);
    JLabel a1 = new JLabel(" 0  ");
     JLabel a2 = new JLabel(" 1  ");
     JLabel a3 = new JLabel(" 0  ");
     JLabel a4 = new JLabel(" 1  ");
     JLabel a5 = new JLabel(" -4 ");
     JLabel a6 = new JLabel(" 1  ");
     JLabel a7 = new JLabel(" 0  ");
     JLabel a8 = new JLabel(" 1  ");
     JLabel a9 = new JLabel(" 0  ");
     
    k1c.gridx = 0;
    k1c.gridy = 0;
    k1layout.setConstraints(a1, k1c);
    k1.add(a1);
    k1c.gridx = 1;
    k1c.gridy = 0;
    k1layout.setConstraints(a2, k1c);
    k1.add(a2);
    k1c.gridx = 2;
    k1c.gridy = 0;
    k1layout.setConstraints(a3, k1c);
    k1.add(a3);
    k1c.gridx = 0;
    k1c.gridy = 1;
    k1layout.setConstraints(a4, k1c);
    k1.add(a4);
    k1c.gridx = 1;
    k1c.gridy = 1;
    k1layout.setConstraints(a5, k1c);
    k1.add(a5);
    k1c.gridx = 2;
    k1c.gridy = 1;
    k1layout.setConstraints(a6, k1c);
    k1.add(a6);
    k1c.gridx = 0;
    k1c.gridy = 2;
    k1layout.setConstraints(a7, k1c);
    k1.add(a7);
    k1c.gridx = 1;
    k1c.gridy = 2;
    k1layout.setConstraints(a8, k1c);
    k1.add(a8);
    k1c.gridx = 2;
    k1c.gridy = 2;
    k1layout.setConstraints(a9, k1c);
    k1.add(a9);

 /* Kernel 2 */
    JPanel k2 = new JPanel();
    GridBagConstraints k2c = new GridBagConstraints();
    GridBagLayout k2layout = new GridBagLayout();
    k2.setLayout(k2layout);
    JLabel b1 = new JLabel(" 1  ");
     JLabel b2 = new JLabel(" 1  ");
     JLabel b3 = new JLabel(" 1  ");
     JLabel b4 = new JLabel(" 1  ");
     JLabel b5 = new JLabel(" -8 ");
     JLabel b6 = new JLabel(" 1  ");
     JLabel b7 = new JLabel(" 1  ");
     JLabel b8 = new JLabel(" 1  ");
     JLabel b9 = new JLabel(" 1  ");
     
    k2c.gridx = 0;
    k2c.gridy = 0;
    k2layout.setConstraints(b1, k2c);
    k2.add(b1);
    k2c.gridx = 1;
    k2c.gridy = 0;
    k2layout.setConstraints(b2, k2c);
    k2.add(b2);
    k2c.gridx = 2;
    k2c.gridy = 0;
    k2layout.setConstraints(b3, k2c);
    k2.add(b3);
    k2c.gridx = 0;
    k2c.gridy = 1;
    k2layout.setConstraints(b4, k2c);
    k2.add(b4);
    k2c.gridx = 1;
    k2c.gridy = 1;
    k2layout.setConstraints(b5, k2c);
    k2.add(b5);
    k2c.gridx = 2;
    k2c.gridy = 1;
    k2layout.setConstraints(b6, k2c);
    k2.add(b6);
    k2c.gridx = 0;
    k2c.gridy = 2;
    k2layout.setConstraints(b7, k2c);
    k2.add(b7);
    k2c.gridx = 1;
    k2c.gridy = 2;
    k2layout.setConstraints(b8, k2c);
    k2.add(b8);
    k2c.gridx = 2;
    k2c.gridy = 2;
    k2layout.setConstraints(b9, k2c);
    k2.add(b9);
  

 /* Kernel 3 */
    JPanel k3 = new JPanel();
    GridBagConstraints k3c = new GridBagConstraints();
    GridBagLayout k3layout = new GridBagLayout();
    k3.setLayout(k3layout);
    JLabel c1 = new JLabel(" -0.5 ");
     JLabel c2 = new JLabel(" 2  ");
     JLabel c3 = new JLabel(" -0.5 ");
     JLabel c4 = new JLabel(" 2  ");
     JLabel c5 = new JLabel(" -6 ");
     JLabel c6 = new JLabel(" 2  ");
     JLabel c7 = new JLabel(" -0.5 ");
     JLabel c8 = new JLabel(" 2  ");
     JLabel c9 = new JLabel(" -0.5 ");
     
    k3c.gridx = 0;
    k3c.gridy = 0;
    k3layout.setConstraints(c1, k3c);
    k3.add(c1);
    k3c.gridx = 1;
    k3c.gridy = 0;
    k3layout.setConstraints(c2, k3c);
    k3.add(c2);
    k3c.gridx = 2;
    k3c.gridy = 0;
    k3layout.setConstraints(c3, k3c);
    k3.add(c3);
    k3c.gridx = 0;
    k3c.gridy = 1;
    k3layout.setConstraints(c4, k3c);
    k3.add(c4);
    k3c.gridx = 1;
    k3c.gridy = 1;
    k3layout.setConstraints(c5, k3c);
    k3.add(c5);
    k3c.gridx = 2;
    k3c.gridy = 1;
    k3layout.setConstraints(c6, k3c);
    k3.add(c6);
    k3c.gridx = 0;
    k3c.gridy = 2;
    k3layout.setConstraints(c7, k3c);
    k3.add(c7);
    k3c.gridx = 1;
    k3c.gridy = 2;
    k3layout.setConstraints(c8, k3c);
    k3.add(c8);
    k3c.gridx = 2;
    k3c.gridy = 2;
    k3layout.setConstraints(c9, k3c);
    k3.add(c9);

    l = new JLabel("Kernel Selection");

    type1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=1;
      }
    });
    type2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=2;
      }
    });
    type3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        t=3;
      }
    });

   
    p.add(l);
    p.add(type1);
    p.add(k1);
    p.add(type2);
    p.add(k2);
    p.add(type3);
    p.add(k3);
    panel.add(p);
    panel.add(scaleLabel);
    panel.add(scaleText);
    panel.add(offsetLabel);
    panel.add(offsetText);
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
          output1 = new image1DInt(
                input1.getWidth(),
                input1.getHeight(),
                imageConversions.pix2gs(
                    laplacianOp.laplacian_image(
                        imageConversions.gs2pix(input1.getValues()),
                        input1.getWidth(),
                        input1.getHeight(),
                        t,
                        (new Double(scaleText.getText()).doubleValue()),
                        (new Double(offsetText.getText()).doubleValue()))));
          System.out.println("Output width "+getOutput1().getWidth());
          propagate();
	}
	catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,("Invalid Scale or Offset Specified"),("Error!"), JOptionPane.WARNING_MESSAGE);
                return;
        }
    }
  }
}
