/* @author rich
 * Created on 23-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import org.nfunk.jep.*;
import org.lsmp.djep.xjep.*;
import java.util.*;
/**
 * @author Rich Morris
 * Created on 23-Nov-2003
 */
public class DSymbolTable extends SymbolTable {
	private VariableFactoryI vf;
	public DSymbolTable(VariableFactoryI varFac)
	{
		super(varFac);
		vf=varFac;
	}
	public PartialDerivative getPartialDeriv(String name,String dnames[])
	{
		DVariable var = (DVariable) getVar(name);
		return var.getDerivative(dnames);
	}
	
	public SymbolTable newInstance()
	{
		return new DSymbolTable(vf);
	}

	public void clearValues()
	{
		for(Enumeration e = this.elements(); e.hasMoreElements(); ) 
		{
			DVariable var = (DVariable) e.nextElement();
			var.invalidateAll();
		}
	}	
	
	public void print(PrintVisitor pv)
	{
		for(Enumeration e = this.elements(); e.hasMoreElements(); ) 
		{
			DVariable var = (DVariable) e.nextElement();
			var.print(pv);
		}

	}	
}
