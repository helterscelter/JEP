/*****************************************************************************

JEP - Java Expression Parser
    JEP is a Java package for parsing and evaluating mathematical 
	expressions. It currently supports user defined variables, 
	constant, and functions. A number of common mathematical 
	functions and constants are included.

Author: Nathan Funk
Copyright (C) 2000 Nathan Funk

    JEP is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    JEP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JEP; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*****************************************************************************/


package org.nfunk.jep;

import java.util.*;
import org.nfunk.jep.function.PostfixMathCommandI;

/**
 * This class is used for the evaluation of an expression. It uses the Visitor
 * design pattern to traverse the function tree and evaluate the expression
 * using a stack.
 * <p>
 * Function nodes are evaluated by first evaluating all the children nodes,
 * then applying the function class associated with the node. Variable and
 * constant nodes are evaluated by pushing their value onto the stack.
 */
public class EvaluatorVisitor implements ParserVisitor
{
	/** Stack used for evaluating the expression */
	private Stack stack;
	
	/** The current error list */
	private Vector errorList;
	
	/** The symbol table for variable lookup */
	private SymbolTable symTab;
	
	/** Flag for errors during evaluation */
	private boolean errorFlag;
	
	/** Debug flag */
	private static final boolean debug = false;
	
	
	/** Constructor. Initialize the stack member */
	public EvaluatorVisitor() {
		errorList = null;
		symTab = null;
		stack = new Stack();
	}
	
	/**
	 * Adds an error message to the list of errors
	 */
	private void addToErrorList(String errorStr) {
		if (errorList != null) {
			errorList.addElement(errorStr);
		}
	}
	
	/**
	 * Returns the value of the expression as an object. The expression
	 * tree is specified with its top node. The algorithm uses a stack
	 * for evaluation.
	 * <p>
	 * The <code>errorList_in</code> parameter is used to
	 * add error information that may occur during the evaluation. It is not
	 * required, and may be set to <code>null</code> if no error information is
	 * needed.
	 * <p>
	 * The symTab parameter can be null, if no variables are expected in the
	 * expression. If a variable is found, an error is added to the error list.
	 * <p>
	 * An exception is thrown, if an error occurs during evaluation.
	 * @return The value of the expression as an object.
	 */
	public Object getValue(Node topNode, Vector errorList_in,
						   SymbolTable symTab_in)
						   throws Exception {
		
		// check if arguments are ok
		if (topNode == null) {
			throw new IllegalArgumentException(
				"topNode parameter is null");
		}
		
		// set member vars
		errorList = errorList_in;
		symTab = symTab_in;
		errorFlag = false;

		// evaluate by letting the top node accept the visitor
		topNode.jjtAccept(this, null);
		
		// something is wrong if not exactly one item remains on the stack
		// or if the error flag has been set
		if (errorFlag || stack.size() != 1) {
			throw new Exception(
				"EvaluatorVisitor.getValue(): Error during evaluation");
		}

		// return the value of the expression
		return stack.pop();
	}
	
	/**
	 * This method should never be called when evaluation a normal
	 * expression.
	 */
	public Object visit(SimpleNode node, Object data) {
		return data;
	}
	
	/**
	 * This method should never be called when evaluating a normal
	 * expression.
	 */
	public Object visit(ASTStart node, Object data) {
		return data;
	}
	
	/**
	 * Visit a function node. The values of the child nodes
	 * are first pushed onto the stack. Then the function class associated
	 * with the node is used to evaluate the function.
	 */
	public Object visit(ASTFunNode node, Object data) {
		PostfixMathCommandI pfmc;
		
		if (node == null) return null;
		
		if (debug == true) {
			System.out.println("Stack size before childrenAccept: " + stack.size());
		}
		
		// evaluate all children (each leaves their result on the stack)
		data = node.childrenAccept(this, data);
		
		if (debug == true) {
			System.out.println("Stack size after childrenAccept: " + stack.size());
		}

		// check if the function class is set
		pfmc = node.getPFMC();
		if (pfmc == null) {
			addToErrorList("No function class associated with "
							+ node.getName());
			return data;
		}
		
		if (pfmc.getNumberOfParameters() == -1) {
			// need to tell the class how many parameters it can take off
			// the stack because it accepts a variable number of params
			pfmc.setCurNumberOfParameters(node.jjtGetNumChildren());
		}

		// try to run the function
		try {
			pfmc.run(stack);
		} catch (ParseException e) {
			addToErrorList(e.getMessage());
			errorFlag = true;
		}
		
		if (debug == true) {
			System.out.println("Stack size after run: " + stack.size());
		}

		return data;
	}

	/**
	 * Visit a variable node. The value of the variable is obtained from the
	 * symbol table (symTab) and pushed onto the stack.
	 */
	public Object visit(ASTVarNode node, Object data) {
		String message = "Could not evaluate " + node.getName() + ": ";

		if (symTab == null) {
			message += "the symbol table is null";
			addToErrorList(message);
			return data;
		}
		
		// TODO: optimize (table lookup is costly?)
		Object temp = symTab.get(node.getName());
			
		if (temp == null) {
			message += "the variable was not found in the symbol table";
			addToErrorList(message);
		} else {
			// all is fine
			// push the value on the stack
			stack.push(temp);
		}

		return data;
	}

	/**
	 * Visit a constant node. The value of the constant is pushed onto the
	 * stack.
	 */
	public Object visit(ASTConstant node, Object data) {
		stack.push(node.getValue());
		return data;
	}
}
