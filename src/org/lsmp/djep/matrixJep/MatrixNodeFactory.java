/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.xjep.*;
import java.util.Stack;

/**
 * This class is used to create nodes of specified types.
 * It can be subclassed to change the nature of how nodes
 * are constructed. Generally there are two methods for creating
 * nodes, methods which take an existing node and methods which
 * take the components.
 * 
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public class MatrixNodeFactory extends NodeFactory {

	public static final Double ZERO = new Double(0.0);
	public static final Double ONE = new Double(1.0);
	public MatrixNodeFactory()
	{
	}
	
	/**
	 * Sets the children of node to be those specified in array.
	 * @param node the node whos children will be set.
	 * @param children an array of nodes which will be the children of the node.
	 */
	public void copyChildren(Node node,Node children[])
	{	
		int nchild = children.length; 
		node.jjtOpen();
		for(int i=0;i<nchild;++i)
		{
			children[i].jjtSetParent(node);
			node.jjtAddChild(children[i],i);
		}
		node.jjtClose();
	}
	
	/** Creates an ASTConstant node with specified value. **/
	public ASTConstant buildConstantNode(Object value)
	{
		ASTMConstant node  = new ASTMConstant(ParserTreeConstants.JJTCONSTANT);
		if(ZERO.equals(value)) node.setValue(ZERO);
		else if(ONE.equals(value)) node.setValue(ONE);
		else node.setValue(value);
		return node;
	}

	/** Creates a ASTConstant node with specified double value. **/
	public ASTConstant buildConstantNode(double val)
	{
		ASTMConstant node  = new ASTMConstant(ParserTreeConstants.JJTCONSTANT);
		if(val ==0.0) node.setValue(ZERO);
		else if(val ==1.0) node.setValue(ONE);
		else node.setValue(new Double(val));
		return node;
	}
	
	/** Create an ASTConstant with same value as argument. **/
	public ASTConstant buildConstantNode(ASTConstant node)
	{
		return buildConstantNode(node.getValue());
	}	

	/** Creates a ASTConstant whose value of applying the function to its arguments. */
	public ASTConstant buildConstantNode(ASTFunNode node,Node child1,Node child2) throws IllegalArgumentException,ParseException
	{
		Stack stack = new Stack();
		stack.push(TreeUtils.getValue(child1));
		stack.push(TreeUtils.getValue(child2));
		node.getPFMC().setCurNumberOfParameters(2);
		node.getPFMC().run(stack);
		return buildConstantNode(stack.pop());
	}

	/** Creates a ASTConstant whose value of applying the operator to its arguments. */
	public ASTConstant buildConstantNode(Operator op,Node child1,Node child2) throws IllegalArgumentException,ParseException
	{
		Stack stack = new Stack();
		stack.push(TreeUtils.getValue(child1));
		stack.push(TreeUtils.getValue(child2));
		op.getPFMC().setCurNumberOfParameters(2);
		op.getPFMC().run(stack);
		return buildConstantNode(stack.pop());
	}

	/** Creates a ASTConstant whose value of applying the operator to its arguments. */
	public ASTConstant buildConstantNode(Operator op,Node child1) throws IllegalArgumentException,ParseException
	{
		Stack stack = new Stack();
		stack.push(TreeUtils.getValue(child1));
		op.getPFMC().setCurNumberOfParameters(1);
		op.getPFMC().run(stack);
		return buildConstantNode(stack.pop());
	}

	/** Creates a ASTConstant whose value of applying the operator to its arguments. */
	public ASTConstant buildConstantNode(ASTFunNode node,Node children[]) throws IllegalArgumentException,ParseException
	{
		Stack stack = new Stack();
		for(int i=0;i<children.length;++i)
			stack.push(TreeUtils.getValue(children[i]));
		node.getPFMC().setCurNumberOfParameters(children.length);
		node.getPFMC().run(stack);
		return buildConstantNode(stack.pop()); // TODO what if it returns a vector!
	}

	/** Creates a ASTConstant whose value of applying the operator to its arguments. */
	public ASTConstant buildConstantNode(Operator op,Node children[]) throws IllegalArgumentException,ParseException
	{
		Stack stack = new Stack();
		for(int i=0;i<children.length;++i)
			stack.push(TreeUtils.getValue(children[i]));
		op.getPFMC().setCurNumberOfParameters(children.length);
		op.getPFMC().run(stack);
		return buildConstantNode(stack.pop()); // TODO what if it returns a vector!
	}

	/** creates a new ASTVarNode with the same name as argument. */ 
	public ASTVarNode buildVariableNode(ASTVarNode node)
	{
		   return buildVariableNode(node.getVar());
	}
	/** Creates a ASTVariable node with specified value. **/
	public ASTVarNode buildVariableNode(Variable var)
	{
		ASTMVarNode node  = new ASTMVarNode(ParserTreeConstants.JJTVARNODE);
		node.setVar(var);
		return node;
	}
	

	/**
	 * Builds a function with n arguments
	 * @param name of function.
	 * @param pfmc PostfixMathCommand for function.
	 * @param arguments the arguments to the function.
	 * @return top Node of expression 
	 */

	public ASTFunNode buildFunctionNode(String name,PostfixMathCommandI pfmc,Node[] arguments)
	{
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setFunction(name,pfmc);
		copyChildren(res,arguments);
		// TODO whats the dim?
		res.setDim(Dimensions.ONE);
		return res;		
	}

	/**
	 * Builds a function with n arguments
	 * @param node the properties (name and pfmc) of this node will be copied.
	 * @param arguments the arguments to the function.
	 * @return top Node of expression 
	 */
	public ASTFunNode buildFunctionNode(ASTFunNode node,Node[] children)
	{
		//MatrixNodeI children[] = (MatrixNodeI []) arguments;
		if(node.isOperator())
			return buildOperatorNode(node,children); 
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setFunction(node.getName(),node.getPFMC());
		copyChildren(res,children);

		PostfixMathCommandI pfmc = node.getPFMC();
		try
		{		
		if(pfmc instanceof BinaryOperatorI)
		{
			if(children.length!=2) throw new ParseException("Operator "+node.getName()+" must have two elements, it has "+children.length);
			BinaryOperatorI bin = (BinaryOperatorI) pfmc;
			Dimensions dim = bin.calcDim(((MatrixNodeI) children[0]).getDim(),((MatrixNodeI) children[1]).getDim());
			res.setDim(dim);
			return res;
		}
		else if(pfmc instanceof UnaryOperatorI)
		{
			if(children.length!=1) throw new ParseException("Operator "+node.getName()+" must have one elements, it has "+children.length);
			UnaryOperatorI uni = (UnaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(((MatrixNodeI) children[0]).getDim());
			res.setDim(dim);
			return res;
		}
		else if(pfmc instanceof NaryOperatorI)
		{
			Dimensions dims[] = new Dimensions[children.length];
			for(int i=0;i<children.length;++i)
				dims[i]=((MatrixNodeI) children[i]).getDim();
			//if(arguments.length!=1) throw new ParseException("Operator "+op.getName()+" must have one elements, it has "+arguments.length);
			NaryOperatorI uni = (NaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(dims);
			res.setDim(dim);
			return res;
		}
		else
		{
			res.setDim(Dimensions.ONE);
//			System.out.println("Warning: assuming 1 for dimensons of "+node.getName());
		}
		}
		catch(ParseException e) { return null; }
		return res;		
	}
	
	/** a function with no arguments. */
	public ASTFunNode buildFunctionNode(ASTFunNode node)
	{
		return buildFunctionNode(node,new Node[]{});
	}
	/** creates a unary function. */
	public ASTFunNode buildFunctionNode(ASTFunNode node,Node child)
	{
		return buildFunctionNode(node,new Node[]{child});		
	}
	/** creates a binary function. */
	public ASTFunNode buildFunctionNode(ASTFunNode node,Node lhs,Node rhs)
	{
		return buildFunctionNode(node,new Node[]{lhs,rhs});		
	}
	/** a function with no arguments. */
	public ASTFunNode buildFunctionNode(String name,PostfixMathCommandI pfmc)
	{
		return buildFunctionNode(name,pfmc,new Node[]{});
	}
	/** creates a unary function. */
	public ASTFunNode buildFunctionNode(String name,PostfixMathCommandI pfmc,Node child)
	{
		return buildFunctionNode(name,pfmc,new Node[]{child});		
	}
	/** creates a binary function. */
	public ASTFunNode buildFunctionNode(String name,PostfixMathCommandI pfmc,Node lhs,Node rhs)
	{
		return buildFunctionNode(name,pfmc,new Node[]{lhs,rhs});		
	}

	/**
	 * Builds a operator node with n arguments
	 * @param name of function.
	 * @param pfmc PostfixMathCommand for function.
	 * @param arguments the arguments to the function.
	 * @return top Node of expression 
	 */

	public ASTFunNode buildOperatorNode(Operator op,Node[] arguments)
	{
		MatrixNodeI children[] = new MatrixNodeI[arguments.length];
		for(int i=0;i<arguments.length;++i)
			children[i] = (MatrixNodeI) arguments[i];
			
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setOperator(op);
		copyChildren(res,arguments);
		PostfixMathCommandI pfmc = op.getPFMC();
		try
		{		
		if(pfmc instanceof BinaryOperatorI)
		{
			if(arguments.length!=2) throw new ParseException("Operator "+op.getName()+" must have two elements, it has "+arguments.length);
			BinaryOperatorI bin = (BinaryOperatorI) pfmc;
			Dimensions dim = bin.calcDim(children[0].getDim(),children[1].getDim());
			res.setDim(dim);
			return res;
		}
		else if(pfmc instanceof UnaryOperatorI)
		{
			if(arguments.length!=1) throw new ParseException("Operator "+op.getName()+" must have one elements, it has "+arguments.length);
			UnaryOperatorI uni = (UnaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(children[0].getDim());
			res.setDim(dim);
			return res;
		}
		else if(pfmc instanceof NaryOperatorI)
		{
			Dimensions dims[] = new Dimensions[children.length];
			for(int i=0;i<children.length;++i)
				dims[i]=((MatrixNodeI) children[i]).getDim();
			NaryOperatorI uni = (NaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(dims);
			res.setDim(dim);
			return res;

		}
		else
		{
			res.setDim(Dimensions.ONE);
			//System.out.println("Warning: assuming 1 for dimensons of "+op.getName());
		}
		}
		catch(ParseException e) { return null; }
		
		return res;		
	}

	public ASTFunNode buildOperatorNode(Operator op,Node[] arguments,Dimensions dim)
	{
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setOperator(op);
		res.setDim(dim);
		copyChildren(res,arguments);
		return res;		
	}

	/** Creates a new operator node with same operator ans node and given arguments. */
	public ASTFunNode buildOperatorNode(ASTFunNode node,Node[] arguments)
	{
		return buildOperatorNode(node.getOperator(),arguments);		
	}

	/** a function with no arguments. */
	public ASTFunNode buildOperatorNode(ASTFunNode node)
	{
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setOperator(node.getOperator());
		res.setDim(Dimensions.ONE);
		return res;		
	}
	/** creates a unary function. */
	public ASTFunNode buildOperatorNode(ASTFunNode node,Node child)
	{
		return buildOperatorNode(node,new Node[]{child});		
	}
	/** creates a binary function. */
	public ASTFunNode buildOperatorNode(ASTFunNode node,Node lhs,Node rhs)
	{
		return buildOperatorNode(node,new Node[]{lhs,rhs});		
	}
	/** a function with no arguments. */
	public ASTFunNode buildOperatorNode(Operator op)
	{
		ASTMFunNode res = new ASTMFunNode(ParserTreeConstants.JJTFUNNODE);
		res.setOperator(op);
		res.setDim(Dimensions.ONE);
		return res;		
	}
	/** creates a unary function. */
	public ASTFunNode buildOperatorNode(Operator op,Node child)
	{
		return buildOperatorNode(op,new Node[]{child});		
	}
	/** creates a binary function. */
	public ASTFunNode buildOperatorNode(Operator op,Node lhs,Node rhs)
	{
		return buildOperatorNode(op,new Node[]{lhs,rhs});		
	}
	
}
