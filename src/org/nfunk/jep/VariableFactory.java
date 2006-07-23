/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
/* @author rich
 * Created on 19-Dec-2003
 */
package org.nfunk.jep;
/**
 * A factory class which is used to create variables.
 * By default this class creates variables of type {@link Variable}.
 * This class should be subclassed if the type of variable used needs to be changed.
 * This class is passed to the constructor of {@link SymbolTable}
 * which ensures that variables of the correct type are always created.
 * <p>
 * This class should only be called from the SymbolTable class and not from application code.
 * @since 23 July 2006 - allows a defaultValue to be set.
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class VariableFactory
{
	Object defaultValue=null;
	/** Create a variable with a name and value */
	public Variable createVariable(String name, Object value) {
		Variable var = new Variable(name,value);
		return var;
	}

	/** Create a variable with a name but not value */
	public Variable createVariable(String name)	{
		if(defaultValue!=null)
			return new Variable(name,defaultValue);
		else
			return new Variable(name);
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value used whenever a new variable is created.
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
