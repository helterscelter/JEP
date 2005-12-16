/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep.function;

import org.nfunk.jep.*;
/**
 * An assignment operator so we can do
 * x=3+4.
 * This function implements the SpecialEvaluationI interface
 * so that it handles seting the value of a variable. 
 * @author Rich Morris
 * Created on 18-Nov-2003
 */
public class Assign extends PostfixMathCommand implements CallbackEvaluationI {

	public Assign() {
		super();
		numberOfParameters = 2;
	}

	/** For assignment set the value of the variable on the lhs to value returned by evaluating the righthand side.
	 *   
	 */
	public Object evaluate(Node node,EvaluatorI pv) throws ParseException
	{
		if(node.jjtGetNumChildren()!=2)
			throw new ParseException("Assignment opperator must have 2 operators.");

		// evaluate the value of the righthand side.
		Object rhsVal = pv.eval(node.jjtGetChild(1));	

		// Set the value of the variable on the lhs. 
		Node lhsNode = node.jjtGetChild(0);
		if(lhsNode instanceof ASTVarNode)
		{
			ASTVarNode vn = (ASTVarNode) lhsNode;
			Variable var = vn.getVar();
			var.setValue(rhsVal);
			return rhsVal;
		}
		throw new ParseException("Assignment should have a variable for the lhs.");
	}
}
