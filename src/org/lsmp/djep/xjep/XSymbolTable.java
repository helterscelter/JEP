/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.lsmp.djep.xjep;
import java.util.*;
import org.nfunk.jep.*;

/**
 * An extension of the symbol table with a few new features.
 * 
 * @author Rich Morris
 * Created on 18-Mar-2004
 */
public class XSymbolTable extends SymbolTable
{
	/**
	 * Create a new XSymbolTable with the given variable factory.
	 */
	public XSymbolTable(VariableFactory varFac)
	{
		super(varFac);
	}
	
	/** Creates a new SymbolTable with the same variable factory as this. */
	public SymbolTable newInstance()
	{
		return new XSymbolTable(this.getVariableFactory());
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
	
	/** Copy the values of all constants into this from the supplied symbol table. */
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
