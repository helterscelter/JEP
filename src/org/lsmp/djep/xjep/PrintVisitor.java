/* @author rich
 * Created on 18-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

package org.lsmp.djep.xjep;
//import org.lsmp.djep.matrixParser.*;
import org.nfunk.jep.*;

import java.io.PrintStream;
import java.util.Hashtable;
/**
 * Prints an expression.
 * Prints the expression with lots of brackets.
 * <tt>((-1.0)/sqrt((1.0-(x^2.0))))</tt>.
 * To use
 * <pre>
 * JEP j = ...; Node in = ...;
 * TreeUtils tu = new TreeUtils(j);
 * PrintVisitor pv = new PrintVisitor(tu);
 * pv.print(in,"x");
 * </pre>
 * @author Rich Morris
 * Created on 20-Jun-2003
 */
public class PrintVisitor extends ErrorCatchingVisitor
{
  protected StringBuffer sb;
  private boolean fullBrackets=false;
  private Hashtable specialRules = new Hashtable();
  
  /** Creates a visitor to create and print string representations of an expression tree. **/

  public PrintVisitor()
  {
	this.addSpecialRule(Operator.OP_LIST,new PrintVisitor.PrintRulesI()
	{	public void append(Node node,PrintVisitor pv) throws ParseException
		{	pv.append("[");
			for(int i=0;i<node.jjtGetNumChildren();++i)
			{
				if(i>0) pv.append(",");
				node.jjtGetChild(i).jjtAccept(pv, null);
			}
			pv.append("]");
		}});
  }

  
  /** Prints the tree decending from node with lots of brackets or specified stream. **/

  public void print(Node node,PrintStream out)
  {
	clearErrors();
	sb = new StringBuffer();
	try
	{
		node.jjtAccept(this,null);
	}
	catch (ParseException e) { out.print(e.getMessage()); }
	out.print(sb);
	if(hasErrors()) 
		out.print(getErrors());
  }

  /** Prints on System.out. */
  public void print(Node node) { print(node,System.out); }
    
  /** Prints the tree decending from node with a newline at end. **/

  public void println(Node node,PrintStream out)
  {
	clearErrors();
	sb = new StringBuffer();
	try
	{
		node.jjtAccept(this,null);
	}
	catch (ParseException e) { out.print(e.getMessage()); }
	out.println(sb);
	if(hasErrors()) 
		out.print(getErrors());
  }

  /** Prints on System.out. */
  public void println(Node node) { println(node,System.out); }

  /** returns a String representation of the equation. */
  
  public String toString(Node node)
  {
	sb = new StringBuffer();
	try
	{
		node.jjtAccept(this,null);
	}
	catch (ParseException e) { sb.append(e.getMessage()); }
	return sb.toString();
  }
  
  /** Add a string to buffer. */
  public void append(String s) { sb.append(s); }
  
	/**
	 * This interface specifies the method needed to implement a special print rule.
	 * A special rule must implement the append method, which should
	 * call pv.append to add data to the output. For example
	 * <pre>
	 * 	pv.addSpecialRule(Operator.OP_LIST,new PrintVisitor.PrintRulesI()
	 *	{
	 *  	public void append(Node node,PrintVisitor pv) throws ParseException
	 *		{
	 *			pv.append("[");
	 *			for(int i=0;i<node.jjtGetNumChildren();++i)
	 *			{
	 *				if(i>0) pv.append(",");
	 *				node.jjtGetChild(i).jjtAccept(pv, null);
	 *			}
	 *			pv.append("]");
	 *		}});
 	 * </pre>
	 * @author Rich Morris
	 * Created on 21-Feb-2004
	 */
  public interface PrintRulesI
  {
  	/** The method called to append data for the rule. **/
  	public void append(Node node,PrintVisitor pv) throws ParseException;
  }
  /** Adds a special print rule to be added for a given operator. 
   * TODO Allow special rules for other functions. */
  public void addSpecialRule(Operator op,PrintRulesI rules)
  {
  	specialRules.put(op,rules);
  }

/***************** visitor methods ********************************/

/** 
 * If subclassed to extend to implement a different visitor
 * this method should be overwritten to ensure the correct 
 * accept method is called.
 * This method simply calls the jjtAccept(this,data) of node.
 */

protected Object nodeAccept(Node node, Object data) throws ParseException
{
	return node.jjtAccept(this,data);
}

/** print the node with no brackets. */
private void printNoBrackets(Node node)
{
	try
	{
		nodeAccept(node,null);
	}
	catch (ParseException e) { addToErrorList(e.getMessage()); }
}

/** print a node suronded by brackets. */
private void printBrackets(Node node)
{
	sb.append("(");
	printNoBrackets(node);
	sb.append(")");
}

/** print a unary operator. */
private Object visitUnary(ASTFunNode node, Object data)
{
	Node rhs = node.jjtGetChild(0);

	// now print the node
	sb.append(node.getOperator().getSymbol());
	// now the rhs
	if(TreeUtils.isOperator(rhs))
		printBrackets(rhs);	// -(-3) -(1+2) or !(-3)
	else
		printNoBrackets(rhs);
	
	return data;
}

public Object visit(ASTFunNode node, Object data)
{
	if(!node.isOperator()) return visitFun(node);
	try
	{
	if(node instanceof PrintRulesI)
	{
		((PrintRulesI) node).append(node,this);
		return null;
	}
	if(node.getOperator()==null)
	{
		addToErrorList("Null operator in print for "+node);
		return null;
	}
	if(specialRules.containsKey(node.getOperator()))
	{
		((PrintRulesI) specialRules.get(node.getOperator())).append(node,this);
		return null;
	}
		
	if(node.getOperator().isUnary())
		return visitUnary(node,data);
	if(node.getOperator().isBinary())
	{
		Node lhs = node.jjtGetChild(0);
		Node rhs = node.jjtGetChild(1);
		Operator top = node.getOperator();
	
		if(fullBrackets)
		{
			printBrackets(lhs);
		}
		else if(TreeUtils.isOperator(lhs))
		{
			Operator lhsop = ((ASTFunNode) lhs).getOperator();
			if(top.getPrecedence() == lhsop.getPrecedence())
			{
				if(top.getBinding() == Operator.LEFT	// (1-2)-3 -> 1-2-3
					|| top.isAssociative() )
						printNoBrackets(lhs);
				else
						printBrackets(lhs);				// (1=2)=3 -> (1=2)=3
			}
			else if(top.getPrecedence() > lhsop.getPrecedence()) // (1*2)+3
						printNoBrackets(lhs);
			else
						printBrackets(lhs);
		}
		else
			printNoBrackets(lhs);
		
		// now print the node
		sb.append(node.getOperator().getSymbol());
		// now the rhs

		if(fullBrackets)
		{
			printBrackets(rhs);
		}
		else if(TreeUtils.isOperator(rhs))
		{
			Operator rhsop = ((ASTFunNode) rhs).getOperator();
			if(top.getPrecedence() == rhsop.getPrecedence())
			{
				if(top.getBinding() == Operator.RIGHT	// 1=(2=3) -> 1=2=3
					|| top.isAssociative() )			// 1+(2-3) -> 1+2-3
						printNoBrackets(rhs);
				else
						printBrackets(rhs);				// 1-(2+3) -> 1-(2-3)
			}
			else if(top.getPrecedence() > rhsop.getPrecedence()) // 1+(2*3) -> 1+2*3
						printNoBrackets(rhs);
			else
						printBrackets(rhs);
		}
		else
			printNoBrackets(rhs);
	}
	}
	catch(ParseException e) { sb.append(e.getMessage()); }
	return null;
}
    
private Object visitFun(ASTFunNode node)
{
	try
	{
		sb.append(node.getName()+"(");
		for(int i=0;i<node.jjtGetNumChildren();++i)
		{
			if(i>0) sb.append(",");
			nodeAccept(node.jjtGetChild(i), null);
		}
		sb.append(")");
	}
	catch (ParseException e) { addToErrorList(e.getMessage()); }
	return null;
}

  public Object visit(ASTVarNode node, Object data) {
	sb.append(node.getName());
	return data;
  }

  public Object visit(ASTConstant node, Object data) {
	sb.append(node.getValue());
	return data;
  }
/**
 * Is full bracket mode on?
 */
public boolean isFullBrackets() {
	return fullBrackets;
}

/**
 * Set full bracket mode.
 * In full bracket mode the brackets each element in the tree will be suronded
 * by brackets to indicate the tree structure. 
 * In the default mode, (full bracket off) the number of brackets is
 * minimised so (x+y)+z will be printed as x+y+z.
 */
public void setFullBrackets(boolean b) {
	fullBrackets = b;
}

}

/*end*/
