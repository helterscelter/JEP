/* @author rich
 * Created on 28-Feb-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.*;

/**
 * Variables which have their equations stored.
 * 
 * @author Rich Morris
 * Created on 28-Feb-2004
 */
public class XVariable extends Variable {
	private Node equation=null;

	public XVariable(String name) {
		super(name);
	}

	public XVariable(String name, Object value) {
		super(name, value);
	}

	/** Does this variable has an associated equation? **/
	public boolean hasEquation() { return equation != null; }
	/** sets the equation */
	public void setEquation(Node eqn)
	{
		equation = eqn; 
		this.setValidValue(false);
	}
	/** get the equation */
	public Node getEquation() { return equation; }
	
	/**
	 * Calculates the value for the variables equation and returns that value.  
	 * 
	 */
	public Object calcValue(XJep jep) throws Exception
	{
		if(equation == null ) return getValue();
		Object val = jep.evaluate(equation);
		setValue(val);
		return val;
	}

	/** Returns a string rep of variable with its equation and value. */ 
	public String toString(PrintVisitor pv)
	{
		StringBuffer sb = new StringBuffer(name);
		sb.append(": val "+getValue() );
		if(!hasValidValue()) sb.append("(invalid)");
		sb.append(" ");
		if(getEquation()!=null) sb.append(pv.toString(getEquation()));
		else sb.append("no equation");
		return sb.toString();
	}
}
