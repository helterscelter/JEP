/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep;

import org.nfunk.jep.*;
import org.nfunk.jep.type.Complex;
import org.lsmp.djep.groupJep.values.*;
/**
 * An extension of JEP with support for basic vectors and matricies.
 * Use this class instead of JEP if you wish to use vectors and matricies.
 *  
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class GroupJep extends JEP {
	protected GroupI group;
	
	public GroupJep(GroupI group) {
		super();
		this.group = group;
		super.numberFactory = group.getNumberFactory();
		opSet = new GOperatorSet(group);
	}

	public void addStandardFunctions()
	{
//		Certinally don't want the jep standard functions
//		super.addStandardFunctions();
		group.addStandardFunctions(this);
	}

	public void addStandardConstants()
	{
		group.addStandardConstants(this);
	}

	public GroupJep(JEP j) {
		super(j);
	}

	public GroupI getGroup()
	{
		return group;
	}
	/* (non-Javadoc)
	 * @see org.nfunk.jep.JEP#getComplexValue()
	 */
	public Complex getComplexValue() {
		Object num = this.getValueAsObject();
		if(num instanceof Complex) return (Complex) num;
		else if(num instanceof HasComplexValueI)
			return ((HasComplexValueI) num).getComplexValue();
		else if(num instanceof Number)
			return new Complex((Number) num);
		return super.getComplexValue();
	}

	/**
	 * A utility function which returns the complex aproximation of a number.
	 * @see HasComplexValueI
	 * @param num the object to be converted
	 * @return the complex aproximation or null if conversion to complex is not posible. 
	 **/
	public static Complex getComplexValue(Object num)
	{
		if(num instanceof Complex) return (Complex) num;
		else if(num instanceof HasComplexValueI)
			return ((HasComplexValueI) num).getComplexValue();
		else if(num instanceof Number)
			return new Complex((Number) num);
		else return null;
	}

}
