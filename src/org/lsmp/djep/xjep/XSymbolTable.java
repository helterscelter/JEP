/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.lsmp.djep.xjep;
import java.util.*;
import org.nfunk.jep.*;

public class XSymbolTable extends SymbolTable
{
	private VariableFactory vf;
	public XSymbolTable(VariableFactory varFac)
	{
		super(varFac);
		vf = varFac;
	}
	
	/** Creates a new SymbolTable with the same variable factory as this. */
	public SymbolTable newInstance()
	{
		return new XSymbolTable(vf);
	}

	/** Prints the contents of the symbol table displaying its equations and value. */	
	public void print(PrintVisitor pv)
	{
		for(Enumeration e = this.elements(); e.hasMoreElements(); ) 
		{
			XVariable var = (XVariable) e.nextElement();
			pv.append(var.toString(pv)+"\n");
			// TODO watchout for posible conflict with overriding pv's string buffer
		}
	}	
	
	/** Copy the values of all constants from the supplied symbol table. */
	public void copyConstants(SymbolTable symTab)
	{
		for(Enumeration e = symTab.elements(); e.hasMoreElements(); ) 
		{
			Variable var = (Variable) e.nextElement();
			if(var.isConstant())
				this.addConstant(var.getName(),var.getValue());
		}
	}

}
