/* @author rich
 * Created on 29-Oct-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;
import org.nfunk.jep.*;
import org.lsmp.djep.xjep.*;
/**
 * Contains infomation about a PartialDerivative of a variable.
 * Should  
 * @author Rich Morris
 * Created on 29-Oct-2003
 */
public class PartialDerivative extends XVariable  {

	private DVariable root;
	private String dnames[] = null;
	private String printString;
	/**
	 * Protected constructor, should only be constructed
	 * through the findDerivative method in {@link DVariable DVariable}.
	**/ 
	protected PartialDerivative(DVariable var, String derivnames[],Node deriv)
	{
		super(var.getName());
		root = var;
		dnames = derivnames;
		setEquation(deriv);
		printString = DVariable.makeDerivString(root.getName(),derivnames);
	}
	
	public String getName() { return printString; }
	
	/**
	 * Every partial derivative has a root variable
	 * for instance the root variable of dy/dx is y.
	 * This method returns than variable.
	 */
	public DVariable getRoot() { return root; }
	public String[] getDnames() { return dnames; }

	public String toString()
	{
		return printString;
	}
	
	public PartialDerivative findDerivative(String name,DJepI jep)
		throws ParseException
	{
		return root.findDerivative(this,name,jep);
	}	
}
