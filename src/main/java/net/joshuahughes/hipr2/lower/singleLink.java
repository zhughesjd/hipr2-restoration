package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99

//package code.connections;

import java.lang.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Represents a link between two operators.  Displayed on the screen as a
 * JPanel added to the panel on the scrollpane, filling it entirely but
 * transparent so other compoents (other links and operatorBoxes can be 
 * seen beneath it.  A link exists between a specific output (1 or 2) of
 * an operator and a specific input (1 or 2) of another operator.  Only
 * one single link can link to a specific input.  Several links can link
 * from an output.
 */
public class singleLink extends JPanel{
  /**
   * The stroke used for the curvy line graphically representing the 
   * actual link on the UI.
   */
  final static BasicStroke linkStroke = new BasicStroke(3.0f);
  /**
   * The operator that outputs through the link.
   */
  public operator linkFrom;
  /* 
   * The operator that takes its input from the link.
   */
  public operator linkTo;
  /**
   * The specific output (1 or 2) that the link originates from.
   */
  public JLabel connectionFrom;
  /**
   * The specific input (1 or 2) that the link terminates at.
   */
  public JLabel connectionTo;
  /**
   * The point (relative to the panel on the scrollpane) that the link
   * terminated at when the screen was last repainted (so an 
   * appropriate bounding rectangle can be calculated to correctly
   * repaint next time.
   */
  Point lastPaintedPoint;
  /**
   * The point (relative to the panel on the scrollpane) that the link
   * originates at (will be within the boundaries of an output JLabel.
   */
  Point pointFrom;
  /**
   * The point (relative to the panel on the scrollpane) that the link
   * terminates at (will be within the boundaries of an input JLabel.
   */
  Point pointTo;
  /**
   * Default no argument constructor.
   */
  public singleLink(){
  }

  public singleLink(operator fromOp, String fromCon,
		    operator toOp, String toCon){
// System.out.println("SINGLELINK entry a: "+fromOp);
// System.out.println("SINGLELINK entry b: "+fromCon);
// System.out.println("SINGLELINK entry c: "+toOp);
// System.out.println("SINGLELINK entry d: "+toCon);
//System.out.println("SINGLELINK: "+" "+fromOp+" "+fromCon+" "+toOp+" "+toCon);
    linkFrom = fromOp;
    double x1,y1;
    if(fromCon.equals("OUT1")){
      x1 = getXCoord(fromOp.getBox().getOut1());
      y1 = getYCoord(fromOp.getBox().getOut1());
      connectionFrom = fromOp.getBox().getOut1();
// System.out.println("SINGLELINK entry e: "+connectionFrom);
    }else{
      x1 = getXCoord(fromOp.getBox().getOut2());
      y1 = getYCoord(fromOp.getBox().getOut2());
      connectionFrom = fromOp.getBox().getOut2();
// System.out.println("SINGLELINK entry f: "+connectionFrom);
    }
    pointFrom = new Point((int) x1,(int) y1);
// System.out.println("SINGLELINK pointFrom: "+pointFrom);

    // set the panel transparent so that other operators/links can be seen
    setOpaque(false);

    // set the location and size to exactly overlay the panel on the scrollpane
    setBounds(0,0,1000,1000);

    // add this new link to the panel on the scrollpane
//System.out.println("singleLink A1");
    linkFrom.getBox().getParent().add(this);
    linkFrom.getBox().getParent().repaint();

    // connect to toOp    
    if(toCon.equals("IN1")){
// System.out.println("SINGLELINK IN1 a: "+toOp);
// System.out.println("SINGLELINK IN1 b: "+toOp.getBox());
//System.out.println("SINGLELINK IN1 c: "+toOp.getBox().getIn1());
      connectLinkTo(toOp,toOp.getBox().getIn1());
    }else{
// System.out.println("SINGLELINK IN2 a: "+toOp);
// System.out.println("SINGLELINK IN2 b: "+toOp.getBox());
//System.out.println("SINGLELINK IN2 c: "+toOp.getBox().getIn2());
      connectLinkTo(toOp,toOp.getBox().getIn2());
    }
  }


  /**
   * Constructor taking an operator and an output (belonging to that
   * operator) where the new link originates.
   * @param fromOp the operator that the link originates from
   * @param fromCon the connection/output that the link originates from
   */
  public singleLink(operator fromOp, JLabel fromCon){
    linkFrom = fromOp;
    connectionFrom = fromCon;
//System.out.println("SINGLELINK B: "+" "+fromOp+" "+fromCon);
    double x1 = getXCoord(fromCon);
    double y1 = getYCoord(fromCon);
    pointFrom = new Point((int) x1,(int) y1);
    pointTo = new Point((int) x1,(int) y1);
    // set the panel transparent so that other operators/links can be seen
    setOpaque(false);
    // set the location and size to exactly overlay the panel on the 
    // scrollpane
    setBounds(0,0,1000,1000);
    // add this new link to the panel on the scrollpane
    linkFrom.getBox().getParent().add(this);
//System.out.println("singleLink B1");
    linkFrom.getBox().getParent().repaint();
    // draw the curvy line on the screen
    updateLink();
  }

  public double getXCoord(JLabel connection){
    double x;
    x = connection.getParent().getParent().getLocation().x +
      connection.getParent().getLocation().x + 
      connection.getLocation().x + 10;
    return x;
  }

  public double getYCoord(JLabel connection){
    double y;
    y = connection.getParent().getParent().getLocation().y +
      connection.getParent().getLocation().y + 
      connection.getLocation().y + 10;
    return y;
  }


  /**
   * Returns a String representing the link, with the names of the
   * operators it links to and from and the names of the connections
   * within these operators.
   * String representing the link
   */
  public String saveLink(){
    String linkData = new String("Link "+
				 linkFrom.getName()+" "+
				 connectionFrom.getName()+" "+
				 linkTo.getName()+" "+
				 connectionTo.getName());
    return linkData;
  }

  /**
   * Called when the terminating end of a link is being dragged to a new
   * point.
   * @param point the new terminating point (relative to the panel on
   * the scrollpane) for the link
   */
  public void dragLinkTo(Point point){
    pointTo = point;
    // find the top-left point of the area requiring repainting
    int x1 = Math.min(pointTo.x,pointFrom.x);
    if(lastPaintedPoint == null){
      lastPaintedPoint = new Point(0,0);
    }
    x1 = Math.min(x1,lastPaintedPoint.x);
    int y1 = Math.min(pointTo.y,pointFrom.y);
    y1 = Math.min(y1,lastPaintedPoint.y);
    // find the bottom-right point of the area requiring repainting
    int x2 = Math.max(pointTo.x,pointFrom.x);
    x2 = Math.max(x2,lastPaintedPoint.x);
    int y2 = Math.max(pointTo.y,pointFrom.y);
    y2 = Math.max(y2,lastPaintedPoint.y);
    // repaint the generous area around the link
//System.out.println("singleLink C1 "+point);
    repaint(x1-10,y1-10,x2+10-(x1-10),y2+10-(y1-10));
  }

  /**
   * Updates the link by calculating the area completely containing
   * the curve and repainting it.
   */
  public void updateLink(){
    Point tl = getTopLeftBound();
    Point br = getBottomRightBound();
//System.out.println("singleLink D1 "+tl.x+" "+tl.y+" "+br.x+" "+br.y);
    repaint(tl.x,tl.y,br.x-tl.x,br.y-tl.y);
  }

  /**
   * Connects the link to an operator and a connection, both 
   * graphically and internally (for propagating data through the operators).
   * @param toOp the operator the link is to be linked to
   * @param toCon the connection (IN1 or IN2) that the link is to link to
   */
  public void connectLinkTo(operator toOp, JLabel toCon){
    linkTo = toOp;
    connectionTo = toCon;

    double x2 = getXCoord(toCon);
    double y2 = getYCoord(toCon);
    
    pointTo = new Point((int) x2,(int) y2);

//System.out.println("CONNECTLINKTO a: "+pointTo);
//System.out.println("CONNECTLINKTO b: "+this);
//System.out.println("CONNECTLINKTO c: "+linkFrom.getBox().getParent());
    linkFrom.getBox().getParent().add(this);
//System.out.println("singleLine E1");
    linkFrom.getBox().getParent().repaint();
  }

  /**
   * Translates the terminating point of this link by the specified
   * vertical and horizontal values.
   * @param x the horizontal translation amount (positive - to the left)
   * @param y the vertical translation amount (positive - down)
   */
  public void translatePointTo(int x, int y){
    pointTo.translate(x,y);
  }

  /**
   * Translates the originating point of this link by the specified
   * vertical and horizontal values.
   * @param x the horizontal translation amount (positive - to the left)
   * @param y the vertical translation amount (positive - down)
   */
  public void translatePointFrom(int x, int y){
    pointFrom.translate(x,y);
  }
  /**
   * Gets the top-left corner of a rectangle that will completely
   * enclose this link.
   * @return the top-left corner of the bounding rectangle
   */
  public Point getTopLeftBound(){
    int x = Math.min(pointFrom.x, pointTo.x) - 10;
    int y = Math.min(pointFrom.y, pointTo.y) -10;
    return(new Point(x,y));
  }
  /**
   * Gets the bottom-right corner of a rectangle that will completely
   * enclose this link.
   * @return the bottom-right corner of the bounding rectangle
   */
  public Point getBottomRightBound(){
    int x = Math.max(pointFrom.x, pointTo.x) + 10;
    int y = Math.max(pointFrom.y, pointTo.y) +10;
    return(new Point(x,y));
  }
  /**
   * Overrides the paint method for JPanel, so that the curvy line
   * is painted on the screen.
   * @param graphics the graphics context to be painted on
   */
  public void paint(Graphics graphics){
    lastPaintedPoint = pointTo;
    Graphics2D graphics2 = (Graphics2D) graphics;
    graphics2.setStroke(linkStroke);

    CubicCurve2D.Double cubic = new CubicCurve2D.Double();
    Point2D control1 = new Point2D.Double(pointFrom.x,pointTo.y);
    Point2D control2 = new Point2D.Double(pointTo.x,pointFrom.y);
    cubic.setCurve(pointFrom, control1, control2, pointTo);
    graphics2.draw(cubic);

    graphics2.setPaint(Color.red);
    graphics2.draw(new Ellipse2D.Double(pointFrom.x-2.5,
					pointFrom.y-2.5,
					5,5));
    graphics2.setPaint(Color.green);
    graphics2.draw(new Ellipse2D.Double(pointTo.x-2.5,
					pointTo.y-2.5,
					5,5));
  }
}
