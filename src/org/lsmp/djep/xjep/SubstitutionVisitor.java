/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public class SubstitutionVisitor extends DoNothingVisitor {

	private String names[];
	private Node replacements[];
	private XJepI xjep;
	public SubstitutionVisitor() {}

	/**
	 * Substitutes all occurences of variable var with replacement
	 * @param orig	the expresion we wish to perform the substitution on
	 * @param name	the name of the variable
	 * @param replacement	the expression var is substituted for
	 * @return the tree with variable replace (does not do a DeepCopy)
	 * @throws ParseException
	 */
	public Node substitute(Node orig,String name,Node replacement,XJepI xjep) throws ParseException
	{
		this.names = new String[]{name};
		this.replacements = new Node[]{replacement};
		this.xjep=xjep;
		Node res = (Node) nodeAccept(orig,null);
		return res;
	}

	public Node substitute(Node orig,String names[],Node replacements[],XJepI xjep) throws ParseException
	{
		this.names = names;
		this.replacements = replacements;
		this.xjep=xjep;
		Node res = (Node) nodeAccept(orig,null);
		return res;
	}

	public Object visit(ASTVarNode node, Object data) throws ParseException
	{
		for(int i=0;i<names.length;++i)
		{
			if(names[i].equals(node.getName()))
				return xjep.deepCopy(replacements[i]);
		}
		if(node.getVar().isConstant())
			return xjep.getNodeFactory().buildVariableNode(xjep.getSymbolTable().getVar(node.getName()));
			
		throw new ParseException("No substitution specified for variable "+node.getName());
	}
}
