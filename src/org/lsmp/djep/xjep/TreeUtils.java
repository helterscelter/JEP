/*
 * Created on 16-Jun-2003 by Rich webmaster@pfaf.org
 * www.comp.leeds.ac.uk/pfaf/lsmp
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

package org.lsmp.djep.xjep;

import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/**
 * A set of Utility functions for working with JEP expression trees.
 * Main methods are
 * <ul>
 * <li> {@link #isConstant isConstant} test if its a constant. Many other is... methods.
 * <li> {@link #getValue getValue} extracts the value from a node without needing to cast and check types.
 * </ul>
 * @author rich
 */
public class TreeUtils {
	private static TreeUtils INSTANCE = new TreeUtils();
	
	private static final DoubleNumberFactory dnf = new DoubleNumberFactory();
	/** Real zero */
	public static final Double ZERO = (Double) dnf.createNumber("0.0");
	/** Real One */
	public static final Double ONE = (Double) dnf.createNumber("1.0");
	/** Real Minus One */
	public static final Double MINUSONE = (Double) dnf.createNumber("-1.0");
	/** Complex Zero **/
	public static final Complex CZERO = new Complex(0.0,0.0);
	/** Complex One **/
	public static final Complex CONE = new Complex(1.0,0.0);
	/** Complex i **/
	public static final Complex CI = new Complex(0.0,1.0);
	/** Complex Minus One **/
	public static final Complex CMINUSONE = new Complex(-1.0,0.0);
	/** Complex Minus i **/
	public static final Complex CMINUSI = new Complex(0.0,-1.0);

	/**
	 * default constructor is private class can never in insantated.
	 */
	protected TreeUtils() {}
	public static TreeUtils getInstance() { return INSTANCE; }
	
	/**
	 * Returns the value represented by node
	 * @throws IllegalArgumentException if given something which is not an ASTConstant
	 */
	static public String getName(Node node) throws IllegalArgumentException
	{
		if(isVariable(node))
			return ((ASTVarNode) node).getName();
		if(isFunction(node))
			return ((ASTFunNode) node).getName();
		
		throw new IllegalArgumentException("Tried to find the name of constant node");
	}

	/** gets the PostfixMathCommand with a given name. */
	/*
	public PostfixMathCommandI getPfmc(String name)
	{
		return (PostfixMathCommandI) myFunTab.get(name);
	}
	*/
	
	/**
	 * Returns the value represented by node
	 * @throws IllegalArgumentException if given something which is not an ASTConstant
	 */
	static public Object getValue(Node node) throws IllegalArgumentException
	{
		if(!isConstant(node)) throw new IllegalArgumentException("Tried to find the value of a non constant node");
		return ((ASTConstant) node).getValue();
	}
	
	/**
	 * Returns the double value represented by node
	 * @throws IllegalArgumentException if given something which is not an ASTConstant with a Double value
	 */
	static public double doubleValue(Node node) throws IllegalArgumentException
	{
		return ((Double) getValue(node)).doubleValue();
	}

	/**
	 * Returns the Complex value represented by node
	 * @throws IllegalArgumentException if given something which is not an ASTConstant with a Complex value
	 */
	static public Complex complexValue(Node node) throws IllegalArgumentException
	{
		return ((Complex) getValue(node));
	}

	/**
	 * returns true if node is a ASTConstant 
	 */
	static public boolean isConstant(Node node)
	{
		return (node instanceof ASTConstant);
	}
	 
	/**
	 * returns true if node is a ASTConstant with Double value 
	 */
	static public boolean isReal(Node node)
	{
			return (node instanceof ASTConstant)
			 && ( ((ASTConstant) node).getValue() instanceof Number );
	}
	
	/**
	 * returns true if node is a ASTConstant with value Double(0) or Complex(0,0) 
	 */
	static public boolean isZero(Node node)
	{
		   return ( isReal(node)
					&& ( ((ASTConstant) node).getValue().equals(ZERO)) )
				||( isComplex(node)
					&& ( ((Complex) ((ASTConstant) node).getValue()).equals(CZERO,0.0) ) );
	}

	/**
	 * returns true if node is a ASTConstant with value Double(0) or Complex(0,0)
	 * @param tol	tolerence for testing for zero
	 */
	
	static public boolean isZero(Node node,double tol)
	{
		   return ( isReal(node)
					&&
					(  (((ASTConstant) node).getValue().equals(ZERO)) )
					 || Math.abs(doubleValue(node)) < tol )
				||( isComplex(node)
					&& ( ((Complex) ((ASTConstant) node).getValue()).equals(CZERO,tol) ) );
	}

	/**
	 * returns true if node is a ASTConstant with value Double(1) or Complex(1,0) 
	 */
	static public boolean isOne(Node node)
	{
		return ( isReal(node)
				 && ( ((ASTConstant) node).getValue().equals(ONE)) )
			 ||( isComplex(node)
				 && ( ((Complex) ((ASTConstant) node).getValue()).equals(CONE,0.0) ) );
	}

	/**
	 * returns true if node is a ASTConstant with value Double(-1) or Complex(-1,0) 
	 */
	static public boolean isMinusOne(Node node)
	{
		return ( isReal(node)
				 && ( ((ASTConstant) node).getValue().equals(MINUSONE)) )
			 ||( isComplex(node)
				 && ( ((Complex) ((ASTConstant) node).getValue()).equals(CMINUSONE,0.0) ) );
	}
	/** 
	 * returns true if node is a ASTConstant with a Infinite component
	 * TODO do propper treatment of signed infinity 
	 */

	static public boolean isInfinity(Node node)
	{
		if(isReal(node))
		{
			Double dub = (Double) ((ASTConstant) node).getValue();
			return dub.isInfinite();
		}
		if(isComplex(node))
		{
			Complex z = (Complex) ((ASTConstant) node).getValue();
			return Double.isInfinite(z.re()) 
				|| Double.isInfinite(z.im());
		}
		return false;
	}

	/**
	 * returns true if node is a ASTConstant with a NaN component 
	 */
	static public boolean isNaN(Node node)
	{
		if(isReal(node))
		{
			Double dub = (Double) ((ASTConstant) node).getValue();
			return dub.isNaN();
		}
		if(isComplex(node))
		{
			Complex z = (Complex) ((ASTConstant) node).getValue();
			return Double.isNaN(z.re()) 
				|| Double.isNaN(z.im());
		}
		return false;
	}

	/**
	 * returns true if node is an ASTConstant with a negative Double value 
	 */
	static public boolean isNegative(Node node)
	{
			return isReal(node)
					 && ( ((Double) ((ASTConstant) node).getValue()).doubleValue() < 0.0 );
	}

	/**
	 * returns true if node is an ASTConstant with a positive Double value 
	 */
	static public boolean isPositive(Node node)
	{
			return isReal(node)
					 && ( ((Double) ((ASTConstant) node).getValue()).doubleValue() > 0.0 );
	}

	/**
	 * returns true if node is an ASTConstant of type Complex
	 */
	static public boolean isComplex(Node node)
	 {
			return isConstant(node)
				 && ( ((ASTConstant) node).getValue() instanceof Complex );
	 }

	/**
	 * returns true if node is an ASTVarNode
	 */
	static public boolean isVariable(Node node)
	{
	   return (node instanceof ASTVarNode);
	}
	
	/**
	 * returns true if node is an ASTOpNode
	 */
	static public boolean isOperator(Node node)
	{
	   return (node instanceof ASTFunNode) && ((ASTFunNode) node).isOperator();
	}

	static public boolean isBinaryOperator(Node node)
	{
	   if(isOperator(node))
	   {
	   		return ((ASTFunNode) node).getOperator().isBinary();
	   }
	   return false;
	}

	static public boolean isUnaryOperator(Node node)
	{
	   if(isOperator(node))
	   {
			return ((ASTFunNode) node).getOperator().isUnary();
	   }
	   return false;
	}

	/**
	 * returns true if node is an ASTOpNode of the specific type.
	 */
	static public Operator getOperator(Node node)
	{
	   if(isOperator(node))
	   	return ((ASTFunNode) node).getOperator();
	   else return null;
	}

	/**
	 * returns true if node is an ASTFunNode
	 */
	static public boolean isFunction(Node node)
	{
	   return (node instanceof ASTFunNode);
	}
	
	/**
	 * 
	 * @param node
	 * @param children
	 * @return
	 */
	public static Node copyChildrenIfNeeded(Node node,Node children[]) throws ParseException
	{
		int n=node.jjtGetNumChildren();
		if(n!=children.length)
			throw new ParseException("copyChildrenIfNeeded: umber of children of node not the same as supplied children");
		for(int i=0;i<n;++i)
			if(node.jjtGetChild(i) != children[i])
			{
				node.jjtAddChild(children[i],i);
				children[i].jjtSetParent(node);
			}
		return node;
	}

	static public Node[] getChildrenAsArray(Node node)
	{
		int n = node.jjtGetNumChildren();
		Node[] children = new Node[n];
		for(int i=0;i<n;++i)
			children[i]=node.jjtGetChild(i);
		return children;
	}
}
