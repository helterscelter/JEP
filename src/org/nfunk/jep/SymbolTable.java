/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.nfunk.jep;
import java.util.*;

public class SymbolTable extends Hashtable
{
	private VariableFactoryI vf;
	public SymbolTable(VariableFactoryI varFac)
	{
		vf = varFac;
	}
	
	/**
	 * @deprecated The getValue or getVar methods should be used instead. 
	 */
	public Object get(Object key) { return getValue(key); }
	

	/** finds the value of the variable with the given name. */
	public Object getValue(Object key)
	{
		Variable var = (Variable) super.get(key);
		return var.getValue();
	}
	
	/** finds the variable with given name. **/
	public Variable getVar(String name)
	{
		Variable var = (Variable) super.get(name);
		if(var != null)
		{
			return var; 
		}
		else
		{
			var = vf.createVariable(name,null);
			super.put(name,var);
			return var;
		}
	}

	/**
	 * @deprecated The setVarValue or makeVar methods should be used instead.
	 */
	public Object put(Object key,Object val)
	{
		return setVarValue((String) key,val);
	}

	/**
	 * Sets the value of variable with the given name.
	 * Creates a new variable if needed. 
	 */
	public Object setVarValue(String name,Object val)
	{
		//super.put(Object a,Object b);
		Variable var = (Variable) super.get(name);
		if(var != null)
		{
			var.setValue(val);
			return val; 
		} 
		else return super.put(name,vf.createVariable(name,val));			
	}

	/** Create a variable with the given name and value.
	 * If the variable exists then its value will be set.
	 * @return the Variable.
	 */
	public Variable makeVarIfNeeded(String name,Object val)
	{
		Variable var = (Variable) super.get(name);
		if(var != null)
		{
			var.setValue(val);
			return var; 
		}
		else
		{
			var = vf.createVariable(name,val);
			super.put(name,var);
			return var;
		}
	}

	/** Create a constant with the given name and value.
	 * If the variable exists then all hell brakes loose.
	 * @return the Variable.
	 */
	public Variable makeConstant(String name,Object val)
	{
		Variable var = (Variable) super.get(name);
		if(var != null)
		{
			System.err.println("Making an existing variable "+var.toString()+" into a constant");
			if(!var.setValue(val))
				System.err.println("Cannot change the value of the variable");
			var.setIsConstant(true);
			var.setValidValue(true);
			return var; 
		}
		else
		{
			var = vf.createVariable(name,val);
			var.setIsConstant(true);
			var.setValidValue(true);
			super.put(name,var);
			return var;
		}
	}
	/** Create a variable with the given name and value.
	 * If the variable exists its value will not be changed.
	 * @return the Variable.
	 */
	public Variable makeVarIfNeeded(String name)
	{
		Variable var = (Variable) super.get(name);
		if(var != null)
		{
			return var; 
		}
		else
		{
			var = vf.createVariable(name,null);
			super.put(name,var);
			return var;
		}
	}

	/**
	 * Returns a list of variable, one per line.
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(Enumeration e = this.elements(); e.hasMoreElements(); ) 
		{
			Variable var = (Variable) e.nextElement();
			sb.append(var.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Returns a list of variable, one per line.
	 */
	public void clearValues()
	{
		for(Enumeration e = this.elements(); e.hasMoreElements(); ) 
		{
			Variable var = (Variable) e.nextElement();
			if(!var.isConstant()) var.setValidValue(false);
		}
	}
	
	public SymbolTable newInstance()
	{
		return new SymbolTable(vf);
	}

	public void copyConstants(SymbolTable symTab)
	{
		for(Enumeration e = symTab.elements(); e.hasMoreElements(); ) 
		{
			Variable var = (Variable) e.nextElement();
			if(var.isConstant())
				this.makeConstant(var.getName(),var.getValue());
		}
	}
}
