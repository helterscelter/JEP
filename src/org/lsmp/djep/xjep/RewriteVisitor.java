   
package org.lsmp.djep.xjep;
//import org.lsmp.djep.matrixParser.*;
import org.nfunk.jep.*;

/**
 * Simplifies an expression.
 * To use
 * <pre>
 * JEP j = ...; Node in = ...;
 * SimplificationVisitor sv = new SimplificationVisitor(tu);
 * Node out = sv.simplify(in);
 * </pre>
 * 
 * <p>
 * Its intended to completly rewrite this class to that simplification
 * rules can be specified by strings in a way similar to DiffRulesI.
 * It also would be nice to change the rules depending on the type of
 * arguments, for example matrix multiplication is not commutative.
 * But some of the in built rules exploit commutativity.
 * 
 * @author Rich Morris
 * Created on 20-Jun-2003
 */

public class RewriteVisitor extends DoNothingVisitor
{
  private NodeFactory nf;
  private OperatorSet opSet;
  private TreeUtils tu;
  private XJep xj;
  private RewriteRuleI rules[];
  private boolean simp=false;
  public RewriteVisitor()
  {
  }

  /** must be implemented for subclasses. **/
  public Node rewrite(Node node,XJep xjep,RewriteRuleI rules[],boolean simplify) throws ParseException,IllegalArgumentException
  {
  	xj = xjep;
	nf = xjep.getNodeFactory();
	opSet = xjep.getOperatorSet();
	tu = xjep.getTreeUtils();
	this.rules = rules;
	this.simp = simplify;
	if(this.rules.length==0) return node;
	
	if (node == null) 
		throw new IllegalArgumentException(
			"topNode parameter is null");
	Node res = (Node) node.jjtAccept(this,null);
	return res;
  }

	
	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		Node children[] = acceptChildrenAsArray(node,data);
		TreeUtils.copyChildrenIfNeeded(node,children);
		for(int i=0;i<rules.length;++i)
		{
			if(rules[i].test(node,children))
			{
				Node newNode = rules[i].apply(node,children);
				if(simp)
					newNode = xj.simplify(newNode);
				return newNode.jjtAccept(this,data);
			}
		}
		return node;
	}
}
