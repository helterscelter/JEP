/*****************************************************************************

JEP - Java Math Expression Parser 2.24
	  December 30 2002
	  (c) Copyright 2002, Nathan Funk
	  See LICENSE.txt for license information.

*****************************************************************************/
package org.lsmp.djep.vectorJep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.matrixJep.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.values.*;

public class Assignment extends Assign implements MatrixSpecialEvaluationI
{
	public Assignment()
	{
		numberOfParameters = 2;
	}

/*	public MatrixNodeI preprocess(MatrixNodeI node,MatrixNodeI children[],MatrixDJep j) throws ParseException
	{
		Operator op = ((ASTOpNode) node).getOperator();
		if(node.jjtGetNumChildren()!=2) throw new ParseException("Operator "+op.getName()+" must have two elements, it has "+children.length);
		Dimensions rhsDim = children[1].getDim();
		MatrixVariable var = (MatrixVariable) ((ASTVarNode) children[0]).getVar();
		var.setDimensions(rhsDim);
		var.setEquation(j.deepCopy(children[1]));
		var.setMValue(Tensor.getInstance(rhsDim));
		return (ASTMOpNode) ((MatrixNodeFactory) j.getNodeFactory()).buildOperatorNode(op,children,rhsDim);
	}
*/
	/**
	 * When an asignment is encountered, first
	 * evaluate the rhs. Then set the value 
	 * of the lhs to the result.
	 * Also puts the result on the stack. 
	 */
	
	public void run(Stack s) throws ParseException 
	{
		throw new ParseException("Eval should not be called by Evaluator"); 
	}

	public MatrixValueI evaluate(MatrixNodeI node,MatrixEvaluator visitor,MatrixDJep j) throws ParseException
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
			MatrixVariable var = (MatrixVariable) vn.getVar();
			MatrixValueI val = var.getMValue();
			val.setEles(rhsVal);
			var.setValidValue(true);
			return (MatrixValueI) rhsVal;
		}
		throw new ParseException("Assignment should have a variable for the lhs.");

	}
}
