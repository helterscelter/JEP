/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * An assignment operator so we can do
 * x=3+4.
 * This function implements the SpecialEvaluationI interface
 * so that it handles seting the value of a variable. 
 * @author Rich Morris
 * Created on 18-Nov-2003
 */
public class XAssign extends Assign implements CommandVisitorI {

	public XAssign() {}
	{
		numberOfParameters = 2;
	}

	/**
	 * In the pre-process stage, set the equation of the lhs variable to the rhs equation.
	 */
	public Node process(Node node,Node children[],XJep xjep) throws ParseException
	{
		if(node.jjtGetNumChildren()!=2)
			throw new ParseException("Assignment opperator must have 2 operators.");

		// evaluate the value of the righthand side. Left on top of stack

		// Set the value of the variable on the lhs. 
		Node lhsNode = children[0];
		if(lhsNode instanceof ASTVarNode)
		{
			ASTVarNode vn = (ASTVarNode) lhsNode;
			XVariable var = (XVariable) vn.getVar();
			var.setEquation(xjep.deepCopy(children[1]));
			TreeUtils.copyChildrenIfNeeded(node,children);
			return node;
		}
		throw new ParseException("Assignment should have a variable for the lhs.");
	}
}
