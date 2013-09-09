package net.joshuahughes.hipr2.lower;

import java.util.*;
import java.io.*;
import javax.swing.*;


import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

/**
 * Contains all functionality for saving and loading the current state
 * of the system, all operators and links, locations, parameter settings 
 * etc.
 */

public class fileIO {

  /**
   * The system layout file for loading from/saving to.
   */
  File file;
  /**
   * Predefined java component for graphically selecting a file
   * to load/save (appears to have some problems with repainting itself).
   */
  JFileChooser fileChooser;

  /**
   * List of all operators currently in the system.
   */
  ArrayList boxes;
  /**
   * All links currently in the system.
   */
  linkData links;

  /**
   * Frame that the whole UI is based around.
   */
  JFrame frame;
  /**
   * The URL of the file
   */
  URL url;
  HttpURLConnection uc;
  InputStream is;
  int urlcode;
  String urlmess;
  URLStreamHandler ush;
  URL fileIOdocbase;
  /**
   * The textbox for giving the URL
   */
  JTextField urlText;
  /**
   * No argument constructor.
   */
  public fileIO(){
  }
  /**
   * Constructor taking details of all operatorBoxes and links in the
   * system, so that all details can be saved correctly.
   * @param boxes a list of all operatorBoxes (and therefore operators)
   * in the system
   * @param links all links in the system
   */
  public fileIO(ArrayList boxes, linkData links, JFrame frame){
    this.boxes = boxes;
    this.links = links;
    this.frame = frame;
    layoutFilter filter = new layoutFilter();
    resetFile();
  }

  /**
   * Clears the current system by removing all the operators and links.
   */
  public void clearSystem(){
    if(!boxes.isEmpty()){
      ((operatorBox) boxes.get(0)).removeAllOperators();
    }
  }

  /**
   * Sets/resets the default file name (to prevent a new blank layout
   * being saved overwriting a recently saved layout).
   */
  public void resetFile(){
    file = new File("UNTITLED.hjv");
  }

  /**
   * Saves the layout of the current system to the current file name,
   * if the current file name is the default file name then allow the
   * user to select a more suitable name (don't save the file as the 
   * default name).
   */
  public void saveSystem(AppletContext appcontext){
      FileWriter saveFile;
      String data = new String();
      String header = new String("//Automatically generated save file.ZZZ1");
      String header2 = new String("//ZZZ1//ZZZ1//. . . . . . . . .ZZZ2BLINKZZZ3SAVE AS PLAIN TEXTZZZ2ZZZ4BLINKZZZ3. . . . . . . . .ZZZ1//ZZZ1//ZZZ1");
      data = data + header + header2;

      // save frame size
      Dimension frameSize = frame.getSize();
      data = data + "Resize " + frameSize.width + " " + frameSize.height + " ENDZZZ1";

      // Save operators
      Iterator it = boxes.iterator();
      while(it.hasNext()){
	operatorBox box = (operatorBox) it.next();
	data = data+box.representsOperator.saveOperator();
      }
      data = data + links.saveLinks();
      data = data + "EOFZZZ1";

      // replace blanks in string by %
//      String newdata = data.replace(' ','%');
      String newdata = data.replaceAll(" ","ZZZ0");

      // Save string in displayed document
      URL cgiurl;
      try {
//              cgiurl = new URL("http://www.dai.ed.ac.uk/cgi-bin/rbf/hipreko.pl?"+newdata);
          cgiurl = new URL("http://homepages.inf.ed.ac.uk/cgi/rbf/hipreko.pl?"+newdata);
          cgiurl = new URL("http://homepages.inf.ed.ac.uk/rbf/HIPR2/thumbs/"+newdata);
      }  catch (MalformedURLException e4) {
            System.out.println("Exec: MalformedURLException: "+e4);
            JOptionPane.showMessageDialog(null,
                      "Couldn't save file for some reason, skipping save.",
                      "Invalid Value",
                      JOptionPane.WARNING_MESSAGE);
            return;
          }
      appcontext.showDocument(cgiurl,"_blank" );
  }

   
  /**
   * Load the location of an operator from the file, so that the 
   * layout is correct.
   * @param op the operator to be placed at the correct location
   * @param tokenizer the input file layered stream
   */ 
  public void loadLocation(operator op, StreamTokenizer tokenizer)
    throws IOException{

    int tokenType;
    tokenType = tokenizer.nextToken();
    double x = tokenizer.nval;
    tokenType = tokenizer.nextToken();
    double y = tokenizer.nval;
    op.getBox().setLocation((int) x, (int) y);
  }

  /**
   * Popup a window to allow the user to give a full URL
   */
  public void popUrl() {
    
    JFrame frame = new JFrame("Network Loader");
    urlText = new JTextField(40);
    JLabel urlLabel = new JLabel("Full URL:");
    JButton enterButton = new JButton("Load");
    JPanel panel = new JPanel();
    panel.add(urlLabel);
    panel.add(urlText);
    panel.add(enterButton);
    enterButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	getUrl();
	if (url != null ) {
	  loadURLSystem();
	}
      }});
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setVisible(true);
    frame.setState(JFrame.NORMAL);
  }

  /**
   * Gets the URL in the popup window
   */
  public void getUrl() {

      // try for either a local demo network or a full URL
      try {

        // see if a full URL
        url = new URL(urlText.getText());
      } catch (MalformedURLException e) {

        // if a file rather than a URL, then try local file space
        try {
          url = new URL(fileIOdocbase,"NETWORKS/"+urlText.getText());
        } catch (MalformedURLException e4) {

          // If still not a URL and not a file then report error
          JOptionPane.showMessageDialog(null,("Incorrect URL given"),
                    "Load Error", JOptionPane.WARNING_MESSAGE);
          return;
        }
      }
  }

  public void loadURLSystem() {
    operator newOp = null;
    StreamTokenizer tokenizer;
    int tokenType;
    boolean isload = false;
    ArrayList namesFromFile = new ArrayList();
    ArrayList operatorsFromUI = new ArrayList();

    // make the connection
    try {
        uc = (HttpURLConnection) url.openConnection();
    }
    catch (IOException e15) {
        System.out.println(" IOException e15: "+e15);
        JOptionPane.showMessageDialog(null,
              "Couldn't open file for some reason, skipping load.",
              "Invalid Value",
              JOptionPane.WARNING_MESSAGE);
        return;
    }

    // see if we can get an input stream for the URL
    try {
                is = uc.getInputStream();
    }
    catch (IOException e4) {
        JOptionPane.showMessageDialog(null,
              "Couldn't get input stream for some reason, skipping load.",
              "Invalid Value",
              JOptionPane.WARNING_MESSAGE);
        System.out.println(" IOException e4: "+e4);
        return;
    }
    
    clearSystem();
    
    try{
        Reader r = new BufferedReader(new InputStreamReader(is));
        tokenizer = new StreamTokenizer(r);
	tokenizer.eolIsSignificant(false);
	tokenizer.parseNumbers();
	tokenizer.slashSlashComments(true);
	tokenizer.slashStarComments(true);
//	System.out.println("Loaded data is as follows - ");
	tokenType = tokenizer.nextToken();//get the first token

        // check for "<" as first character -> improperly saved file
        if (tokenizer.ttype != tokenizer.TT_NUMBER && tokenizer.ttype != tokenizer.TT_WORD) {
                char c = (char)tokenizer.ttype;
                if (c == '<') {
                  JOptionPane.showMessageDialog(null,
                      "Invalid file format. Resave as plain text?",
                      "Invalid Value",
                      JOptionPane.WARNING_MESSAGE);
                  return;
                }
        }

        // might be ok - start scanning             
	while(tokenType!=tokenizer.TT_EOF){//stop when EOF is reached
	  // see what the type of the token read is
//System.out.println("TOKEN TYPE: "+tokenType+" SVAL: "+tokenizer.sval);
          isload = false;
	  if (tokenizer.ttype == tokenizer.TT_WORD) {
//System.out.println("IO T1:");

            if (tokenizer.sval.equals("EOF")) break;
//System.out.println("IO T2:");

            // see if links to be added
	    if(tokenizer.sval.equals("Link")){

//System.out.println("IO L1:");
	        links.loadLink(tokenizer,namesFromFile,operatorsFromUI);
//System.out.println("IO L2:");
                tokenType = tokenizer.nextToken();      // get END token
                tokenType = tokenizer.nextToken();      // get next
//System.out.println("IO L3:");
                continue;			   
	    }else if (tokenizer.sval.equals("Resize")){
                tokenType = tokenizer.nextToken();      // get X size
                int xsize = ((int)tokenizer.nval);
                tokenType = tokenizer.nextToken();      // get Ysize
                int ysize = ((int)tokenizer.nval);
                frame.setSize(xsize,ysize);
                tokenType = tokenizer.nextToken();      // get END token
                tokenType = tokenizer.nextToken();      // get next
                continue;			   
	    }else{
//System.out.println("IO OP1:");

	      // see if operator to be added
	      String optype = tokenizer.sval;
//System.out.println("IO 2.7: "+tokenizer);          
              tokenType = tokenizer.nextToken();      // get _
//System.out.println("IO 2.8: "+tokenizer);          
              tokenType = tokenizer.nextToken();      // get instance
              int opinst = ((int)tokenizer.nval);
              String opname = optype + "_" + opinst;
//System.out.println("IO 2.85: opname: "+opname);              
	      namesFromFile.add(opname);
//System.out.println("IO OP2:");
	    
	      if(optype.startsWith("Kernel3x3TwoState")){
	        newOp = new kernel3x3TwoState(links.getPanel(),links);
	      }else if(optype.startsWith("Kernel3x3ThreeState")){
	        newOp = new kernel3x3ThreeState(links.getPanel(),links);
	      }else if(optype.startsWith("Kernel3x3double")){
	        newOp = new kernel3x3Double(links.getPanel(),links);
	      }else if(optype.startsWith("ScaleOffset")){
	        newOp = new scaleOffset(links.getPanel(),links);
	      }else if(optype.startsWith("ConvolutionPadded")){
	        newOp = new convolutionPadded(links.getPanel(),links);
	      }else if(optype.startsWith("Convolution")){
	        newOp = new convolution(links.getPanel(),links);
	      }else if(optype.startsWith("ImageDisplay")){
	        newOp = new imageDisplay(links.getPanel(),links);
	      }else if(optype.startsWith("ImageLoad")){
//System.out.println("IO 1: "+fileIOdocbase);
	        newOp = new imageLoad(links.getPanel(),links,fileIOdocbase);
//System.out.println("IO 2: ");
	        isload = true;
	      }else if(optype.startsWith("Addition")){
	        newOp = new addition(links.getPanel(),links);
	      }else if(optype.startsWith("Subtraction")){
	        newOp = new subtraction(links.getPanel(),links);
	      }else if(optype.startsWith("Multiplication")){
	        newOp = new multiplication(links.getPanel(),links);
	      }else if(optype.startsWith("Division")){
	        newOp = new division(links.getPanel(),links);
	      }else if(optype.startsWith("Blending")){
	        newOp = new blending(links.getPanel(),links);
	      }else if(optype.startsWith("Thresholding")){
	        newOp = new threshold(links.getPanel(),links);
	      }else if(optype.startsWith("RobertsCross")){
	        newOp = new robertsCross(links.getPanel(),links);
	      }else if(optype.startsWith("Sobel")){
	        newOp = new sobel(links.getPanel(),links);
	      }else if(optype.startsWith("Canny")){
	        newOp = new canny(links.getPanel(),links);
	      }else if(optype.startsWith("Hough")){
	        newOp = new hough(links.getPanel(),links);
	      }else if(optype.startsWith("AND")){
	        newOp = new and(links.getPanel(),links);
	      }else if(optype.startsWith("OR")){
	        newOp = new or(links.getPanel(),links);
	      }else if(optype.startsWith("XOR")){
	        newOp = new xor(links.getPanel(),links);
	      }else if(optype.startsWith("NOT")){
	        newOp = new not(links.getPanel(),links);
	      }else if(optype.startsWith("BitShifting")){
	        newOp = new bitshift(links.getPanel(),links);
	      }else if(optype.startsWith("AdaptiveThresholding")){
	        newOp = new adaptiveThreshold(links.getPanel(),links);
	      }else if(optype.startsWith("ContrastStretching")){
	        newOp = new contrastStretch(links.getPanel(),links);
	      }else if(optype.startsWith("HistogramEqualizing")){
	        newOp = new equalize(links.getPanel(),links);
	      }else if(optype.startsWith("Logarithm")){
	        newOp = new logarithm(links.getPanel(),links);
	      }else if(optype.startsWith("Exponential")){
	        newOp = new exponential(links.getPanel(),links);
	      }else if(optype.startsWith("RaiseToPower")){
	        newOp = new raiseToPower(links.getPanel(),links);
	      }else if(optype.startsWith("Scaling")){
	        newOp = new scale(links.getPanel(),links);
	      }else if(optype.startsWith("Rotation")){
	        newOp = new rotate(links.getPanel(),links);
	      }else if(optype.startsWith("Reflection")){
	        newOp = new reflect(links.getPanel(),links);
	      }else if(optype.startsWith("Translation")){
	        newOp = new translate(links.getPanel(),links);
	      }else if(optype.startsWith("Affine")){
	        newOp = new affine(links.getPanel(),links);
	      }else if(optype.startsWith("Intensity")){
	        newOp = new histogram(links.getPanel(),links);
	      }else if(optype.startsWith("Classification")){
	        newOp = new classify(links.getPanel(),links);
	      }else if(optype.startsWith("Labelling")){
	        newOp = new labelling(links.getPanel(),links);
	      }else if(optype.startsWith("Dilation")){
	        newOp = new dilation(links.getPanel(),links);
	      }else if(optype.startsWith("Erosion")){
	        newOp = new erosion(links.getPanel(),links);
	      }else if(optype.startsWith("Opening")){
	        newOp = new opening(links.getPanel(),links);
	      }else if(optype.startsWith("Closing")){
	        newOp = new closing(links.getPanel(),links);
	      }else if(optype.startsWith("HitAndMiss")){
	        newOp = new hitAndMiss(links.getPanel(),links);
	      }else if(optype.startsWith("Thinning")){
	        newOp = new thinning(links.getPanel(),links);
	      }else if(optype.startsWith("Thickening")){
	        newOp = new thickening(links.getPanel(),links);
	      }else if(optype.startsWith("Skeletonization")){
	        newOp = new skeletonize(links.getPanel(),links);
	      }else if(optype.startsWith("MedialAxisTransform")){
	        newOp = new mat(links.getPanel(),links);
	      }else if(optype.startsWith("MeanSmooth")){
	        newOp = new meanSmooth(links.getPanel(),links);
	      }else if(optype.startsWith("MedianSmoothing")){
	        newOp = new medianSmooth(links.getPanel(),links);
	      }else if(optype.startsWith("GaussianSmoothing")){
	        newOp = new gaussianSmooth(links.getPanel(),links);
	      }else if(optype.startsWith("ConservativeSmoothing")){
	        newOp = new conservativeSmooth(links.getPanel(),links);
	      }else if(optype.startsWith("Crimmins")){
	        newOp = new crimmins(links.getPanel(),links);
	      }else if(optype.startsWith("LaplacianOfGaussian")){
	        newOp = new lapOfGauss(links.getPanel(),links);
	      }else if(optype.startsWith("Laplacian")){
	        newOp = new laplacian(links.getPanel(),links);
	      }else if(optype.startsWith("Unsharp")){
	        newOp = new unsharpFilter(links.getPanel(),links);
	      }else if(optype.startsWith("Compass")){
	        newOp = new compass(links.getPanel(),links);
	      }else if(optype.startsWith("ZeroCrossing")){
	        newOp = new zeroCrossing(links.getPanel(),links);
	      }else if(optype.startsWith("LineDetector")){
	        newOp = new lineDetector(links.getPanel(),links);
	      }else if(optype.startsWith("BinaryBoundaryExtractor")){
	        newOp = new boundary(links.getPanel(),links);
	      }else if(optype.startsWith("Distance")){
	        newOp = new distance(links.getPanel(),links);
	      }else if(optype.startsWith("Fourier")){
	        newOp = new fourier(links.getPanel(),links);
	      }else if(optype.startsWith("FFTMask")){
	        newOp = new mask(links.getPanel(),links);
	      }else if(optype.startsWith("RandomNoise")){
	        newOp = new noise(links.getPanel(),links);
	      }else{
	        System.out.println("fileIO - Unrecognized operator: "+tokenizer.sval);
	      }
	    }
	  }
	  else if (tokenizer.ttype == tokenizer.TT_NUMBER) {
	    System.out.println("fileIO load error: "+tokenType+" "+tokenizer.nval);
	  }

          // load location and parameters: need special case for imageLoad
//System.out.println("IO 2.9: "+tokenizer);          
	  loadLocation(newOp,tokenizer);
//System.out.println("IO 4: ");
	  if (isload) {
//System.out.println("IO 5: ");
                ((imageLoad)newOp).loadParameters(tokenizer,fileIOdocbase);
//System.out.println("IO 6: ");
          }
          else {
//System.out.println("IO 7: ");
                newOp.loadParameters(tokenizer);
                tokenType = tokenizer.nextToken();      // get END token
                tokenType = tokenizer.nextToken();      // get next
//System.out.println("IO 8: ");
	  }
//System.out.println("IO 8a: ");
	  operatorsFromUI.add(newOp);
//System.out.println("IO 9: ");
	  
	}
//System.out.println("IO 10: ");
//	tokenType = tokenizer.nextToken();
//System.out.println("IO 11: ");
      }
      catch(NumberFormatException nfe){
//System.out.println("IO 12: ");
	System.out.println("fileIO: unexpected number format error"+nfe);
        JOptionPane.showMessageDialog(null,
              "Unexpected number format error, skipping load.",
              "Invalid Value",
              JOptionPane.WARNING_MESSAGE);
      }catch(FileNotFoundException fnfe){
	System.out.println("fileIO - File not found"+fnfe);
        JOptionPane.showMessageDialog(null,
              "File not found, skipping load.",
              "Invalid Value",
              JOptionPane.WARNING_MESSAGE);
      }catch(IOException ioe){
	System.out.println("fileIO - IO error"+ioe);
        JOptionPane.showMessageDialog(null,
              "Some IO error, skipping load.",
              "Invalid Value",
              JOptionPane.WARNING_MESSAGE);
      }

  // close the connection
    uc.disconnect();
  }

  /**
   * Popup a file load dialog box, to allow the user to select a 
   * layout to load and then load it in to the program.
   */
  public void loadSystem(URL tableaudocbase){

    // get the URL and check if it's a correct URL
    fileIOdocbase = tableaudocbase;
    popUrl();
  }

/**
 * Filter for the load/save dialog boxes to limit the files visible
 * to the user (user selectable from a combo-box in the dialog.
 */
class layoutFilter extends javax.swing.filechooser.FileFilter{
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }
    if(f.getName().endsWith(".hip")){
      return true;
    }else{
      return false;
    }
  }
  public String getDescription(){
    return "System layout files (*.hip)";
  }
}

  
  }

  
