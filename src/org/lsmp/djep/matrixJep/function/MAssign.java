/*****************************************************************************

JEP - Java Math Expression Parser 2.24
	  December 30 2002
	  (c) Copyright 2002, Nathan Funk
	  See LICENSE.txt for license information.

*****************************************************************************/
package org.lsmp.djep.matrixJep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.matrixJep.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.values.*;

/**
 * A matrix enabled asignment function.
 * The lhs of an assigment must be a variable.
 * 
 * @author Rich Morris
 * Created on 23-Feb-2004
 */
public class MAssign extends Assign implements MatrixSpecialEvaluationI
{
	public MAssign()
	{
		numberOfParameters = 2;
	}

	/** The run method should not be called. 
	 * Use {@link #evaluate} instead.
	 */	
	public void run(Stack s) throws ParseException 
	{
		throw new ParseException("Eval should not be called by Evaluator"); 
	}

	/**
	 * A special methods for evaluating an assignment.
	 * When an asignment is encountered, first
	 * evaluate the rhs. Then set the value 
	 * of the lhs to the result.
	 */
	public MatrixValueI evaluate(MatrixNodeI node,MatrixEvaluator visitor,MatrixJep j) throws ParseException
	{
		if(node.jjtGetNumChildren()!=2)
			throw new ParseException("Assignment opperator must have 2 operators.");

		// evaluate the value of the righthand side. Left on top of stack
		
		MatrixValueI rhsVal = (MatrixValueI) node.jjtGetChild(1).jjtAccept(visitor,null);	

		// Set the value of the variable on the lhs. 
		Node lhsNode = node.jjtGetChild(0);
		if(lhsNode instanceof ASTMVarNode)
		{
			ASTMVarNode vn = (ASTMVarNode) lhsNode;
			MatrixVariableI var = (MatrixVariableI) vn.getVar();
			var.setMValue(rhsVal);
			return rhsVal;
		}
		throw new ParseException("Assignment should have a variable for the lhs.");
	}
}
