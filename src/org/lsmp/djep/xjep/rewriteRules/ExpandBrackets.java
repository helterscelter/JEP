/* @author rich
 * Created on 01-Oct-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep.rewriteRules;

import org.lsmp.djep.xjep.RewriteRuleI;
import org.nfunk.jep.*;
import org.lsmp.djep.xjep.*;

/**
 * @author Rich Morris
 * Created on 01-Oct-2004
 */
public class ExpandBrackets implements RewriteRuleI {

	private NodeFactory nf;
	private OperatorSet opSet;
	private TreeUtils tu;
	private XJep xj;

	/**
	 * 
	 */
	public ExpandBrackets(XJep xj) {
		opSet = xj.getOperatorSet();
		tu = xj.getTreeUtils();
		nf = xj.getNodeFactory();
		this.xj = xj;
	}
	private ExpandBrackets() {}
	/* (non-Javadoc)
	 * @see org.lsmp.djep.xjep.RewriteRuleI#test(org.nfunk.jep.Node, org.nfunk.jep.Node[])
	 */
	public boolean test(ASTFunNode node, Node[] children) {
		if(!node.isOperator())	return false;
		XOperator op= (XOperator) node.getOperator();

		if(opSet.getMultiply() == op)
		{
			if(tu.getOperator(children[0]) == opSet.getAdd())
				return true;
			if(tu.getOperator(children[0]) == opSet.getSubtract())
				return true;
			if(tu.getOperator(children[1]) == opSet.getAdd())
				return true;
			if(tu.getOperator(children[1]) == opSet.getSubtract())
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.xjep.RewriteRuleI#apply(org.nfunk.jep.Node, org.nfunk.jep.Node[])
	 */
	public Node apply(ASTFunNode node, Node[] children) throws ParseException {
		OperatorSet opSet = xj.getOperatorSet();
		TreeUtils tu = xj.getTreeUtils();
		
		Operator lhsOp = tu.getOperator(children[0]); 
		Operator rhsOp = tu.getOperator(children[1]); 
		if(lhsOp == opSet.getAdd() || lhsOp == opSet.getSubtract())
		{ /* (a+b)*c --> (a*c)+(b*c) */
			return nf.buildOperatorNode(
				lhsOp,
				nf.buildOperatorNode(
					opSet.getMultiply(),
						children[0].jjtGetChild(0),
						xj.deepCopy(children[1])),
				nf.buildOperatorNode(
					opSet.getMultiply(),
						children[0].jjtGetChild(1),
						xj.deepCopy(children[1]))
						);
	
		}
		if(rhsOp == opSet.getAdd() || rhsOp == opSet.getSubtract())
		{	/* a*(b+c) -> (a*b)+(a*c) */
			return nf.buildOperatorNode(
				rhsOp,
				nf.buildOperatorNode(
					opSet.getMultiply(),
						xj.deepCopy(children[0]),
						children[1].jjtGetChild(0)),
				nf.buildOperatorNode(
					opSet.getMultiply(),
						xj.deepCopy(children[0]),
						children[1].jjtGetChild(1))
						);
		}
		throw new ParseException("ExpandBrackets at least one child must be + or -");
	}

}
