/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep;

/**
 * A factory class which is used to create variables.
 * By default this class creates variables of type {@link Variable}.
 * This class should be subclassed if the type of variable used needs to be changed.
 * This class is passed to the constructor of {@link SymbolTable}
 * which ensures that variables of the correct type are always created.
 * 
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class VariableFactory
{
	/** Create a variable with a name and value */
	public Variable createVariable(String name,Object value) {
		return new Variable(name,value);
	}

	/** Create a variable with a name but not value */
	public Variable createVariable(String name)	{
		return new Variable(name);
	}
}
