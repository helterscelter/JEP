/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep;

/**
 * Information about a variable. 
 * Each variable has a name, a value and a flag to indicate
 * whether it is a constant or not (constants cannot have their value changed).
 * <p>
 * @author Rich Morris
 * Created on 18-Nov-2003
 */
public class Variable {
	protected String name;
	private Object value;
	private boolean isConstant=false;
	private boolean validValue=false;
	private static final Double ZERO = new Double(0.0);

	/** constructors are protected. They should only be called
	 * through the SymbolTable methods.
	 * @param name
	 */
	protected Variable(String name)
	{
		this.name = name;
		this.value= ZERO;
		validValue=false;
	}
	protected Variable(String name,Object value)
	{
		this.name = name;
		this.value=value;
		validValue=true;
	}
	public String getName() {return name;}
	//private void setName(String string) {name = string;	}
	public boolean isConstant() { return this.isConstant; }
	public void setIsConstant(boolean b) { this.isConstant = b; }
	public Object getValue() {return value;}
	/** Is the value of this variable valid? **/
	public boolean hasValidValue() { return validValue; }
	/** Sets whether the value of variable is valid. **/
	public void setValidValue(boolean val) { validValue = val; }

	/**
	 * Sets the value of the variable. Constant values cannot be changed.
	 * @return false if tried to change a constant value.
	 */
	public boolean setValue(Object object) {
		if(isConstant) return false;
		validValue=true;
		value = object;
		return true;
	}
	public String toString() {
		if(!validValue || value==null)
			return name + ": null";
		else if(isConstant)
			return name + ": "+value.toString() + " (Constant)";
		else
			return name + ": "+value.toString(); 
	}
}
