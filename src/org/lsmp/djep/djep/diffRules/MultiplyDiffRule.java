/* @author rich
 * Created on 04-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep.diffRules;

import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.lsmp.djep.djep.DJepI;
import org.lsmp.djep.djep.DiffRulesI;
import org.lsmp.djep.xjep.*;

/**
   * Diffrentiates a product with respect to var.
   * diff(y*z,x) -> diff(y,x)*z+y*diff(z,x)
   */
  public class MultiplyDiffRule implements DiffRulesI
  {
	private String name;
	//private DifferentationVisitor dv;

	private MultiplyDiffRule() {}
	public MultiplyDiffRule(String inName)
	{	  
	  //dv = inDv;
	  name = inName;
	}

	public String toString()
	{	  return name + "  \t\tdiff(f*g,x) -> diff(f,x)*g+f*diff(g,x)";  }
	public String getName() { return name; }
  	
	public Node differentiate(ASTFunNode node,String var,Node [] children,Node [] dchildren,DJepI djep) throws ParseException
	{
		OperatorSet op = djep.getOperatorSet();
		NodeFactory nf = djep.getNodeFactory();

	  int nchild = node.jjtGetNumChildren();
	  if(nchild==2) 
		  return nf.buildOperatorNode(op.getAdd(),
			nf.buildOperatorNode(op.getMultiply(),
			  dchildren[0],
			  djep.deepCopy(children[1])),
			nf.buildOperatorNode(op.getMultiply(),
			  djep.deepCopy(children[0]),
			  dchildren[1]));
	  else
		  throw new ParseException("Too many children "+nchild+" for "+node+"\n");
	}
  } /* end MultiplyDiffRule */
