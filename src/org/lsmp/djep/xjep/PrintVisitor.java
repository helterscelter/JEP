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
  /** All brackets are printed. Removes all ambiguity. */
  public static final int FULL_BRACKET = 1;
  private int maxLen = -1;
  protected StringBuffer sb;
  /** The current mode for printing. */
//  protected boolean fullBrackets=false;
  protected int mode=0;
  private Hashtable specialRules = new Hashtable();
  
  /** Creates a visitor to create and print string representations of an expression tree. **/

  public PrintVisitor()
  {
/*	this.addSpecialRule(Operator.OP_LIST,new PrintVisitor.PrintRulesI()
	{	public void append(Node node,PrintVisitor pv) throws ParseException
		{	pv.append("[");
			for(int i=0;i<node.jjtGetNumChildren();++i)
			{
				if(i>0) pv.append(",");
				node.jjtGetChild(i).jjtAccept(pv, null);
			}
			pv.append("]");
		}});
*/
  }

  
  /** Prints the tree decending from node with lots of brackets or specified stream. **/

  public void print(Node node,PrintStream out)
  {
	sb = new StringBuffer();
	acceptCatchingErrors(node,null);
	if(maxLen == -1)
		out.print(sb);
	else
	{
		while(true)	{
			if(sb.length() < maxLen) {
				out.print(sb);
				return;
			}
			int pos = maxLen-2;
			for(int i=maxLen-2;i>=0;--i) {
				char c = sb.charAt(i);
				if(c == '+' || c == '-' || c == '*' || c == '/'){
					pos = i; break;
				}
			}
			//out.println("<"+sb.substring(0,pos+10)+">");
			out.println(sb.substring(0,pos+1));
			sb.delete(0,pos+1);
		}
	}
  }

  /** Prints on System.out. */
  public void print(Node node) { print(node,System.out); }
    
  /** Prints the tree decending from node with a newline at end. **/

  public void println(Node node,PrintStream out)
  {
	print(node,out);
	out.println("");
  }

  /** Prints on System.out. */
  public void println(Node node) { println(node,System.out); }

  /** returns a String representation of the equation. */
  
  public String toString(Node node)
  {
	sb = new StringBuffer();
	acceptCatchingErrors(node,null);
	return sb.toString();
  }
  
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

  /** Add a string to buffer. Classes implementing PrintRulesI 
   * should call this add the */
  public void append(String s) { sb.append(s); }

  /** Adds a special print rule to be added for a given operator. 
   * TODO Allow special rules for other functions, i.e. not operators. */
  public void addSpecialRule(Operator op,PrintRulesI rules)
  {
  	specialRules.put(op,rules);
  }

/***************** visitor methods ********************************/

/** print the node with no brackets. */
private void printNoBrackets(Node node) throws ParseException
{
	node.jjtAccept(this,null);
}

/** print a node suronded by brackets. */
private void printBrackets(Node node) throws ParseException
{
	sb.append("(");
	printNoBrackets(node);
	sb.append(")");
}

/** print a unary operator. */
private Object visitUnary(ASTFunNode node, Object data) throws ParseException
{
	Node rhs = node.jjtGetChild(0);

	// now print the node
	sb.append(node.getOperator().getSymbol());
	// now the rhs
	if(rhs instanceof ASTFunNode && ((ASTFunNode) rhs).isOperator())
		printBrackets(rhs);	// -(-3) -(1+2) or !(-3)
	else
		printNoBrackets(rhs);
	
	return data;
}

public Object visit(ASTFunNode node, Object data) throws ParseException
{
	if(!node.isOperator()) return visitFun(node);
	if(node instanceof PrintRulesI)
	{
		((PrintRulesI) node).append(node,this);
		return null;
	}
	if(node.getOperator()==null)
	{
		throw new ParseException("Null operator in print for "+node);
	}
	if(specialRules.containsKey(node.getOperator()))
	{
		((PrintRulesI) specialRules.get(node.getOperator())).append(node,this);
		return null;
	}
	if(node.getPFMC() instanceof org.nfunk.jep.function.List)
	{	
		append("[");
			for(int i=0;i<node.jjtGetNumChildren();++i)
			{
				if(i>0) append(",");
				node.jjtGetChild(i).jjtAccept(this, null);
			}
			append("]");
		return null;
	}
		
	if(((XOperator) node.getOperator()).isUnary())
		return visitUnary(node,data);
	if(((XOperator) node.getOperator()).isBinary())
	{
		Node lhs = node.jjtGetChild(0);
		Node rhs = node.jjtGetChild(1);
		XOperator top = (XOperator) node.getOperator();
	
		if((mode & FULL_BRACKET)!= 0)
		{
			printBrackets(lhs);
		}
		else if(lhs instanceof ASTFunNode && ((ASTFunNode) lhs).isOperator())
		{
			XOperator lhsop = (XOperator) ((ASTFunNode) lhs).getOperator();
			if(top == lhsop)
			{
				if(top.getBinding() == XOperator.LEFT	// (1-2)-3 -> 1-2-3
					&& top.isAssociative() )
						printNoBrackets(lhs);
				else if(top.useBindingForPrint())
						printNoBrackets(lhs);
				else
						printBrackets(lhs);				// (1=2)=3 -> (1=2)=3
			}
			else if(top.getPrecedence() == lhsop.getPrecedence())
			{
				if(lhsop.getBinding() == XOperator.LEFT && lhsop.isAssociative())
						printNoBrackets(lhs);
				else if(lhsop.useBindingForPrint())
						printNoBrackets(lhs);
				else	printBrackets(lhs);
			} 				// (1=2)=3 -> (1=2)=3
			
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

		if((mode & FULL_BRACKET)!= 0)
		{
			printBrackets(rhs);
		}
		else if(rhs instanceof ASTFunNode && ((ASTFunNode) rhs).isOperator())
		{
			XOperator rhsop = (XOperator) ((ASTFunNode) rhs).getOperator();
			if(top == rhsop)
			{
				if(top.getBinding() == XOperator.RIGHT	// 1=(2=3) -> 1=2=3
					|| top.isAssociative() )			// 1+(2-3) -> 1+2-3
						printNoBrackets(rhs);
				else
						printBrackets(rhs);				// 1-(2+3) -> 1-(2-3)
			}
			else if(top.getPrecedence() == rhsop.getPrecedence())
			{
				if(top.getBinding() == XOperator.LEFT && top.isAssociative() )			// 1+(2-3) -> 1+2-3)
					printNoBrackets(rhs);	// a+(b-c) -> a+b-c
				else
					printBrackets(rhs);		// a-(b+c) -> a-(b+c)
			}
			else if(top.getPrecedence() > rhsop.getPrecedence()) // 1+(2*3) -> 1+2*3
						printNoBrackets(rhs);
			else
						printBrackets(rhs);
		}
		else
			printNoBrackets(rhs);
	}
	return null;
}

/** prints a standard function: fun(arg,arg) */
private Object visitFun(ASTFunNode node) throws ParseException
{
	sb.append(node.getName()+"(");
	for(int i=0;i<node.jjtGetNumChildren();++i)
	{
		if(i>0) sb.append(",");
		node.jjtGetChild(i).jjtAccept(this, null);
	}
	sb.append(")");

	return null;
}

  public Object visit(ASTVarNode node, Object data) throws ParseException  {
	sb.append(node.getName());
	return data;
  }

  public Object visit(ASTConstant node, Object data) {
	sb.append(node.getValue());
	return data;
  }
	/**
	 * Return the current print mode.
	 */
	public int getMode() {
		return mode;
	}
	
	/**
	 * Set printing mode.
	 * In full bracket mode the brackets each element in the tree will be suronded
	 * by brackets to indicate the tree structure. 
	 * In the default mode, (full bracket off) the number of brackets is
	 * minimised so (x+y)+z will be printed as x+y+z.
	 * @param mode which flags to change, typically FULL_BRACKET
	 * @param flag whether to switch this mode on or off
	 */
	public void setMode(int mode,boolean flag) {
		if(flag)
			this.mode |= mode;
		else
			this.mode ^= mode;
	}

/**
 * @return the maximum length printed per line
 */
public int getMaxLen() {
	return maxLen;
}

/**
 * Sets the maximium length printed per line.
 * If the value is not -1 then the string will be broken into chunks
 * each of which is less than the max lenght.
 * @param i the maximum length
 */
public void setMaxLen(int i) {
	maxLen = i;
}

}

/*end*/
