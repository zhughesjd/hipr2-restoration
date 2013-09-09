package net.joshuahughes.hipr2.lower;
import java.awt.color.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;


import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class tableau extends JApplet {
  static JFrame frame;
  static JPanel panel;
  static JScrollPane scroll;
  static linkData links;
  static fileIO loadSave;
  URL tableaudocbase;    
  AppletContext tableauappletcontext;
  
  public static void setupFrame(){
    frame = new JFrame("Interactive Demo");
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	
	System.exit(0);
      }
    });
    panel = new JPanel();
    panel.setBounds(0,0,1000,1000);
    panel.setName("BigPanel");
    scroll = new JScrollPane(panel);
    panel.setLayout(null);
    scroll.setPreferredSize(new Dimension(500,500));
    frame.getContentPane().add(scroll);
    frame.setSize(500,500);
  }

  public void setupMenuBar(){
    
    //This is the setup of the popup window displaying the about details
    
    //Create the components to go in the window
    final JFrame aboutFrame = new JFrame("About");
    //aboutFrame. setSize(200,200);
    final JTextArea aboutText = new JTextArea("This tableau and the associated image processing operators\nwere written by:\n\n"
					     +"    Neil Brown, Nathalie Cammas, Helmut Cantzler\n"
					     +"    Bob Fisher, Andrew Fitzgibbon,\n"
					     +"    Simon Horne, Konstantinos Koryllos\n"
					     +"    Andrew Murdoch, Judy Robertson\n"
					     +"    Timothy Sharman, Craig Strachan\n\n"
					     +"Edinburgh University, Div. of Informatics\n"
					     +"1997-2000");
    final JScrollPane aboutScrollPane = new JScrollPane(aboutText);
    aboutScrollPane.setPreferredSize(new Dimension(450, 250));
    //Add them to the window
    aboutFrame.getContentPane().add(aboutScrollPane);
    //Add a listener so that the window can be closed

    //The applet menu bar
    JMenuBar menuBar = new JMenuBar();

    //The file menu
    JMenu fileMenu = new JMenu("File");
    
    //The menu item for creating a new network
    JMenuItem fileNewMenuItem = new JMenuItem("New");
    fileNewMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	loadSave.clearSystem();
	loadSave.resetFile();
      }
    });
    
    //The menu item for loading a network
    JMenuItem fileLoadMenuItem = new JMenuItem("Load");
    fileLoadMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	loadSave.loadSystem(tableaudocbase);
      }
    });

    //The menu item for saving a network using the current file
    JMenuItem fileSaveMenuItem = new JMenuItem("Save");
    fileSaveMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	loadSave.saveSystem(tableauappletcontext);
      }
    });

/*

    //The menu item for saving a network using a new file
    JMenuItem fileSaveAsMenuItem = new JMenuItem("Save As...");
    fileSaveAsMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	loadSave.saveSystemAs();
      }
    });


    //This is the menu item for exiting the program
    JMenuItem fileQuitMenuItem = new JMenuItem("Quit");
    fileQuitMenuItem. addActionListener(new ActionListener(){
      public synchronized void actionPerformed(ActionEvent e){
	System.exit(0);
      }
    });

*/
    //The menu item for finding out about the program
    JMenuItem fileAboutMenuItem = new JMenuItem("About");
    fileAboutMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	aboutFrame.pack();
        aboutFrame.setVisible(true);
      }
    });

    //The menu which contains all the components
    JMenu componentMenu = new JMenu("Components");

    //The submenu's which are contained in this main menu
    JMenu imageArithmeticMenu = new JMenu("Image Arithmetic");
    JMenu pointOperationsMenu = new JMenu("Point Operations");
    JMenu geometricOperationsMenu = new JMenu("Geometric Operations");
    JMenu imageAnalysisMenu = new JMenu("Image Analysis");
    JMenu morphologyMenu = new JMenu("Morphology");
    JMenu digitalFiltersMenu = new JMenu("Digital Filters");
    JMenu featureDetectorsMenu = new JMenu("Feature Detectors");
    JMenu imageTransformsMenu = new JMenu("Image Transforms");
    JMenu imageSynthesisMenu = new JMenu("Image Synthesis");
    JMenu otherMenu = new JMenu("Other");
    JMenu maskMenu = new JMenu("Masks");
    
    //The image arithmetic menu items
    JMenuItem additionMenuItem = new JMenuItem("Image Addition");
    additionMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	addition op1 = new addition(panel, links);
      }
    });

    JMenuItem subtractionMenuItem = new JMenuItem("Image Subtraction");
    subtractionMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	subtraction op1 = new subtraction(panel, links);
      }
    });

    JMenuItem multiplicationMenuItem = new JMenuItem("Image Multiplication");
    multiplicationMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	multiplication op1 = new multiplication(panel, links);
      }
    });

    JMenuItem divisionMenuItem = new JMenuItem("Image Division");
    divisionMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	division op1 = new division(panel, links);
      }
    });

    JMenuItem blendingMenuItem = new JMenuItem("Image Blending");
    blendingMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	blending op1 = new blending(panel, links);
      }
    });

    JMenuItem andMenuItem = new JMenuItem("Logical AND/NAND");
    andMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	and op1 = new and(panel, links);
      }
    });

    JMenuItem orMenuItem = new JMenuItem("Logical OR/NOR");
    orMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	or op1 = new or(panel, links);
      }
    });

    JMenuItem xorMenuItem = new JMenuItem("Logical XOR/XNOR");
    xorMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	xor op1 = new xor(panel, links);
      }
    });

    JMenuItem notMenuItem = new JMenuItem("Logical NOT");
    notMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	not op1 = new not(panel, links);
      }
    });

    JMenuItem bitshiftMenuItem = new JMenuItem("Image Bitshifting");
    bitshiftMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	bitshift op1 = new bitshift(panel, links);
      }
    });
    
    //The point operations menu items
    
    JMenuItem thresholdMenuItem = new JMenuItem("Threshold");
    thresholdMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	threshold op1 = new threshold(panel, links);
      }
    });

    JMenuItem adaptiveThresholdMenuItem = new JMenuItem("Adaptive Thresholding");
    adaptiveThresholdMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	adaptiveThreshold op1 = new adaptiveThreshold(panel, links);
      }
    });

    JMenuItem contrastStretchMenuItem = new JMenuItem("Contrast Stretching");
    contrastStretchMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	contrastStretch op1 = new contrastStretch(panel, links);
      }
    });

    JMenuItem equalizeMenuItem = new JMenuItem("Histogram Equalization");
    equalizeMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	equalize op1 = new equalize(panel, links);
      }
    });

    JMenuItem logarithmMenuItem = new JMenuItem("Image Logarithm");
    logarithmMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	logarithm op1 = new logarithm(panel, links);
      }
    });
     
    JMenuItem exponentialMenuItem = new JMenuItem("Image Exponentiation");
    exponentialMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	exponential op1 = new exponential(panel, links);
      }
    });

    JMenuItem raiseToPowerMenuItem = new JMenuItem("Image Raise To Power");
    raiseToPowerMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	raiseToPower  op1 = new raiseToPower(panel, links);
      }
    });
    
    //The geomtric operations submenu item
    
    JMenuItem scaleMenuItem = new JMenuItem("Scaling");
    scaleMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	scale op1 = new scale(panel, links);
      }
    });

    JMenuItem rotateMenuItem = new JMenuItem("Rotation");
    rotateMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	rotate op1 = new rotate(panel, links);
      }
    });

    JMenuItem reflectMenuItem = new JMenuItem("Reflect");
    reflectMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	reflect op1 = new reflect(panel, links);
      }
    });

    JMenuItem translateMenuItem = new JMenuItem("Translation");
    translateMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	translate op1 = new translate(panel, links);
      }
    });

    JMenuItem affineMenuItem = new JMenuItem("Affine Transformation");
    affineMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	affine op1 = new affine(panel, links);
      }
    });
    
    //The image analysis menu items
    
    JMenuItem histogramMenuItem = new JMenuItem("Intensity Histogram");
    histogramMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	histogram op1 = new histogram(panel, links);
      }
    });

    JMenuItem classifyMenuItem = new JMenuItem("Classification");
    classifyMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	classify op1 = new classify(panel, links);
      }
    });

    JMenuItem labellingMenuItem = new JMenuItem("Connected Components Labelling");
    labellingMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	labelling op1 = new labelling(panel, links);
      }
    });
    
    //The morphology menu items
    
    JMenuItem dilationMenuItem = new JMenuItem("Dilation");
    dilationMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	dilation op1 = new dilation(panel, links);
      }
    });

    JMenuItem erosionMenuItem = new JMenuItem("Erosion");
    erosionMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	erosion op1 = new erosion(panel, links);
      }
    });

    JMenuItem openingMenuItem = new JMenuItem("Opening");
    openingMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	opening op1 = new opening(panel, links);
      }
    });

    JMenuItem closingMenuItem = new JMenuItem("Closing");
    closingMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	closing op1 = new closing(panel, links);
      }
    });
    
    JMenuItem hitAndMissMenuItem = new JMenuItem("Hit and Miss Transform");
    hitAndMissMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	hitAndMiss op1 = new hitAndMiss(panel, links);
      }
    });

    JMenuItem thinningMenuItem = new JMenuItem("Thinning");
    thinningMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	thinning op1 = new thinning(panel, links);
      }
    });

    JMenuItem thickeningMenuItem = new JMenuItem("Thickening");
    thickeningMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	thickening op1 = new thickening(panel, links);
      }
    });

    JMenuItem skeletonizeMenuItem = new JMenuItem("Skeletonization");
    skeletonizeMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	skeletonize op1 = new skeletonize(panel, links);
      }
    });

    JMenuItem matMenuItem = new JMenuItem("Medial Axis Transform");
    matMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	mat op1 = new mat(panel, links);
      }
    });
    
    //The digital filters menu items

    JMenuItem meanSmoothMenuItem = new JMenuItem("Mean Smoothing");
    meanSmoothMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	meanSmooth op1 = new meanSmooth(panel, links);
      }
    });
    
    JMenuItem medianSmoothMenuItem = new JMenuItem("Median Smoothing");
    medianSmoothMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	medianSmooth op1 = new medianSmooth(panel, links);
      }
    });

    JMenuItem gaussianSmoothMenuItem = new JMenuItem("Gaussian Smoothing");
    gaussianSmoothMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	gaussianSmooth op1 = new gaussianSmooth(panel, links);
      }
    });

    JMenuItem conservativeSmoothMenuItem = new JMenuItem("Conservative Smoothing");
    conservativeSmoothMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	conservativeSmooth op1 = new conservativeSmooth(panel, links);
      }
    });

    JMenuItem crimminsMenuItem = new JMenuItem("Crimmins Speckle Removal");
    crimminsMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	crimmins op1 = new crimmins(panel, links);
      }
    });

    JMenuItem laplacianMenuItem = new JMenuItem("Laplacian");
    laplacianMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	laplacian op1 = new laplacian(panel, links);
      }
    });

    JMenuItem lapOfGaussMenuItem = new JMenuItem("Laplacian of Gaussian");
    lapOfGaussMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	lapOfGauss op1 = new lapOfGauss(panel, links);
      }
    });

    JMenuItem unsharpFilterMenuItem = new JMenuItem("Unsharp Filter");
    unsharpFilterMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	unsharpFilter op1 = new unsharpFilter(panel, links);
      }
    });
    
    //The feature detector menu items
    
    JMenuItem robertsMenuItem = new JMenuItem("Roberts Cross Edge Detector");
    robertsMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	robertsCross op1 = new robertsCross(panel, links);
      }
    });

    JMenuItem sobelMenuItem = new JMenuItem("Sobel Edge Detector");
    sobelMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	sobel op1 = new sobel(panel, links);
      }
    });
    
    JMenuItem cannyMenuItem = new JMenuItem("Canny Edge Detector");
    cannyMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	canny op1 = new canny(panel, links);
      }
    });

    JMenuItem compassMenuItem = new JMenuItem("Compass Edge Detector");
    compassMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	compass op1 = new compass(panel, links);
      }
    });

    JMenuItem zeroCrossingMenuItem = new JMenuItem("Zero Crossing Detector");
    zeroCrossingMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	zeroCrossing op1 = new zeroCrossing(panel, links);
      }
    });

    JMenuItem lineDetectorMenuItem = new JMenuItem("Line Detector");
    lineDetectorMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	lineDetector op1 = new lineDetector(panel, links);
      }
    });

    JMenuItem boundaryMenuItem = new JMenuItem("Binary Boundary Extractor");
    boundaryMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	boundary op1 = new boundary(panel, links);
      }
    });
    
    //The image transform menu items
    
    JMenuItem distanceMenuItem = new JMenuItem("Distance Transform");
    distanceMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	distance op1 = new distance(panel, links);
      }
    });

    JMenuItem fourierMenuItem = new JMenuItem("Fourier Transform");
    fourierMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	fourier op1 = new fourier(panel, links);
      }
    });

    JMenuItem houghMenuItem = new JMenuItem("Hough Transform");
    houghMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	hough op1 = new hough(panel, links);
      }
    });
    
    //The image synthesis menu items
    
    JMenuItem noiseMenuItem = new JMenuItem("Noise Generation");
    noiseMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	noise op1 = new noise(panel, links);
      }
    });
    
    //The other menu items

    JMenuItem imageLoadMenuItem = new JMenuItem("ImageLoad");
    imageLoadMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	imageLoad op1 = new imageLoad(panel,links,tableaudocbase);
      }
    });
    
    JMenuItem imageDisplayMenuItem = new JMenuItem("ImageDisplay");
    imageDisplayMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	imageDisplay op1 = new imageDisplay(panel,links);
      }
    });
    JMenuItem scaleOffsetMenuItem = new JMenuItem("ScaleOffset");
    scaleOffsetMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	scaleOffset op1 = new scaleOffset(panel,links);
      }
    });

    //The convolution sub menu items
    
    JMenuItem convolutionMenuItem = new JMenuItem("Convolution");
    convolutionMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	convolution op1 = new convolution(panel,links);
      }
    });
    JMenuItem convolutionPaddedMenuItem = new JMenuItem("ConvolutionPadded");
    convolutionPaddedMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	convolutionPadded op1 = new convolutionPadded(panel,links);
      }
    });

    //The kernel submenu items

    JMenuItem kernel1MenuItem = new JMenuItem("3x3Kernel2State");
    kernel1MenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	kernel3x3TwoState op1 = new kernel3x3TwoState(panel,links);
	panel.setVisible(true);
      }
    });
    JMenuItem kernel3MenuItem = new JMenuItem("3x3Kernel3State");
    kernel3MenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	kernel3x3ThreeState op1 = new kernel3x3ThreeState(panel,links);
	panel.setVisible(true);
      }
    });
    JMenuItem kernel2MenuItem = new JMenuItem("3x3KernelDouble");
    kernel2MenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	kernel3x3Double op1 = new kernel3x3Double(panel,links);
	panel.setVisible(true);
      }
    });

    //Masks menu

    JMenuItem maskMenuItem = new JMenuItem("FFTMask");
    maskMenuItem. addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
	mask op1 = new mask(panel,links);
	panel.setVisible(true);
      }
    });


    //Kernels sub sub menu
    JMenu kernelMenu = new JMenu("Kernel");
    kernelMenu. add(kernel1MenuItem);
    kernelMenu. add(kernel3MenuItem);
    kernelMenu. add(kernel2MenuItem);

    //Convolution sub sub menu
    JMenu convolutionMenu = new JMenu("Convolution");
    convolutionMenu. add(convolutionMenuItem);
    convolutionMenu. add(convolutionPaddedMenuItem);

    //Add the items to the submenus
    
    imageArithmeticMenu. add(additionMenuItem);
    imageArithmeticMenu. add(subtractionMenuItem);
    imageArithmeticMenu. add(multiplicationMenuItem);
    imageArithmeticMenu. add(divisionMenuItem);
    imageArithmeticMenu. add(blendingMenuItem);
    imageArithmeticMenu. add(andMenuItem);
    imageArithmeticMenu. add(orMenuItem);
    imageArithmeticMenu. add(xorMenuItem);
    imageArithmeticMenu. add(notMenuItem);
    imageArithmeticMenu. add(bitshiftMenuItem);

    pointOperationsMenu. add(thresholdMenuItem);
    pointOperationsMenu. add(adaptiveThresholdMenuItem);
    pointOperationsMenu. add(contrastStretchMenuItem);
    pointOperationsMenu. add(equalizeMenuItem);
    pointOperationsMenu. add(logarithmMenuItem);
    pointOperationsMenu. add(exponentialMenuItem);
    pointOperationsMenu. add(raiseToPowerMenuItem);

    geometricOperationsMenu. add(scaleMenuItem);
    geometricOperationsMenu. add(rotateMenuItem);
    geometricOperationsMenu. add(reflectMenuItem);
    geometricOperationsMenu. add(translateMenuItem);
    geometricOperationsMenu. add(affineMenuItem);

    imageAnalysisMenu. add(histogramMenuItem);
    imageAnalysisMenu. add(classifyMenuItem);
    imageAnalysisMenu. add(labellingMenuItem);

    morphologyMenu. add(dilationMenuItem);
    morphologyMenu. add(erosionMenuItem);
    morphologyMenu. add(openingMenuItem);
    morphologyMenu. add(closingMenuItem);
    morphologyMenu. add(hitAndMissMenuItem);
    morphologyMenu. add(thinningMenuItem);
    morphologyMenu. add(thickeningMenuItem);
    morphologyMenu. add(skeletonizeMenuItem);
    morphologyMenu. add(matMenuItem);
    
    digitalFiltersMenu. add(meanSmoothMenuItem);
    digitalFiltersMenu. add(medianSmoothMenuItem);
    digitalFiltersMenu. add(gaussianSmoothMenuItem);
    digitalFiltersMenu. add(conservativeSmoothMenuItem);
    digitalFiltersMenu. add(crimminsMenuItem);
    digitalFiltersMenu. add(laplacianMenuItem);
    digitalFiltersMenu. add(lapOfGaussMenuItem);
    digitalFiltersMenu. add(unsharpFilterMenuItem);
    
    featureDetectorsMenu. add(robertsMenuItem);
    featureDetectorsMenu. add(sobelMenuItem);
    featureDetectorsMenu. add(cannyMenuItem);
    featureDetectorsMenu. add(compassMenuItem);
    featureDetectorsMenu. add(zeroCrossingMenuItem);
    featureDetectorsMenu. add(lineDetectorMenuItem);
    featureDetectorsMenu. add(boundaryMenuItem);
   

    imageTransformsMenu. add(distanceMenuItem);
    imageTransformsMenu. add(fourierMenuItem);
    imageTransformsMenu. add(houghMenuItem);
    
    imageSynthesisMenu. add(noiseMenuItem);
    
    otherMenu. add(kernelMenu);
    otherMenu. add(convolutionMenu);
    otherMenu. add(imageLoadMenuItem);
    
    otherMenu. add(imageDisplayMenuItem);
    otherMenu. add(scaleOffsetMenuItem);
    otherMenu. add(maskMenu);
    maskMenu. add(maskMenuItem);

    //Add the submenus to the component menu
    
    componentMenu. add(imageArithmeticMenu);
    componentMenu. add(pointOperationsMenu);
    componentMenu. add(geometricOperationsMenu);
    componentMenu. add(imageAnalysisMenu);
    componentMenu. add(morphologyMenu);
    componentMenu. add(digitalFiltersMenu);
    componentMenu. add(featureDetectorsMenu);
    componentMenu. add(imageTransformsMenu);
    componentMenu. add(imageSynthesisMenu);
    componentMenu. add(otherMenu);
    

    //Main component menu
    //componentMenu. add(imageLoadMenuItem);
    //componentMenu. add(imageDisplayMenuItem);
    //componentMenu. add(scaleOffsetMenuItem);
    //componentMenu. add(meanSmoothMenuItem);
    //componentMenu. add(convolutionMenu);
    //componentMenu. add(kernelMenu);
    
    //File menu
    fileMenu. add(fileNewMenuItem);
    fileMenu. addSeparator();
    fileMenu. add(fileLoadMenuItem);
    fileMenu. add(fileSaveMenuItem);
    //fileMenu. add(fileSaveAsMenuItem);
    fileMenu. addSeparator();
    //fileMenu. add(fileQuitMenuItem);
    fileMenu. add(fileAboutMenuItem);
   
    //Add these menus to the menu bar
    menuBar. add(fileMenu);
    menuBar. add(componentMenu);

    //Add the menu bar to the frame
    frame. setJMenuBar(menuBar); 

  }

//  public static void main(String args[]){
  public void init(){            
    tableaudocbase = getDocumentBase();
    tableauappletcontext = getAppletContext();
    setupFrame();
    setupMenuBar();
    links = new linkData(panel);
    loadSave = new fileIO(operatorBox.getBoxes(),links,frame);
    frame.setVisible(true);
  }
}


