/* @author rich
 * Created on 14-Dec-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.sjep;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 14-Dec-2004
 */
public class PVariable extends AbstractPNode {

	XVariable variable;
	/**
	 * 
	 */
	public PVariable(PolynomialCreator pc,XVariable var) throws ParseException {
		super(pc);
		this.variable = var;
	}

	public boolean equals(PNodeI node)
	{
		if(node instanceof PVariable)
			if(variable.equals(((PVariable)node).variable))
				return true;	

		return false;
	}

	/**
	this < arg ---> -1
	this > arg ---> 1
	*/
	public int compareTo(PVariable vf)
	{
			return variable.getName().compareTo(vf.variable.getName());
	}
	
	public String toString()
	{
		return variable.getName();
	}
	
	public Node toNode() throws ParseException
	{
		return pc.nf.buildVariableNode(variable);
	}
	
	public PNodeI expand()	{ return this;	}
}
