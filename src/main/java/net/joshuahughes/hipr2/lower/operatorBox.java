package net.joshuahughes.hipr2.lower;

import java.awt.color.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.*;

public class operatorBox extends JPanel{
  static ArrayList boxes = new ArrayList();
  operator representsOperator;
  static linkData links;
  JPanel ins;
  JPanel outs;
  JLabel in1;
  JLabel in2;
  JLabel out1;
  JLabel out2;
 

  public operatorBox(operator represents, 
		      JPanel panel, linkData links, 
		      int inputs, int outputs){
    boxes.add(this);
    this.links = links;
    representsOperator = represents;
    boxListener listener = new boxListener();
    linkListener linklistener = new linkListener();

    JLabel details = new JLabel((represents.getName()).replace('_',' '));
    details.setName(represents.getName());
    details.setHorizontalAlignment(SwingConstants.CENTER);
    details.addMouseListener(listener);
    details.addMouseMotionListener(listener);
    details.setOpaque(false);
    details.setForeground(Color.black);

    ins = new JPanel();
    outs = new JPanel();
    ins.setOpaque(false);
    outs.setOpaque(false);
    
    in1 = new JLabel("IN1");
    in2 = new JLabel("IN2");
    out1 = new JLabel("OUT1");
    out2 = new JLabel("OUT2");
    in1.setOpaque(false);
    in2.setOpaque(false);
    out1.setOpaque(false);
    out2.setOpaque(false);

    in1.setName("IN1");
    in2.setName("IN2");
    out1.setName("OUT1");
    out2.setName("OUT2");

    in1.setHorizontalAlignment(SwingConstants.LEFT);

    in1.addMouseListener(linklistener);
    in2.addMouseListener(linklistener);
    out1.addMouseListener(linklistener);
    out2.addMouseListener(linklistener);
    in1.addMouseMotionListener(linklistener);
    in2.addMouseMotionListener(linklistener);
    out1.addMouseMotionListener(linklistener);
    out2.addMouseMotionListener(linklistener);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEtchedBorder());
    setOpaque(false);

    if(inputs>=1){
      ins.add(in1);
      if(inputs>=2){
	ins.add(in2);
      }
    }
    outs.add(out1);
    if(outputs>=2){
      outs.add(out2);
    }

    add(ins,"North");
    add(details,"Center");
    add(outs,"South");
    panel.add(this);
    setBounds(0,0,Math.max(Math.max(ins.getPreferredSize().width,
				    details.getPreferredSize().width),
			   outs.getPreferredSize().width)+30,
	      80);
    invalidate();
    validate();
    setVisible(true);
    panel.setVisible(true);
  }

  public JLabel getIn1(){
    return in1;
  }
  public JLabel getIn2(){
    return in2;
  }
  public JLabel getOut1(){
    return out1;
  }
  public JLabel getOut2(){
    return out2;
  }
  

  public static ArrayList getBoxes(){
    return boxes;
  }

  public void removeOperator(){
    links.removeAllLinksTo(representsOperator);
    links.removeAllLinksFrom(representsOperator);
    boxes.remove(this);
    representsOperator.getParameters().dispose();
    JPanel panel = (JPanel) getParent();
    panel.remove(this);
    panel.repaint();
  }

  public static void removeAllOperators(){
    JPanel panel = new JPanel();;
    ArrayList boxes2 = new ArrayList();
    Iterator it = boxes.iterator();
    while(it.hasNext()){
      operatorBox next = (operatorBox) it.next();
      links.removeAllLinksTo(next.representsOperator);
      links.removeAllLinksFrom(next.representsOperator);
      it.remove();
      next.representsOperator.getParameters().dispose();
      panel = (JPanel) next.getParent();
      panel.remove(next);
    }
    panel.repaint();
  }

  public linkData getLinkData(){
    return links;
  }

  public operator getOperator(){
    return representsOperator;
  }

  class linkListener extends MouseInputAdapter{
    singleLink newLink;
    JLabel connection;
    int x;
    int y;
    public void mousePressed(MouseEvent e){
      connection = (JLabel) e.getSource();
      if (connection==null) return;

System.out.println("mouse press: "+((operatorBox) connection.getParent().getParent()).representsOperator.getName());

      if(connection.getName()=="OUT1" || connection.getName()=="OUT2"){

        // starting a new link
	newLink = new singleLink(representsOperator,connection);
	links.getLinks().add(newLink);
	System.out.println("Link begun");

      }else if((connection.getName()=="IN1" || connection.getName()=="IN2") && 
	       links.linkExists(((operatorBox) connection.getParent().getParent()).representsOperator,connection)){

        // mouse press on a input port where a link already exists
//System.out.println("old connection moving: "+connection);

        // disconnect from operator
        operator toOp = ((operatorBox)connection.getParent().getParent()).representsOperator;
//System.out.println("connection moving from: "+toOp);
//System.out.println("connection name: "+connection.getName());
        if((connection.getName()).equals("IN1")){
          toOp.setInput1(null);
        }else{
          toOp.setInput2(null);
        }

        // set up drifting connection
	newLink = links.getLink(((operatorBox)connection.
				 getParent().getParent()).
				representsOperator,connection);
	connection = newLink.connectionFrom;
//System.out.println("connection moving: "+connection);
      }
      else connection = null;
    }

    // link dragging
    public void mouseDragged(MouseEvent e){
      if (connection==null) return;
      if(connection.getName()=="OUT1" || connection.getName()=="OUT2"){
	Point boxOrigin = representsOperator.getBox().getLocation();
	Point conOrigin = e.getComponent().getLocation();
	Point inOutOrigin = e.getComponent().getParent().getLocation();
	x = e.getPoint().x + boxOrigin.x + inOutOrigin.x + conOrigin.x;
	y = e.getPoint().y + boxOrigin.y + inOutOrigin.y + conOrigin.y;
	Point newPoint = new Point(x,y);
	newLink.dragLinkTo(newPoint);
      }
    }


    public void mouseReleased(MouseEvent e){
      if (connection==null) return;
      if(connection.getName()=="OUT1" || connection.getName()=="OUT2"){
	Component [] components = 
	  ((Component)e.getSource()).getParent().getParent().getParent().getComponents();

	// find which component mouse released in
	for(int i=0;i<components.length;++i){
	  if(components[i] instanceof operatorBox){
	    operatorBox box = (operatorBox) components[i];
	    if(box.representsOperator != newLink.linkFrom){
	      Point boxOrigin = box.getLocation();
	      Dimension boxSize = box.getSize();
	      Point insOrigin = box.ins.getLocation();
	      Point in1Origin = box.in1.getLocation();
	      Point in2Origin = box.in2.getLocation();
	      Dimension in1Size = box.in1.getSize();
	      Dimension in2Size = box.in2.getSize();
	      Point in1Global = 
		new Point(in1Origin.x + insOrigin.x + boxOrigin.x,
			  in1Origin.y + insOrigin.y + boxOrigin.y);
	      Point in2Global = 
		new Point(in2Origin.x + insOrigin.x + boxOrigin.x,
			  in2Origin.y + insOrigin.y + boxOrigin.y);

              // check if release is at an input port
	      if(x >= in1Global.x && x <= in1Size.width + in1Global.x &&
		 y >= in1Global.y && y <= in1Size.height + in1Global.y &&
		 !links.linkExists(box.representsOperator,box.in1)){

		// connect to port 1
		newLink.connectLinkTo(box.representsOperator,box.in1);
		representsOperator.propagateSingleLink(newLink); //***
                connection=null;
		return;
	      } else if(x >= in2Global.x && x <= in2Size.width + in2Global.x && 
		       y >= in2Global.y && y <= in2Size.height + in2Global.y &&
		       !links.linkExists(box.representsOperator,box.in2)){

		// connect to port 2
//System.out.println("ADDLINK IN2 a: "+box.representsOperator);
//System.out.println("ADDLINK IN2 b: "+box.in2);
		newLink.connectLinkTo(box.representsOperator,box.in2);
		representsOperator.propagateSingleLink(newLink); //***
                connection=null;
		return;
	      }

	    } //if box representsOperator
	  } //if components
	} //for

	// if it's not a new connection, end is in the background so link is removed.
	links.removeLink(newLink);
      } //if getName
      connection=null;
    } //mouseReleased
  }
  
  class boxListener extends MouseInputAdapter{
    int xOffset;
    int yOffset;

    public void mousePressed(MouseEvent e){
      xOffset = e.getX();
      yOffset = e.getY();
    }

    public void mouseClicked(MouseEvent e){
      if(SwingUtilities.isLeftMouseButton(e)){
	representsOperator.getParameters().setVisible(true);
	representsOperator.getParameters().setState(Frame.NORMAL);
	Iterator it = boxes.iterator();
	while(it.hasNext()){
	  operatorBox box = (operatorBox) it.next();
	}
      }else if(SwingUtilities.isRightMouseButton(e)){
	removeOperator();
      }
    }
    public void mouseDragged(MouseEvent e){
      operatorBox box = (operatorBox) ((JLabel) e.getSource()).getParent();
      Point oldLocation = new Point(box.getLocation());
      int x = (int) Math.round(oldLocation.getX());
      int y = (int) Math.round(oldLocation.getY());
      box.setLocation(x+e.getX()-xOffset,y+e.getY()-yOffset);

      Iterator it = links.getLinks().iterator();
      while(it.hasNext()){
	singleLink link = (singleLink) it.next();
	if(link.linkTo == box.representsOperator){
	  link.translatePointTo(e.getX()-xOffset,e.getY()-yOffset);
	  Point tl = link.getTopLeftBound();
	  Point br = link.getBottomRightBound();
	  link.repaint(tl.x,tl.y,br.x-tl.x,br.y-tl.y);
	}else if(link.linkFrom == box.representsOperator){
	  link.translatePointFrom(e.getX()-xOffset,e.getY()-yOffset);
	  Point tl = link.getTopLeftBound();
	  Point br = link.getBottomRightBound();
	  link.repaint(tl.x,tl.y,br.x-tl.x,br.y-tl.y);
	}
      }
    }
  }
}
