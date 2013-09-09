package net.joshuahughes.hipr2.lower;
// Author Simon Horne
// Last Modified 16/9/99

//package code.connections;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Details of all the links in the system and details of the
 * link currently being adjusted.
 */
public class linkData{
  /**
   * The operator that the current link originates from.
   */
  operator currentLinkFrom;
  /**
   * The operator that the current link terminates at.
   */
  operator currentLinkTo;
  /**
   * The output that the current link originates from.
   */
  JLabel fromConnection;
  /**
   * The input that the current link terminates at.
   */
  JLabel toConnection;
  /**
   * All links that are in the system.
   */
  ArrayList links;
  /**
   * The parent panel of all the links (the panel on the big scrollpane
   * that takes up most of the UI).
   */
  JPanel panel;
  /**
   * Constructor that takes a panel to be every link's parent.
   * @param panel the parent panel of the links
   */
  public linkData(JPanel panel){
    this.panel = panel;
    links = new ArrayList();
    currentLinkFrom = null;
    currentLinkTo = null;
    fromConnection = null;
    toConnection = null;
  }

  public JPanel getPanel(){
    return panel;
  }

  /**
   * Returns a String representing all the links in the system.
   * @return String representing all the links
   */
  public String saveLinks(){
    String linksData = new String();
    Iterator it = links.iterator();
    while(it.hasNext()){
      singleLink link = (singleLink) it.next();
      linksData = linksData + link.saveLink() + " ENDZZZ1";
    }
    return linksData;
  }

  public void loadLink(StreamTokenizer tokenizer,
		       ArrayList names,
		       ArrayList operators) throws IOException{
    int tokenType;
    String linkFrom,connectionFrom,linkTo,connectionTo;
    operator opFrom,opTo;

    // get name, _ and instance
    tokenType = tokenizer.nextToken();
    linkFrom = tokenizer.sval;
    tokenType = tokenizer.nextToken();
    tokenType = tokenizer.nextToken();
    linkFrom = linkFrom + "_" + ((int)tokenizer.nval);
//System.out.println("Link from: "+linkFrom);

    // get port
    tokenType = tokenizer.nextToken();
    connectionFrom = tokenizer.sval;
//System.out.println("From port: "+connectionFrom);

    // get name, _ and instance
    tokenType = tokenizer.nextToken();
    linkTo = tokenizer.sval;
    tokenType = tokenizer.nextToken();
    tokenType = tokenizer.nextToken();
    linkTo = linkTo + "_" + ((int)tokenizer.nval);
//System.out.println("Link to: "+linkTo);

    // get port
    tokenType = tokenizer.nextToken();
    connectionTo = tokenizer.sval;
//System.out.println("To port: "+connectionTo);

    // do connections
    Iterator it = names.iterator();
    opFrom = (operator) operators.get(names.indexOf(linkFrom));
    opTo = (operator) operators.get(names.indexOf(linkTo));
    singleLink link = new singleLink(opFrom,connectionFrom,
				     opTo,connectionTo);
//System.out.println("LOADLINK add: "+link);
    links.add(link);

    // propogate results
    opFrom.propagateSingleLink(link);
  }
    
  /**
   * Takes an operator and removes all the links to it (both graphically
   * and internally.
   * @param op the operator that requires all links to it removing
   */
  void removeAllLinksTo(operator op){
    Iterator it = getLinks().iterator();
    while(it.hasNext()){
      singleLink next = (singleLink) it.next();
      if(next.linkTo == op){
	it.remove();
	JPanel panel = (JPanel) next.getParent();
	panel.remove(next);
      }
    }
    op.setInput1(null);
    op.setInput2(null);
  }
  /**
   * Takes an operator and removes all the links from it (both graphically
   * and internally.
   * @param op the operator that requires all links from it removing
   */
  void removeAllLinksFrom(operator op){
    Iterator it = getLinks().iterator();
    while(it.hasNext()){
      singleLink next = (singleLink) it.next();
      if(next.linkFrom == op){
	it.remove();
	JPanel panel = (JPanel) next.getParent();
	panel.remove(next);
	if(next.connectionTo.equals("IN1")){
	  next.linkTo.setInput1(null);
	}else{
	  next.linkTo.setInput2(null);
	}
      }
    }
  }

  /**
   * Removes a link described by the terminating operator and connection.
   * @param toOp the operator that the link terminates at
   * @param toCon the connection (IN1 or IN2) that the link terminates at
   * (this uniquely identifies the link to be removed as only one link can
   * terminate at a connection)
   */
  public void removeLink(operator toOp, JLabel toCon){
    singleLink link = new singleLink();
    link = getLink(toOp,toCon);
    removeLink(link);
    if(toCon.equals("IN1")){
      toOp.setInput1(null);
    }else{
      toOp.setInput2(null);
    }
  }

  /**
   * Removes a link both graphically from the UI and internally from the 
   * image routing between operators.
   * @param link the link to be removed
   */
  public void removeLink(singleLink link){
    links.remove(link);
    JPanel panel = (JPanel) link.getParent();
    try{
      panel. remove(link);
    }
    catch(NullPointerException e){
    }
    panel.repaint();
    link = null;
  }
  /**
   * Returns the link that terminates at the specified operator/connection.
   * @param op the operator where the link terminates
   * @param connection the connection where the link terminates
   * @return the unique link terminating here (null if none)
   */
  public singleLink getLink(operator op, JLabel connection){
    Iterator it = links.iterator();

//System.out.println("GETLINK a: "+op);
//System.out.println("GETLINK b: "+connection);
    while(it.hasNext()){
      singleLink link = (singleLink) it.next();
//System.out.println("GETLINK c: "+link.linkTo);
//System.out.println("GETLINK d: "+link.connectionTo.getName());
      if(link.linkTo == op && 
	 link.connectionTo.getName() == connection.getName()){
	return link;
      }
    }
    return null;
  }

  /**
   * Returns an ArrayList containing all the links in the system.
   * @return all the links
   */
  public ArrayList getLinks(){
    return links;
  }

  /**
   * Returns true if there is a link in the system that terminates at
   * the specified operator/connection.
   * @param op the operator being tested
   * @param connection the connection being tested
   * @return true if a link exists, false if not
   */
  public boolean linkExists(operator op, JLabel connection){
    Iterator it = links.iterator();
    while(it.hasNext()){
      singleLink link = (singleLink) it.next();
      if(link.linkTo == op && 
	 link.connectionTo.getName() == connection.getName()){
	return true;
      }
    }
    return false;
  }
}

