/* @author Rich Morris
 * Created on 19-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
//import org.lsmp.djep.matrixParser.*;
import org.nfunk.jep.*;
import java.util.Vector;
import java.util.Enumeration;

/**
 * An abstract ParserVisitor
 * which adds some useful error handeling facilities.
 * Visitors which require these facilities should extend this class.
 * General format should be
 * <pre>  
 * 	clearErrors();
 *	Object res = (Node) node.jjtAccept(this,data);
 *	if(hasErrors())
 *		throw new ParseException(getErrors());
 *</pre>
 * @author Rich Morris
 * Created on 19-Jun-2003
 */
abstract public class ErrorCatchingVisitor implements ParserVisitor
{
	/** The current error list. */
	private Vector errorList;
	/** Flag for errors during evaluation. */
	private boolean errorFlag;

	/**
	 * Reset the list of errors.
	 */
	public void clearErrors()
	{
		errorList = new Vector();
		errorFlag = false;
	}

	/*
	 * Are their any errors?
	 */	
	public boolean hasErrors() { return errorFlag; }
	
	/**
	 * Adds an error message to the list of errors.
	 */
	public void addToErrorList(String errorStr) 
	{
		if(errorList == null)
		  errorList = new Vector();
	  	
		errorList.addElement(errorStr);
		errorFlag = true;
	}

	/**
	 * Returns a String of all the error messages.
	 */
	public String getErrors()
	{
		StringBuffer sb = new StringBuffer();
		Enumeration enume = errorList.elements();
		while(enume.hasMoreElements())
			sb.append((String) enume.nextElement());
		return sb.toString();
	}
	
	public Object visit(SimpleNode node, Object data) {
		addToErrorList(this.toString()+": encountered a simple node, problem with visitor.");
		return null;
	}

	public Object visit(ASTStart node, Object data) {
		addToErrorList(this.toString()+": encountered a start node, problem with visitor.");
		return null;
	}
}
