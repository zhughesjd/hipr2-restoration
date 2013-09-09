package net.joshuahughes.hipr2.upper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.color.*;

/**
 * The interface for the convolution kernel.
 *
 * @author Simon Horne.
 */
public class ConvolutionKernel{

  /**
   * Panel for inclusion in the convolution GUI.
   */
  JPanel panel;
  private JLabel label;
  private int i,j;
  /**
   * 2D array of text fields for kernel value entry and storage.
   */
  private JTextField valueField [][] = new JTextField [5][5];
  /**
   * 2D array of buttons, only [0][j] and [i][0] are displayed (top and left),
   * for selecting size of kernel.
   */
  private JButton button [][] = new JButton [6][6];
  /**
   * Width of current kernel.
   */
  private int columns = 5;
  /**
   * Height of current kernel.
   */
  private int rows = 5;

  /**
   * Checks the text boxes for values and returns a 2D array of their
   * double representations.
   *
   * @return The 2D array of the double values from the text boxes.
   */
  public double [][] getKernel(){
    double [][] value = new double [5][5];
    for(j=0;j<rows;++j){
      for(i=0;i<columns;++i){
	if(valueField[i][j].isEnabled()==true){
	  value[i][j] = 
	    Double.valueOf(valueField[i][j].getText()).doubleValue();
	}else{
	  value[i][j]=0;
	}
      }
    }
    return value;
  }

  /**
   * Gets the width of the current kernel.
   *
   * @return The width of the kernel.
   */
  public int getKernelWidth(){
    return columns;
  }

  /**
   * Gets the height of the current kernel.
   *
   * @return The height of the kernel.
   */
  public int getKernelHeight(){
    return rows;
  }

  /**
   * Creates the user interface for inclusion in the GUI.
   */
  public JPanel create25inputPanel(){

    JPanel p = new JPanel();
    p.setLayout(new GridLayout(6,6));
    p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    
    button[0][0] = new JButton();
    p.add(button[0][0]);

    for(i=1;i<6;++i){
      button[i][0] = new JButton(""+i);
      button[i][0].setToolTipText("Click to alter kernel size");
      p.add(button[i][0]);
    }      
    for(j=1;j<6;++j){
      button[0][j] = new JButton(""+j);
      button[0][j].setToolTipText("Click to alter kernel size");
      p.add(button[0][j]);
      for(i=1;i<6;++i){
	valueField[i-1][j-1] = new JTextField("0");
	p.add(valueField[i-1][j-1]);
      }
    }

    /*button[0][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	System.out.println(rows+" "+columns);
      }});*/

    button[1][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	columns = 1;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(i=1;i<5;++i){
	  for(j=0;j<5;++j){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[2][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	columns = 2;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(i=2;i<5;++i){
	  for(j=0;j<5;++j){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[3][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	columns = 3;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(i=3;i<5;++i){
	  for(j=0;j<5;++j){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[4][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	columns = 4;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(i=4;i<5;++i){
	  for(j=0;j<5;++j){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[5][0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	columns = 5;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
      }
    });
    button[0][1].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rows = 1;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(j=1;j<5;++j){
	  for(i=0;i<5;++i){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[0][2].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rows = 2;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(j=2;j<5;++j){
	  for(i=0;i<5;++i){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[0][3].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rows = 3;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(j=3;j<5;++j){
	  for(i=0;i<5;++i){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[0][4].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rows = 4;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
	for(j=4;j<5;++j){
	  for(i=0;i<5;++i){
	    valueField[i][j].setEnabled(false);
	    valueField[i][j].setBackground(Color.lightGray);
	  }
	}

      }
    });
    button[0][5].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rows = 5;
	for(i=0;i<columns;++i){
	  for(j=0;j<rows;++j){
	    valueField[i][j].setEnabled(true);
	    valueField[i][j].setBackground(Color.white);
	  }
	}
      }
    });

    p.setOpaque(false);
    return p;
  }

  /**
   * Creates the interface panel for inclusion in the convolution GUI.
   */
  public JPanel createPanel(){
    
    panel = new JPanel();
    label = new JLabel("Kernel");
    panel.add(label);
    panel.add(create25inputPanel());
    panel.setOpaque(false);
    return panel;
  }

}


