/*****************************************************************************

JEP - Java Math Expression Parser 2.24
      December 30 2002
      (c) Copyright 2002, Nathan Funk
      See LICENSE.txt for license information.

*****************************************************************************/

package org.lsmp.djep.xjep;

import org.nfunk.jep.*;

/**
 * This class is used for the evaluation of an expression. It uses the Visitor
 * design pattern to traverse the function tree and evaluate the expression
 * using a stack.
 * <p>
 * Function nodes are evaluated by first evaluating all the children nodes,
 * then applying the function class associated with the node. Variable and
 * constant nodes are evaluated by pushing their value onto the stack.

 * <p>
 * Some changes implemented by rjm. Nov 03.
 * Added hook to SpecialEvaluationI.
 * Clears stack before evaluation.
 * Simplifies error handeling by making visit methods throw ParseException.
 * Changed visit(ASTVarNode node) so messages not calculated every time. 
 */
public class XEvaluatorVisitor extends EvaluatorVisitor {

	/** Constructor. Initialize the stack member */
	public XEvaluatorVisitor() {
//		errorList = null;
//		symTab = null;
//		stack = new Stack();
	}

	/**
	 * Visit a variable node. The value of the variable is obtained from the
	 * symbol table (symTab) and pushed onto the stack.
	 */
	public Object visit(ASTVarNode node, Object data) throws ParseException {

		Variable var = node.getVar();
		if (var == null) {
			String message = "Could not evaluate " + node.getName() + ": ";
			throw new ParseException(message + " variable not set");
		}
		
		if(var.hasValidValue())
		{
			stack.push(var.getValue());
		} 
		else if(var instanceof XVariable)
		{
			Node equation = ((XVariable) var).getEquation();
			if(equation==null)
				throw new ParseException("Cannot find value of "+var.getName()+" no equation.");
			equation.jjtAccept(this,data);
			var.setValue(stack.peek());
		}
		else
		{
			throw new ParseException("Could not evaluate " + node.getName() + ": value not set");
		}

		return data;
	}
}
